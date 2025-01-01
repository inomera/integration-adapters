package com.inomera.mirketadapter;

import static com.inomera.middleware.config.HttpBeanConfiguration.BEAN_APACHE_HTTP_REST_CLIENT;
import static com.inomera.middleware.config.HttpBeanConfiguration.BEAN_APACHE_HTTP_SOAP_CLIENT;
import static com.inomera.telco.commons.config.spring.BeanNames.BEAN_CONFIGURATION_HOLDER;

import com.inomera.adapter.config.bridge.DynamicAdapterConfigDataBridgeSupplierHandler;
import com.inomera.integration.config.AdapterConfigDataSupplier;
import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.middleware.client.rest.ApacheHttpRestAdapterClient;
import com.inomera.middleware.client.soap.ApacheHttpSoapAdapterClient;
import com.inomera.mirketadapter.rest.MirketAdapter;
import com.inomera.mirketadapter.rest.MirketAdapterImpl;
import com.inomera.mirketadapter.soap.CountryAdapter;
import com.inomera.mirketadapter.soap.CountryAdapterImpl;
import com.inomera.telco.commons.config.ConfigurationHolder;
import com.inomera.telco.commons.lang.Assert;
import generated.countryinfoservice.CountryInfoService;
import java.util.function.Supplier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MirketAdapterBeanConfiguration {

  private static final String CONFIG_MIRKET_KEY = "config.adapter.mirket.v1";
  private static final String CONFIG_COUNTRY_KEY = "config.adapter.country.v1";

  final ApplicationContext applicationContext;

  public MirketAdapterBeanConfiguration(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Bean
  @ConditionalOnClass(AdapterConfigDataSupplier.class)
  public MirketAdapter mirketAdapter(AdapterConfigDataSupplier adapterConfigDataSupplier) {
    final ApacheHttpRestAdapterClient apacheHttpRestAdapterClient = (ApacheHttpRestAdapterClient) applicationContext.getBean(
        BEAN_APACHE_HTTP_REST_CLIENT);
    Supplier<AdapterConfig> configSupplierFunc = () -> adapterConfigDataSupplier.getConfigV1(
        CONFIG_MIRKET_KEY);
    Assert.notNull(apacheHttpRestAdapterClient, "Mirket ApacheHttpRestAdapterClient cannot be NULL");
    return new MirketAdapterImpl(configSupplierFunc, apacheHttpRestAdapterClient);
  }

  @Bean
  @ConditionalOnClass(AdapterConfigDataSupplier.class)
  public CountryAdapter countryAdapter(AdapterConfigDataSupplier adapterConfigDataSupplier) {
    Supplier<AdapterConfig> configSupplierFunc = () -> adapterConfigDataSupplier.getConfigV1(
        CONFIG_COUNTRY_KEY);

    final ApacheHttpSoapAdapterClient soapAdapterClient = (ApacheHttpSoapAdapterClient) applicationContext.getBean(
        BEAN_APACHE_HTTP_SOAP_CLIENT, configSupplierFunc, CountryInfoService.class.getPackage().getName());
    Assert.notNull(soapAdapterClient, "Country ApacheHttpSoapAdapterClient cannot be NULL");
    return new CountryAdapterImpl(configSupplierFunc, soapAdapterClient);
  }

  @Bean
  @ConditionalOnClass(ConfigurationHolder.class)
  public AdapterConfigDataSupplier adapterConfigDataSupplier() {
    final ConfigurationHolder configurationHolder = (ConfigurationHolder) applicationContext.getBean(
        BEAN_CONFIGURATION_HOLDER);
    Assert.notNull(configurationHolder, "ConfigurationHolder cannot be NULL");
    return new DynamicAdapterConfigDataBridgeSupplierHandler(configurationHolder);
  }
}
