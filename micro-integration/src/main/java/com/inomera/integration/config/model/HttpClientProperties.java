package com.inomera.integration.config.model;

import java.io.Serializable;

public class HttpClientProperties implements Serializable {
    private int requestTimeout;
    private int connectTimeout;
    private int idleConnectionsTimeout;
    private int maxConnections;
    private int maxConnPerRoute;
    private String poolConcurrencyPolicy;
    private int timeToLive;
    private boolean skipSsl;
    private boolean redirectsEnable;
    //TODO: SSL props should be added

    public HttpClientProperties() {
    }

    public HttpClientProperties(int requestTimeout, int connectTimeout, int idleConnectionsTimeout, int maxConnections, int maxConnPerRoute, String poolConcurrencyPolicy, int timeToLive, boolean skipSsl, boolean redirectsEnable) {
        this.requestTimeout = requestTimeout;
        this.connectTimeout = connectTimeout;
        this.idleConnectionsTimeout = idleConnectionsTimeout;
        this.maxConnections = maxConnections;
        this.maxConnPerRoute = maxConnPerRoute;
        this.poolConcurrencyPolicy = poolConcurrencyPolicy;
        this.timeToLive = timeToLive;
        this.skipSsl = skipSsl;
        this.redirectsEnable = redirectsEnable;
    }

    private HttpClientProperties(Builder builder) {
        this.requestTimeout = builder.requestTimeout;
        this.connectTimeout = builder.connectTimeout;
        this.idleConnectionsTimeout = builder.idleConnectionsTimeout;
        this.maxConnections = builder.maxConnections;
        this.maxConnPerRoute = builder.maxConnPerRoute;
        this.poolConcurrencyPolicy = builder.poolConcurrencyPolicy;
        this.timeToLive = builder.timeToLive;
        this.skipSsl = builder.skipSsl;
        this.redirectsEnable = builder.redirectsEnable;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getIdleConnectionsTimeout() {
        return idleConnectionsTimeout;
    }

    public void setIdleConnectionsTimeout(int idleConnectionsTimeout) {
        this.idleConnectionsTimeout = idleConnectionsTimeout;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMaxConnPerRoute() {
        return maxConnPerRoute;
    }

    public void setMaxConnPerRoute(int maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
    }

    public String getPoolConcurrencyPolicy() {
        return poolConcurrencyPolicy;
    }

    public void setPoolConcurrencyPolicy(String poolConcurrencyPolicy) {
        this.poolConcurrencyPolicy = poolConcurrencyPolicy;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public boolean isSkipSsl() {
        return skipSsl;
    }

    public void setSkipSsl(boolean skipSsl) {
        this.skipSsl = skipSsl;
    }

    public boolean isRedirectsEnable() {
        return redirectsEnable;
    }

    public void setRedirectsEnable(boolean redirectsEnable) {
        this.redirectsEnable = redirectsEnable;
    }

    @Override
    public String toString() {
        return "HttpClientProperties{" +
                "requestTimeout=" + requestTimeout +
                ", connectTimeout=" + connectTimeout +
                ", idleConnectionsTimeout=" + idleConnectionsTimeout +
                ", maxConnections=" + maxConnections +
                ", maxConnPerRoute=" + maxConnPerRoute +
                ", poolConcurrencyPolicy='" + poolConcurrencyPolicy + '\'' +
                ", timeToLive=" + timeToLive +
                ", skipSsl=" + skipSsl +
                ", redirectsEnable=" + redirectsEnable +
                '}';
    }

    public void patch(HttpClientProperties httpClientProperties) {
        if (httpClientProperties == null) {
            return;
        }

        if (getRequestTimeout() == 0) {
            setRequestTimeout(httpClientProperties.getRequestTimeout());
        }

        if (getConnectTimeout() == 0) {
            setConnectTimeout(httpClientProperties.getConnectTimeout());
        }

        if (getIdleConnectionsTimeout() == 0) {
            setIdleConnectionsTimeout(httpClientProperties.getIdleConnectionsTimeout());
        }

        if (getMaxConnections() == 0) {
            setMaxConnections(httpClientProperties.getMaxConnections());
        }

        if (getMaxConnPerRoute() == 0) {
            setMaxConnPerRoute(httpClientProperties.getMaxConnPerRoute());
        }

        if (getPoolConcurrencyPolicy() == null) {
            setPoolConcurrencyPolicy(httpClientProperties.getPoolConcurrencyPolicy());
        }

        if (getTimeToLive() == 0) {
            setTimeToLive(httpClientProperties.getTimeToLive());
        }

        if (!isSkipSsl()) {
            setSkipSsl(httpClientProperties.isSkipSsl());
        }

        if (!isRedirectsEnable()) {
            setRedirectsEnable(httpClientProperties.isRedirectsEnable());
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int requestTimeout;
        private int connectTimeout;
        private int idleConnectionsTimeout;
        private int maxConnections;
        private int maxConnPerRoute;
        private String poolConcurrencyPolicy;
        private int timeToLive;
        private boolean skipSsl;
        private boolean redirectsEnable;

        public Builder requestTimeout(int requestTimeout) {
            this.requestTimeout = requestTimeout;
            return this;
        }

        public Builder connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder idleConnectionsTimeout(int idleConnectionsTimeout) {
            this.idleConnectionsTimeout = idleConnectionsTimeout;
            return this;
        }

        public Builder maxConnections(int maxConnections) {
            this.maxConnections = maxConnections;
            return this;
        }

        public Builder maxConnPerRoute(int maxConnPerRoute) {
            this.maxConnPerRoute = maxConnPerRoute;
            return this;
        }

        public Builder poolConcurrencyPolicy(String poolConcurrencyPolicy) {
            this.poolConcurrencyPolicy = poolConcurrencyPolicy;
            return this;
        }

        public Builder timeToLive(int timeToLive) {
            this.timeToLive = timeToLive;
            return this;
        }

        public Builder skipSsl(boolean skipSsl) {
            this.skipSsl = skipSsl;
            return this;
        }

        public Builder redirectsEnable(boolean redirectsEnable) {
            this.redirectsEnable = redirectsEnable;
            return this;
        }

        public HttpClientProperties build() {
            return new HttpClientProperties(this);
        }
    }

}