package com.inomera.middleware.client.interceptor.log;

import com.inomera.integration.config.model.AdapterLogging;
import com.inomera.integration.config.model.LogStrategy;
import com.inomera.middleware.client.BufferingClientHttpResponseWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Intercepts the HTTP request to log the request and response.
 */
@Slf4j
public class RestLoggingInterceptor extends BaseClientLoggingInterceptor implements
    ClientHttpRequestInterceptor {

  private static final String JSON_MASK_PATTERN = "(?:\\\"${PRM}\\\"|${PRM})\\s*:(\\s*(\\\"(.*?)\\\")|(\\s*(.\\d),)|\\s*(\\[(.*?)\\])|(\\s*((.*?)(\\n}))))";

  public RestLoggingInterceptor(AdapterLogging adapterLogging) {
    super(adapterLogging, JSON_MASK_PATTERN);
  }

  /**
   * Intercepts the HTTP request to log the request and response.
   */
  @Override
  public ClientHttpResponse intercept(
      HttpRequest req, byte[] reqBody, ClientHttpRequestExecution ex) throws IOException {
    if (hasNotLoggingStrategyOrLogStrategyIsOff()) {
      return ex.execute(req, reqBody);
    }
    LogContext.LogContextBuilder logContextBuilder = LogContext.builder();
    try {
      logContextBuilder.uri(req.getURI().toString());
      String request = new String(reqBody, StandardCharsets.UTF_8);
      logContextBuilder.requestBody(request);
      if (adapterLogging.getStrategy().allOrFailure()) {
        logContextBuilder.requestHeaders(req.getHeaders().toString());
      }
      ClientHttpResponse response = new BufferingClientHttpResponseWrapper(ex.execute(req, reqBody));
      InputStreamReader isr = new InputStreamReader(response.getBody(), StandardCharsets.UTF_8);
      final String body = new BufferedReader(isr).lines().collect(Collectors.joining("\n"));
      logContextBuilder.responseBody(body);
      logContextBuilder.status(response.getStatusCode());
      if (adapterLogging.getStrategy().allOrFailure()) {
        logContextBuilder.responseHeaders(response.getHeaders().toString());
      }
      return response;
    } catch (IOException e) {
      LOG.error("Adapter IOException occurred when logging", e);
      throw e;
    } finally {
      LogContext logContext = logContextBuilder.build();
      if (adapterLogging.getStrategy() == LogStrategy.REQ_RES) {
        //TODO : content split if any limitation of logging infra!!
        LOG.info("{}", makeLogMoreSensibleIfHasAnyRule(logContext.toRequestAll()));
        LOG.info("{}", logContext.toResponse());
      } else if (isAllOrFailCase(logContext)) {
        LOG.info("{}", makeLogMoreSensibleIfHasAnyRule(logContext.toRequestAll()));
        LOG.info("{}", logContext.toResponseAll());
      }
    }
  }

  private boolean hasNotLoggingStrategyOrLogStrategyIsOff() {
    if (adapterLogging == null || adapterLogging.getStrategy() == null) {
      return true;
    }
    return adapterLogging.getStrategy() == LogStrategy.OFF;
  }

  private boolean isAllOrFailCase(LogContext logContext) {
    if (LogStrategy.ALL == adapterLogging.getStrategy()) {
      return true;
    }
    return LogStrategy.FAILURE == adapterLogging.getStrategy()
        && !logContext.status().is2xxSuccessful()
        && StringUtils.isNotBlank(logContext.responseBody());
  }

}
