package com.inomera.integration.fault;

import com.inomera.integration.model.AdapterStatus;


/**
 * Gateway token exception, this is thrown when there is an error while creating a new GW token.
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
