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

    public BearerTokenCredentials(String url, String scope, String username, String password, String clientId, String clientSecret,
                                  String grantType, long ttl, String contentType, String accept, String tokenJsonPath) {
        super(AuthType.BEARER);
        this.url = url;
        this.scope = scope;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
        this.ttl = ttl;
        this.contentType = contentType;
        this.accept = accept;
        this.tokenJsonPath = tokenJsonPath;
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
}
