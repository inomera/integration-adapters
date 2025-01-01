package com.inomera.integration.config;

import com.inomera.integration.config.model.AdapterConfig;

import java.util.List;

public interface AdapterConfigDataSupplier {

    default AdapterConfig getConfig(){
        return null;
    }

    AdapterConfig getConfigV1(String key);

    <T> T getConfig(String key, Class<T> classToDeserialize);

    <T> T getConfig(String key, Class<T> classToDeserialize, T defaultValue);

    <T> List<T> getConfigs(String key, Class<T> classToDeserialize);

    <T> List<T> getConfigs(String key, Class<T> classToDeserialize, List<T> defaultValue);
}
