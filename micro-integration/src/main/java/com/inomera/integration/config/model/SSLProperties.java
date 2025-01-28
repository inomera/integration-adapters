package com.inomera.integration.config.model;

/**
 * Properties for centralized SSL trust material configuration.
 */
public class SSLProperties {

    /**
     * PEM-encoded SSL trust material.
     */
    private final PemSSLBundleProperties pem = new PemSSLBundleProperties();

    public PemSSLBundleProperties getPem() {
        return this.pem;
    }
}
