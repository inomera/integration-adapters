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
 * <p>
 * Abstract base class for SOAP adapters that extend the functionality of {@link BaseAdapter}.
 * This class provides a structure for integrating with SOAP-based services.
 * @author Burak Dogan Akyildiz, Turgay Can
 *
 * @param <C> the type of {@link AdapterConfig} that this adapter uses.
 */
public abstract class SoapAdapter<C extends AdapterConfig> extends BaseAdapter<C> {

    /**
     * Constructs a new {@code SoapAdapter} instance with the specified configuration supplier and HTTP client.
     *
     * @param adapterConfigDataSupplier a {@link Supplier} that provides the adapter configuration.
     * @param httpAdapterClient         the {@link HttpSoapAdapterClient} used for making SOAP requests.
     */
    public SoapAdapter(Supplier<C> adapterConfigDataSupplier, HttpSoapAdapterClient httpAdapterClient) {
        super(adapterConfigDataSupplier, httpAdapterClient);
    }

    /**
     * Constructs a new {@code SoapAdapter} instance with the specified configuration supplier,
     * HTTP client, and a list of interceptors.
     *
     * @param adapterConfigDataSupplier a {@link Supplier} that provides the adapter configuration.
     * @param httpAdapterClient         the {@link HttpSoapAdapterClient} used for making SOAP requests.
     * @param interceptors              a list of {@link IntegrationAdapterInterceptor} used for processing requests
     *                                  and responses during integration.
     * @throws InstantiationException if an error occurs during instantiation of the adapter.
     */
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
