package com.inomera.middleware.client.interceptor.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;

/*
ref : HttpComponentsClientHttpResponse
package access problem!!
 */
public class HttpTestClientResponse implements ClientHttpResponse {

  private final ClassicHttpResponse httpResponse;

  private HttpHeaders headers;

  public HttpTestClientResponse(ClassicHttpResponse httpResponse) {
    this.httpResponse = httpResponse;
  }

  @Override
  public HttpStatusCode getStatusCode() {
    return HttpStatusCode.valueOf(this.httpResponse.getCode());
  }

  @Override
  @Deprecated
  public int getRawStatusCode() {
    return this.httpResponse.getCode();
  }

  @Override
  public String getStatusText() {
    return this.httpResponse.getReasonPhrase();
  }

  @Override
  public HttpHeaders getHeaders() {
    if (this.headers == null) {
      this.headers = new HttpHeaders();
      for (Header header : this.httpResponse.getHeaders()) {
        this.headers.add(header.getName(), header.getValue());
      }
    }
    return this.headers;
  }

  @Override
  public InputStream getBody() throws IOException {
    HttpEntity entity = this.httpResponse.getEntity();
    return (entity != null ? entity.getContent() : InputStream.nullInputStream());
  }

  @Override
  public void close() {
    try {
      try {
        EntityUtils.consume(this.httpResponse.getEntity());
      } finally {
        this.httpResponse.close();
      }
    } catch (IOException ex) {
      // Ignore exception on close...
    }
  }

}

