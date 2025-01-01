package com.inomera.integration.fault;

import com.inomera.integration.model.AdapterStatus;
import com.inomera.integration.model.HttpAdapterRequest;

import java.io.IOException;


/**
 * Represents http level connection exceptions.
 *
 * @author Burak Dogan Akyildiz
 * @see AdapterException
 */
public class AdapterIOException extends AdapterException {

    /**
     * The request data that was used in the http request where the actual IOException occurs.
     */
    private final HttpAdapterRequest httpAdapterRequest;

    public AdapterIOException(IOException ioException, HttpAdapterRequest httpAdapterRequest,
                              AdapterStatus adapterStatus) {
        super(ioException, adapterStatus);
        this.httpAdapterRequest = httpAdapterRequest;
    }

    public HttpAdapterRequest getHttpAdapterRequest() {
        return httpAdapterRequest;
    }
}
