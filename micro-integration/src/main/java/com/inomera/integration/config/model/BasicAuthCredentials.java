package com.inomera.integration.config.model;

import com.inomera.integration.auth.AuthType;

public class BasicAuthCredentials extends Auth {
    private final String username;
    private final String password;

    public BasicAuthCredentials() {
        super(AuthType.BASIC);
        this.username = null;
        this.password = null;
    }

    public BasicAuthCredentials(String username, String password) {
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

    @Override
    public String toString() {
        return "BasicAuthentication{" +
                "username='" + username + '\'' +
                ", password='" + "**masked**" + '\'' +
                '}';
    }
}
