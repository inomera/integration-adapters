package com.inomera.middleware.client.interceptor.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.inomera.integration.config.model.BearerTokenCredentials;
import com.inomera.integration.fault.AdapterException;
import com.inomera.middleware.client.interceptor.auth.BearerTokenInterceptor.ActiveBearerToken;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

class BearerTokenInterceptorTest {

  private static final String TOKEN_RESPONSE = "{\"token_type\":\"bearer\",\"access_token\":\"AAAAAAAAAAAAAAAAAAAAAMLheAAAAAAA0%2BuSeid%2BULvsea4JtiGRiSDSJSI%3DEUifiRBkKG5E2XzMDjRfl76ZC9Ub0wnz4XsNiRVBChTYbJcE3F\"}";
  private static final String EMPTY_TOKEN_RESPONSE = "{\"token_type\":\"bearer\",\"access_token\":\"\"}";

  private BearerTokenCredentials credentials;
  private BearerTokenInterceptor interceptor;
  private RestTemplate restTemplate;

  @BeforeEach
  void setUp() {
    credentials = mock(BearerTokenCredentials.class);
    when(credentials.getUrl()).thenReturn("http://example.com/token");
    when(credentials.getTokenJsonPath()).thenReturn("$.access_token");
    when(credentials.getTtl()).thenReturn(3600L);
    restTemplate = mock(RestTemplate.class);
    interceptor = new BearerTokenInterceptor(credentials, restTemplate);
    when(restTemplate.postForEntity(eq("http://example.com/token"), any(HttpEntity.class),
        eq(Object.class)))
        .thenReturn(new ResponseEntity<>(TOKEN_RESPONSE, HttpStatus.OK));
  }

  @Test
  void initialize() {
    ClientHttpRequest request = mock(ClientHttpRequest.class);
    HttpHeaders headers = new HttpHeaders();
    when(request.getHeaders()).thenReturn(headers);

    interceptor.initialize(request);

    assertNotNull(headers.get("Authorization"));
  }

  @Test
  void intercept() throws IOException {
    HttpRequest request = mock(HttpRequest.class);
    ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);
    HttpHeaders headers = new HttpHeaders();
    when(request.getHeaders()).thenReturn(headers);
    when(execution.execute(any(), any())).thenReturn(mock(ClientHttpResponse.class));

    ClientHttpResponse response = interceptor.intercept(request, new byte[0], execution);

    assertNotNull(headers.get("Authorization"));
    assertNotNull(response);
  }

  @Test
  void getHttpHeaders() {
    when(credentials.getContentType()).thenReturn("application/json");
    when(credentials.getAccept()).thenReturn("application/json");

    HttpHeaders headers = interceptor.getHttpHeaders();

    assertEquals("application/json", headers.getContentType().toString());
    assertEquals("application/json", headers.getAccept().get(0).toString());
  }

  @Test
  void pullAllCredentials() {
    when(credentials.getUsername()).thenReturn("user");
    when(credentials.getPassword()).thenReturn("pass");
    when(credentials.getClientId()).thenReturn("client_id");
    when(credentials.getClientSecret()).thenReturn("client_secret");
    when(credentials.getGrantType()).thenReturn("password");
    when(credentials.getScope()).thenReturn("scope");

    var credentialsMap = interceptor.pullAllCredentials(credentials);

    assertEquals("user", credentialsMap.getFirst("username"));
    assertEquals("pass", credentialsMap.getFirst("password"));
    assertEquals("client_id", credentialsMap.getFirst("client_id"));
    assertEquals("client_secret", credentialsMap.getFirst("client_secret"));
    assertEquals("password", credentialsMap.getFirst("grant_type"));
    assertEquals("scope", credentialsMap.getFirst("scope"));
  }

  @Test
  @Disabled
  void getBearerToken_whenTokenExpired() throws InterruptedException {
    ActiveBearerToken expiredToken = new ActiveBearerToken("expired_token",
        System.currentTimeMillis() - 1);
    interceptor = spy(new BearerTokenInterceptor(credentials, restTemplate));
    doReturn(expiredToken).when(interceptor).getBearerToken();
    Thread.sleep(2_000);
    assertThrows(AdapterException.class, () -> interceptor.getBearerToken());
  }

  @Test
  void getBearerToken_whenTokenResponseBodyIsNull() {
    when(credentials.getUrl()).thenReturn("http://example.com/token");
    when(credentials.getTokenJsonPath()).thenReturn("$.access_token");
    when(credentials.getTtl()).thenReturn(3600L);
    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Object.class)))
        .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

    assertThrows(AdapterException.class, () -> interceptor.getBearerToken());
  }

  @Test
  void getBearerToken_whenTokenIsNullOrEmpty() {
    when(credentials.getUrl()).thenReturn("http://example.com/token");
    when(credentials.getTokenJsonPath()).thenReturn("$.access_token");
    when(credentials.getTtl()).thenReturn(3600L);
    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Object.class)))
        .thenReturn(
            new ResponseEntity<>(EMPTY_TOKEN_RESPONSE, HttpStatus.OK));

    assertThrows(AdapterException.class, () -> interceptor.getBearerToken());
  }

  @Test
  void getBearerToken_whenResourceAccessException() {
    when(credentials.getUrl()).thenReturn("http://example.com/token");
    when(credentials.getTokenJsonPath()).thenReturn("$.access_token");
    when(credentials.getTtl()).thenReturn(3600L);
    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Object.class)))
        .thenThrow(new ResourceAccessException("Resource access exception"));

    assertThrows(AdapterException.class, () -> interceptor.getBearerToken());
  }

  @Test
  void getBearerToken_whenValidResponse() {
    when(restTemplate.postForEntity(anyString(), any(HttpEntity.class), eq(Object.class)))
        .thenReturn(new ResponseEntity<>(TOKEN_RESPONSE, HttpStatus.OK));

    ActiveBearerToken token = interceptor.getBearerToken();

    assertNotNull(token);
    assertEquals(
        "AAAAAAAAAAAAAAAAAAAAAMLheAAAAAAA0%2BuSeid%2BULvsea4JtiGRiSDSJSI%3DEUifiRBkKG5E2XzMDjRfl76ZC9Ub0wnz4XsNiRVBChTYbJcE3F",
        token.token());
  }
}