package com.inomera.integration.fault;

import com.inomera.integration.model.AdapterStatus;


/**
 * Represents serialization exceptions, like json or xml serialization. for request or response body
 * or headers.
 *
 * @author Burak Dogan Akyildiz
 * @see AdapterException
 */
public class AdapterSerializationException extends AdapterException {

    public AdapterSerializationException(Exception cause, AdapterStatus adapterStatus) {
        super(cause, adapterStatus);
    }

}
