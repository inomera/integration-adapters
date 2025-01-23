package com.inomera.mirketadapter.soap;

import static com.inomera.integration.constant.Status.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.inomera.integration.config.DefaultAdapterConfigDataSupplier;
import com.inomera.integration.config.model.AdapterLogging;
import com.inomera.integration.config.model.LogStrategy;
import com.inomera.integration.model.AdapterResponse;
import com.inomera.middleware.client.soap.ApacheHttpSoapAdapterClient;
import com.inomera.mirketadapter.soap.model.Continents;
import com.inomera.mirketadapter.soap.model.CountrySoapAdapterProperties;
import com.inomera.mirketadapter.soap.model.CountrySoapAdapterRequest;
import generated.countryinfoservice.CountryInfoService;
import generated.countryinfoservice.ListOfContinentsByNameResponse;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.net.ssl.SSLContext;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.ClientHttpRequestMessageSender;

class CountryAdapterImplTest {

  private CountryAdapterImpl defaultCountryAdapter;

  @BeforeEach
  void prepare() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    String url = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso";
    DefaultAdapterConfigDataSupplier defaultAdapterConfigDataSupplier = new DefaultAdapterConfigDataSupplier(
        url);

    SSLContext sslContext = SSLContextBuilder.create()
        .loadTrustMaterial(null, new TrustSelfSignedStrategy())
        .build();

    Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register(URIScheme.HTTPS.getId(),
            new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
        .register(URIScheme.HTTP.getId(), new PlainConnectionSocketFactory())
        .build();

    HttpClient httpClient = HttpClientBuilder.create()
        .setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
        .setConnectionManagerShared(true)
        .build();
    ClientHttpRequestFactory reqFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
    final WebServiceMessageSender messageSender = new ClientHttpRequestMessageSender(reqFactory);

    final String marshallPath = CountryInfoService.class.getPackage().getName();
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setPackagesToScan(marshallPath);
    final ApacheHttpSoapAdapterClient httpSoapClient;
    try {
      marshaller.afterPropertiesSet();
      AdapterLogging adapterLoggingDetail = new AdapterLogging(LogStrategy.REQ_RES);
      adapterLoggingDetail.setNonLoggingFields(List.of("sCountryISOCode"));
      adapterLoggingDetail.setSensitiveFields(List.of("CountryNameResult"));
      httpSoapClient = new ApacheHttpSoapAdapterClient(defaultAdapterConfigDataSupplier::getConfig,
          messageSender, marshaller, marshaller, marshallPath);
      final WebServiceTemplate webServiceTemplate = httpSoapClient.getWebServiceTemplate();
      webServiceTemplate.setCheckConnectionForFault(true);
      this.defaultCountryAdapter = new CountryAdapterImpl(
          defaultAdapterConfigDataSupplier::getConfig,
          httpSoapClient);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Test
  void shouldGetFirstContinentOfNameByCallingListContinentsByName() {
    final AdapterResponse<String> adapterResponse = defaultCountryAdapter.listOfContinentsByName(
        new CountrySoapAdapterRequest(), new CountrySoapAdapterProperties(), continents -> {
          final ListOfContinentsByNameResponse continentList = (ListOfContinentsByNameResponse) continents;
          return continentList.getListOfContinentsByNameResult().getTContinent().stream()
              .findFirst().get().getSName();
        });

    assertNotNull(adapterResponse);
    assertNotNull(adapterResponse.getData());
    assertEquals("Africa", adapterResponse.getData());
    assertEquals(adapterResponse.getStatus().code(), SUCCESS.getCode());
    assertEquals(adapterResponse.getStatus().description(), "Success");
  }

  @Test
  void shouldListContinentsByCallingListContinents() {
    final AdapterResponse<Continents> adapterResponse = defaultCountryAdapter.listOfContinentsByName(
        new CountrySoapAdapterRequest(), new CountrySoapAdapterProperties());

    assertNotNull(adapterResponse);
    assertNotNull(adapterResponse.getData());
    assertEquals("Africa",
        adapterResponse.getData().getContinents().get(0)
            .getName());
    assertEquals(adapterResponse.getStatus().code(), SUCCESS.getCode());
    assertEquals(adapterResponse.getStatus().description(), "Success");
  }

  @Test
  void shouldGetCountryNameByISOCodeWithLoggingMasks() {
    final AdapterResponse<String> country = defaultCountryAdapter.getCountryNameByISOCode("TR");

    assertNotNull(country);
    assertNotNull(country.getData());
    assertEquals("Turkey", country.getData());
    assertEquals(country.getStatus().code(), SUCCESS.getCode());
    assertEquals(country.getStatus().description(), "Success");
  }
}
