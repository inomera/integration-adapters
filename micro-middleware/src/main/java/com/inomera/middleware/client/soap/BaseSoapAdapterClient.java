package com.inomera.middleware.client.soap;

import com.inomera.integration.auth.AuthType;
import com.inomera.integration.client.HttpAdapterClient;
import com.inomera.integration.client.HttpSoapAdapterClient;
import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.integration.config.model.AuthHeadersCredentials;
import com.inomera.integration.config.model.BasicAuthCredentials;
import com.inomera.integration.config.model.BearerTokenCredentials;
import com.inomera.integration.config.model.HttpClientProperties;
import com.inomera.integration.constant.Status;
import com.inomera.integration.fault.AdapterAuthenticationException;
import com.inomera.integration.fault.AdapterIOException;
import com.inomera.integration.fault.AdapterSerializationException;
import com.inomera.integration.model.AdapterStatus;
import com.inomera.integration.model.HttpAdapterRequest;
import com.inomera.integration.model.HttpAdapterResponse;
import com.inomera.middleware.client.interceptor.auth.soap.SoapBasicAuthenticationInterceptor;
import com.inomera.middleware.client.interceptor.auth.soap.SoapDefaultBearerTokenInterceptor;
import com.inomera.middleware.client.interceptor.auth.soap.SoapHttpHeaderInterceptor;
import com.inomera.middleware.client.interceptor.auth.soap.SoapNoneAuthInterceptor;
import com.inomera.middleware.client.interceptor.log.SoapLoggingInterceptor;
import com.inomera.middleware.util.SslBundleUtils;
import jakarta.xml.bind.JAXBElement;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.webservices.client.HttpWebServiceMessageSenderBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.ws.FaultAwareWebServiceMessage;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.WebServiceTransportException;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.support.MarshallingUtils;
import org.springframework.ws.transport.HeadersAwareSenderWebServiceConnection;
import org.springframework.ws.transport.TransportConstants;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;


/**
 * Base soap adapter implementation of {@link HttpAdapterClient}. Uses spring
 * {@link WebServiceGatewaySupport} as intermediate layer to make http level configurations in a
 * standard way.
 * <p>
 * User can provide {@link ClientHttpRequestFactory} or {@link Supplier<AdapterConfig>} with
 * {@link ClientHttpRequestFactory} type to create a {@link WebServiceMessageSender}. Both way there
 * will be an instance of {@link WebServiceMessageSender} that uses an implementation of
 * {@link ClientHttpRequestFactory}. User can create a custom {@link ClientHttpRequestFactory} to
 * use custom configurations.
 * <p>
 * User can also optionally provide {@link WebServiceMessageSender} or
 * {@link WebServiceMessageFactory} to customize this classes.
 * <p>
 * This class uses {@link Jaxb2Marshaller} as default marshaller. User can provide his/her own
 * marshaller.
 *
 * @author Burak Dogan Akyildiz, Turgay Can.
 */
@Slf4j
public abstract class BaseSoapAdapterClient extends WebServiceGatewaySupport implements
    HttpSoapAdapterClient {

  private final ConcurrentHashMap<String, ReentrantLock> reloadSoapLockMap = new ConcurrentHashMap<>();

  private final String[] marshallerContextPath;
  private final WebServiceMessageSender webServiceMessageSender;
  private final Supplier<AdapterConfig> configSupplierFunc;

  private Marshaller marshaller;
  private Unmarshaller unmarshaller;

  /**
   * Static configs without dynamic configs constructor!!
   *
   * @param webServiceMessageSender
   * @param marshallerContextPath
   */
  public BaseSoapAdapterClient(WebServiceMessageSender webServiceMessageSender,
      String... marshallerContextPath) {
    Assert.notNull(webServiceMessageSender, "webServiceMessageSender cannot be null");
    Assert.notNull(marshallerContextPath, "marshallerContextPath cannot be null");

    this.marshallerContextPath = marshallerContextPath;
    this.webServiceMessageSender = webServiceMessageSender;
    this.configSupplierFunc = () -> null;
  }

  /**
   * Lazy configs init constructor
   *
   * @param configSupplierFunc
   * @param webServiceMessageSender
   * @param marshallerContextPath
   */
  public BaseSoapAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      WebServiceMessageSender webServiceMessageSender, String... marshallerContextPath) {
    Assert.notNull(configSupplierFunc, "configSupplierFunc cannot be null");
    Assert.notNull(webServiceMessageSender, "webServiceMessageSender cannot be null");
    Assert.notNull(marshallerContextPath, "marshallerContextPath cannot be null");

    this.marshallerContextPath = marshallerContextPath;
    this.webServiceMessageSender = webServiceMessageSender;
    this.configSupplierFunc = configSupplierFunc;
  }

  /**
   * Eager configs init constructor
   *
   * @param configSupplierFunc
   * @param requestFactoryClass
   * @param marshallerContextPath
   */
  public BaseSoapAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      Class<? extends ClientHttpRequestFactory> requestFactoryClass,
      String... marshallerContextPath) {
    Assert.notNull(configSupplierFunc, "configSupplierFunc cannot be null");
    Assert.notNull(requestFactoryClass, "requestFactoryClass cannot be null");
    Assert.notNull(marshallerContextPath, "marshallerContextPath cannot be null");
    AdapterConfig adapterConfig = configSupplierFunc.get();
    Assert.notNull(adapterConfig, "AdapterConfig cannot be null");

    this.configSupplierFunc = configSupplierFunc;
    HttpClientProperties http = adapterConfig.getAdapterProperties().getHttp();
    // duplicate config is because of a spring bug(closed) -> https://github.com/spring-projects/spring-boot/issues/35658
    Duration connectTimeout = Duration.ofMillis(http.getConnectTimeout());
    Duration readTimeout = Duration.ofMillis(http.getRequestTimeout());
    final SslBundle sslBundle = SslBundleUtils.createSslBundle(http, adapterConfig.getUrl());
    this.webServiceMessageSender = new HttpWebServiceMessageSenderBuilder()
        .setConnectTimeout(connectTimeout)
        .setReadTimeout(readTimeout)
        .sslBundle(sslBundle)
        .requestFactory(
            (settings -> ClientHttpRequestFactories.get(requestFactoryClass,
                settings.withConnectTimeout(connectTimeout)
                    .withReadTimeout(readTimeout)
                    .withSslBundle(sslBundle))))
        .build();
    this.marshallerContextPath = marshallerContextPath;
  }


  /**
   * Lazy configs init constructor
   *
   * @param configSupplierFunc
   * @param webServiceMessageSender
   * @param marshaller
   * @param unmarshaller
   * @param marshallerContextPath
   */
  public BaseSoapAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      WebServiceMessageSender webServiceMessageSender, Marshaller marshaller,
      Unmarshaller unmarshaller, String... marshallerContextPath) {
    Assert.notNull(configSupplierFunc, "configSupplierFunc cannot be null");
    Assert.notNull(webServiceMessageSender, "webServiceMessageSender cannot be null");
    Assert.notNull(marshaller, "marshaller cannot be null");
    Assert.notNull(unmarshaller, "unmarshaller cannot be null");
    Assert.notNull(marshallerContextPath, "marshallerContextPath cannot be null");

    this.configSupplierFunc = configSupplierFunc;
    this.webServiceMessageSender = webServiceMessageSender;
    this.marshaller = marshaller;
    this.unmarshaller = unmarshaller;
    this.marshallerContextPath = marshallerContextPath;
  }

  /**
   * Lazy configs init constructor
   *
   * @param configSupplierFunc
   * @param webServiceMessageSender
   * @param marshaller
   * @param unmarshaller
   * @param webServiceMessageFactory
   * @param marshallerContextPath
   */
  public BaseSoapAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      WebServiceMessageSender webServiceMessageSender, Marshaller marshaller,
      Unmarshaller unmarshaller, WebServiceMessageFactory webServiceMessageFactory,
      String... marshallerContextPath) {
    super(webServiceMessageFactory);
    Assert.notNull(configSupplierFunc, "configSupplierFunc cannot be null");
    Assert.notNull(webServiceMessageSender, "webServiceMessageSender cannot be null");
    Assert.notNull(marshaller, "marshaller cannot be null");
    Assert.notNull(unmarshaller, "unmarshaller cannot be null");
    Assert.notNull(marshallerContextPath, "marshallerContextPath cannot be null");

    this.configSupplierFunc = configSupplierFunc;
    this.webServiceMessageSender = webServiceMessageSender;
    this.marshaller = marshaller;
    this.unmarshaller = unmarshaller;
    this.marshallerContextPath = marshallerContextPath;
  }

  /**
   * Execute after every constructor
   */
  protected void initGateway() {
    try {
      if (this.marshaller == null || this.unmarshaller == null) {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        //default scan type is package scan, another one is context path
        marshaller.setPackagesToScan(this.marshallerContextPath);
        marshaller.afterPropertiesSet();
        if (this.marshaller == null) {
          this.marshaller = marshaller;
        }

        if (this.unmarshaller == null) {
          this.unmarshaller = marshaller;
        }
      }

      getWebServiceTemplate().setMarshaller(this.marshaller);
      getWebServiceTemplate().setUnmarshaller(this.unmarshaller);
      final AdapterConfig adapterConfig = this.configSupplierFunc.get();
      if (adapterConfig == null) {
        setMessageSender(this.webServiceMessageSender);
        return;
      }

      enrichWebserviceTemplateWithHttpConfigs(adapterConfig);
    } catch (Exception e) {
      LOG.error("WebServiceClient marshaller couldn't be initialized from context path : "
          + Arrays.toString(this.marshallerContextPath) + " Error : " + e.getMessage(), e);
    }
  }

  @Override
  public <O> HttpAdapterResponse<O> send(HttpAdapterRequest httpAdapterRequest,
      Class<O> responseType) {
    try {
      if (this.reloadRuntime()) {
        AdapterConfig adapterConfig = this.configSupplierFunc.get();
        enrichWebserviceTemplateWithHttpConfigs(adapterConfig);
      }
      ResponseAndHeader responseAndHeaders = getWebServiceTemplate().sendAndReceive(
          httpAdapterRequest.getUrl(), message -> this.handleRequest(httpAdapterRequest, message),
          (WebServiceMessageExtractor<? extends ResponseAndHeader>) this::handleResponse);
      if (responseAndHeaders == null) {
        throw new AdapterSerializationException(null,
            new AdapterStatus(Status.TECHNICAL_ERROR, Status.TECHNICAL_ERROR.getCode(),
                "Response is null"));
      }
      O response = (O) responseAndHeaders.getResponse();
      if (response instanceof JAXBElement) {
        response = ((JAXBElement<O>) response).getValue();
      }
      return new HttpAdapterResponse<>(HttpStatus.OK.value(), responseAndHeaders.getHeaders(),
          response);
    } catch (WebServiceTransportException te) {
      if (isAuthProblem(httpAdapterRequest, te)) {
        throw new AdapterAuthenticationException(te,
            AdapterStatus.createStatusFailedAsTechnical(te));
      }
      throw new AdapterSerializationException(te, AdapterStatus.createStatusFailedAsTechnical(te));
    } catch (WebServiceIOException io) {
      throw new AdapterIOException((IOException) io.getCause(), httpAdapterRequest,
          AdapterStatus.createStatusFailedAsTechnical(io));
    }
  }

  @Override
  public boolean reloadRuntime() {
    if (null == this.configSupplierFunc) {
      return false;
    }
    AdapterConfig adapterConfig = this.configSupplierFunc.get();
    if (null == adapterConfig) {
      return false;
    }
    return adapterConfig.getAdapterProperties()
        .isRuntime() && adapterConfig.isRefresh();
  }

  private List<ClientInterceptor> getClientHttpRequestInterceptors(AdapterConfig adapterConfig) {
    List<ClientInterceptor> interceptors = new ArrayList<>();
    final ClientInterceptor authInterceptor = getAuthInterceptor(adapterConfig);
    interceptors.add(authInterceptor);
    interceptors.add(new SoapLoggingInterceptor(adapterConfig.getAdapterLogging()));
    return interceptors;
  }

  private ClientInterceptor getAuthInterceptor(AdapterConfig adapterConfig) {
    AuthType authType = adapterConfig.getAdapterProperties().getAuth().getType();
    switch (authType) {
      case BASIC -> {
        BasicAuthCredentials basicAuth = (BasicAuthCredentials) adapterConfig.getAdapterProperties()
            .getAuth();
        Assert.notNull(basicAuth, "BasicAuthentication cannot be null");
        return new SoapBasicAuthenticationInterceptor(basicAuth.getUsername(),
            basicAuth.getPassword());
      }
      case HEADER -> {
        AuthHeadersCredentials headerAuth = (AuthHeadersCredentials) adapterConfig.getAdapterProperties()
            .getAuth();
        Assert.notNull(headerAuth, "AuthHeaders cannot be null");
        Map<String, String> headers = headerAuth.getHeadersAsStringMap();
        return new SoapHttpHeaderInterceptor(headers);
      }
      case BEARER -> {
        BearerTokenCredentials bearerAuth = (BearerTokenCredentials) adapterConfig.getAdapterProperties()
            .getAuth();
        Assert.notNull(bearerAuth, "BearerToken cannot be null");
        return new SoapDefaultBearerTokenInterceptor(bearerAuth);
      }
      default -> {
        return new SoapNoneAuthInterceptor();
      }
    }
  }

  protected boolean isAuthProblem(HttpAdapterRequest httpAdapterRequest,
      WebServiceTransportException te) {
    return !httpAdapterRequest.getHeaders().isEmpty() && te.getMessage()
        .contains(String.valueOf(HttpStatus.FORBIDDEN.value()));
  }

  private void enrichWebserviceTemplateWithHttpConfigs(AdapterConfig adapterConfig) {
    ReentrantLock configLock = reloadSoapLockMap.computeIfAbsent(adapterConfig.getKey(),
        key -> new ReentrantLock());
    configLock.lock();
    try {
      getWebServiceTemplate().setDefaultUri(adapterConfig.getUrl());
      // custom interceptors
      List<ClientInterceptor> interceptors = new ArrayList<>();
      if (!ObjectUtils.isEmpty(getWebServiceTemplate().getInterceptors())) {
        interceptors.addAll(Arrays.stream(getWebServiceTemplate().getInterceptors()).toList());
      }
      // cross-cut interceptors security, logging
      List<ClientInterceptor> clientHttpRequestInterceptors = getClientHttpRequestInterceptors(
          adapterConfig);
      interceptors.addAll(clientHttpRequestInterceptors);
      getWebServiceTemplate().setInterceptors(interceptors.toArray(new ClientInterceptor[]{}));

      setMessageSender(this.webServiceMessageSender);
      LOG.info("WebServiceTemplate reloaded at runtime with new configuration. key : {}, adapterConfig : {}",
          adapterConfig.getKey(), adapterConfig.toSecureString());
    } finally {
      configLock.unlock();
      reloadSoapLockMap.computeIfPresent(adapterConfig.getKey(),
          (id, existingLock) -> existingLock.hasQueuedThreads() ? existingLock : null);
    }
  }

  private void handleRequest(HttpAdapterRequest httpAdapterRequest, WebServiceMessage message)
      throws IOException {
    TransportContext context = TransportContextHolder.getTransportContext();
    HeadersAwareSenderWebServiceConnection connection = (HeadersAwareSenderWebServiceConnection) context.getConnection();
    //runs before the request is sent.
    if (httpAdapterRequest.getHeaders() != null) {
      for (Map.Entry<String, String> header : httpAdapterRequest.getHeaders().entrySet()) {
        try {
          if (header.getKey().equals(TransportConstants.HEADER_SOAP_ACTION)) {
            final var soapMessage = (SoapMessage) message;
            soapMessage.setSoapAction(header.getValue());
            continue;
          }
          connection.addRequestHeader(header.getKey(), header.getValue());
        } catch (IOException e) {
          LOG.error("IOException occurred", e);
          throw new AdapterIOException(e, httpAdapterRequest,
              AdapterStatus.createStatusFailedAsTechnical(e));
        }
      }
    }
    MarshallingUtils.marshal(marshaller, httpAdapterRequest.getRequestBody(), message);
  }


  private ResponseAndHeader handleResponse(WebServiceMessage message) throws IOException {
    final Object response = MarshallingUtils.unmarshal(unmarshaller, message);

    if (message instanceof FaultAwareWebServiceMessage faultAwareWebServiceMessage
        && faultAwareWebServiceMessage.getFaultCode() != null) {
      String faultCode = faultAwareWebServiceMessage.getFaultCode().getLocalPart();
      if (faultCode != null && faultCode.equals(String.valueOf(HttpStatus.FORBIDDEN.value()))
          && response instanceof String strResp && strResp.startsWith("Invalid Token")) {
        throw new AdapterAuthenticationException(AdapterStatus.createStatusFailedAsTechnical());
      }
    }
    //TODO : callback function may be more readable
    TransportContext context = TransportContextHolder.getTransportContext();
    HeadersAwareSenderWebServiceConnection connection = (HeadersAwareSenderWebServiceConnection) context.getConnection();
    Map<String, String> headers = new HashMap<>();
    connection.getResponseHeaderNames().forEachRemaining(headerName -> {
      try {
        final Iterator<String> responseHeaders = connection.getResponseHeaders(headerName);
        if (responseHeaders.hasNext()) {
          headers.put(headerName, responseHeaders.next());
        }
      } catch (IOException e) {
        //swallow exception, just logging
        LOG.error("Error occurred when handling response", e);
      }
    });

    return new ResponseAndHeader(headers, response);
  }

  @Getter
  @AllArgsConstructor
  static class ResponseAndHeader {

    private Map<String, String> headers;
    private Object response;
  }
}
