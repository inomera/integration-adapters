package com.inomera.middleware.client.interceptor.auth;

import com.inomera.integration.config.model.BearerTokenCredentials;
import com.inomera.integration.fault.AdapterException;
import com.inomera.integration.model.AdapterStatus;
import com.jayway.jsonpath.JsonPath;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public class BaseBearerTokenProvider {

  private final BearerTokenCredentials bearerTokenCredentials;
  private final RestTemplate restTemplate;

  protected ActiveBearerToken activeBearerToken;

  public BaseBearerTokenProvider(BearerTokenCredentials bearerTokenCredentials,
      RestTemplate restTemplate) {
    this.bearerTokenCredentials = bearerTokenCredentials;
    this.restTemplate = restTemplate;
  }

  protected void checkAndSetTokenHeader(TokenSetter tokenSetter) {
    synchronized (this) {
      if (activeBearerToken == null
          || activeBearerToken.expiresAt() < System.currentTimeMillis()) {
        activeBearerToken = getBearerToken();
      }
      final String token = activeBearerToken.token();
      tokenSetter.setToken(token);
    }
  }

  /**
   * Retrieves a new Bearer token if the current one is expired or null.
   *
   * @return the active Bearer token
   */
  protected ActiveBearerToken getBearerToken() {
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
      ResponseEntity<String> tokenResponse = restTemplate.postForEntity(url, tokenRequest,
          String.class);
      if (tokenResponse.getStatusCode() == HttpStatus.OK) {
        String body = tokenResponse.hasBody() ? tokenResponse.getBody() : null;
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
  protected HttpHeaders getHttpHeaders() {
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
  protected MultiValueMap<String, String> pullAllCredentials(
      BearerTokenCredentials bearerTokenCredentials) {
    MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
    Map<String, String> tokenMap = new LinkedHashMap<>();
    addIfNotBlank(multiValueMap, "username", bearerTokenCredentials.getUsername());
    addIfNotBlank(multiValueMap, "password", bearerTokenCredentials.getPassword());
    addIfNotBlank(multiValueMap, "client_id", bearerTokenCredentials.getClientId());
    addIfNotBlank(multiValueMap, "client_secret", bearerTokenCredentials.getClientSecret());
    addIfNotBlank(multiValueMap, "grant_type", bearerTokenCredentials.getGrantType());
    addIfNotBlank(multiValueMap, "scope", bearerTokenCredentials.getScope());
    multiValueMap.setAll(tokenMap);
    return multiValueMap;
  }

  private void addIfNotBlank(MultiValueMap<String, String> map, String key, String value) {
    if (StringUtils.isNotBlank(value)) {
      map.add(key, value);
    }
  }

  /**
   * Record class representing an active Bearer token.
   */
  protected record ActiveBearerToken(String token, long expiresAt) {

  }

}
