package com.inomera.middleware.config;

import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.middleware.client.rest.ApacheHttpRestAdapterClient;
import com.inomera.middleware.client.rest.SimpleHttpRestAdapterClient;
import com.inomera.middleware.client.soap.ApacheHttpSoapAdapterClient;
import com.inomera.middleware.client.soap.SimpleSoapAdapterClient;
import java.util.function.Supplier;
import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.transport.WebServiceMessageSender;

@Configuration
public class HttpBeanConfiguration {

  //todo Add auto renew beans on config update
  public static final String BEAN_APACHE_HTTP_REST_CLIENT_WITH_CONFIG = "apacheHttpRestClientWithConfig";
  public static final String BEAN_APACHE_HTTP_REST_CLIENT = "apacheHttpRestClient";
  public static final String BEAN_APACHE_CUSTOM_HTTP_REST_CLIENT = "apacheCustomHttpRestClient";
  public static final String BEAN_APACHE_CUSTOM_CONFIG_HTTP_REST_CLIENT = "apacheCustomConfigHttpRestClient";
  public static final String BEAN_APACHE_HTTP_REST_CLIENT_WITH_REQUEST_FACTORY = "apacheHttpRestClientWithRequestFactory";
  public static final String BEAN_SIMPLE_HTTP_REST_CLIENT_WITH_CONFIG = "simpleRestHttpClientWithConfig";
  public static final String BEAN_SIMPLE_HTTP_REST_CLIENT = "simpleHttpRestClient";
  public static final String BEAN_SIMPLE_SOAP_CLIENT = "simpleSoapClient";
  public static final String BEAN_APACHE_HTTP_SOAP_CLIENT = "apacheHttpSoapClient";
  public static final String BEAN_APACHE_CUSTOM_HTTP_SOAP_CLIENT = "apacheCustomHttpSoapClient";
  public static final String BEAN_APACHE_HTTP_SOAP_CLIENT_WITH_MESSAGE_SENDER = "apacheHttpSoapClientWithMessageSender";
  public static final String BEAN_APACHE_HTTP_SOAP_CLIENT_WITH_MARSHALLER = "apacheHttpSoapClientWithMarshaller";
  public static final String BEAN_APACHE_HTTP_SOAP_CLIENT_WITH_MARSHALLER_AND_MESSAGE_FACTORY = "apacheHttpSoapClientWithMarshallerAndMessageFactory";


  //REST BEANS - <editor-fold desc="REST BEANS">
  @Scope("prototype")
  @Bean(BEAN_APACHE_HTTP_REST_CLIENT)
  public ApacheHttpRestAdapterClient apacheHttpRestAdapterClient() {
    return new ApacheHttpRestAdapterClient();
  }

  @Scope("prototype")
  @Bean(BEAN_APACHE_HTTP_REST_CLIENT_WITH_CONFIG)
  public ApacheHttpRestAdapterClient apacheHttpRestAdapterClientWithConfig(Supplier<AdapterConfig> configSupplierFunc) {
    return new ApacheHttpRestAdapterClient(configSupplierFunc);
  }

  @Scope("prototype")
  @Bean(BEAN_APACHE_CUSTOM_HTTP_REST_CLIENT)
  public ApacheHttpRestAdapterClient apacheCustomHttpRestAdapterClient(HttpClient httpClient) {
    return new ApacheHttpRestAdapterClient(httpClient);
  }

  @Scope("prototype")
  @Bean(BEAN_APACHE_CUSTOM_CONFIG_HTTP_REST_CLIENT)
  public ApacheHttpRestAdapterClient apacheCustomConfigHttpRestAdapterClient(Supplier<AdapterConfig> configSupplierFunc,
      HttpClient httpClient) {
    return new ApacheHttpRestAdapterClient(configSupplierFunc, httpClient);
  }

  @Scope("prototype")
  @Bean(BEAN_APACHE_HTTP_REST_CLIENT_WITH_REQUEST_FACTORY)
  public ApacheHttpRestAdapterClient apacheHttpRestAdapterClientWithReqFactory(Supplier<AdapterConfig> configSupplierFunc,
      ClientHttpRequestFactory clientHttpRequestFactory) {
    return new ApacheHttpRestAdapterClient(configSupplierFunc, clientHttpRequestFactory);
  }

  @Scope("prototype")
  @Bean(BEAN_SIMPLE_HTTP_REST_CLIENT)
  public SimpleHttpRestAdapterClient simpleHttpRestAdapterClient() {
    return new SimpleHttpRestAdapterClient();
  }

  @Scope("prototype")
  @Bean(BEAN_SIMPLE_HTTP_REST_CLIENT_WITH_CONFIG)
  public SimpleHttpRestAdapterClient simpleHttpRestAdapterClientWithConfig(
      Supplier<AdapterConfig> configSupplierFunc) {
    return new SimpleHttpRestAdapterClient(configSupplierFunc);
  }

  //SOAP BEANS - <editor-fold desc="SOAP BEANS">
  @Scope("prototype")
  @Bean(BEAN_SIMPLE_SOAP_CLIENT)
  public SimpleSoapAdapterClient simpleSoapAdapterClient(
      Supplier<AdapterConfig> configSupplierFunc, String marshallerContextPath) {
    return new SimpleSoapAdapterClient(configSupplierFunc, marshallerContextPath);
  }

  @Scope("prototype")
  @Bean(BEAN_APACHE_HTTP_SOAP_CLIENT)
  public ApacheHttpSoapAdapterClient apacheHttpSoapAdapterClient(
      Supplier<AdapterConfig> configSupplierFunc,
      String marshallerContextPath) {
    return new ApacheHttpSoapAdapterClient(configSupplierFunc, marshallerContextPath);
  }

  @Scope("prototype")
  @Bean(BEAN_APACHE_CUSTOM_HTTP_SOAP_CLIENT)
  public ApacheHttpSoapAdapterClient apacheCustomHttpSoapAdapterClient(
      Supplier<AdapterConfig> configSupplierFunc,
      HttpClient httpClient, String marshallerContextPath) {
    return new ApacheHttpSoapAdapterClient(configSupplierFunc,
        httpClient, marshallerContextPath);
  }


  @Scope("prototype")
  @Bean(BEAN_APACHE_HTTP_SOAP_CLIENT_WITH_MESSAGE_SENDER)
  public ApacheHttpSoapAdapterClient apacheHttpSoapAdapterClientWithSender(
      Supplier<AdapterConfig> configSupplierFunc, WebServiceMessageSender webServiceMessageSender,
      String marshallerContextPath) {
    return new ApacheHttpSoapAdapterClient(configSupplierFunc, webServiceMessageSender,
        marshallerContextPath);
  }

  @Scope("prototype")
  @Bean(BEAN_APACHE_HTTP_SOAP_CLIENT_WITH_MARSHALLER)
  public ApacheHttpSoapAdapterClient apacheHttpSoapAdapterClientWithMarshaller(
      Supplier<AdapterConfig> configSupplierFunc,
      WebServiceMessageSender webServiceMessageSender,
      Marshaller marshaller,
      Unmarshaller unmarshaller,
      String marshallerContextPath) {
    return new ApacheHttpSoapAdapterClient(configSupplierFunc,
        webServiceMessageSender, marshaller, unmarshaller, marshallerContextPath);
  }

  @Scope("prototype")
  @Bean(BEAN_APACHE_HTTP_SOAP_CLIENT_WITH_MARSHALLER_AND_MESSAGE_FACTORY)
  public ApacheHttpSoapAdapterClient apacheHttpSoapAdapterClientWitMarshallAndMsgFactory(
      Supplier<AdapterConfig> configSupplierFunc,
      WebServiceMessageSender webServiceMessageSender,
      Marshaller marshaller,
      Unmarshaller unmarshaller,
      WebServiceMessageFactory webServiceMessageFactory,
      String marshallerContextPath) {
    return new ApacheHttpSoapAdapterClient(configSupplierFunc,
        webServiceMessageSender, marshaller, unmarshaller, webServiceMessageFactory,
        marshallerContextPath);
  }


}
