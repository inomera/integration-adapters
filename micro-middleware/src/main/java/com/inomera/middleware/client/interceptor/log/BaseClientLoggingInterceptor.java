package com.inomera.middleware.client.interceptor.log;

import com.inomera.integration.config.model.AdapterLogging;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.CollectionUtils;

/*
Why do we log request and response separately?
fluent bit log size default 32K. some of the backend services' request or response logs are huge.
we optimize the log size per line
 */
public class BaseClientLoggingInterceptor {

  private static final String FIELD_PARAMETER = "${PRM}";

  protected AdapterLogging adapterLogging;

  protected Pattern multilineMaskPattern;
  protected Pattern multilineNonLogPattern;

  public BaseClientLoggingInterceptor(AdapterLogging adapterLogging, String maskPatterns) {
    this.adapterLogging = adapterLogging;
    checkAndSetPatterns(adapterLogging, maskPatterns);
  }

  private void checkAndSetPatterns(AdapterLogging loggingDetail, String maskPatterns) {
    if (!CollectionUtils.isEmpty(loggingDetail.getSensitiveFields())) {
      final List<String> maskedPatterns = new ArrayList<>();
      for (String sensitiveField : loggingDetail.getSensitiveFields()) {
        String replace = maskPatterns.replace(FIELD_PARAMETER, sensitiveField);
        maskedPatterns.add(replace);
      }
      this.multilineMaskPattern = Pattern.compile(String.join("|", maskedPatterns),
          Pattern.MULTILINE);
    }
    if (!CollectionUtils.isEmpty(loggingDetail.getNonLoggingFields())) {
      final List<String> nonLoggingMaskedPatterns = loggingDetail.getNonLoggingFields().stream()
          .map(nonLoggingField -> maskPatterns.replace(FIELD_PARAMETER, nonLoggingField))
          .toList();

      this.multilineNonLogPattern = Pattern.compile(String.join("|", nonLoggingMaskedPatterns),
          Pattern.MULTILINE);
    }
  }

  protected String makeLogMoreSensibleIfHasAnyRule(String log) {
    if (StringUtils.isBlank(log)) {
      return log;
    }
    if (adapterLogging == null) {
      return log;
    }

    String processedLog = maskSensitiveFieldValues(log);
    return maskNonLogFieldWithFixedValue(processedLog);
  }

  private String maskSensitiveFieldValues(String log) {
    if (CollectionUtils.isEmpty(adapterLogging.getSensitiveFields())) {
      return log;
    }
    final Matcher matcher = multilineMaskPattern.matcher(log);
    while (matcher.find()) {
      for (int group = 1; group <= matcher.groupCount(); group++) {
        String matchedGroup = matcher.group(group);
        if (matchedGroup == null) {
          continue;
        }
        log = log.replace(matchedGroup, "**masked**");
      }
    }
    return log;
  }

  private String maskNonLogFieldWithFixedValue(String log) {
    if (CollectionUtils.isEmpty(adapterLogging.getNonLoggingFields())) {
      return log;
    }
    final Matcher matcher = multilineNonLogPattern.matcher(log);
    while (matcher.find()) {
      for (int group = 1; group <= matcher.groupCount(); group++) {
        String matchedGroup = matcher.group(group);
        if (matchedGroup == null) {
          continue;
        }
        log = log.replace(matchedGroup, "-empty-");
      }
    }
    return log;
  }

  /**
   * Context of http request-response log
   *
   * @param uri
   * @param requestBody
   * @param responseBody
   * @param status
   * @param requestHeaders
   * @param responseHeaders
   */
  protected record LogContext(String uri, String requestBody, String responseBody,
                              HttpStatusCode status, String requestHeaders,
                              String responseHeaders) {

    public String toRequest() {
      return "Request={" +
          "uri='" + uri + "'\n" +
          ", requestBody='" + requestBody + '\'' +
          '}';
    }

    public String toRequestAll() {
      return "Request={" +
          "uri='" + uri + "'\n" +
          ", requestBody='" + requestBody + "'\n" +
          ", requestHeaders='" + requestHeaders + '\'' +
          '}';
    }

    public String toResponse() {
      return "Response={" +
          " status='" + status + "'\n" +
          ", responseBody='" + responseBody + '\'' +
          '}';
    }

    public String toResponseAll() {
      return "Response={" +
          "status='" + status + "'\n" +
          ", responseBody='" + responseBody + "'\n" +
          ", responseHeaders='" + responseHeaders + '\'' +
          '}';
    }

    public static class LogContextBuilder {

      private String uri;
      private String requestBody;
      private String responseBody;
      private HttpStatusCode status;
      private String requestHeaders;
      private String responseHeaders;

      public LogContextBuilder uri(String uri) {
        this.uri = uri;
        return this;
      }

      public LogContextBuilder requestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
      }

      public LogContextBuilder responseBody(String responseBody) {
        this.responseBody = responseBody;
        return this;
      }

      public LogContextBuilder status(HttpStatusCode status) {
        this.status = status;
        return this;
      }

      public LogContextBuilder requestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
        return this;
      }

      public LogContextBuilder responseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
        return this;
      }

      public LogContext build() {
        return new LogContext(uri, requestBody, responseBody, status, requestHeaders,
            responseHeaders);
      }
    }

    public static LogContextBuilder builder() {
      return new LogContextBuilder();
    }


  }

}
