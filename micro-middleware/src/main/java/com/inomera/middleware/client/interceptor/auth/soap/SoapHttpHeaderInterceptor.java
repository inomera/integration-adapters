package com.inomera.middleware.client.interceptor.auth.soap;

import com.inomera.integration.auth.AuthenticationProvider;
import com.inomera.middleware.client.interceptor.auth.rest.RestHttpHeaderInterceptor;
import jakarta.xml.soap.MimeHeaders;
import java.io.IOException;
import java.util.Map;
import javax.xml.namespace.QName;
import lombok.extern.slf4j.Slf4j;
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
 * A {@link ClientInterceptor} implementation that adds headers to the request headers.
 */
@Slf4j
public class SoapHttpHeaderInterceptor implements ClientInterceptor, AuthenticationProvider {

  private final Map<String, String> headers;

  /**
   * Create a new instance of the {@link RestHttpHeaderInterceptor} with the provided headers.
   *
   * @param headers the headers to add to the request
   */
  public SoapHttpHeaderInterceptor(Map<String, String> headers) {
    Assert.notNull(headers, "headers Cannot be null");
    this.headers = headers;
  }

  /**
   * Intercepts the HTTP request to add the headers.
   *
   * @param messageContext the message context
   * @return true if the request should be processed; false if the interceptor should be skipped
   */
  @Override
  public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
    if (messageContext.getRequest() instanceof SaajSoapMessage soapMessage) {
      MimeHeaders mimeHeader = soapMessage.getSaajMessage().getMimeHeaders();
      headers.forEach(mimeHeader::setHeader);
      return true;
    }
    if (messageContext.getRequest() instanceof SoapMessage soapMessage) {
      SoapHeader soapHeader = soapMessage.getSoapHeader();
      headers.forEach((key, value) -> soapHeader.addHeaderElement(new QName(key))
          .setText(value));
      return true;
    }
    TransportContext context = TransportContextHolder.getTransportContext();
    HeadersAwareSenderWebServiceConnection connection = (HeadersAwareSenderWebServiceConnection) context.getConnection();
    headers.forEach((key, value) -> {
      try {
        connection.addRequestHeader(key, value);
      } catch (IOException e) {
        LOG.error("Error while adding key:{}, value:{} header", key, value, e);
      }
    });
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