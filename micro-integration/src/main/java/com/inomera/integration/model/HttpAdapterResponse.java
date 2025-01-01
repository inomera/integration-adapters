package com.inomera.integration.model;


import java.util.Map;


/**
 * Http Response Container for internal library usage. User maps the response that using this
 * container.
 *
 * @param statusCode Response http status code.
 * @param headers    Response headers
 * @param body       Response body.
 * @param <O>        Response body type.
 * @author Burak Dogan Akyildiz, Turgay Can.
 */
public record HttpAdapterResponse<O>(int statusCode, Map<String, String> headers, O body) {

    @Override
    public String toString() {
        return "HttpAdapterResponse{" +
                "statusCode=" + statusCode +
                ", headers=" + headers +
                ", body=" + body +
                '}';
    }
}
