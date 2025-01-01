package com.inomera.integration.interceptor;

import com.inomera.integration.model.AdapterResponse;
import com.inomera.integration.model.HttpAdapterRequest;
import com.inomera.integration.model.HttpAdapterResponse;

/**
 * Adapter interceptor while sending the adapter request to endpoint *
 *
 * @author Burak Dogan Akyildiz, Turgay Can.
 */
public class IntegrationAdapterInterceptor {

    /**
     * Manipulates the request just before sending it.
     *
     * @param httpAdapterRequest existing request instance
     * @return updated or new request instance.
     */
    public HttpAdapterRequest beforeHttpRequest(HttpAdapterRequest httpAdapterRequest) {
        return httpAdapterRequest;
    }

    /**
     * Manipulates the response in case of success response. This interceptor method won't be
     * triggered if any exception occurs while sending the request.
     *
     * @param httpAdapterRequest  existing request instance
     * @param httpAdapterResponse existing response instance
     * @param <O>                 Response body type
     * @return updated or new response instance.
     */
    public <O> HttpAdapterResponse<O> afterHttpRequest(HttpAdapterRequest httpAdapterRequest,
                                                       HttpAdapterResponse<O> httpAdapterResponse) {
        return httpAdapterResponse;
    }

    /**
     * Manipulates the adapter response in any case. Will be triggered after completion of all request
     * operations.
     *
     * @param adapterResponse existing response instance
     * @param <T>             Adapter response type.
     * @return Adapter Response
     */
    public <T> AdapterResponse<T> afterCompletion(AdapterResponse<T> adapterResponse) {
        return adapterResponse;
    }
}
