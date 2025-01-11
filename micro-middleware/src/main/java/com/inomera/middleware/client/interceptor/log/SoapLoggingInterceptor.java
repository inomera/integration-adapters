package com.inomera.middleware.client.interceptor.log;

import com.inomera.integration.config.model.AdapterLogging;
import com.inomera.integration.config.model.LogStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.ClientHttpRequestConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/*
  bug case: https://github.com/spring-projects/spring-ws/issues/1054

   when 404 not found exception happened, afterCompletion is triggered twice. this is a spring ws bug.

   please take a look:
   WebServiceTemplate.doSendAndReceive() and  WebServiceTemplate.handleError()
*/
@Slf4j
public class SoapLoggingInterceptor extends BaseClientLoggingInterceptor implements
    ClientInterceptor {

  private static final String XML_MASK_PATTERN = "\\<${PRM}\\>(.*?)\\<\\/${PRM}\\>|${PRM}\\s*:(\\s*(\\\"(.*?)\\\"))|\\<[a-zA-Z0-9_]*:${PRM}\\>(.*?)\\<\\/[a-zA-Z0-9_]*:${PRM}\\>";

  /**
   * Create a new instance of the {@link SoapLoggingInterceptor} with the provided
   * {@link AdapterLogging}.
   *
   * @param adapterLogging the adapter logging configuration
   */
  public SoapLoggingInterceptor(AdapterLogging adapterLogging) {
    super(adapterLogging, XML_MASK_PATTERN);
  }

  /**
   * Intercepts the SOAP request to log the request and response.
   *
   * @param messageContext contains the outgoing request message
   * @return
   * @throws WebServiceClientException
   */
  @Override
  public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
    if (isOffOrFailure()) {
      return true;
    }
    LogContext.LogContextBuilder logContextBuilder = LogContext.builder();
    try {
      createRequestLoggerContext(messageContext, logContextBuilder);
    } catch (IOException e) {
      LOG.error("{}", logContextBuilder.build(), e);
      throw new WebServiceClientException("Can not write the SOAP request into the out stream", e) {
        @Serial
        private static final long serialVersionUID = 6228716949728543640L;
      };
    } finally {
      LogContext logContext = logContextBuilder.build();
      if (LogStrategy.REQ_RES == adapterLogging.getStrategy()) {
        LOG.info("{}", makeLogMoreSensibleIfHasAnyRule(logContext.toRequest()));
      }
      if (LogStrategy.ALL == adapterLogging.getStrategy()) {
        LOG.info("{}", makeLogMoreSensibleIfHasAnyRule(logContext.toRequestAll()));
      }
    }
    return true;
  }

  /**
   * Intercepts the SOAP response to log the request and response.
   */
  @Override
  public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
    if (isLoggingOff()) {
      return true;
    }
    LogContext.LogContextBuilder logContextBuilder = LogContext.builder();
    try {
      createResponseLoggerContext(messageContext, logContextBuilder);
    } catch (IOException e) {
      LOG.error("{}", logContextBuilder.build(), e);
      throw new WebServiceClientException("Can not write the SOAP request into the out stream", e) {
        @Serial
        private static final long serialVersionUID = -6912693030339155111L;
      };
    } finally {
      LogContext logContext = logContextBuilder.build();
      if (LogStrategy.ALL == adapterLogging.getStrategy()) {
        LOG.info("{}", makeLogMoreSensibleIfHasAnyRule(logContext.toResponseAll()));
      } else if (LogStrategy.REQ_RES == adapterLogging.getStrategy()) {
        LOG.info("{}", makeLogMoreSensibleIfHasAnyRule(logContext.toResponse()));
      }
    }
    return true;
  }

  /**
   * Intercepts the SOAP fault to log the request and response.
   */
  @Override
  public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
    if (isLoggingOff()) {
      return true;
    }
    LogContext.LogContextBuilder logContextBuilder = LogContext.builder();
    try {
      createRequestLoggerContext(messageContext, logContextBuilder);
      createResponseLoggerContext(messageContext, logContextBuilder);
    } catch (IOException e) {
      LOG.error("{}", logContextBuilder.build(), e);
      throw new WebServiceClientException("Can not write the SOAP request into the out stream", e) {
        @Serial
        private static final long serialVersionUID = 274916903863944015L;
      };
    } finally {
      LogContext logContext = logContextBuilder.build();
      if (LogStrategy.REQ_RES == adapterLogging.getStrategy()) {
        //TODO : split log content if any limitation of log infra one line
        LOG.info("{}", makeLogMoreSensibleIfHasAnyRule(logContext.toResponse()));
      } else if (LogStrategy.ALL == adapterLogging.getStrategy()) {
        LOG.info("{}", makeLogMoreSensibleIfHasAnyRule(logContext.toResponseAll()));
      } else if (isFailCase(logContext)) {
        LOG.info("{}", makeLogMoreSensibleIfHasAnyRule(logContext.toRequestAll()));
        LOG.info("{}", makeLogMoreSensibleIfHasAnyRule(logContext.toResponseAll()));
      }
    }
    return true;
  }

  @Override
  public void afterCompletion(MessageContext messageContext, Exception ex)
      throws WebServiceClientException {
  }

  private boolean isFailCase(LogContext logContext) {
    return LogStrategy.FAILURE == adapterLogging.getStrategy()
        && StringUtils.isNotBlank(logContext.responseBody());
  }

  private boolean isOffOrFailure() {
    if (isLoggingOff()) {
      return true;
    }
    return LogStrategy.FAILURE == adapterLogging.getStrategy();
  }

  private boolean isLoggingOff() {
    if (adapterLogging == null || adapterLogging.getStrategy() == null) {
      return true;
    }
    return LogStrategy.OFF == adapterLogging.getStrategy();
  }

  private void createRequestLoggerContext(MessageContext messageContext,
      LogContext.LogContextBuilder logContextBuilder) throws IOException {
    try {
      final URI uri = TransportContextHolder.getTransportContext().getConnection().getUri();
      logContextBuilder.uri(uri.toString());
    } catch (URISyntaxException e) {
      LOG.error("swallow URISyntaxException for logging", e);
    }
    ByteArrayOutputStream reqBuffer = new ByteArrayOutputStream();
    messageContext.getRequest().writeTo(reqBuffer);
    logContextBuilder.requestBody(reqBuffer.toString(StandardCharsets.UTF_8));
    WebServiceConnection connection = TransportContextHolder.getTransportContext()
        .getConnection();
    if (adapterLogging.getStrategy().allOrFailure()
        && connection instanceof ClientHttpRequestConnection h5Conn) {
      logContextBuilder.requestHeaders(h5Conn.getClientHttpRequest().getHeaders().toString());
    }
  }

  private void createResponseLoggerContext(MessageContext messageContext,
      LogContext.LogContextBuilder logContextBuilder) throws IOException {
    ByteArrayOutputStream respBuffer = new ByteArrayOutputStream();
    messageContext.getResponse().writeTo(respBuffer);
    logContextBuilder.responseBody(respBuffer.toString(StandardCharsets.UTF_8));
    WebServiceConnection connection = TransportContextHolder.getTransportContext()
        .getConnection();
    if (!(connection instanceof ClientHttpRequestConnection h5Conn)) {
      return;
    }
    if (adapterLogging.getStrategy().allOrFailure()) {
      logContextBuilder.responseHeaders(h5Conn.getClientHttpResponse().getHeaders().toString());
    }
    int statusCode = h5Conn.getClientHttpResponse().getStatusCode().value();
    logContextBuilder.status(HttpStatusCode.valueOf(statusCode));
  }
}

