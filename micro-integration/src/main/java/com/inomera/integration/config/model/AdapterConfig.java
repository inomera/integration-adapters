package com.inomera.integration.config.model;

import com.inomera.integration.config.EndpointConfig;
import com.inomera.integration.fault.AdapterException;
import com.inomera.integration.model.AdapterStatus;

import java.io.Serializable;

public class AdapterConfig implements EndpointConfig, Serializable {
    private String key;
    private AdapterProperties adapterProperties;

    public AdapterConfig() {
    }

    public AdapterConfig(String key, AdapterProperties adapterProperties) {
        this.key = key;
        this.adapterProperties = adapterProperties;
    }

    private AdapterConfig(Builder builder) {
        this.key = builder.key;
        this.adapterProperties = builder.adapterProperties;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public AdapterProperties getAdapterProperties() {
        return adapterProperties;
    }

    public void setAdapterProperties(AdapterProperties adapterProperties) {
        this.adapterProperties = adapterProperties;
    }

    @Override
    public String toString() {
        return "Config{" +
                "key='" + key + '\'' +
                ", adapterProperties=" + adapterProperties +
                '}';
    }

    public void patch(AdapterConfig commonConfig) {
        if (this.adapterProperties == null || commonConfig == null) {
            return;
        }
        adapterProperties.patch(commonConfig.getAdapterProperties());
    }

    public static Builder builder() {
        return new Builder();
    }

    public AdapterLogging getAdapterLogging() {
        return adapterProperties != null ? adapterProperties.getLogging() : AdapterLogging.defaultAdapterLogging();
    }

    public String getUrl() {
        if (adapterProperties == null) {
            throw new AdapterException(AdapterStatus.createStatusFailedAsTechnical("adapterProperties cannot empty when get url"));
        }
        final String url = adapterProperties.getUrl();
        if (url == null || url.isEmpty()) {
            throw new AdapterException(AdapterStatus.createStatusFailedAsTechnical("url cannot empty"));
        }
        return url;
    }

    public static class Builder {
        private String key;
        private AdapterProperties adapterProperties;

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder value(AdapterProperties adapterProperties) {
            this.adapterProperties = adapterProperties;
            return this;
        }

        public AdapterConfig build() {
            return new AdapterConfig(this);
        }
    }
}




