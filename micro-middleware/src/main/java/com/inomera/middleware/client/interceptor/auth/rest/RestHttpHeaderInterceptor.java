package com.inomera.middleware.client.interceptor.auth.rest;

import com.inomera.integration.auth.AuthenticationProvider;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

public class RestHttpHeaderInterceptor implements ClientHttpRequestInterceptor,
    AuthenticationProvider {

  private final Map<String, String> headers;

  /**
   * Create a new instance of the {@link RestHttpHeaderInterceptor} with the provided headers.
   *
   * @param headers the headers to add to the request
   */
  public RestHttpHeaderInterceptor(Map<String, String> headers) {
    Assert.notNull(headers, "headers Cannot be null");
    this.headers = headers;
  }


  /**
   * Intercepts the HTTP request to add the headers.
   *
   * @param request   the HTTP request
   * @param body      the body of the request
   * @param execution the request execution
   * @return the HTTP response
   * @throws IOException if an I/O error occurs
   */
  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
      throws IOException {
    headers.forEach(request.getHeaders()::add);
    return execution.execute(request, body);
  }

}
