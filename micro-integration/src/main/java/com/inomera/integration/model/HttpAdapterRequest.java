package com.inomera.integration.model;

import com.inomera.integration.constant.HttpMethod;

import java.util.Map;


/**
 * The request container for internal http request usage.
 *
 * @author Burak Dogan Akyildiz, Turgay Can.
 */
public class HttpAdapterRequest {

    /**
     * Http Method.
     */
    private HttpMethod method;
    /**
     * Full request url.
     * <p>
     * Contains query parameters if there are any.
     */
    private String url;
    /**
     * Http Headers.
     * <p>
     * GW Auth header etc.
     */
    private Map<String, String> headers;
    /**
     * Request body, this body data format should be same as endpoint api required format.
     */
    private Object requestBody;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public String toString() {
        return "HttpAdapterRequest{" +
                "method=" + method +
                ", url='" + url + '\'' +
                ", headers=" + headers +
                ", requestBody=" + requestBody +
                '}';
    }
}
