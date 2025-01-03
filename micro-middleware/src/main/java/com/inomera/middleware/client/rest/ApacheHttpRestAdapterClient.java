package com.inomera.middleware.client.rest;

import com.inomera.integration.config.model.AdapterConfig;
import java.util.function.Supplier;
import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * An Apache HTTP based REST adapter client.
 */
public final class ApacheHttpRestAdapterClient extends BaseRestAdapterClient {

  public ApacheHttpRestAdapterClient() {
    super(new HttpComponentsClientHttpRequestFactory());
  }

  public ApacheHttpRestAdapterClient(ClientHttpRequestFactory clientHttpRequestFactory) {
    super(clientHttpRequestFactory);
  }

  public ApacheHttpRestAdapterClient(Supplier<AdapterConfig> configSupplierFunc) {
    super(configSupplierFunc, HttpComponentsClientHttpRequestFactory.class);
  }

  public ApacheHttpRestAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      ClientHttpRequestFactory clientHttpRequestFactory) {
    super(configSupplierFunc, clientHttpRequestFactory);
  }

  public ApacheHttpRestAdapterClient(HttpClient httpClient) {
    super(new HttpComponentsClientHttpRequestFactory(httpClient));
  }

  public ApacheHttpRestAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      HttpClient httpClient) {
    super(configSupplierFunc, new HttpComponentsClientHttpRequestFactory(httpClient));
  }


}
