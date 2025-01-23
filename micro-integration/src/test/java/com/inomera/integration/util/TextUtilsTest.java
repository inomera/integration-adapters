package com.inomera.integration.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class TextUtilsTest {

    @Test
    void shouldSameUUIDForSameConfigValue_whenGenerateUniqueUUID() {
        String encodedConfig = Base64.getEncoder().encodeToString(SAMPLE_ADAPTER_CONFIG.getBytes(StandardCharsets.UTF_8));
        String configUuId = TextUtils.generateUniqueUUID(encodedConfig);
        String configUuIdV2 = TextUtils.generateUniqueUUID(encodedConfig);
        assertEquals(configUuIdV2, configUuId);
    }

    @Test
    void shouldNotSameUUIDForSameConfigValue_whenGenerateUniqueUUID() {
        String encodedConfig = Base64.getEncoder().encodeToString((SAMPLE_ADAPTER_CONFIG + "}").getBytes(StandardCharsets.UTF_8));
        String encodedConfigV2 = Base64.getEncoder().encodeToString((SAMPLE_ADAPTER_CONFIG).getBytes(StandardCharsets.UTF_8));
        String configUuId = TextUtils.generateUniqueUUID(encodedConfig);
        String configUuIdV2 = TextUtils.generateUniqueUUID(encodedConfigV2);
        assertNotEquals(configUuIdV2, configUuId);
    }

    static final String SAMPLE_ADAPTER_CONFIG = "{\n" +
            "  \"key\": \"config.adapter.mirket.v1\",\n" +
            "  \"adapterProperties\": {\n" +
            "    \"runtime\": true,\n" +
            "    \"auth\": {\n" +
            "      \"type\": \"BEARER\",\n" +
            "      \"username\": \"username\",\n" +
            "      \"password\": \"password\",\n" +
            "      \"url\": \"https://www.googleapis.com/oauth2/v4/token\",\n" +
            "      \"grantType\": \"urn:ietf:params:oauth:grant-type:jwt-bearer\",\n" +
            "      \"ttl\": 3600000,\n" +
            "      \"scope\": \"https://www.googleapis.com/auth/cloud-platform\",\n" +
            "      \"clientId\": \"client_id\",\n" +
            "      \"clientSecret\": \"client_secret\",\n" +
            "      \"contentType\": \"application/x-www-form-urlencoded\",\n" +
            "      \"accept\": \"application/json\",\n" +
            "      \"tokenJsonPath\": \"$.access_token\"\n" +
            "    },\n" +
            "    \"headers\": {\n" +
            "      \"X-GW-TOKEN\": \"\"\n" +
            "    },\n" +
            "    \"http\": {\n" +
            "      \"requestTimeout\": 30000,\n" +
            "      \"connectTimeout\": 10000,\n" +
            "      \"idleConnectionsTimeout\": 60000,\n" +
            "      \"maxConnections\": 50,\n" +
            "      \"maxConnPerRoute\": 50,\n" +
            "      \"poolConcurrencyPolicy\": \"LAX\",\n" +
            "      \"timeToLive\": 60000,\n" +
            "      \"skipSsl\": true,\n" +
            "      \"redirectsEnable\": true\n" +
            "    },\n" +
            "    \"logging\": {\n" +
            "      \"strategy\": \"REQ_RES\",\n" +
            "      \"sensitiveFields\": [\n" +
            "        \"Authorization\"\n" +
            "      ],\n" +
            "      \"nonLoggingFields\": [\n" +
            "        \"file\",\n" +
            "        \"content\"\n" +
            "      ]\n" +
            "    },\n" +
            "    \"url\": \"https://api.mirket.inomera.com/v10/first\"\n" +
            "  }\n" +
            "}\n";
}