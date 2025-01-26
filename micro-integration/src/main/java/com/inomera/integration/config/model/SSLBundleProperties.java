package com.inomera.integration.config.model;

import java.util.Set;

/**
 * Base class for SSL Bundle properties.
 */
public abstract class SSLBundleProperties {

    /**
     * Key details for the bundle.
     */
    private final Key key = new Key();

    /**
     * Options for the SLL connection.
     */
    private final Options options = new Options();

    /**
     * SSL Protocol to use.
     */
    private String protocol = "TLS";

    public Key getKey() {
        return this.key;
    }

    public Options getOptions() {
        return this.options;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public static class Options {

        /**
         * Supported SSL ciphers.
         */
        private Set<String> ciphers;

        /**
         * Enabled SSL protocols.
         */
        private Set<String> enabledProtocols;

        public Set<String> getCiphers() {
            return this.ciphers;
        }

        public void setCiphers(Set<String> ciphers) {
            this.ciphers = ciphers;
        }

        public Set<String> getEnabledProtocols() {
            return this.enabledProtocols;
        }

        public void setEnabledProtocols(Set<String> enabledProtocols) {
            this.enabledProtocols = enabledProtocols;
        }

    }

    public static class Key {

        /**
         * The password used to access the key in the key store.
         */
        private String password;

        /**
         * The alias that identifies the key in the key store.
         */
        private String alias;

        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAlias() {
            return this.alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

    }
}
