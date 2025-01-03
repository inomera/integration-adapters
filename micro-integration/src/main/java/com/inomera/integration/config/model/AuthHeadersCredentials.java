package com.inomera.integration.config.model;

import com.inomera.integration.auth.AuthType;

import java.util.Map;
import java.util.stream.Collectors;

public class AuthHeadersCredentials extends Auth {

    private Map<String, Object> headers;

    public AuthHeadersCredentials() {
    }

    public AuthHeadersCredentials(Map<String, Object> headers) {
        super(AuthType.HEADER);
        this.headers = headers;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, Object value) {
        headers.put(key, value);
    }

    @Override
    public String toString() {
        return "AuthHeaders{" +
                "headers=" + headers +
                '}';
    }

    public Map<String, String> getHeadersAsStringMap() {
        return headers.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue() != null ? entry.getValue().toString() : ""
                ));
    }
}
