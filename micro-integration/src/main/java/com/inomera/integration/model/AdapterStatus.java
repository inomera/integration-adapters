package com.inomera.integration.model;

import com.inomera.integration.constant.AdapterConstants;
import com.inomera.integration.constant.Status;

import java.io.Serial;
import java.io.Serializable;


/**
 * Adapter response status. Contains response code and description that comes from endpoint.
 * <p>
 * Adapter library does not throw any exception to the user of this library so this status is taken
 * to consideration for all adapter responses.
 *
 * @param status      Status data.
 * @param code        Response code of endpoint or default response code if there is no response code coming from
 *                    endpoint.
 * @param description Response description of endpoint or default response description if there is no response
 *                    description coming from endpoint.
 * @author Burak Dogan Akyildiz, Turgay Can.
 * @see Status
 */
public record AdapterStatus(Status status, int code, String description) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static AdapterStatus createSuccess() {
        return new AdapterStatus(Status.SUCCESS, Status.SUCCESS.getCode(), "Success");
    }

    public static AdapterStatus createStatusFailedAsTechnical(String description) {
        return new AdapterStatus(Status.TECHNICAL_ERROR, Status.TECHNICAL_ERROR.getCode(),
                description);
    }

    public static AdapterStatus createStatusFailedAsTechnical() {
        return new AdapterStatus(Status.TECHNICAL_ERROR, Status.TECHNICAL_ERROR.getCode(),
                AdapterConstants.TECHNICAL_ERROR_STATUS_DESCRIPTION);
    }

    public static AdapterStatus createStatusFailedAsTechnical(Exception e) {
        final String description =
                e.getMessage() != null ? e.getMessage()
                        : AdapterConstants.TECHNICAL_ERROR_STATUS_DESCRIPTION;
        return new AdapterStatus(Status.TECHNICAL_ERROR, Status.TECHNICAL_ERROR.getCode(),
                description);
    }

    public static AdapterStatus createStatusFailedAsBusiness() {
        return new AdapterStatus(Status.BUSINESS_ERROR, Status.BUSINESS_ERROR.getCode(),
                AdapterConstants.BUSINESS_ERROR_STATUS_DESCRIPTION);
    }

    public boolean isSuccess() {
        return Status.SUCCESS.equals(this.status);
    }

    @Override
    public String toString() {
        return "AdapterStatus{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
