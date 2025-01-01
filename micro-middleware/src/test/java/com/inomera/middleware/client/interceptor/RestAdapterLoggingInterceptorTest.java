package com.inomera.middleware.client.interceptor;

import static com.inomera.middleware.client.interceptor.util.Samples.SAMPLE_JSON_REQUEST;
import static com.inomera.middleware.client.interceptor.util.Samples.SAMPLE_JSON_RESPONSE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.inomera.integration.config.model.AdapterLogging;
import com.inomera.integration.config.model.LogStrategy;
import com.inomera.middleware.client.interceptor.log.RestLoggingInterceptor;
import com.inomera.middleware.client.interceptor.util.HttpTestClientResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.BasicHttpEntity;
import org.apache.hc.core5.http.message.BasicClassicHttpResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

class RestAdapterLoggingInterceptorTest {

  @DisplayName("log request and response without masking")
  @Test
  void intercept_logAsPlainText() throws IOException {
    RestLoggingInterceptor restLoggingInterceptor = new RestLoggingInterceptor(
        new AdapterLogging(LogStrategy.REQ_RES));
    ClientHttpRequest request = new HttpComponentsClientHttpRequestFactory().createRequest(
        URI.create("https://mirket.ist"), HttpMethod.GET);
    request.getHeaders().add("Content-Type", "application/json");
    ClientHttpResponse clientResponse = restLoggingInterceptor.intercept(request,
        SAMPLE_JSON_REQUEST.getBytes(StandardCharsets.UTF_8),
        (request1, body) -> {
          ClassicHttpResponse response = new BasicClassicHttpResponse(HttpStatus.SC_OK);
          response.setEntity(new BasicHttpEntity(
              new ByteArrayInputStream(SAMPLE_JSON_RESPONSE.getBytes(StandardCharsets.UTF_8)),
              ContentType.APPLICATION_JSON));
          return new HttpTestClientResponse(response);
        });

    assertNotNull(clientResponse);
  }

  @DisplayName("mask sensitive and non loggable fields")
  @Test
  void intercept_maskedSensitiveAndNonLoggableFields() throws IOException {
    AdapterLogging AdapterLogging = new AdapterLogging(LogStrategy.REQ_RES, List.of("password"),
        List.of("age", "hobbies"));
    RestLoggingInterceptor restLoggingInterceptor = new RestLoggingInterceptor(AdapterLogging);

    ClientHttpRequest request = new HttpComponentsClientHttpRequestFactory().createRequest(
        URI.create("https://mirket.ist"), HttpMethod.GET);
    request.getHeaders().add("Content-Type", "application/json");
    ClientHttpResponse clientResponse = restLoggingInterceptor.intercept(request,
        SAMPLE_JSON_REQUEST.getBytes(StandardCharsets.UTF_8),
        (request1, body) -> {
          ClassicHttpResponse response = new BasicClassicHttpResponse(HttpStatus.SC_OK);
          response.setEntity(new BasicHttpEntity(
              new ByteArrayInputStream(SAMPLE_JSON_RESPONSE.getBytes(StandardCharsets.UTF_8)),
              ContentType.APPLICATION_JSON));
          return new HttpTestClientResponse(response);
        });

    assertNotNull(clientResponse);
  }

  @DisplayName("mask sensitive and non loggable fields with all")
  @Test
  void intercept_maskedSensitiveAndNonLoggableFieldsForAllStrategy() throws IOException {
    AdapterLogging AdapterLogging = new AdapterLogging(LogStrategy.ALL,
        List.of("password", "Authorization"),
        List.of("age", "hobbies"));
    RestLoggingInterceptor restLoggingInterceptor = new RestLoggingInterceptor(AdapterLogging);

    ClientHttpRequest request = new HttpComponentsClientHttpRequestFactory().createRequest(
        URI.create("https://mirket.ist"), HttpMethod.GET);
    request.getHeaders().add("Content-Type", "application/json");
    request.getHeaders().add("Authorization", "Basic 239xb7yn8273nyx2=");
    ClientHttpResponse clientResponse = restLoggingInterceptor.intercept(request,
        SAMPLE_JSON_REQUEST.getBytes(StandardCharsets.UTF_8),
        (request1, body) -> {
          ClassicHttpResponse response = new BasicClassicHttpResponse(HttpStatus.SC_OK);
          response.setEntity(new BasicHttpEntity(
              new ByteArrayInputStream(SAMPLE_JSON_RESPONSE.getBytes(StandardCharsets.UTF_8)),
              ContentType.APPLICATION_JSON));
          return new HttpTestClientResponse(response);
        });

    assertNotNull(clientResponse);
  }


}