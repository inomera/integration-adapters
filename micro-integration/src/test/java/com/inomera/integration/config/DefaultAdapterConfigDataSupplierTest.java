package com.inomera.integration.config;

import com.inomera.integration.auth.AuthType;
import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.integration.config.model.LogStrategy;
import com.inomera.integration.fault.NotImplementedException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultAdapterConfigDataSupplierTest {

    @Test
    void should_getDefaultConfig() {
        var supplier = new DefaultAdapterConfigDataSupplier("https://mirket.ist/configs/v10?limit=10");
        AdapterConfig config = supplier.getConfig();

        assertEquals("https://mirket.ist/configs/v10?limit=10", config.getUrl());
        assertEquals(LogStrategy.REQ_RES, config.getAdapterLogging().getStrategy());
        assertEquals(AuthType.NONE, config.getAdapterProperties().getAuth().getType());
        assertEquals(0, config.getAdapterProperties().getHeaders().size());
        assertEquals(5000L, config.getAdapterProperties().getHttp().getConnectTimeout());
        assertEquals(20000L, config.getAdapterProperties().getHttp().getRequestTimeout());
        assertEquals(30000L, config.getAdapterProperties().getHttp().getIdleConnectionsTimeout());
        assertEquals(30000L, config.getAdapterProperties().getHttp().getTimeToLive());
        assertTrue(config.getAdapterProperties().getHttp().isSkipSsl());
    }

    @Test
    void shouldThrowNotImplementedException_whenGetConfigV1() {
        var supplier = new DefaultAdapterConfigDataSupplier("https://mirket.ist/configs/v10?limit=10");

        assertThrowsExactly(NotImplementedException.class, () -> supplier.getConfigV1("default.config.key"));
    }

    @Test
    void shouldThrowNotImplementedException_whenGetConfigObject() {
        var supplier = new DefaultAdapterConfigDataSupplier("https://mirket.ist/configs/v10?limit=10");

        assertThrowsExactly(NotImplementedException.class, () -> supplier.getConfig("default.config.key", Object.class));
    }

    @Test
    void shouldThrowNotImplementedException_whenGetConfigObjectWithDefaultValue() {
        var supplier = new DefaultAdapterConfigDataSupplier("https://mirket.ist/configs/v10?limit=10");

        assertThrowsExactly(NotImplementedException.class, () -> supplier.getConfig("default.config.key", Object.class, Object.class));
    }

    @Test
    void shouldThrowNotImplementedException_whenGetConfigs() {
        var supplier = new DefaultAdapterConfigDataSupplier("https://mirket.ist/configs/v10?limit=10");

        assertThrowsExactly(NotImplementedException.class, () -> supplier.getConfigs("default.config.key", Object.class));
    }

    @Test
    void shouldThrowNotImplementedException_whenGetConfigsWithDefaultValue() {
        var supplier = new DefaultAdapterConfigDataSupplier("https://mirket.ist/configs/v10?limit=10");

        assertThrowsExactly(NotImplementedException.class, () -> supplier.getConfigs("default.config.key", Object.class, List.of()));
    }
}