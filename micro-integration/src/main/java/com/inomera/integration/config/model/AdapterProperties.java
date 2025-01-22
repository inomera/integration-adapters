package com.inomera.integration.config.model;

import java.io.Serializable;
import java.util.Map;

public class AdapterProperties implements Serializable {
    private AdapterLogging logging = new AdapterLogging(LogStrategy.REQ_RES);
    private String url;
    private Map<String, String> headers;
    private HttpClientProperties http;
    private Auth auth;
    private Boolean runtime;

    public AdapterProperties() {
    }

    private AdapterProperties(Builder builder) {
        this.logging = builder.adapterLogging;
        this.url = builder.url;
        this.headers = builder.headers;
        this.http = builder.httpClientProperties;
        this.auth = builder.auth;
        this.runtime = builder.runtime;
    }

    public AdapterLogging getLogging() {
        return logging;
    }

    public void setLogging(AdapterLogging adapterLogging) {
        this.logging = adapterLogging;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public HttpClientProperties getHttp() {
        return http;
    }

    public void setHttp(HttpClientProperties http) {
        this.http = http;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public boolean isRuntime() {
        return runtime;
    }

    public void setRuntime(boolean runtime) {
        this.runtime = runtime;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "AdapterProperties{" +
                "logging=" + logging +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                ", httpClientProperties=" + http +
                ", auth=" + auth +
                ", runtime=" + runtime +
                '}';
    }

    public void patch(AdapterProperties commonConfigAdapterProperties) {
        if (commonConfigAdapterProperties == null) {
            return;
        }
        if (this.logging == null && commonConfigAdapterProperties.getLogging() == null) {
            this.logging = new AdapterLogging();
        }
        this.logging.patch(commonConfigAdapterProperties.getLogging());

        if (this.http == null && commonConfigAdapterProperties.getHttp() == null) {
            this.http = new HttpClientProperties();
        }
        this.http.patch(commonConfigAdapterProperties.getHttp());

        if (this.auth == null && commonConfigAdapterProperties.getAuth() == null) {
            this.auth = new Auth.NoneAuth();
        }
        this.auth.patch(commonConfigAdapterProperties.getAuth());

        if (this.getHeaders() == null) {
            this.setHeaders(commonConfigAdapterProperties.getHeaders());
        } else if (commonConfigAdapterProperties.getHeaders() != null) {
            commonConfigAdapterProperties.getHeaders().forEach((key, value) ->
                    this.getHeaders().putIfAbsent(key, value)
            );
        }
        this.runtime = this.runtime != null ? this.runtime : commonConfigAdapterProperties.isRuntime();
    }

    public String toSecureString() {
        return "AdapterProperties{" +
                "logging=" + logging +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                ", httpClientProperties=" + http +
                ", auth= **masked**" +
                ", runtime=" + runtime +
                '}';
    }

    public static class Builder {
        private AdapterLogging adapterLogging;
        private String url;
        private Map<String, String> headers;
        private HttpClientProperties httpClientProperties;
        private Auth auth;
        private Boolean runtime;

        public Builder logging(AdapterLogging adapterLogging) {
            this.adapterLogging = adapterLogging;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder headers(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder httpClientProperties(HttpClientProperties httpClientProperties) {
            this.httpClientProperties = httpClientProperties;
            return this;
        }

        public Builder auth(Auth auth) {
            this.auth = auth;
            return this;
        }

        public Builder runtime(Boolean runtime) {
            this.runtime = runtime;
            return this;
        }

        public AdapterProperties build() {
            return new AdapterProperties(this);
        }
    }
}
