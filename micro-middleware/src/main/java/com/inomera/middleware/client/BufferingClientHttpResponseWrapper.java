package com.inomera.middleware.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public final class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

  private final ClientHttpResponse response;
  private byte[] body;

  public BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
    this.response = response;
  }

  @Override
  public HttpStatusCode getStatusCode() throws IOException {
    return this.response.getStatusCode();
  }

  @Override
  @Deprecated
  public int getRawStatusCode() throws IOException {
    return this.response.getRawStatusCode();
  }

  @Override
  public String getStatusText() throws IOException {
    return this.response.getStatusText();
  }

  @Override
  public HttpHeaders getHeaders() {
    return this.response.getHeaders();
  }

  @Override
  public InputStream getBody() throws IOException {
    if (this.body == null) {
      this.body = StreamUtils.copyToByteArray(this.response.getBody());
    }
    return new ByteArrayInputStream(this.body);
  }

  @Override
  public void close() {
    this.response.close();
  }
}
