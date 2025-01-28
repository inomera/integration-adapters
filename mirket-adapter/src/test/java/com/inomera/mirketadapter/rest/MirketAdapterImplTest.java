package com.inomera.mirketadapter.rest;


import static com.inomera.integration.constant.Status.SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.inomera.integration.config.DefaultAdapterConfigDataSupplier;
import com.inomera.integration.model.AdapterResponse;
import com.inomera.middleware.client.rest.ApacheHttpRestAdapterClient;
import com.inomera.mirketadapter.rest.model.MirketRestAdapterProperties;
import com.inomera.mirketadapter.rest.model.MirketRestAdapterRequest;
import com.inomera.mirketadapter.rest.rto.FirstRestResponse;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.URIScheme;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Slf4j
class MirketAdapterImplTest {

  private MirketAdapterImpl defaultMirketAdapter;

  @BeforeEach
  public void prepare() throws NoSuchAlgorithmException, KeyManagementException {
    DefaultAdapterConfigDataSupplier defaultAdapterConfigDataSupplier = new DefaultAdapterConfigDataSupplier(
        "https://api.mirket.inomera.com/outageInfo");

    SSLContext sslContext = SSLContextBuilder.create()
        .build();

    Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register(URIScheme.HTTPS.getId(),
            new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
        .build();

    HttpClient httpClient = HttpClientBuilder.create()
        .setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
        .setConnectionManagerShared(true)
        .build();

    final ApacheHttpRestAdapterClient apacheHttpRestAdapterClient = new ApacheHttpRestAdapterClient(
        defaultAdapterConfigDataSupplier::getConfig,
        httpClient);

    defaultMirketAdapter = new MirketAdapterImpl(defaultAdapterConfigDataSupplier::getConfig,
        apacheHttpRestAdapterClient);
  }

  @Test
  @Disabled
  void shouldGetClientIpWhenCallingGetFirst() {
    final MirketRestAdapterProperties mirketProps = new MirketRestAdapterProperties();
    mirketProps.setAuthHeader("X-GW-API-KEY");
    mirketProps.setAuthHeaderValue("3cf9ff3b-c2d0-4813-8f31-cd940490923f");

    final AdapterResponse<String> adapterResponse = defaultMirketAdapter.getFirst(
        new MirketRestAdapterRequest(), mirketProps);

    assertNotNull(adapterResponse);
    assertNotNull(adapterResponse.getData());
    assertEquals(adapterResponse.getStatus().code(), SUCCESS.getCode());
    assertEquals(adapterResponse.getStatus().description(), "Success");
  }

  @Test
  @Disabled
  void shouldGetFirst() {
    final MirketRestAdapterProperties mirketProps = new MirketRestAdapterProperties();

    mirketProps.setAuthHeader("X-GW-API-KEY");
    mirketProps.setAuthHeaderValue("3cf9ff3b-c2d0-4813-8f31-cd940490923f");

    final AdapterResponse<FirstRestResponse> adapterResponse = defaultMirketAdapter.getFirst(
        new MirketRestAdapterRequest(), mirketProps, o -> (FirstRestResponse) o);

    assertNotNull(adapterResponse);
    assertNotNull(adapterResponse.getData());
    assertEquals(adapterResponse.getStatus().code(), SUCCESS.getCode());
    assertEquals(adapterResponse.getStatus().description(), "Success");
  }

  @Test
  @Disabled
  void shouldGetFirstForConcreteMethod() {
    final MirketRestAdapterProperties mirketProps = new MirketRestAdapterProperties();
    mirketProps.setAuthHeader("X-GW-API-KEY");
    mirketProps.setAuthHeaderValue("3cf9ff3b-c2d0-4813-8f31-cd940490923f");

    final AdapterResponse<FirstRestResponse> adapterResponse = defaultMirketAdapter.getFirst(
        new MirketRestAdapterRequest(), mirketProps, o -> (FirstRestResponse) o);
    assertNotNull(adapterResponse);
    assertNotNull(adapterResponse.getData());
    assertEquals(adapterResponse.getStatus().code(), SUCCESS.getCode());
    assertEquals(adapterResponse.getStatus().description(), "Success");
  }

}
