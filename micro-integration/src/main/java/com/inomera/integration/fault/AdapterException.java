package com.inomera.integration.fault;

import com.inomera.integration.constant.Status;
import com.inomera.integration.model.AdapterResponse;
import com.inomera.integration.model.AdapterStatus;


/**
 * Base error for all adapter exceptions. Adapter library doesn't throw any exception while sending
 * a request but this error type is being used by library itself for exception management.
 *
 * @author Burak Dogan Akyildiz, Turgay Can
 */
public class AdapterException extends RuntimeException {

    /**
     * The adapter status to use in {@link AdapterResponse}. Can be
     * an {@link Status#TECHNICAL_ERROR} or
     * {@link Status#BUSINESS_ERROR} error.
     */
    private final AdapterStatus adapterStatus;

    public AdapterException(AdapterStatus adapterStatus) {
        this.adapterStatus = adapterStatus;
    }

    public AdapterException(Exception cause, AdapterStatus adapterStatus) {
        super(cause);
        this.adapterStatus = adapterStatus;
    }

    public AdapterStatus getAdapterStatus() {
        return adapterStatus;
    }
}
