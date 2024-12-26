package com.inomera.integration.config.model;

import com.inomera.integration.auth.AuthType;

public class BearerToken extends Auth {

    private String token;

    public BearerToken(String token) {
        super(AuthType.BEARER);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    //TODO : token should be masked
    @Override
    public String toString() {
        return "BearerToken{" +
                "token='" + token + '\'' +
                '}';
    }
}
