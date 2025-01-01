package com.inomera.integration.config.model;

import com.inomera.integration.auth.AuthType;

import java.io.Serializable;

public class Auth implements Serializable {
    private AuthType type;

    public Auth() {
    }

    public Auth(AuthType type) {
        this.type = type;
    }

    private Auth(Builder builder) {
        this.type = builder.type;
    }

    public AuthType getType() {
        return type;
    }

    public void setType(AuthType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "type='" + type + '\'' +
                '}';
    }

    public void patch(Auth auth) {
        if (auth == null) {
            return;
        }

        if (getType() == null) {
            setType(auth.getType());
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AuthType type;

        public Builder type(AuthType type) {
            this.type = type;
            return this;
        }

        public Auth build() {
            return new Auth(this);
        }
    }

    static class NoneAuth extends Auth {
        public NoneAuth() {
            super(AuthType.NONE);
        }
    }
}
