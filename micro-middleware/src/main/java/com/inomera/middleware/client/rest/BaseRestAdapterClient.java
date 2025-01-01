package com.inomera.middleware.client.rest;

import com.inomera.integration.auth.AuthType;
import com.inomera.integration.client.HttpAdapterClient;
import com.inomera.integration.client.HttpRestAdapterClient;
import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.integration.config.model.AdapterLogging;
import com.inomera.integration.config.model.AuthHeadersCredentials;
import com.inomera.integration.config.model.BasicAuthCredentials;
import com.inomera.integration.config.model.BearerTokenCredentials;
import com.inomera.integration.fault.AdapterAuthenticationException;
import com.inomera.integration.fault.AdapterException;
import com.inomera.integration.fault.AdapterIOException;
import com.inomera.integration.fault.AdapterSerializationException;
import com.inomera.integration.model.AdapterStatus;
import com.inomera.integration.model.HttpAdapterRequest;
import com.inomera.integration.model.HttpAdapterResponse;
import com.inomera.middleware.client.interceptor.auth.BearerTokenInterceptor;
import com.inomera.middleware.client.interceptor.auth.HttpHeaderInterceptor;
import com.inomera.middleware.client.interceptor.log.RestLoggingInterceptor;
import com.inomera.middleware.util.HeaderUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Base rest adapter implementation of {@link HttpAdapterClient}. Uses spring rest template as
 * intermediate layer to make http level configurations in a standard way.
 * <p>
 * User can provide {@link ClientHttpRequestFactory} or {@link Supplier<AdapterConfig>} with
 * {@link ClientHttpRequestFactory} type to create a rest template. Both way there will be an
 * instance of rest template that uses an implementation of {@link ClientHttpRequestFactory}. User
 * can create a custom {@link ClientHttpRequestFactory} to use custom configurations.
 *
 * @author Burak Dogan Akyildiz, Turgay Can.
 */
@Slf4j
public abstract class BaseRestAdapterClient implements HttpRestAdapterClient {

  private final RestTemplate restTemplate;

  public BaseRestAdapterClient(ClientHttpRequestFactory clientHttpRequestFactory) {
    this.restTemplate = new RestTemplateBuilder()
        .requestFactory(() -> new BufferingClientHttpRequestFactory(clientHttpRequestFactory))
        .build();
    this.restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
  }

  public BaseRestAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      ClientHttpRequestFactory clientHttpRequestFactory) {
    Assert.notNull(configSupplierFunc, "AdapterConfig cannot be null");
    Assert.notNull(clientHttpRequestFactory, "ClientHttpRequestFactory cannot be null");
    AdapterConfig adapterConfig = configSupplierFunc.get();
    //TODO : AdapterConfig should be checked for null or not
    Assert.notNull(adapterConfig, "AdapterConfig cannot be null");
    final AdapterLogging adapterLogging = adapterConfig.getAdapterLogging();

    //TODO : enrich interceptors

    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    if (adapterConfig.getAdapterProperties().getAuth().getType() == AuthType.BASIC) {
      BasicAuthCredentials basicAuthCredentials = (BasicAuthCredentials) adapterConfig.getAdapterProperties()
          .getAuth();
      Assert.notNull(basicAuthCredentials, "BasicAuthentication cannot be null");
      BasicAuthenticationInterceptor basicAuthenticationInterceptor = new BasicAuthenticationInterceptor(
          basicAuthCredentials.getUsername(), basicAuthCredentials.getPassword());
      interceptors.add(basicAuthenticationInterceptor);
    }
    if (adapterConfig.getAdapterProperties().getAuth().getType() == AuthType.HEADER) {
      AuthHeadersCredentials authHeadersCredentials = (AuthHeadersCredentials) adapterConfig.getAdapterProperties()
          .getAuth();
      Assert.notNull(authHeadersCredentials, "AuthHeaders cannot be null");
      interceptors.add(new HttpHeaderInterceptor(authHeadersCredentials.getHeaders()));
    }

    if (adapterConfig.getAdapterProperties().getAuth().getType() == AuthType.BEARER) {
      BearerTokenCredentials bearerTokenCredentials = (BearerTokenCredentials) adapterConfig.getAdapterProperties()
          .getAuth();
      Assert.notNull(bearerTokenCredentials, "BearerToken cannot be null");
      interceptors.add(new BearerTokenInterceptor(bearerTokenCredentials));
    }

    RestLoggingInterceptor restLoggingInterceptor = new RestLoggingInterceptor(adapterLogging);
    interceptors.add(restLoggingInterceptor);

    this.restTemplate = new RestTemplateBuilder()
        .requestFactory(() -> new BufferingClientHttpRequestFactory(clientHttpRequestFactory))
        .interceptors(interceptors)
        .build();
    this.restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
  }

  public BaseRestAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      Class<? extends ClientHttpRequestFactory> clientHttpRequestFactoryType) {
    Assert.notNull(configSupplierFunc, "AdapterConfig cannot be null");
    Assert.notNull(clientHttpRequestFactoryType, "clientHttpRequestFactoryType cannot be null");
    AdapterConfig adapterConfig = configSupplierFunc.get();
    //TODO : AdapterConfig should be checked for null or not
    Assert.notNull(adapterConfig, "AdapterConfig cannot be null");
    final AdapterLogging adapterLogging = adapterConfig.getAdapterLogging();

    this.restTemplate = new RestTemplateBuilder()
        .requestFactory(settings -> new BufferingClientHttpRequestFactory(
            ClientHttpRequestFactories.get(clientHttpRequestFactoryType,
                settings
                    .withConnectTimeout(Duration.ofMillis(
                        adapterConfig.getAdapterProperties().getHttp().getConnectTimeout()))
                    .withReadTimeout(Duration.ofMillis(
                        adapterConfig.getAdapterProperties().getHttp().getRequestTimeout()))
//                    .withSslBundle(adapterConfig.getAdapterProperties().getHttp().isSkipSsl()) //TODO : SSLBundle with ssl forge library
            )))
        .interceptors(new RestLoggingInterceptor(adapterLogging),
            new BasicAuthenticationInterceptor("user",
                "pass")) //TODO : Auth provider or config value pass using rest template abstraction
        .build();
    this.restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
  }

  @Override
  public <RESP> HttpAdapterResponse<RESP> send(HttpAdapterRequest httpAdapterRequest,
      Class<RESP> responseType) throws AdapterException {
    try {
      var headers = httpAdapterRequest.getHeaders() != null ? new LinkedMultiValueMap<>(
          HeaderUtils.convertToListMap(httpAdapterRequest.getHeaders()))
          : new LinkedMultiValueMap<String, String>();

      headers.addIfAbsent(HttpHeaders.CONTENT_TYPE,
          getHeaderOrDefault(httpAdapterRequest, HttpHeaders.CONTENT_TYPE,
              MimeTypeUtils.APPLICATION_JSON_VALUE));
      headers.addIfAbsent(HttpHeaders.ACCEPT,
          getHeaderOrDefault(httpAdapterRequest, HttpHeaders.ACCEPT,
              MimeTypeUtils.APPLICATION_JSON_VALUE));
      headers.addIfAbsent(HttpHeaders.ACCEPT_CHARSET,
          getHeaderOrDefault(httpAdapterRequest, HttpHeaders.ACCEPT_CHARSET,
              StandardCharsets.UTF_8.name()));

      var httpEntity = new HttpEntity<>(httpAdapterRequest.getRequestBody(), headers);
      var respResponseEntity = this.restTemplate.exchange(
          httpAdapterRequest.getUrl(),
          HttpMethod.valueOf(httpAdapterRequest.getMethod().name()),
          httpEntity,
          responseType
      );
      return new HttpAdapterResponse<>(
          respResponseEntity.getStatusCode().value(),
          respResponseEntity.getHeaders().toSingleValueMap(),
          respResponseEntity.getBody()
      );
    } catch (RestClientException e) {
      LOG.error("RestClientException exception occurred", e);
      throw createException(e, httpAdapterRequest);
    }
  }

  private String getHeaderOrDefault(HttpAdapterRequest httpAdapterRequest, String header,
      String defaultVal) {
    return StringUtils.defaultIfBlank(httpAdapterRequest.getHeaders().get(header), defaultVal);
  }

  private AdapterException createException(
      RestClientException restClientException,
      HttpAdapterRequest httpAdapterRequest
  ) {
    if (restClientException instanceof HttpClientErrorException e &&
        HttpStatus.FORBIDDEN.equals(e.getStatusCode()) &&
        e.getResponseBodyAsString().startsWith("Invalid Token")
    ) {
      return new AdapterAuthenticationException(e, AdapterStatus.createStatusFailedAsTechnical(e));
    } else if (restClientException instanceof ResourceAccessException ra) {
      if (ra.getCause() instanceof HttpMessageConversionException ha) {
        return new AdapterSerializationException(ha,
            AdapterStatus.createStatusFailedAsTechnical(ha));
      }
      if (ra.getCause() instanceof IOException io) {
        return new AdapterIOException(io, httpAdapterRequest,
            AdapterStatus.createStatusFailedAsTechnical(io));
      }
      return new AdapterException(ra, AdapterStatus.createStatusFailedAsTechnical(ra));
    }
    return new AdapterException(restClientException,
        AdapterStatus.createStatusFailedAsTechnical(restClientException));
  }
}
