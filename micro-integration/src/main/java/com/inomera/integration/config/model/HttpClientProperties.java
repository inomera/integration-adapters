package com.inomera.integration.config.model;

import java.io.Serializable;

/**
 * A configuration class for HTTP client properties.
 *
 * <p>This class holds settings for managing HTTP client behavior, including timeouts,
 * connection limits, SSL handling, and redirection options. It supports a builder
 * pattern for easy instantiation and a method for patching properties with
 * non-default values from another instance.</p>
 */
public class HttpClientProperties implements Serializable {
    /**
     * The maximum time (in milliseconds) to wait for an HTTP request to complete.
     */
    private long requestTimeout;

    /**
     * The maximum time (in milliseconds) to establish a connection.
     */
    private long connectTimeout;

    /**
     * The maximum time (in milliseconds) to keep idle connections in the pool.
     */
    private long idleConnectionsTimeout;

    /**
     * The maximum number of connections allowed in the connection pool.
     */
    private int maxConnections;

    /**
     * The maximum number of connections per route.
     */
    private int maxConnPerRoute;

    /**
     * The concurrency policy for the connection pool.
     */
    private String poolConcurrencyPolicy;

    /**
     * The maximum lifetime (in milliseconds) of a connection.
     */
    private long timeToLive;

    /**
     * Whether SSL validation should be skipped.
     */
    private boolean skipSsl;

    /**
     * Whether automatic redirection handling is enabled.
     */
    private boolean redirectsEnable;

    //TODO: SSL props should be added

    /**
     * Default constructor for {@code HttpClientProperties}.
     */
    public HttpClientProperties() {
    }

    /**
     * Constructs an instance of {@code HttpClientProperties} with specified values.
     *
     * @param requestTimeout       The request timeout in milliseconds.
     * @param connectTimeout       The connection timeout in milliseconds.
     * @param idleConnectionsTimeout The idle connection timeout in milliseconds.
     * @param maxConnections       The maximum number of connections.
     * @param maxConnPerRoute      The maximum connections per route.
     * @param poolConcurrencyPolicy The connection pool concurrency policy.
     * @param timeToLive           The connection time-to-live in milliseconds.
     * @param skipSsl              Whether to skip SSL validation.
     * @param redirectsEnable      Whether to enable automatic redirects.
     */
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

    public long getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(long requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getIdleConnectionsTimeout() {
        return idleConnectionsTimeout;
    }

    public void setIdleConnectionsTimeout(long idleConnectionsTimeout) {
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

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
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
        private long requestTimeout;
        private long connectTimeout;
        private long idleConnectionsTimeout;
        private int maxConnections;
        private int maxConnPerRoute;
        private String poolConcurrencyPolicy;
        private long timeToLive;
        private boolean skipSsl;
        private boolean redirectsEnable;

        public Builder requestTimeout(long requestTimeout) {
            this.requestTimeout = requestTimeout;
            return this;
        }

        public Builder connectTimeout(long connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        public Builder idleConnectionsTimeout(long idleConnectionsTimeout) {
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

        public Builder timeToLive(long timeToLive) {
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