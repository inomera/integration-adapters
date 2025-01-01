package com.inomera.middleware.client.interceptor;

import static com.inomera.middleware.client.interceptor.util.Samples.SAMPLE_SOAP_REQUEST;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.inomera.integration.config.model.AdapterLogging;
import com.inomera.integration.config.model.LogStrategy;
import com.inomera.middleware.client.interceptor.log.SoapLoggingInterceptor;
import com.inomera.middleware.client.interceptor.util.HttpTestClientRequest;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.classic.MinimalHttpClient;
import org.apache.hc.core5.http.protocol.BasicHttpContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ws.context.DefaultMessageContext;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.context.DefaultTransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.ClientHttpRequestConnection;

class SoapAdapterLoggingInterceptorTest {

  private DefaultMessageContext messageContext;

  @BeforeEach
  public void init() throws SOAPException, IOException {
    final HttpPost httpPost = new HttpPost("http://country.org");
    final MinimalHttpClient minimalHttpClient = HttpClients.createMinimal();
    final HttpTestClientRequest request = new HttpTestClientRequest(minimalHttpClient, httpPost,
        new BasicHttpContext());
    request.getHeaders().add("Authorization", "Basic 2398nxyd7832ydx2=");
    final ClientHttpRequestConnection connection = new ClientHttpRequestConnection(request);
    final DefaultTransportContext context = new DefaultTransportContext(connection);
    TransportContextHolder.setTransportContext(context);
    final SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory(
        MessageFactory.newInstance());
    final ByteArrayInputStream requestAsBA = new ByteArrayInputStream(
        SAMPLE_SOAP_REQUEST.getBytes(StandardCharsets.UTF_8));
    final SaajSoapMessage webServiceMessage = messageFactory.createWebServiceMessage(requestAsBA);
    this.messageContext = new DefaultMessageContext(webServiceMessage, messageFactory);
  }

  @DisplayName("log request with only failure")
  @Test
  void handleRequest_withLogStrategyAsFailure() {
    SoapLoggingInterceptor soapLoggingInterceptor = new SoapLoggingInterceptor(
        new AdapterLogging(LogStrategy.FAILURE));
    boolean handled = soapLoggingInterceptor.handleRequest(this.messageContext);

    assertTrue(handled);
  }

  @DisplayName("log request without logdetail")
  @Test
  void handleRequest_withLogStrategyConstructor() {
    SoapLoggingInterceptor soapLoggingInterceptor = new SoapLoggingInterceptor(
        new AdapterLogging(LogStrategy.REQ_RES));
    boolean handled = soapLoggingInterceptor.handleRequest(this.messageContext);

    assertTrue(handled);
  }

  @DisplayName("log request including non-logged fields with logdetail")
  @Test
  void handleRequest_withAdapterLoggingConstructor() {
    AdapterLogging AdapterLogging = new AdapterLogging(LogStrategy.REQ_RES, List.of("corpCode"),
        List.of("contentBytes"));
    SoapLoggingInterceptor soapLoggingInterceptor = new SoapLoggingInterceptor(AdapterLogging);
    boolean handled = soapLoggingInterceptor.handleRequest(this.messageContext);

    assertTrue(handled);
  }

  @DisplayName("log request including non-logged fields with all logdetail")
  @Test
  void handleRequest_withAdapterLoggingAll() {
    AdapterLogging AdapterLogging = new AdapterLogging(LogStrategy.ALL,
        List.of("corpCode", "Authorization"),
        List.of("contentBytes"));
    SoapLoggingInterceptor soapLoggingInterceptor = new SoapLoggingInterceptor(AdapterLogging);
    boolean handled = soapLoggingInterceptor.handleRequest(this.messageContext);

    assertTrue(handled);
  }

}