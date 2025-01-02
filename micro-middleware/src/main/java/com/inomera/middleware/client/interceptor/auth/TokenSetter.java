package com.inomera.middleware.client.interceptor.auth;

@FunctionalInterface
public interface TokenSetter {
  void setToken(String token);
}
