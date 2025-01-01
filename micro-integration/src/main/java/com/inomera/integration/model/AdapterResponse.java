package com.inomera.integration.model;

import com.inomera.integration.constant.Status;

import java.io.Serial;
import java.io.Serializable;


/**
 * Adapter response container.
 * <p>
 * This class contains endpoint response data and it's status.
 *
 * @param <T> response data type.
 * @author Burak Dogan Akyildiz, Turgay Can.
 */
public class AdapterResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Adapter response status. Contains response code and description that comes from endpoint.
     * <p>
     * Adapter library does not throw any exception to the user of this library so this status is
     * taken to consideration for all adapter responses.
     *
     * @see Status
     */
    private final AdapterStatus status;

    /**
     * Response data that will be used by user of adapter library. This data can contain different
     * format or limited information that actual response of endpoint.
     */
    private final T data;

    public AdapterResponse() {
        this(null, null);
    }

    public AdapterResponse(AdapterStatus status) {
        this(status, null);
    }

    public AdapterResponse(AdapterStatus status, T data) {
        this.data = data;
        this.status = status;
    }

    public AdapterStatus getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "AdapterResponse{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}
