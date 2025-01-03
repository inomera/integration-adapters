package com.inomera.integration.config.model;

import com.inomera.integration.auth.AuthType;

import java.io.Serializable;

public class Auth implements Serializable {
    private AuthType type = AuthType.NONE;

    public Auth() {
    }

    public Auth(AuthType type) {
        this.type = type;
    }

    private Auth(Builder<?> builder) {
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

    public abstract static class Builder<T extends Builder<T>> {
        private AuthType type;

        public T type(AuthType type) {
            this.type = type;
            return self();
        }

        protected abstract T self();

        public abstract Auth build();
    }

    public static class NoneAuth extends Auth {
        public NoneAuth() {
            super(AuthType.NONE);
        }

        public static class Builder extends Auth.Builder<Builder> {
            @Override
            protected Builder self() {
                return this;
            }

            @Override
            public NoneAuth build() {
                return new NoneAuth();
            }
        }
    }
}
