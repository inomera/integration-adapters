package com.inomera.middleware.client.interceptor.auth.soap;

import com.inomera.integration.auth.AuthenticationProvider;
import com.inomera.integration.config.model.BearerTokenCredentials;
import com.inomera.middleware.client.interceptor.auth.BaseBearerTokenProvider;
import jakarta.xml.soap.MimeHeaders;
import java.io.IOException;
import javax.xml.namespace.QName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
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
public class SoapDefaultBearerTokenInterceptor extends BaseBearerTokenProvider implements
    ClientInterceptor, AuthenticationProvider {

  /**
   * Constructor for BearerTokenInterceptor.
   *
   * @param bearerTokenCredentials the credentials required to obtain the Bearer token
   */
  public SoapDefaultBearerTokenInterceptor(BearerTokenCredentials bearerTokenCredentials) {
    super(bearerTokenCredentials, new RestTemplate());
  }

  public SoapDefaultBearerTokenInterceptor(BearerTokenCredentials bearerTokenCredentials,
      RestTemplate restTemplate) {
    super(bearerTokenCredentials, restTemplate);
  }

  @Override
  public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
    if (messageContext.getRequest() instanceof SaajSoapMessage soapMessage) {
      MimeHeaders mimeHeader = soapMessage.getSaajMessage().getMimeHeaders();
      checkAndSetTokenHeader(token -> mimeHeader.setHeader("Authorization",
          String.format("Bearer %s", token)));
      return true;
    }
    if (messageContext.getRequest() instanceof SoapMessage soapMessage) {
      SoapHeader soapHeader = soapMessage.getSoapHeader();
      checkAndSetTokenHeader(token -> soapHeader.addHeaderElement(new QName("Authorization"))
          .setText(String.format("Bearer %s", getBearerToken().token())));
      return true;
    }
    TransportContext context = TransportContextHolder.getTransportContext();
    HeadersAwareSenderWebServiceConnection connection = (HeadersAwareSenderWebServiceConnection) context.getConnection();
    checkAndSetTokenHeader(token -> {
      try {
        connection.addRequestHeader("Authorization",
            String.format("Bearer %s", token));
      } catch (IOException e) {
        LOG.error("Error while adding Authorization header", e);
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