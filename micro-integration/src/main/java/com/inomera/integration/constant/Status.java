package com.inomera.integration.constant;

import com.inomera.integration.model.AdapterStatus;

/**
 * Adapter response status. Adapter library does not throw any exception to the user of this library
 * so this status is taken to consideration for all adapter responses.
 *
 * @author Burak Dogan Akyildiz
 * @see AdapterStatus
 */
public enum Status {

    /**
     * Successful adapter status. {@link AdapterStatus} uses this
     * status value when everything is successful. This represents the technical level and business
     * level status of adapter request. If status is true this means that the operation that user
     * intended is done successfully.
     */
    SUCCESS(2000),
    /**
     * This means there is a technical level error. Like a serialization exception or connection error
     * or http 5** level responses coming from endpoint.
     */
    TECHNICAL_ERROR(5000),
    /**
     * This means that the request run successfully on http level but there is an error about the
     * business level operation. Endpoint can throw http 4** errors which means there eis a business
     * level error. Like validation errors or data level errors.
     */
    BUSINESS_ERROR(4000);

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
