package com.inomera.integration.config.model;

/**
 * {@link SSLBundleProperties} for PEM-encoded certificates and private keys.
 */
public class PemSSLBundleProperties extends SSLBundleProperties {
    /**
     * Keystore properties.
     */
    private final Store keystore = new Store();

    /**
     * Truststore properties.
     */
    private final Store truststore = new Store();

    /**
     * Store properties.
     */
    public static class Store {

        /**
         * Type of the store to create, e.g. JKS.
         */
        private String type;

        /**
         * Location or content of the certificate or certificate chain in PEM format.
         */
        private String certificate;

        /**
         * Location or content of the private key in PEM format.
         */
        private String privateKey;

        /**
         * Password used to decrypt an encrypted private key.
         */
        private String privateKeyPassword;

        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCertificate() {
            return this.certificate;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }

        public String getPrivateKey() {
            return this.privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getPrivateKeyPassword() {
            return this.privateKeyPassword;
        }

        public void setPrivateKeyPassword(String privateKeyPassword) {
            this.privateKeyPassword = privateKeyPassword;
        }

    }

    public Store getKeystore() {
        return this.keystore;
    }

    public Store getTruststore() {
        return this.truststore;
    }
}
