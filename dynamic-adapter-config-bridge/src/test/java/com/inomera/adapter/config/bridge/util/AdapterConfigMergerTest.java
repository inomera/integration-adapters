package com.inomera.adapter.config.bridge.util;

import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.integration.config.model.AdapterProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdapterConfigMergerTest {

  @Test
  void mergeIfNotExistAdapterConfig() {
    AdapterConfig commonConfig = new AdapterConfig();
    commonConfig.setRefresh(false);
    commonConfig.setKey("config.adapter.common");
    AdapterConfig mirketAdapterConfig = new AdapterConfig();
    mirketAdapterConfig.setKey("config.adapter.mirket.v1");
    mirketAdapterConfig.setAdapterProperties(new AdapterProperties());
    mirketAdapterConfig.setRefresh(true);
    AdapterConfig adapterConfig = AdapterConfigMerger.mergeIfNotExistAdapterConfig(
        mirketAdapterConfig, commonConfig);

    assertEquals("config.adapter.mirket.v1", adapterConfig.getKey());
    assertTrue(adapterConfig.isRefresh());
  }
}