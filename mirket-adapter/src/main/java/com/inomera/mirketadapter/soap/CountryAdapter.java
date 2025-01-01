package com.inomera.mirketadapter.soap;


import com.inomera.integration.model.AdapterResponse;
import com.inomera.mirketadapter.soap.model.Continents;
import com.inomera.mirketadapter.soap.model.CountrySoapAdapterProperties;
import com.inomera.mirketadapter.soap.model.CountrySoapAdapterRequest;
import java.util.function.Function;

public interface CountryAdapter {

  <I, O> AdapterResponse<O> listOfContinentsByName(CountrySoapAdapterRequest adapterRequest,
      CountrySoapAdapterProperties countryProps, Function<I, O> resultMapperFunction);

  AdapterResponse<Continents> listOfContinentsByName(
      CountrySoapAdapterRequest adapterRequest, CountrySoapAdapterProperties countryProps);

  AdapterResponse<String> getCountryNameByISOCode(String isoCode);
}
