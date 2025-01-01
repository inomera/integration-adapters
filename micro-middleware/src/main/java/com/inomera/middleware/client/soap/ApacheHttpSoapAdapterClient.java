package com.inomera.middleware.client.soap;

import com.inomera.integration.config.model.AdapterConfig;
import java.util.function.Supplier;
import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceMessageSender;

public final class ApacheHttpSoapAdapterClient extends BaseSoapAdapterClient {

  public ApacheHttpSoapAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      String... marshallerContextPath) {
    super(configSupplierFunc, new CustomHttpComponents5MessageSender(), marshallerContextPath);
  }

  public ApacheHttpSoapAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      HttpClient httpClient, String... marshallerContextPath
  ) {
    super(configSupplierFunc, new CustomHttpComponents5MessageSender(httpClient),
        marshallerContextPath);
  }

  public ApacheHttpSoapAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      WebServiceMessageSender webServiceMessageSender,
      Marshaller marshaller,
      Unmarshaller unmarshaller,
      String... marshallerContextPath
  ) {
    super(configSupplierFunc, webServiceMessageSender,
        marshaller, unmarshaller, marshallerContextPath);
  }

  public ApacheHttpSoapAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      WebServiceMessageSender webServiceMessageSender,
      Marshaller marshaller,
      Unmarshaller unmarshaller,
      WebServiceMessageFactory webServiceMessageFactory,
      String... marshallerContextPath
  ) {
    super(configSupplierFunc, webServiceMessageSender,
        marshaller, unmarshaller, webServiceMessageFactory,
        marshallerContextPath);
  }

  public ApacheHttpSoapAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      WebServiceMessageSender webServiceMessageSender, String marshallerContextPath) {
    super(configSupplierFunc, webServiceMessageSender, marshallerContextPath);
  }
}
