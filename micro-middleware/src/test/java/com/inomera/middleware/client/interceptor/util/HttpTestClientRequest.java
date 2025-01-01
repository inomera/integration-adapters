package com.inomera.middleware.client.interceptor.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.AbstractClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class HttpTestClientRequest extends AbstractClientHttpRequest {

  private final HttpClient httpClient;
  private final ClassicHttpRequest httpRequest;
  private final HttpContext httpContext;
  private ByteArrayOutputStream bufferedOutput = new ByteArrayOutputStream(1024);


  public HttpTestClientRequest(HttpClient client, ClassicHttpRequest request, HttpContext context) {
    this.httpClient = client;
    this.httpRequest = request;
    this.httpContext = context;
  }

  /**
   * Add the given headers to the given HTTP request.
   *
   * @param httpRequest the request to add the headers to
   * @param headers     the headers to add
   */
  static void addHeaders(ClassicHttpRequest httpRequest, HttpHeaders headers) {
    headers.forEach((headerName, headerValues) -> {
      if (HttpHeaders.COOKIE.equalsIgnoreCase(headerName)) {  // RFC 6265
        String headerValue = StringUtils.collectionToDelimitedString(headerValues, "; ");
        httpRequest.addHeader(headerName, headerValue);
      } else if (!HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(headerName) &&
          !HttpHeaders.TRANSFER_ENCODING.equalsIgnoreCase(headerName)) {
        for (String headerValue : headerValues) {
          httpRequest.addHeader(headerName, headerValue);
        }
      }
    });
  }

  @Override
  public HttpMethod getMethod() {
    return HttpMethod.valueOf(this.httpRequest.getMethod());
  }

  @Override
  public URI getURI() {
    try {
      return this.httpRequest.getUri();
    } catch (URISyntaxException ex) {
      throw new IllegalStateException(ex.getMessage(), ex);
    }
  }

  HttpContext getHttpContext() {
    return this.httpContext;
  }

  @SuppressWarnings("deprecation")  // execute(ClassicHttpRequest, HttpContext)
  protected ClientHttpResponse executeInternal(HttpHeaders headers, byte[] bufferedOutput)
      throws IOException {
    addHeaders(this.httpRequest, headers);

    ContentType contentType = ContentType.parse(headers.getFirst(HttpHeaders.CONTENT_TYPE));
    HttpEntity requestEntity = new ByteArrayEntity(bufferedOutput, contentType);
    this.httpRequest.setEntity(requestEntity);
    HttpResponse httpResponse = this.httpClient.execute(this.httpRequest, this.httpContext);
    Assert.isInstanceOf(ClassicHttpResponse.class, httpResponse,
        "HttpResponse not an instance of ClassicHttpResponse");
    return new HttpTestClientResponse((ClassicHttpResponse) httpResponse);
  }

  @Override
  protected OutputStream getBodyInternal(HttpHeaders headers) {
    return this.bufferedOutput;
  }

  @Override
  protected ClientHttpResponse executeInternal(HttpHeaders headers) throws IOException {
    byte[] bytes = this.bufferedOutput.toByteArray();
    if (headers.getContentLength() < 0) {
      headers.setContentLength(bytes.length);
    }
    ClientHttpResponse result = executeInternal(headers, bytes);
    this.bufferedOutput = new ByteArrayOutputStream(0);
    return result;
  }
}
