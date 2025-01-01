package com.inomera.integration.constant;

import com.inomera.integration.model.AdapterStatus;

/**
 * Adapter constants.
 *
 * @author Burak Dogan Akyildiz
 */
public class AdapterConstants {

    /**
     * Default technical error code. This error code is used for {@link AdapterStatus#code()} when
     * there is an error caused by this library or user of this library which means an error not
     * coming from the endpoint.
     */
    public static final String TECHNICAL_ERROR_STATUS_CODE = "TECH-";

    /**
     * Will be used for same reason as {@link #TECHNICAL_ERROR_STATUS_CODE} did but for
     * {@link AdapterStatus#description()} parameter.
     */
    public static final String TECHNICAL_ERROR_STATUS_DESCRIPTION = "Adapter technical error !";

    /**
     * Default business error code. Business error means that there is an error coming from endpoint
     * itself. This code will be used when there is no error code that comes from endpoint response.
     */
    public static final String BUSINESS_ERROR_STATUS_CODE = "BUS-";


    /**
     * Default business error code description. This description will be used when there is no error
     * description that comes from endpoint response.
     */
    public static final String BUSINESS_ERROR_STATUS_DESCRIPTION = "Adapter business error !";
}
