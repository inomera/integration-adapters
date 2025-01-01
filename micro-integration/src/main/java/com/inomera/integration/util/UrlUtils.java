package com.inomera.integration.util;

/**
 * URL utility methods.
 *
 * @author Turgay Can.
 */
public final class UrlUtils {

    private UrlUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Combines a base URL and a sub-URL with proper handling of slashes.
     * Ensures the base URL ends with a single slash and the sub-URL doesn't start with one.
     *
     * @param baseUrl          the base URL
     * @param subUrlWithParams the sub-URL, potentially with query parameters
     * @return the combined URL
     */
    public static String combineUrl(String baseUrl, String subUrlWithParams) {
        if (baseUrl == null) {
            throw new IllegalArgumentException("Base URL and sub-URL must not be null.");
        }

        if (subUrlWithParams == null || subUrlWithParams.isEmpty()) {
            return baseUrl.replaceAll("/$", "");
        }

        // Ensure the base URL ends with a single slash
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }

        // Ensure the sub-URL doesn't start with a slash
        if (subUrlWithParams.startsWith("/")) {
            subUrlWithParams = subUrlWithParams.substring(1);
        }

        return baseUrl + subUrlWithParams;
    }

}
