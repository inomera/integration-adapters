package com.inomera.integration.config;

import com.inomera.integration.auth.AuthType;
import com.inomera.integration.config.model.*;
import com.inomera.integration.fault.NotImplementedException;

import java.util.List;
import java.util.Map;

public class DefaultAdapterConfigDataSupplier implements AdapterConfigDataSupplier {

    private final String url;

    public DefaultAdapterConfigDataSupplier(String url) {
        this.url = url;
    }

    @Override
    public AdapterConfig getConfig() {
        return AdapterConfig
                .builder()
                .value(AdapterProperties.builder()
                        .logging(AdapterLogging.builder()
                                .strategy(LogStrategy.REQ_RES)
                                .build())
                        .headers(Map.of())
                        .url(this.url)
                        .httpClientProperties(HttpClientProperties.builder()
                                .requestTimeout(30000)
                                .connectTimeout(10000)
                                .idleConnectionsTimeout(60000)
                                .maxConnections(10)
                                .skipSsl(true)
                                .build())
                        .auth(Auth.builder()
                                .type(AuthType.NONE)
                                .build())
                        .build())
                .build();
    }

    @Override
    public AdapterConfig getConfigV1(String key) {
        throw new NotImplementedException();
    }

    @Override
    public <T> T getConfig(String key, Class<T> classToDeserialize) {
        throw new NotImplementedException();
    }

    @Override
    public <T> T getConfig(String key, Class<T> classToDeserialize, T defaultValue) {
        throw new NotImplementedException();
    }

    @Override
    public <T> List<T> getConfigs(String key, Class<T> classToDeserialize) {
        throw new NotImplementedException();
    }

    @Override
    public <T> List<T> getConfigs(String key, Class<T> classToDeserialize, List<T> defaultValue) {
        throw new NotImplementedException();
    }
}
