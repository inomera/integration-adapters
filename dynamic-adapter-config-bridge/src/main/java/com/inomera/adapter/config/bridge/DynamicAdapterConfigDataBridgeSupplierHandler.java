package com.inomera.adapter.config.bridge;

import com.inomera.adapter.config.bridge.exception.AdapterConfigException;
import com.inomera.adapter.config.bridge.util.AdapterConfigMerger;
import com.inomera.integration.auth.AuthType;
import com.inomera.integration.config.AdapterConfigDataSupplier;
import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.integration.config.model.AdapterProperties;
import com.inomera.integration.config.model.Auth;
import com.inomera.integration.config.model.Auth.NoneAuth;
import com.inomera.integration.config.model.AuthHeadersCredentials;
import com.inomera.integration.config.model.BasicAuthCredentials;
import com.inomera.integration.config.model.BearerTokenCredentials;
import com.inomera.telco.commons.config.ConfigurationHolder;
import com.inomera.telco.commons.lang.Assert;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@RequiredArgsConstructor
public class DynamicAdapterConfigDataBridgeSupplierHandler implements AdapterConfigDataSupplier {

  private static final String COMMON_CONFIG_V1_KEY = "config.adapter.common.v1";
  private final ConcurrentHashMap<String, String> configReloadMap = new ConcurrentHashMap<>();

  private final ConfigurationHolder configurationHolder;

  @Override
  public AdapterConfig getConfigV1(String key) {
    Assert.notNull(key, "Key cannot be null");
    try {
      // Get the adapter properties map
      Map<String, Object> adapterPropertiesMap = getConfig(key, Map.class);
      if (adapterPropertiesMap == null) {
        throw new AdapterConfigException("Adapter config for key '" + key + "' is not found");
      }

      // Extract and set authentication credentials
      Auth authCredentials = extractAuthCredentials(adapterPropertiesMap);

      // Create AdapterProperties and set authentication
      AdapterProperties adapterProperties = getConfig(key, AdapterProperties.class);
      adapterProperties.setAuth(authCredentials);

      // Merge with common configuration
      AdapterConfig adapterConfig = new AdapterConfig(key, adapterProperties);
      AdapterConfig mergedAdapterConfig = mergeWithCommonConfigIfNotSetInAdapterConfig(
          adapterConfig);

      // Handle configuration reload logic
      handleConfigReload(key, mergedAdapterConfig);

      return mergedAdapterConfig;
    } catch (Exception e) {
      LOG.error("Key: {}, Config deserialization error occurred", key, e);
      throw new AdapterConfigException("Adapter Config Exception", e);
    }
  }

  @Override
  public <T> T getConfig(String key, Class<T> classToDeserialize) {
    final Object result = switch (classToDeserialize.getSimpleName()) {
      case "String" -> resolveProperty(key, configurationHolder::getStringProperty);
      case "Long" -> resolveProperty(key, configurationHolder::getLongProperty);
      case "Integer" -> resolveProperty(key, configurationHolder::getIntegerProperty);
      case "Boolean" -> resolveProperty(key, configurationHolder::getBooleanProperty);
      case "Double" -> resolveProperty(key, configurationHolder::getDoubleProperty);
      case "Float" -> resolveProperty(key, configurationHolder::getFloatProperty);
      case "Set" -> resolveProperty(key, configurationHolder::getStringSetProperty);
      default -> configurationHolder.getJsonObjectProperty(key, classToDeserialize);
    };
    return classToDeserialize.cast(result);
  }

  @Override
  public <T> T getConfig(String key, Class<T> classToDeserialize, T defaultValue) {
    Object result = switch (classToDeserialize.getSimpleName()) {
      case "String" -> resolveProperty(key, defaultValue, configurationHolder::getStringProperty);
      case "Long" -> resolveProperty(key, defaultValue, configurationHolder::getLongProperty);
      case "Integer" -> resolveProperty(key, defaultValue, configurationHolder::getIntegerProperty);
      case "Boolean" -> resolveProperty(key, defaultValue, configurationHolder::getBooleanProperty);
      case "Double" -> resolveProperty(key, defaultValue, configurationHolder::getDoubleProperty);
      case "Float" -> resolveProperty(key, defaultValue, configurationHolder::getFloatProperty);
      case "Set" -> resolveProperty(key, defaultValue, configurationHolder::getStringSetProperty);
      default -> configurationHolder.getJsonObjectProperty(key, classToDeserialize, defaultValue);
    };
    return classToDeserialize.cast(result);
  }

  @Override
  public <T> List<T> getConfigs(String key, Class<T> classToDeserialize) {
    return switch (classToDeserialize.getSimpleName()) {
      case "Enum" -> List.of(classToDeserialize.cast(
          configurationHolder.getEnumList(key, (Class<Enum>) classToDeserialize)));
      default -> configurationHolder.getJsonListProperty(key, classToDeserialize);
    };
  }

  @Override
  public <T> List<T> getConfigs(String key, Class<T> classToDeserialize, List<T> defaultValue) {
    return switch (classToDeserialize.getSimpleName()) {
      case "Enum" -> List.of(classToDeserialize.cast(
          configurationHolder.getEnumList(key, (Class<Enum>) classToDeserialize,
              (List<Enum>) defaultValue)));
      default -> configurationHolder.getJsonListProperty(key, classToDeserialize, defaultValue);
    };
  }


  private Auth extractAuthCredentials(Map<String, Object> adapterPropertiesMap) {
    Map<String, Object> authMap = (Map<String, Object>) adapterPropertiesMap.get("auth");
    if (authMap == null || authMap.isEmpty()) {
      LOG.info("'auth' section is missing in adapter properties so that return null for using common config value");
      return null;
    }
    return getAuthCredentials(authMap);
  }

  private void handleConfigReload(String key, AdapterConfig mergedAdapterConfig) {
    final String currentHash = configReloadMap.get(key);
    if (StringUtils.isBlank(currentHash)) {
      // Initial load
      configReloadMap.put(key, mergedAdapterConfig.hashConfig());
      mergedAdapterConfig.setRefresh(true);
    } else if (currentHash.equals(mergedAdapterConfig.hashConfig())) {
      // No changes in configuration
      mergedAdapterConfig.setRefresh(false);
    } else {
      // Configuration has changed
      configReloadMap.put(key, mergedAdapterConfig.hashConfig());
      mergedAdapterConfig.setRefresh(true);
    }
  }

  private Auth getAuthCredentials(Map<String, Object> authMap) {
    String authType = (String) authMap.get("type");
    switch (AuthType.valueOf(authType)) {
      case BASIC -> {
        return new BasicAuthCredentials((String) authMap.get("username"),
            (String) authMap.get("password"));
      }
      case HEADER -> {
        return new AuthHeadersCredentials((Map<String, Object>) authMap.get("headers"));
      }
      case BEARER -> {
        return new BearerTokenCredentials.Builder()
            .url((String) authMap.get("url"))
            .scope((String) authMap.get("scope"))
            .username((String) authMap.get("username"))
            .password((String) authMap.get("password"))
            .clientId((String) authMap.get("clientId"))
            .clientSecret((String) authMap.get("clientSecret"))
            .grantType((String) authMap.get("grantType"))
            .ttl(Long.parseLong(authMap.get("ttl").toString()))
            .contentType((String) authMap.get("contentType"))
            .accept((String) authMap.get("accept"))
            .tokenJsonPath((String) authMap.get("tokenJsonPath"))
            .build();
      }
      default -> {
        return new NoneAuth();
      }
    }
  }

  private AdapterConfig mergeWithCommonConfigIfNotSetInAdapterConfig(AdapterConfig adapterConfig) {
    final AdapterProperties commonAdapterProperties = getConfig(COMMON_CONFIG_V1_KEY,
        AdapterProperties.class);
    if (commonAdapterProperties == null) {
      LOG.warn("common config key :: {} is not found, please check it!!", COMMON_CONFIG_V1_KEY);
      return adapterConfig;
    }
    final AdapterConfig commonConfig = new AdapterConfig(COMMON_CONFIG_V1_KEY,
        commonAdapterProperties);
    return AdapterConfigMerger.mergeIfNotExistAdapterConfig(adapterConfig, commonConfig);
  }

  private <T> Object resolveProperty(String key, FunctionWithDefault<T> resolver) {
    return resolver.apply(key);
  }

  private <T> Object resolveProperty(String key, T defaultValue, FunctionWithDefault<T> resolver) {
    return defaultValue == null ? resolver.apply(key) : resolver.apply(key, defaultValue);
  }

  @FunctionalInterface
  private interface FunctionWithDefault<T> {

    Object apply(String key);

    default Object apply(String key, T defaultValue) {
      return apply(key); // Default implementation if not overridden
    }
  }
}
