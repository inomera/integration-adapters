package com.inomera.middleware.client.interceptor.auth.rest;

import com.inomera.integration.auth.AuthenticationProvider;
import com.inomera.integration.config.model.BearerTokenCredentials;
import com.inomera.middleware.client.interceptor.auth.BaseBearerTokenProvider;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Bearer token interceptor for rest template. This interceptor handles the addition of a Bearer
 * token to HTTP requests. Plain Text Bearer Token Interceptor (It should be re-written decode of
 * OAUTH2 response)
 */
public class RestDefaultBearerTokenInterceptor extends BaseBearerTokenProvider implements
    ClientHttpRequestInterceptor,
    ClientHttpRequestInitializer, AuthenticationProvider {

  /**
   * Constructor for BearerTokenInterceptor.
   *
   * @param bearerTokenCredentials the credentials required to obtain the Bearer token
   */
  public RestDefaultBearerTokenInterceptor(BearerTokenCredentials bearerTokenCredentials) {
    super(bearerTokenCredentials, new RestTemplate());
  }

  public RestDefaultBearerTokenInterceptor(BearerTokenCredentials bearerTokenCredentials,
      RestTemplate restTemplate) {
    super(bearerTokenCredentials, restTemplate);
  }

  /**
   * Intercepts the HTTP request to add the Bearer token.
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
    checkAndSetTokenHeader(token -> request.getHeaders().setBearerAuth(token));
    return execution.execute(request, body);
  }

  /**
   * Initializes the HTTP request with the Bearer token.
   *
   * @param request the HTTP request
   */
  @Override
  public void initialize(ClientHttpRequest request) {
    request.getHeaders().setBearerAuth(getBearerToken().token());
  }


}