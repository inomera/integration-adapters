package com.inomera.middleware.client.interceptor.auth;

import com.inomera.integration.config.model.BearerTokenCredentials;
import com.inomera.integration.fault.AdapterException;
import com.inomera.integration.model.AdapterStatus;
import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInitializer;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Bearer token interceptor for rest template. This interceptor handles the addition of a Bearer
 * token to HTTP requests.
 */
public class BearerTokenInterceptor implements ClientHttpRequestInterceptor,
    ClientHttpRequestInitializer {

  private final BearerTokenCredentials bearerTokenCredentials;
  private final RestTemplate restTemplate;

  private ActiveBearerToken activeBearerToken;

  /**
   * Constructor for BearerTokenInterceptor.
   *
   * @param bearerTokenCredentials the credentials required to obtain the Bearer token
   */
  public BearerTokenInterceptor(BearerTokenCredentials bearerTokenCredentials) {
    this.bearerTokenCredentials = bearerTokenCredentials;
    this.restTemplate = new RestTemplate();
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
    synchronized (this) {
      if (activeBearerToken == null
          || activeBearerToken.expiresAt() < System.currentTimeMillis()) {
        activeBearerToken = getBearerToken();
      }
      final String token = activeBearerToken.token();
      request.getHeaders().setBearerAuth(token);
    }
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

  /**
   * Retrieves a new Bearer token if the current one is expired or null.
   *
   * @return the active Bearer token
   */
  private ActiveBearerToken getBearerToken() {
    if (this.activeBearerToken != null
        && activeBearerToken.expiresAt() > System.currentTimeMillis()) {
      return activeBearerToken;
    }

    final String url = bearerTokenCredentials.getUrl();
    Assert.notNull(url, "Bearer token url must not be null");
    HttpHeaders headers = getHttpHeaders();
    HttpEntity<MultiValueMap<String, String>> tokenRequest =
        new HttpEntity<>(pullAllCredentials(bearerTokenCredentials), headers);
    try {
      ResponseEntity<Object> tokenResponse = restTemplate.postForEntity(url, tokenRequest,
          Object.class);
      if (tokenResponse.getStatusCode() == HttpStatus.OK) {
        Object body = tokenResponse.getBody();
        if (Objects.isNull(body)) {
          throw new AdapterException(
              AdapterStatus.createStatusFailedAsTechnical("Bearer token response body is null"));
        }
        final String token = JsonPath.read(body, bearerTokenCredentials.getTokenJsonPath());
        if (StringUtils.isBlank(token)) {
          throw new AdapterException(
              AdapterStatus.createStatusFailedAsTechnical("Bearer token is null or empty"));
        }
        return new ActiveBearerToken(token,
            System.currentTimeMillis() + bearerTokenCredentials.getTtl());
      }
      throw new AdapterException(
          AdapterStatus.createStatusFailedAsTechnical(
              "Bearer token http response status is not OK"));
    } catch (ResourceAccessException e) {
      throw new AdapterException(e, AdapterStatus.createStatusFailedAsTechnical(e));
    }
  }

  /**
   * Creates HTTP headers for the token request.
   *
   * @return the HTTP headers
   */
  private HttpHeaders getHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(bearerTokenCredentials.getContentType() != null ? MediaType.valueOf(
        bearerTokenCredentials.getContentType()) : MediaType.APPLICATION_FORM_URLENCODED);
    headers.setAccept(Collections.singletonList(
        bearerTokenCredentials.getAccept() != null ? MediaType.valueOf(
            bearerTokenCredentials.getAccept()) : MediaType.APPLICATION_JSON));
    return headers;
  }

  /**
   * Pulls all credentials from the BearerTokenCredentials object.
   *
   * @param bearerTokenCredentials the credentials required to obtain the Bearer token
   * @return a MultiValueMap containing the credentials
   */
  private MultiValueMap<String, String> pullAllCredentials(
      BearerTokenCredentials bearerTokenCredentials) {
    MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
    Map<String, String> tokenMap = new LinkedHashMap<>();
    tokenMap.put("username", bearerTokenCredentials.getUsername());
    tokenMap.put("password", bearerTokenCredentials.getPassword());
    tokenMap.put("client_id", bearerTokenCredentials.getClientId());
    tokenMap.put("client_secret", bearerTokenCredentials.getClientSecret());
    tokenMap.put("grant_type", bearerTokenCredentials.getGrantType());
    tokenMap.put("scope", bearerTokenCredentials.getScope());
    multiValueMap.setAll(tokenMap);
    return multiValueMap;
  }

  /**
   * Record class representing an active Bearer token.
   */
  private record ActiveBearerToken(String token, long expiresAt) {

  }

}