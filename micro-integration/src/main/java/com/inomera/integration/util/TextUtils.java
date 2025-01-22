package com.inomera.integration.util;

import java.util.UUID;

public final class TextUtils {

    private TextUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    public static String generateUniqueUUID(String input) {
        UUID uuid = UUID.nameUUIDFromBytes(input.getBytes());
        return uuid.toString().replace("-", "").substring(0, 32);
    }
}
