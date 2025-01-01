package com.inomera.integration.config.model;

public enum LogStrategy {
    /*
    log with uri, request headers, request body, response http status, response headers, response body
     */
    ALL,
    /*
    log with uri, request body, response http status,response body
   */
    REQ_RES,
    /*
    log with uri, request headers, request body, response http status, response headers, response body
    only in failure request case
    */
    FAILURE,
    /*
    none
    */
    OFF;

    public boolean allOrFailure() {
        return this == LogStrategy.ALL || this == LogStrategy.FAILURE;
    }

    public boolean allOrReqRes() {
        return this == LogStrategy.ALL || this == LogStrategy.REQ_RES;
    }
}
