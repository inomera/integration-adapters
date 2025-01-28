package com.inomera.middleware.client.rest;

import com.inomera.integration.config.model.AdapterConfig;
import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.http.client.ClientHttpRequestFactory;

import java.util.function.Supplier;

/**
 * An Apache HTTP based REST adapter client.
 * @author Salih Oran, Turgay Can
 */
public final class ApacheHttpRestAdapterClient extends BaseRestAdapterClient {

  public ApacheHttpRestAdapterClient() {
    super(new CustomHttpComponentsClientHttpRequestFactory());
  }

  public ApacheHttpRestAdapterClient(ClientHttpRequestFactory clientHttpRequestFactory) {
    super(clientHttpRequestFactory);
  }

  public ApacheHttpRestAdapterClient(Supplier<AdapterConfig> configSupplierFunc) {
    super(configSupplierFunc, CustomHttpComponentsClientHttpRequestFactory.class);
  }

  public ApacheHttpRestAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      ClientHttpRequestFactory clientHttpRequestFactory) {
    super(configSupplierFunc, clientHttpRequestFactory);
  }

  public ApacheHttpRestAdapterClient(HttpClient httpClient) {
    super(new CustomHttpComponentsClientHttpRequestFactory(httpClient));
  }

  public ApacheHttpRestAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      HttpClient httpClient) {
    super(configSupplierFunc, new CustomHttpComponentsClientHttpRequestFactory(httpClient));
  }


}
