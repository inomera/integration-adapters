package com.inomera.adapter.config.bridge.util;

import com.inomera.integration.config.model.AdapterConfig;

public interface AdapterConfigMerger {

  static AdapterConfig mergeIfNotExistAdapterConfig(AdapterConfig adapterConfig,
      AdapterConfig commonConfig) {
    adapterConfig.patch(commonConfig);
    return adapterConfig;
  }
}
