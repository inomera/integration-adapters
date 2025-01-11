package com.inomera.integration.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlUtilsTest {

    @Test
    void should_CombineUrls() {
        final String urlV1 = UrlUtils.combineUrl("https://mirket.ist", "/subscribers/v10");
        assertEquals("https://mirket.ist/subscribers/v10", urlV1);

        final String urlV2 = UrlUtils.combineUrl("https://mirket.ist/", "/subscribers/v10");
        assertEquals("https://mirket.ist/subscribers/v10", urlV2);


        final String urlV3 = UrlUtils.combineUrl("https://mirket.ist", "subscribers/v10");
        assertEquals("https://mirket.ist/subscribers/v10", urlV3);

        final String urlV4 = UrlUtils.combineUrl("https://mirket.ist", "/subscribers/v10?q:status=active");
        assertEquals("https://mirket.ist/subscribers/v10?q:status=active", urlV4);
    }
}