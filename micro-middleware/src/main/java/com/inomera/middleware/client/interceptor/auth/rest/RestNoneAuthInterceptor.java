package com.inomera.middleware.client.interceptor.auth.rest;

import com.inomera.integration.auth.AuthenticationProvider;
import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * A {@link ClientHttpRequestInterceptor} implementation that adds no authentication to the
 * request.
 */
public class RestNoneAuthInterceptor implements ClientHttpRequestInterceptor,
    AuthenticationProvider {

  public RestNoneAuthInterceptor() {
  }

  /**
   * Intercepts the HTTP request to add no authentication.
   *
   * @param request   the HTTP request
   * @param body      the body of the request
   * @param execution the request execution
   * @return the HTTP response
   * @throws IOException if an I/O error occurs
   */
  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    return execution.execute(request, body);
  }
}
