package com.inomera.integration.type;

import com.inomera.integration.client.HttpSoapAdapterClient;
import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.integration.constant.HttpMethod;
import com.inomera.integration.interceptor.IntegrationAdapterInterceptor;
import com.inomera.integration.interceptor.ResponseBodyHandler;
import com.inomera.integration.model.AdapterResponse;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Soap api specification of {@link BaseAdapter}.
 *
 * @param <C> adapterConfigDataSupplier<C> adapterConfigDataSupplier config type.
 * @author Burak Dogan Akyildiz, Turgay Can.
 */
public abstract class SoapAdapter<C extends AdapterConfig> extends BaseAdapter<C> {

    public SoapAdapter(Supplier<C> adapterConfigDataSupplier, HttpSoapAdapterClient httpAdapterClient) {
        super(adapterConfigDataSupplier, httpAdapterClient);
    }

    public SoapAdapter(Supplier<C> adapterConfigDataSupplier, HttpSoapAdapterClient httpAdapterClient,
                       List<IntegrationAdapterInterceptor> interceptors) throws InstantiationException {
        super(adapterConfigDataSupplier, httpAdapterClient, interceptors);
    }

    /**
     * Soap execution specification of
     * {@link BaseAdapter#execute(String, HttpMethod, Map, Object, Class, ResponseBodyHandler)}
     * method.
     *
     * @param requestHeaders Http Headers.
     * @param requestBody    Http request body.
     * @param httpMethod     Http method.
     * @param responseType   Http response body type. response.
     * @param <T>            AdapterResponse data type.
     * @param <O>            HttpResponse data type.
     * @return Adapter response.
     */
    public <T, O> AdapterResponse<T> executeInternal(
            Map<String, String> requestHeaders,
            Object requestBody,
            HttpMethod httpMethod,
            Class<O> responseType
    ) {
        return this.execute(
                "",
                httpMethod,
                requestHeaders,
                requestBody,
                responseType,
                null
        );
    }

    /**
     * Soap execution specification of
     * {@link BaseAdapter#execute(String, HttpMethod, Map, Object, Class, ResponseBodyHandler)} method
     * with custom response handler.
     *
     * @param requestHeaders                Http Headers.
     * @param requestBody                   Http request body.
     * @param httpMethod                    Http method.
     * @param responseType                  Http response body type. response.
     * @param customResponseHandlerFunction Custom response handler to create custom adapter
     *                                      response.
     * @param <T>                           AdapterResponse data type.
     * @param <O>                           HttpResponse data type.
     * @return Adapter response.
     */
    public <T, O> AdapterResponse<T> executeInternalWithCustomHandler(
            Map<String, String> requestHeaders,
            Object requestBody,
            HttpMethod httpMethod,
            Class<O> responseType,
            ResponseBodyHandler<O, T> customResponseHandlerFunction
    ) {
        return this.execute(
                "",
                httpMethod,
                requestHeaders,
                requestBody,
                responseType,
                customResponseHandlerFunction
        );
    }
}
