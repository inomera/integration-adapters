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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DynamicAdapterConfigDataBridgeSupplierHandler implements AdapterConfigDataSupplier {

  private static final String COMMON_CONFIG_V1_KEY = "config.adapter.common.v1";

  private final ConfigurationHolder configurationHolder;

  @Override
  public AdapterConfig getConfigV1(String key) {
    Assert.notNull(key);
    try {
      final Map<String, Object> adapterPropertiesMap = getConfig(key, Map.class);
      if (null == adapterPropertiesMap) {
        throw new AdapterConfigException("adapter config Key::" + key + " is not found");
      }

      Map<String, Object> authMap = (Map<String, Object>) adapterPropertiesMap.get("auth");
      final Auth authCredentials = getAuthCredentials(authMap);
      final AdapterProperties adapterProperties = getConfig(key, AdapterProperties.class);
      adapterProperties.setAuth(authCredentials);
      final AdapterConfig adapterConfig = new AdapterConfig(key, adapterProperties);
      return mergeWithCommonConfigIfNotSetInAdapterConfig(adapterConfig);
    } catch (Exception e) {
      LOG.error("key::{}, Config deserialization error occurred", key, e);
      throw new AdapterConfigException("Adapter Config Exception", e.getCause());
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
