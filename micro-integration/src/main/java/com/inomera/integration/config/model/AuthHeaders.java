package com.inomera.integration.config.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuthHeaders extends Auth {

    private Map<String, String> headers = new LinkedHashMap<>();

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
