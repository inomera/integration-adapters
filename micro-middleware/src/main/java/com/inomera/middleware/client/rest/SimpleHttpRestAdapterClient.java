package com.inomera.middleware.client.rest;

import com.inomera.integration.config.model.AdapterConfig;
import java.util.function.Supplier;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

public final class SimpleHttpRestAdapterClient extends BaseRestAdapterClient {


  public SimpleHttpRestAdapterClient() {
    super(new SimpleClientHttpRequestFactory());
  }

  public SimpleHttpRestAdapterClient(Supplier<AdapterConfig> configSupplierFunc) {
    super(configSupplierFunc, SimpleClientHttpRequestFactory.class);
  }

  //TODO : implement native jdk http

}
