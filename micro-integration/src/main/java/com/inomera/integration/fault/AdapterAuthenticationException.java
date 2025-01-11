package com.inomera.integration.fault;

import com.inomera.integration.model.AdapterStatus;


/**
 * Backend service authentication error adapter exception.
 *
 * @author Burak Dogan Akyildiz
 * @see AdapterException
 */
public class AdapterAuthenticationException extends AdapterException {

    public AdapterAuthenticationException(AdapterStatus adapterStatus) {
        super(adapterStatus);
    }

    public AdapterAuthenticationException(Exception cause, AdapterStatus adapterStatus) {
        super(cause, adapterStatus);
    }
}
