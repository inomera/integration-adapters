package com.inomera.mirketadapter.soap;


import com.inomera.integration.client.HttpSoapAdapterClient;
import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.integration.constant.HttpMethod;
import com.inomera.integration.interceptor.IntegrationAdapterInterceptor;
import com.inomera.integration.model.AdapterResponse;
import com.inomera.integration.model.AdapterStatus;
import com.inomera.integration.type.SoapAdapter;
import com.inomera.mirketadapter.soap.model.Continents;
import com.inomera.mirketadapter.soap.model.CountrySoapAdapterProperties;
import com.inomera.mirketadapter.soap.model.CountrySoapAdapterRequest;
import generated.countryinfoservice.CountryName;
import generated.countryinfoservice.CountryNameResponse;
import generated.countryinfoservice.ListOfContinentsByName;
import generated.countryinfoservice.ListOfContinentsByNameResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CountryAdapterImpl extends SoapAdapter<AdapterConfig> implements
    CountryAdapter {

  public CountryAdapterImpl(Supplier<AdapterConfig> configSupplierFunc,
      HttpSoapAdapterClient httpAdapterClient) {
    super(configSupplierFunc, httpAdapterClient);
  }

  public CountryAdapterImpl(Supplier<AdapterConfig> configSupplierFunc,
      HttpSoapAdapterClient httpAdapterClient, List<IntegrationAdapterInterceptor> interceptors)
      throws InstantiationException {
    super(configSupplierFunc, httpAdapterClient, interceptors);
  }

  @Override
  public <I, O> AdapterResponse<O> listOfContinentsByName(CountrySoapAdapterRequest adapterRequest,
      CountrySoapAdapterProperties countryProps, Function<I, O> resultMapperFunction) {
    final Map<String, String> requestHeaders = new HashMap<>();
    final AdapterResponse<ListOfContinentsByNameResponse> adapterResponse = executeInternal(
        requestHeaders, new ListOfContinentsByName(), HttpMethod.POST,
        ListOfContinentsByNameResponse.class);
    if (adapterResponse.getStatus().isSuccess()) {
      var result = resultMapperFunction.apply((I) adapterResponse.getData());
      return new AdapterResponse<>(adapterResponse.getStatus(), result);
    }
    return (AdapterResponse<O>) adapterResponse;//data is empty
  }

  @Override
  public AdapterResponse<Continents> listOfContinentsByName(
      CountrySoapAdapterRequest adapterRequest, CountrySoapAdapterProperties countryProps) {
    final Map<String, String> requestHeaders = new HashMap<>();
    return executeInternalWithCustomHandler(requestHeaders, new ListOfContinentsByName(),
        HttpMethod.POST, ListOfContinentsByNameResponse.class,
        (httpAdapterResponse) ->
            new AdapterResponse<>(AdapterStatus.createSuccess(),
                new Continents(httpAdapterResponse.body()))
    );
  }

  @Override
  public AdapterResponse<String> getCountryNameByISOCode(String isoCode) {
    final Map<String, String> requestHeaders = new HashMap<>();
    CountryName requestBody = new CountryName();
    requestBody.setSCountryISOCode(isoCode);
    return executeInternalWithCustomHandler(requestHeaders, requestBody,
        HttpMethod.POST, CountryNameResponse.class,
        (httpAdapterResponse) ->
            new AdapterResponse<>(AdapterStatus.createSuccess(),
                httpAdapterResponse.body().getCountryNameResult())
    );
  }

}
