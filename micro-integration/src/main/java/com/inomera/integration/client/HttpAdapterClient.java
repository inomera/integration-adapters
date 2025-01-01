package com.inomera.integration.client;

import com.inomera.integration.fault.AdapterException;
import com.inomera.integration.model.HttpAdapterRequest;
import com.inomera.integration.model.HttpAdapterResponse;

/**
 * This interface is an abstraction for http implementation layer. All http level configurations,
 * connection pooling, ssl configurations will be handled by implementor.
 * <p>
 * Implementor of this interface is responsible from;
 * <ul>
 *  <li>Implementing the http layer or using any library as implementation.</li>
 *  <li> Sending the request using the parameters inside {@link HttpAdapterRequest}.</li>
 *  <li> Serializing the response to given response type.</li>
 *  <li> Returning the http response using {@link HttpAdapterResponse}.</li>
 * </ul>
 *
 * @author Burak Dogan Akyildiz, Turgay Can
 */
public interface HttpAdapterClient {

    /**
     * Sends http requests using the underlying implementation.
     * <p>
     * Implementor of this method should throw {@link AdapterException} or its subtypes in case of an
     * exception.
     *
     * @param httpAdapterRequest full request information
     * @param responseType       Class of the model that represents the response format of this
     *                           request.
     * @return Http Response Data
     * @throws AdapterException can throw AdapterException as a generic type or its subtype for
     *                          specific exceptions.
     */
    <O> HttpAdapterResponse<O> send(HttpAdapterRequest httpAdapterRequest, Class<O> responseType)
            throws AdapterException;

}
