package com.inomera.middleware.client.soap;

import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.ws.transport.http.HttpComponents5MessageSender;

/**
 * A custom HTTP components 5 message sender.
 */
public class CustomHttpComponents5MessageSender extends HttpComponents5MessageSender {

  public CustomHttpComponents5MessageSender() {
    super();
    try {
      super.afterPropertiesSet();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public CustomHttpComponents5MessageSender(HttpClient httpClient) {
    super(httpClient);
  }

}
