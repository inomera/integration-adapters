package com.inomera.middleware.client.interceptor.auth;

import java.io.IOException;
import java.util.Map;
import com.inomera.integration.auth.AuthenticationProvider;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

public class HttpHeaderInterceptor implements ClientHttpRequestInterceptor, AuthenticationProvider {

  private final Map<String, String> headers;

  /**
   * Create a new instance of the {@link HttpHeaderInterceptor} with the provided headers.
   *
   * @param headers the headers to add to the request
   */
  public HttpHeaderInterceptor(Map<String, String> headers) {
    Assert.notNull(headers, "headers Cannot be null");
    this.headers = headers;
  }

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
      throws IOException {
    headers.forEach(request.getHeaders()::add);
    return execution.execute(request, body);
  }

}
