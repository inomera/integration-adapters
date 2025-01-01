package com.inomera.integration.type;

import com.inomera.integration.client.HttpRestAdapterClient;
import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.integration.constant.HttpMethod;
import com.inomera.integration.interceptor.IntegrationAdapterInterceptor;
import com.inomera.integration.interceptor.ResponseBodyHandler;
import com.inomera.integration.model.AdapterResponse;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Rest api specification of {@link BaseAdapter}.
 *
 * @param <C> adapterConfigDataSupplier config supplier function.
 * @author Burak Dogan Akyildiz, Turgay Can.
 */
public abstract class RestAdapter<C extends AdapterConfig> extends BaseAdapter<C> {

    public RestAdapter(Supplier<C> adapterConfigDataSupplier, HttpRestAdapterClient httpAdapterClient) {
        super(adapterConfigDataSupplier, httpAdapterClient);
    }

    public RestAdapter(Supplier<C> adapterConfigDataSupplier,
                       HttpRestAdapterClient httpRestAdapterClient,
                       List<IntegrationAdapterInterceptor> interceptors) throws InstantiationException {
        super(adapterConfigDataSupplier, httpRestAdapterClient, interceptors);
    }


    /**
     * Http get method specification of
     * {@link BaseAdapter#execute(String, HttpMethod, Map, Object, Class, ResponseBodyHandler)}
     * method.
     *
     * @param subUrlWithParams              Method level subpath of endpoint API. This path will be
     *                                      added after
     *                                      )} method.
     * @param requestHeaders                Http Headers.
     * @param requestBody                   Http request body.
     * @param responseType                  Http response body type.
     * @param customResponseHandlerFunction Custom response handler to create custom adapter
     *                                      response.
     * @param <T>                           AdapterResponse data type.
     * @param <O>                           HttpResponse data type.
     * @return Adapter response.
     */
    public <T, O> AdapterResponse<T> get(
            String subUrlWithParams,
            Map<String, String> requestHeaders,
            Object requestBody,
            Class<O> responseType,
            ResponseBodyHandler<O, T> customResponseHandlerFunction
    ) {
        return this.execute(
                subUrlWithParams,
                HttpMethod.GET,
                requestHeaders,
                requestBody,
                responseType,
                customResponseHandlerFunction
        );
    }


    /**
     * Http post method specification of
     * {@link BaseAdapter#execute(String, HttpMethod, Map, Object, Class, ResponseBodyHandler)}
     * method.
     *
     * @param subUrlWithParams              Method level subpath of endpoint API. This path will be
     *                                      added after
     *                                      )} method.
     * @param requestHeaders                Http Headers.
     * @param requestBody                   Http request body.
     * @param responseType                  Http response body type.
     * @param customResponseHandlerFunction Custom response handler to create custom adapter
     *                                      response.
     * @param <T>                           AdapterResponse data type.
     * @param <O>                           HttpResponse data type.
     * @return Adapter response.
     */
    public <T, O> AdapterResponse<T> post(
            String subUrlWithParams,
            Map<String, String> requestHeaders,
            Object requestBody,
            Class<O> responseType,
            ResponseBodyHandler<O, T> customResponseHandlerFunction
    ) {
        return this.execute(
                subUrlWithParams,
                HttpMethod.POST,
                requestHeaders,
                requestBody,
                responseType,
                customResponseHandlerFunction
        );
    }


    /**
     * Http put method specification of
     * {@link BaseAdapter#execute(String, HttpMethod, Map, Object, Class, ResponseBodyHandler)}
     * method.
     *
     * @param subUrlWithParams              Method level subpath of endpoint API. This path will be
     *                                      added after
     *                                      )} method.
     * @param requestHeaders                Http Headers.
     * @param requestBody                   Http request body.
     * @param responseType                  Http response body type.
     * @param customResponseHandlerFunction Custom response handler to create custom adapter
     *                                      response.
     * @param <T>                           AdapterResponse data type.
     * @param <O>                           HttpResponse data type.
     * @return Adapter response.
     */
    public <T, O> AdapterResponse<T> put(
            String subUrlWithParams,
            Map<String, String> requestHeaders,
            Object requestBody,
            Class<O> responseType,
            ResponseBodyHandler<O, T> customResponseHandlerFunction
    ) {
        return this.execute(
                subUrlWithParams,
                HttpMethod.PUT,
                requestHeaders,
                requestBody,
                responseType,
                customResponseHandlerFunction
        );
    }


    /**
     * Http patch method specification of
     * {@link BaseAdapter#execute(String, HttpMethod, Map, Object, Class, ResponseBodyHandler)}
     * method.
     *
     * @param subUrlWithParams              Method level subpath of endpoint API. This path will be
     *                                      added after
     *                                      )} method.
     * @param requestHeaders                Http Headers.
     * @param requestBody                   Http request body.
     * @param responseType                  Http response body type.
     * @param customResponseHandlerFunction Custom response handler to create custom adapter
     *                                      response.
     * @param <T>                           AdapterResponse data type.
     * @param <O>                           HttpResponse data type.
     * @return Adapter response.
     */
    public <T, O> AdapterResponse<T> patch(
            String subUrlWithParams,
            Map<String, String> requestHeaders,
            Object requestBody,
            Class<O> responseType,
            ResponseBodyHandler<O, T> customResponseHandlerFunction
    ) {
        return this.execute(
                subUrlWithParams,
                HttpMethod.PATCH,
                requestHeaders,
                requestBody,
                responseType,
                customResponseHandlerFunction
        );
    }

    /**
     * Http delete method specification of
     * {@link BaseAdapter#execute(String, HttpMethod, Map, Object, Class, ResponseBodyHandler)}
     * method.
     *
     * @param subUrlWithParams              Method level subpath of endpoint API. This path will be
     *                                      added after
     *                                      )} method.
     * @param requestHeaders                Http Headers.
     * @param requestBody                   Http request body.
     * @param responseType                  Http response body type.
     * @param customResponseHandlerFunction Custom response handler to create custom adapter
     *                                      response.
     * @param <T>                           AdapterResponse data type.
     * @param <O>                           HttpResponse data type.
     * @return Adapter response.
     */
    public <T, O> AdapterResponse<T> delete(
            String subUrlWithParams,
            Map<String, String> requestHeaders,
            Object requestBody,
            Class<O> responseType,
            ResponseBodyHandler<O, T> customResponseHandlerFunction
    ) {
        return this.execute(
                subUrlWithParams,
                HttpMethod.DELETE,
                requestHeaders,
                requestBody,
                responseType,
                customResponseHandlerFunction
        );
    }

    /**
     * Generic http request specification of
     * {@link BaseAdapter#execute(String, HttpMethod, Map, Object, Class, ResponseBodyHandler)}
     * method.
     *
     * @param subUrlWithParams              Method level subpath of endpoint API. This path will be
     *                                      added after
     *                                      )} method.
     * @param requestMethod                 Http method.
     * @param requestHeaders                Http Headers.
     * @param requestBody                   Http request body.
     * @param responseType                  Http response body type.
     * @param customResponseHandlerFunction Custom response handler to create custom adapter
     *                                      response.
     * @param <T>                           AdapterResponse data type.
     * @param <O>                           HttpResponse data type.
     * @return Adapter response.
     */
    public <T, O> AdapterResponse<T> executeGeneric(String subUrlWithParams, HttpMethod requestMethod,
                                                    Map<String, String> requestHeaders,
                                                    Object requestBody, Class<O> responseType,
                                                    ResponseBodyHandler<O, T> customResponseHandlerFunction) {
        return this.execute(subUrlWithParams, requestMethod, requestHeaders, requestBody, responseType,
                customResponseHandlerFunction);
    }
}
