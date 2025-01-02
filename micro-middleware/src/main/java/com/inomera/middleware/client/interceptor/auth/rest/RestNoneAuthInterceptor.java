package com.inomera.middleware.client.interceptor.auth.rest;

import com.inomera.integration.auth.AuthenticationProvider;
import java.io.IOException;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class RestNoneAuthInterceptor implements ClientHttpRequestInterceptor, AuthenticationProvider {

  public RestNoneAuthInterceptor() {
  }

  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    return execution.execute(request, body);
  }
}
