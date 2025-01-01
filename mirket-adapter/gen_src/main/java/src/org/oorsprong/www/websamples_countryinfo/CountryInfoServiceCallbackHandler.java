/**
 * CountryInfoServiceCallbackHandler.java
 *
 * <p>This file was auto-generated from WSDL by the Apache Axis2 version: 1.8.2 Built on : Jul 13,
 * 2022 (06:38:03 EDT)
 */
package org.oorsprong.www.websamples_countryinfo;

/**
 * CountryInfoServiceCallbackHandler Callback class, Users can extend this class and implement their
 * own receiveResult and receiveError methods.
 */
public abstract class CountryInfoServiceCallbackHandler {

  protected Object clientData;

  /**
   * User can pass in any object that needs to be accessed once the NonBlocking Web service call is
   * finished and appropriate method of this CallBack is called.
   *
   * @param clientData Object mechanism by which the user can pass in user data that will be
   *     avilable at the time this callback is called.
   */
  public CountryInfoServiceCallbackHandler(Object clientData) {
    this.clientData = clientData;
  }

  /** Please use this constructor if you don't want to set any clientData */
  public CountryInfoServiceCallbackHandler() {
    this.clientData = null;
  }

  /** Get the client data */
  public Object getClientData() {
    return clientData;
  }

  /**
   * auto generated Axis2 call back method for fullCountryInfo method override this method for
   * handling normal response from fullCountryInfo operation
   */
  public void receiveResultfullCountryInfo(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.FullCountryInfoResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * fullCountryInfo operation
   */
  public void receiveErrorfullCountryInfo(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for countryFlag method override this method for handling
   * normal response from countryFlag operation
   */
  public void receiveResultcountryFlag(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.CountryFlagResponse result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * countryFlag operation
   */
  public void receiveErrorcountryFlag(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for countryName method override this method for handling
   * normal response from countryName operation
   */
  public void receiveResultcountryName(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.CountryNameResponse result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * countryName operation
   */
  public void receiveErrorcountryName(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for countryCurrency method override this method for
   * handling normal response from countryCurrency operation
   */
  public void receiveResultcountryCurrency(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.CountryCurrencyResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * countryCurrency operation
   */
  public void receiveErrorcountryCurrency(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for listOfCountryNamesByCode method override this method
   * for handling normal response from listOfCountryNamesByCode operation
   */
  public void receiveResultlistOfCountryNamesByCode(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub
              .ListOfCountryNamesByCodeResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * listOfCountryNamesByCode operation
   */
  public void receiveErrorlistOfCountryNamesByCode(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for listOfCurrenciesByName method override this method
   * for handling normal response from listOfCurrenciesByName operation
   */
  public void receiveResultlistOfCurrenciesByName(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.ListOfCurrenciesByNameResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * listOfCurrenciesByName operation
   */
  public void receiveErrorlistOfCurrenciesByName(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for countryIntPhoneCode method override this method for
   * handling normal response from countryIntPhoneCode operation
   */
  public void receiveResultcountryIntPhoneCode(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.CountryIntPhoneCodeResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * countryIntPhoneCode operation
   */
  public void receiveErrorcountryIntPhoneCode(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for currencyName method override this method for handling
   * normal response from currencyName operation
   */
  public void receiveResultcurrencyName(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.CurrencyNameResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * currencyName operation
   */
  public void receiveErrorcurrencyName(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for listOfCurrenciesByCode method override this method
   * for handling normal response from listOfCurrenciesByCode operation
   */
  public void receiveResultlistOfCurrenciesByCode(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.ListOfCurrenciesByCodeResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * listOfCurrenciesByCode operation
   */
  public void receiveErrorlistOfCurrenciesByCode(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for listOfLanguagesByCode method override this method for
   * handling normal response from listOfLanguagesByCode operation
   */
  public void receiveResultlistOfLanguagesByCode(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.ListOfLanguagesByCodeResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * listOfLanguagesByCode operation
   */
  public void receiveErrorlistOfLanguagesByCode(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for languageISOCode method override this method for
   * handling normal response from languageISOCode operation
   */
  public void receiveResultlanguageISOCode(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.LanguageISOCodeResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * languageISOCode operation
   */
  public void receiveErrorlanguageISOCode(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for capitalCity method override this method for handling
   * normal response from capitalCity operation
   */
  public void receiveResultcapitalCity(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.CapitalCityResponse result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * capitalCity operation
   */
  public void receiveErrorcapitalCity(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for listOfLanguagesByName method override this method for
   * handling normal response from listOfLanguagesByName operation
   */
  public void receiveResultlistOfLanguagesByName(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.ListOfLanguagesByNameResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * listOfLanguagesByName operation
   */
  public void receiveErrorlistOfLanguagesByName(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for listOfCountryNamesGroupedByContinent method override
   * this method for handling normal response from listOfCountryNamesGroupedByContinent operation
   */
  public void receiveResultlistOfCountryNamesGroupedByContinent(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub
              .ListOfCountryNamesGroupedByContinentResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * listOfCountryNamesGroupedByContinent operation
   */
  public void receiveErrorlistOfCountryNamesGroupedByContinent(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for listOfCountryNamesByName method override this method
   * for handling normal response from listOfCountryNamesByName operation
   */
  public void receiveResultlistOfCountryNamesByName(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub
              .ListOfCountryNamesByNameResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * listOfCountryNamesByName operation
   */
  public void receiveErrorlistOfCountryNamesByName(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for listOfContinentsByName method override this method
   * for handling normal response from listOfContinentsByName operation
   */
  public void receiveResultlistOfContinentsByName(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.ListOfContinentsByNameResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * listOfContinentsByName operation
   */
  public void receiveErrorlistOfContinentsByName(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for languageName method override this method for handling
   * normal response from languageName operation
   */
  public void receiveResultlanguageName(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.LanguageNameResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * languageName operation
   */
  public void receiveErrorlanguageName(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for listOfContinentsByCode method override this method
   * for handling normal response from listOfContinentsByCode operation
   */
  public void receiveResultlistOfContinentsByCode(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.ListOfContinentsByCodeResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * listOfContinentsByCode operation
   */
  public void receiveErrorlistOfContinentsByCode(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for countriesUsingCurrency method override this method
   * for handling normal response from countriesUsingCurrency operation
   */
  public void receiveResultcountriesUsingCurrency(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.CountriesUsingCurrencyResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * countriesUsingCurrency operation
   */
  public void receiveErrorcountriesUsingCurrency(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for countryISOCode method override this method for
   * handling normal response from countryISOCode operation
   */
  public void receiveResultcountryISOCode(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub.CountryISOCodeResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * countryISOCode operation
   */
  public void receiveErrorcountryISOCode(java.lang.Exception e) {}

  /**
   * auto generated Axis2 call back method for fullCountryInfoAllCountries method override this
   * method for handling normal response from fullCountryInfoAllCountries operation
   */
  public void receiveResultfullCountryInfoAllCountries(
      org.oorsprong.www.websamples_countryinfo.CountryInfoServiceStub
              .FullCountryInfoAllCountriesResponse
          result) {}

  /**
   * auto generated Axis2 Error handler override this method for handling error response from
   * fullCountryInfoAllCountries operation
   */
  public void receiveErrorfullCountryInfoAllCountries(java.lang.Exception e) {}
}
