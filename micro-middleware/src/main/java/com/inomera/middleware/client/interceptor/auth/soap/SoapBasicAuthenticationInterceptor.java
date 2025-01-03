package com.inomera.middleware.client.interceptor.auth.soap;

import com.inomera.integration.auth.AuthenticationProvider;
import jakarta.xml.soap.MimeHeaders;
import java.io.IOException;
import java.nio.charset.Charset;
import javax.xml.namespace.QName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import org.springframework.ws.transport.HeadersAwareSenderWebServiceConnection;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;

/**
 * A {@link ClientInterceptor} implementation that adds a Bearer token to the request headers.
 */
@Slf4j
public class SoapBasicAuthenticationInterceptor implements ClientInterceptor,
    AuthenticationProvider {

  private final String encodedCredentials;

  public SoapBasicAuthenticationInterceptor(String username, String password) {
    this(username, password, null);
  }

  public SoapBasicAuthenticationInterceptor(String username, String password, Charset charset) {
    Assert.notNull(username, "username cannot be null");
    Assert.notNull(password, "password cannot be null");
    this.encodedCredentials = HttpHeaders.encodeBasicAuth(username, password, charset);
  }


  @Override
  public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
    if (messageContext.getRequest() instanceof SaajSoapMessage soapMessage) {
      MimeHeaders mimeHeader = soapMessage.getSaajMessage().getMimeHeaders();
      mimeHeader.setHeader("Authorization", String.format("Basic %s", this.encodedCredentials));
      return true;
    }
    if (messageContext.getRequest() instanceof SoapMessage soapMessage) {
      SoapHeader soapHeader = soapMessage.getSoapHeader();
      soapHeader.addHeaderElement(new QName("Authorization"))
          .setText(String.format("Basic %s", this.encodedCredentials));
      return true;
    }
    TransportContext context = TransportContextHolder.getTransportContext();
    HeadersAwareSenderWebServiceConnection connection = (HeadersAwareSenderWebServiceConnection) context.getConnection();
    try {
      connection.addRequestHeader("Authorization",
          String.format("Basic %s", this.encodedCredentials));
    } catch (IOException e) {
      LOG.error("Error while adding Authorization header", e);
    }
    return true;
  }

  @Override
  public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
    return true;
  }

  @Override
  public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
    return true;
  }

  @Override
  public void afterCompletion(MessageContext messageContext, Exception ex)
      throws WebServiceClientException {

  }
}