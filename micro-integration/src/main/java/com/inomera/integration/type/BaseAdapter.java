package com.inomera.integration.type;

import com.inomera.integration.client.HttpAdapterClient;
import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.integration.constant.HttpMethod;
import com.inomera.integration.constant.Status;
import com.inomera.integration.fault.AdapterAuthenticationException;
import com.inomera.integration.fault.AdapterException;
import com.inomera.integration.interceptor.IntegrationAdapterInterceptor;
import com.inomera.integration.interceptor.ResponseBodyHandler;
import com.inomera.integration.model.AdapterResponse;
import com.inomera.integration.model.AdapterStatus;
import com.inomera.integration.model.HttpAdapterRequest;
import com.inomera.integration.model.HttpAdapterResponse;
import com.inomera.integration.util.UrlUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base adapter class for both rest and soap services. This class contains all high level definition
 * of integration flow for one endpoint service.
 * <p>
 * In order to create an instance of this class user needs to provide a {@link HttpAdapterClient}.
 * The implementation of {@link HttpAdapterClient} class should depend on {@link RestAdapter} or
 * {@link SoapAdapter} usage. There can be multiple implementation of {@link HttpAdapterClient} for
 * both rest and soap, user can choose any of them based on the endpoint requirements.
 * <p>
 * User can override the {@link #handleExceptionWithDefault(Exception)} method to add endpoint level
 * default exception handling.
 * <p>
 * User can override the {@link #checkStatusDefaultIsHttp200(HttpAdapterResponse)} method to check
 * the response status from the body of response. Sometimes services can return an incorrect http
 * status which means a response with a successful http status but an error inside the body.
 * <p>
 * User of this library should not extend this class, user should extend {@link RestAdapter} or
 * {@link SoapAdapter} abstractions and use its' methods.
 *
 * @param <C> EndpointConnectionConfig type.
 * @author Burak Dogan Akyildiz, Turgay Can.
 */
public abstract class BaseAdapter<C extends AdapterConfig> implements Adapter<C>{

    /**
     * Logging with using java util logger.
     */
    private static final Logger LOGGER = Logger.getLogger(BaseAdapter.class.getName());

    /**
     * Adapter configuration data supplier.
     */
    private final Supplier<C> adapterConfigDataSupplier;

    /**
     * Http adapter client to send requests.
     */

    private final HttpAdapterClient httpAdapterClient;
    /**
     * User defined interceptors to intercept http request steps.
     */
    private final List<IntegrationAdapterInterceptor> interceptors;

    public BaseAdapter(Supplier<C> adapterConfigDataSupplier, HttpAdapterClient httpAdapterClient) {
        this.adapterConfigDataSupplier = adapterConfigDataSupplier;
        this.httpAdapterClient = httpAdapterClient;
        this.interceptors = List.of();
    }


    public BaseAdapter(Supplier<C> adapterConfigDataSupplier, HttpAdapterClient httpAdapterClient,
                       List<IntegrationAdapterInterceptor> interceptors)
            throws InstantiationException {
        this.adapterConfigDataSupplier = adapterConfigDataSupplier;
        this.httpAdapterClient = httpAdapterClient;
        this.interceptors = interceptors;

        if (adapterConfigDataSupplier == null) {
            throw new InstantiationException("adapterConfigDataSupplier cannot be NULL !");
        }

        if (httpAdapterClient == null) {
            throw new InstantiationException("HttpAdapterClient cannot be NULL !");
        }
    }


    /**
     * Base method for all http send operations. All endpoint requests uses this method.
     *
     * @param subUrlWithParams              Method level subpath of endpoint API. This path will be
     *                                      added after
     *                                      {@link
     * @param requestMethod                 Http Method of this request.
     * @param requestHeaders                Http Headers.
     * @param requestBody                   Http request body.
     * @param responseType                  Http response body type.
     * @param customResponseHandlerFunction Custom response handler to create custom adapter
     *                                      response.
     * @param <T>                           AdapterResponse data type.
     * @param <O>                           HttpResponse data type.
     * @return Adapter response.
     */
    <T, O> AdapterResponse<T> execute(
            String subUrlWithParams,
            HttpMethod requestMethod,
            Map<String, String> requestHeaders,
            Object requestBody,
            Class<O> responseType,
            ResponseBodyHandler<O, T> customResponseHandlerFunction
    ) {
        AdapterResponse<T> adapterResponse = null;
        try {

            HttpAdapterRequest httpAdapterRequest = createRequest(subUrlWithParams, requestMethod,
                    requestHeaders, requestBody);
            // adapter request interceptors apply
            if (this.interceptors != null && !this.interceptors.isEmpty()) {
                for (IntegrationAdapterInterceptor interceptor : this.interceptors) {
                    httpAdapterRequest = interceptor.beforeHttpRequest(httpAdapterRequest);
                }
            }

            HttpAdapterResponse<O> httpAdapterResponse;
            try {
                // auth interceptor from config or external provider
                httpAdapterResponse = this.httpAdapterClient.send(httpAdapterRequest, responseType);
            } catch (AdapterAuthenticationException gwException) {
                //handle it if possible!!
                httpAdapterResponse = this.httpAdapterClient.send(httpAdapterRequest, responseType);
            }

            // adapter response interceptors apply
            if (this.interceptors != null && !this.interceptors.isEmpty()) {
                for (IntegrationAdapterInterceptor interceptor : this.interceptors) {
                    httpAdapterResponse = interceptor.afterHttpRequest(httpAdapterRequest,
                            httpAdapterResponse);
                }
            }

            // customize for http response handler in adapter method (you have to implement rest of the code block)
            // mapping the body, optional: intercepts after completion phase
            if (customResponseHandlerFunction != null) {
                return customResponseHandlerFunction.apply(httpAdapterResponse);
            }

            //default all service operation impl
            var status = this.checkStatusDefaultIsHttp200(httpAdapterResponse);
            if (status.status().equals(Status.SUCCESS)) {
                adapterResponse = new AdapterResponse<T>(status, (T) httpAdapterResponse.body());
            } else {
                throw new AdapterException(status);
            }

            //Log debug response
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error: ", ex);
            adapterResponse = this.handleExceptionInternalWithDefault(ex);
        } finally {
            try {
                // response interceptors apply
                if (this.interceptors != null && !this.interceptors.isEmpty()) {
                    for (IntegrationAdapterInterceptor interceptor : this.interceptors) {
                        adapterResponse = interceptor.afterCompletion(adapterResponse);
                    }
                }
            } catch (Exception ex) {
                //log error exception
                adapterResponse = this.handleExceptionInternalWithDefault(ex);
            }
        }

        return adapterResponse;
    }

    private HttpAdapterRequest createRequest(String subUrlWithParams, HttpMethod requestMethod,
                                             Map<String, String> requestHeaders, Object requestBody) {
        if (requestHeaders == null) {
            requestHeaders = new HashMap<>();
        }

        AdapterConfig config = adapterConfigDataSupplier.get();

        final String url = UrlUtils.combineUrl(config.getAdapterProperties().getUrl(), subUrlWithParams);
        HttpAdapterRequest httpAdapterRequest = new HttpAdapterRequest();
        httpAdapterRequest.setMethod(requestMethod);
        httpAdapterRequest.setUrl(url);
        httpAdapterRequest.setHeaders(requestHeaders);
        httpAdapterRequest.setRequestBody(requestBody);
        return httpAdapterRequest;
    }

    /**
     * User can override this method to check the response status from the body of response. Sometimes
     * services can return an incorrect http status which means a response with a successful http
     * status but an error inside the body.
     *
     * @param httpAdapterResponse http response.
     * @return Adapter status which will be created from http response.
     */
    protected AdapterStatus checkStatusDefaultIsHttp200(HttpAdapterResponse<?> httpAdapterResponse) {
        if (httpAdapterResponse.statusCode() == 200) {
            return AdapterStatus.createSuccess();
        }
        return AdapterStatus.createStatusFailedAsTechnical();
    }

    /**
     * Handle exception with default implementation. If user does not override this method, this
     * method will be used.
     *
     * @param e
     * @param <T>
     * @return
     */
    <T> AdapterResponse<T> handleExceptionInternalWithDefault(Exception e) {

        final AdapterResponse<T> resp = this.handleExceptionWithDefault(e);
        if (resp != null) {
            return resp;
        }
        final AdapterStatus statusFailedAsTechnical = AdapterStatus.createStatusFailedAsTechnical(e);
        return new AdapterResponse<>(statusFailedAsTechnical);
    }

    /**
     * User can override this method to add endpoint level default exception handling.
     *
     * @param e   exception to handle
     * @param <T> response data type.
     * @return New adapter response that represents this error.
     */
    protected <T> AdapterResponse<T> handleExceptionWithDefault(Exception e) {
        if (e instanceof AdapterException adapterException) {
            return new AdapterResponse<>(adapterException.getAdapterStatus());
        }

        final AdapterStatus statusFailedAsTechnical = AdapterStatus.createStatusFailedAsTechnical(e);
        return new AdapterResponse<>(statusFailedAsTechnical);
    }

}
