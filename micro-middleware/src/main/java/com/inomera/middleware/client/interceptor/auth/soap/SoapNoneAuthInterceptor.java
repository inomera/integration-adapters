package com.inomera.middleware.client.interceptor.auth.soap;

import com.inomera.integration.auth.AuthenticationProvider;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;

/**
 * A {@link ClientInterceptor} implementation that adds no authentication to the request.
 */
public class SoapNoneAuthInterceptor implements ClientInterceptor, AuthenticationProvider {

  @Override
  public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
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
