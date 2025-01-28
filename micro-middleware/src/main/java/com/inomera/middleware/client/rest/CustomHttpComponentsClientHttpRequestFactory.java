package com.inomera.middleware.client.rest;

import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 *  @author Salih Oran, Turgay Can
 */
public class CustomHttpComponentsClientHttpRequestFactory extends
    HttpComponentsClientHttpRequestFactory {

  private int readTimeout = -1;

  public CustomHttpComponentsClientHttpRequestFactory() {
  }

  public CustomHttpComponentsClientHttpRequestFactory(HttpClient httpClient) {
    super(httpClient);
  }

  /**
   * hacky method for Spring RestTemplate implementation
   * @param readTimeout
   */
  @Override
  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }
}