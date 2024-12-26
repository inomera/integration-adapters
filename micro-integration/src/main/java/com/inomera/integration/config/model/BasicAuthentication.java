package com.inomera.integration.config.model;

import com.inomera.integration.auth.AuthType;

public class BasicAuthentication extends Auth {
    private final String username;
    private final String password;

    public BasicAuthentication(String username, String password) {
        super(AuthType.BASIC);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
