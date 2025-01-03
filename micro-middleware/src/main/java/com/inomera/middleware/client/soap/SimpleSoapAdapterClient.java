package com.inomera.middleware.client.soap;

import com.inomera.integration.config.model.AdapterConfig;
import java.util.function.Supplier;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.ws.transport.http.ClientHttpRequestMessageSender;

/**
 * A simple SOAP adapter client.
 */
public final class SimpleSoapAdapterClient extends BaseSoapAdapterClient {

  public SimpleSoapAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      String... marshallerContextPath) {
    super(configSupplierFunc,
        new ClientHttpRequestMessageSender(new SimpleClientHttpRequestFactory()),
        marshallerContextPath);
  }
}
