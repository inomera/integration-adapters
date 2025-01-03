package com.inomera.integration.config.model;

import com.inomera.integration.auth.AuthType;

public class BearerTokenCredentials extends Auth {

    private final String url;
    private final String scope;
    private final String username;
    private final String password;
    private final String clientId;
    private final String clientSecret;
    private final String grantType;
    private final long ttl;
    private final String contentType;
    private final String accept;
    private final String tokenJsonPath;

    public BearerTokenCredentials() {
        super(AuthType.BEARER);
        this.url = null;
        this.scope = null;
        this.username = null;
        this.password = null;
        this.clientId = null;
        this.clientSecret = null;
        this.grantType = null;
        this.ttl = 0;
        this.contentType = null;
        this.accept = null;
        this.tokenJsonPath = null;
    }

    private BearerTokenCredentials(Builder builder) {
        super(AuthType.BEARER);
        this.url = builder.url;
        this.scope = builder.scope;
        this.username = builder.username;
        this.password = builder.password;
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.grantType = builder.grantType;
        this.ttl = builder.ttl;
        this.contentType = builder.contentType;
        this.accept = builder.accept;
        this.tokenJsonPath = builder.tokenJsonPath;
    }

    public String getUrl() {
        return url;
    }

    public String getScope() {
        return scope;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public long getTtl() {
        return ttl;
    }

    public String getContentType() {
        return contentType;
    }

    public String getAccept() {
        return accept;
    }

    public String getTokenJsonPath() {
        return tokenJsonPath;
    }

    @Override
    public String toString() {
        return "BearerToken{" +
                "url='" + url + '\'' +
                ", scope='" + scope + '\'' +
                ", username='" + username + '\'' +
                ", password='" + "**masked**" + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + "**masked**" + '\'' +
                ", grantType='" + grantType + '\'' +
                ", ttl='" + ttl + '\'' +
                ", contentType='" + contentType + '\'' +
                ", accept='" + accept + '\'' +
                ", tokenJsonPath=" + tokenJsonPath +
                '}';
    }

    public static class Builder {
        private String url;
        private String scope;
        private String username;
        private String password;
        private String clientId;
        private String clientSecret;
        private String grantType;
        private long ttl;
        private String contentType;
        private String accept;
        private String tokenJsonPath;

        public Builder() {}

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder scope(String scope) {
            this.scope = scope;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder grantType(String grantType) {
            this.grantType = grantType;
            return this;
        }

        public Builder ttl(long ttl) {
            this.ttl = ttl;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder accept(String accept) {
            this.accept = accept;
            return this;
        }

        public Builder tokenJsonPath(String tokenJsonPath) {
            this.tokenJsonPath = tokenJsonPath;
            return this;
        }

        public BearerTokenCredentials build() {
            return new BearerTokenCredentials(this);
        }
    }
}
