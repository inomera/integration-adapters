# Backend/Infra Services Adapters Libraries

The adapter serves backend/infra system services as a library.

You can use the library just giving the http client with your suitable configuration.

The library handles the adapter integration layer works such as data modeling, exception handling,
http rest/soap protocol complexities.

**IMPORTANT NOTE:** The doc should be updated when doing implementation.

## Architecture

Class diagram

![Class diagram](files/diagrams/integration-adapters-class-diagram.png)

## Issues

- add logger - OK
- implementation of rest template for jdk 17 http client - OK
- tcell apigw token header extension - OK
- rule based apigw bypass and direct consume the backend services - OK
- versioning with CI/CD pipelines @author - OK
- add header adapterLogging with security concern - OK
- centralize the build gradle as parametric - OK
- code generation packaging is centralized - OK

## TODO LIST

- create base IntegrationTest utility class

## Advantages

* Data Adapter Response Model pattern utilization (re-usability)
* REST/SOAP Exception Handling pattern utilization (re-usability)
* Layering/Abstraction of integration
* Standardizing integrations
* Increase development productivity
* Please add any one of them when using...

## Libraries

There are two levels of library layers for now.

### micro-integration

The top of the integration layer of the microservice.
There is no dependency without JDK.
The layer consists of interfaces, models, utilities and related stuffs.

### micro-middleware

The second layer of the integration architecture.
It consists of the abstractions of integration parts such as http client, spring-ws-core, javax
validation, apache common utility libs.

security, adapterLogging, client implementation

## Example adapter implementation

mirket-adapter is the sample adapter implementation.
Please check the implementation of the features before asking any questions.

## Protocols

### REST

Apache HttpClient 5

### SOAP

### Generation (WSDL2JAVA)

One of following optionals is for generation. (Choose one of them what your comfort-zone is)

    Apache CXF (optional)
    AXIS (optional)
    GRADLE JAXB Plugin (Optional)    
    JDK 17, 21

Stubs are generated via apache cxf library using sh script or gradle task.

You can find the example shell scripts under the files/scripts directory.

You can generate client three ways.
There are shell scripts under the files/scripts folder that are to provide generating wsdl to java
code using axis/cxf.

* 1 - apache cxf

  Download apache binary dist.
  Config sh script variable values with your path configs
  Execute sh or bat scripts.

* 2 - axis2

  Download apache binary dist.
  Config sh script variable values with your path configs
  Execute sh or bat scripts.

Enabling axis dependencies please add following dependencies to gradle file.
mirket-adapter has an example of the axis generation.

    //axis
    implementation "org.apache.axis2:axis2:${versions.axis2}"
    implementation "org.apache.axis:axis-jaxrpc:${versions.axis2_jaxrpc}"
    implementation "org.apache.axis2:axis2-saaj:${versions.axis2}"
    implementation "org.apache.axis2:axis2-codegen:${versions.axis2}"
    implementation "org.apache.axis2:axis2-adb-codegen:${versions.axis2}"
    
    // axis
    axis "org.apache.axis2:axis2:${versions.axis2}",
    "org.apache.axis:axis-jaxrpc:${versions.axis2_jaxrpc}",
    "org.apache.axis2:axis2-codegen:${versions.axis2}",
    "org.apache.axis2:axis2-adb-codegen:${versions.axis2}",
    "javax.activation:activation:${versions.javax_activation}"

* 3 - gradle jaxb task(will be added) nad gradle axis (genCountryInfoService)


Recommend third way. It is more easy and minimum dependency.

    Gradle clean/build
    Execute genXXX task.

#### Rest Layer

    Spring rest template
    Apache HttpClient 5
    JDK Http Client

#### WS Layer

    Spring webservice template
    Apache HttpClient 5

## External Adapter (Backend service) Response Pattern

The generic code of the segmentation should be finalized.

The codes in the templates below are given as an example.

```json
{
  "status": {
    "code": "0",
    "message": "Success"
  },
  "data": {
    //generic type "T"
    //Any Object of server success response
  }
}
```

## Exception Cases

There are three phases to get error.

### client exception

```json
{
  "status": {
    "code": "4000",
    "message": "Adapter Client Message"
    // fixed message
  }
}
```

### server exception

code : it is originated from server response
message : it is originated from server response

```json
{
  "status": {
    "code": "5000",
    "message": "Server Message"
    //get from server
  }
}
```

### client exception after server response handling

```json
{
  "status": {
    "code": "4500",
    "message": "Server Response Message Client Handling Exception Message"
    // fixed message
  }
}
```

## Example Adapter Usage

- TODO: Create a template project to use it to create new adapter modules.
- TODO: Explain how to use an example adapter in user project. How to create beans etc.

## Example Adapter Service Creation

Since you need to write an integration code, there should be one class that manages all requests
that you need to send to that endpoint. To make it simple to write integration code, this library
provides two base adapter classes that you can extend in your integration service
class ```RestAdapter``` and ```SoapAdapter```.

Let's say that we have below services.

#### First Service Definition

| Property Name              | Value                                                 | Description                                   |
|----------------------------|-------------------------------------------------------|-----------------------------------------------|
| Service Name               | ExampleRestService                                    | User readable name of service                 |
| Service Type               | REST                                                  | Service api model                             |
| Service APIs               | /example-rest/first, /example-rest/first/any-sub-path | API urls that exposed by service              |
| Service Gw Url             | https://api-gw.ourgw.com/example-rest                 | Main url of service that accessible from GW   |
| Service Gw SubUrl          | /example-rest                                         | Sub/context url of service that defined in GW |
| Service Service Direct Url | https://example.servicedomain.com/example-rest        | Direct access url of service                  |

#### Second Service Definition

| Property Name              | Value                                                   | Description                                   |
|----------------------------|---------------------------------------------------------|-----------------------------------------------|
| Service Name               | ExampleSoapService                                      | User readable name of service                 |
| Service Type               | SOAP                                                    | Service api model                             |
| Service APIs               | /example-soap/second, /example-soap/second/any-sub-path | API urls that exposed by service              |
| Service Gw Url             | https://api-gw.ourgw.com/example-soap                   | Main url of service that accessible from GW   |
| Service Gw SubUrl          | /example-soap                                           | Sub/context url of service that defined in GW |
| Service Service Direct Url | https://example.servicedomain.com/example-soap          | Direct access url of service                  |

First you need to create your service class. After you create it you can override default methods to
gain some more control.

### Rest Service Usage

#### Creating the example service and overriding default methods:

```java
public class ExampleRestService extends RestAdapter<EndpointConnectionConfig> {

  // User can override this method to check the response status from the body of response. Sometimes
  // services can return an incorrect http status which means a response with a successful http
  // status but an error inside the body.
  @Override
  protected AdapterStatus checkStatusDefaultIsHttp200(HttpAdapterResponse<?> httpAdapterResponse) {
    final FirstServiceResponseContainer responseContainer = (FirstServiceResponseContainer) httpAdapterResponse.body();
    //check the body data for status, create business failed status if there is any error.
    if (responseContainer.checkSomeBusinessError()) {
      return new AdapterResponse<>(AdapterStatus.createStatusFailedAsBusiness());
    }
    return AdapterStatus.createSuccess();
  }

  //User can override this method to add endpoint level default exception handling.
  @Override
  protected <T> AdapterResponse<T> handleExceptionWithDefault(Exception e) {
    if (e instanceof SomeException) {
      //do something and return a different AdapterResponse.
    }
    return super.handleExceptionWithDefault(e);
  }

}

``` 

There are more detail about creation steps of example service in below. Let's create an api
integration method first.

#### How to send request to an endpoint

Use get,post,put,delete or patch method of RestAdapter to send request to the endpoint.

```java
public class ExampleRestService extends RestAdapter<EndpointConnectionConfig> {

  public AdapterResponse firstOperation(String firstOpParam) {
    final Map<String, String> headers = new HashMap();
    headers.put("X-Example-Header", "random-value");

    return get(
        "/first?firstOpParam=" + firstOpParam,
        // api sub url, this will be added to endpointConfig serviceUrl(gateway or direct)
        headers,  // headers
        null, // request body ( null for get request)
        FirstRestResponse.class,// response type 
        httpAdapterResponse -> { // response customizer callback.
          final FirstRestResponse first = httpAdapterResponse.body();
          if (first.checkSomeBusinessError()) {
            return new AdapterResponse<>(AdapterStatus.createStatusFailedAsBusiness());
          }
          // you should map your response to your model and don't use Json library classes or generated response class dependent classes in your adapter response.
          return new AdapterResponse<>(AdapterStatus.createSuccess(),
              first.getTheDataYouWantToReturn());
        });
  }

}

``` 

### Soap Service Usage

#### Creating the example service and overriding default methods:

```java
public class ExampleSoapService extends SoapAdapter<EndpointConnectionConfig> {

  // User can override this method to check the response status from the body of response. Sometimes
  // services can return an incorrect http status which means a response with a successful http
  // status but an error inside the body.
  @Override
  protected AdapterStatus checkStatusDefaultIsHttp200(HttpAdapterResponse<?> httpAdapterResponse) {
    final SecondServiceResponseContainer responseContainer = (SecondServiceResponseContainer) httpAdapterResponse.body();
    //check the body data for status, create business failed status if there is any error.
    if (responseContainer.checkSomeBusinessError()) {
      return new AdapterResponse<>(AdapterStatus.createStatusFailedAsBusiness());
    }
    return AdapterStatus.createSuccess();
  }

  //User can override this method to add endpoint level default exception handling.
  @Override
  protected <T> AdapterResponse<T> handleExceptionWithDefault(Exception e) {
    if (e instanceof SomeException) {
      //do something and return a different AdapterResponse.
    }
    return super.handleExceptionWithDefault(e);
  }

}

``` 

There are more detail about creation steps of example service in below. Let's create an api
integration method first.

#### How to send request to an endpoint

Use get,post,put,delete or patch method of RestAdapter to send request to the endpoint.

```java
public class ExampleSoapService extends SoapAdapter<EndpointConnectionConfig> {

  public AdapterResponse secondOperation(String firstOpParam) {
    final Map<String, String> headers = new HashMap();
    headers.put("X-Example-Header", "random-value");

    return executeInternalWithCustomHandler(
        headers, // request headers
        new ListOfContinentsByName(),
        // request body ( this can be created by your soap service's ObjectFactory depending on implementation.)
        HttpMethod.POST,// In a soap request you need to use either POST or GET http method. 
        ListOfContinentsByNameResponse.class, // response type
        (httpAdapterResponse) -> { // optional response customizer
          //do any customization in here for your adapter response
          var yourResponseDto = httpAdapterResponse.body();// you should map your response to your model and don't use Soap library classes or generated response class dependent classes in your adapter response. 
          return new AdapterResponse<>(AdapterStatus.createSuccess(), yourResponseDto);
        });
  }

}

``` 

### How To Instantiate The Service

```java
import static config.com.inomera.middleware.BeanConfig.BEAN_APACHE_HTTP_REST_CLIENT;
import static config.com.inomera.middleware.BeanConfig.BEAN_APACHE_HTTP_SOAP_CLIENT;

@Configuration
public class BeanConfig {

    @Autowire
    ApplicationContext applicationContext;


    //REST SERVICES
    //Chose one of the three ways.

    @Bean//FIRST WAY
    public ExampleRestService exampleRestServiceWithoutGateway()
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, InstantiationException {

        //You can use any preconfigured bean from BeanConfig of adapter library.
        //You can use any implementation of HttpAdapterClient using any of its constructors.
        final ApacheHttpRestAdapterClient apacheHttpRestAdapterClient = (ApacheHttpRestAdapterClient) applicationContext.getBean(
                BEAN_APACHE_HTTP_REST_CLIENT, apacheHttpClient());
        return new ExampleRestService(mirketAdapterEndpointConfig(), apacheHttpRestAdapterClient);// add this constructor to your implementation.
    }

    @Bean//SECOND WAY
    public ExampleRestService exampleRestServiceWithoutGateway() {
        //You can create a clientHttpRequestFactory 3 different ways and provide it as Rest or Soap Adapter constructor parameter.
        ClientHttpRequestFactory clientHttpRequestFactory = ClientHttpRequestFactories.get(ClientHttpRequestFactorySettings.DEFAULTS);
        ClientHttpRequestFactory clientHttpRequestFactory = ClientHttpRequestFactories.get(
                HttpComponentsClientHttpRequestFactory.class,// you can use any implementation of ReqFactory. This is a spring implementation but you can implement yours.
                ClientHttpRequestFactorySettings
                        .DEFAULTS
                        .withConnectTimeout(Duration.of(60, ChronoUnit.SECONDS))
        );
        ClientHttpRequestFactory clientHttpRequestFactory = new ClientHttpRequestFactory() {
            @Override
            public ClientHttpRequest createRequest(URI uri, HttpMethod httpMethod) throws IOException {
                //custom implementation.
            }
        };

        return new ExampleRestService(mirketAdapterEndpointConfig(), clientHttpRequestFactory);// add this constructor to your implementation.
    }

    @Bean//THIRD WAY
    public ExampleRestService exampleRestServiceWithoutGateway() {
        //You can create a RequestFactoryConfig and provide standard configurations, it creates a ClientHttpRequestFactory with these configs.
        RequestFactoryConfig requestFactoryConfig = new RequestFactoryConfig();
        requestFactoryConfig.setRequestTimeout(Duration.of(60, ChronoUnit.SECONDS));
        requestFactoryConfig.setConnectionTimeout(Duration.of(60, ChronoUnit.SECONDS));
        requestFactoryConfig.setSslBundle(SslBundle.of(SslStoreBundle.NONE));
        return new ExampleRestService(mirketAdapterEndpointConfig(), requestFactoryConfig);// add this constructor to your implementation.
    }

    //Create your service with gateway config
    @Bean//FIRST WAY
    public ExampleRestService exampleRestServiceWithGateway(
            GwTokenProvider gwTokenProvider// Since you have created GatewayConfig, this bean will be created inside library automatically.
    )
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, InstantiationException {
        final ApacheHttpRestAdapterClient apacheHttpRestAdapterClient = (ApacheHttpRestAdapterClient) applicationContext.getBean(BEAN_APACHE_HTTP_REST_CLIENT, apacheHttpClient());
        return new ExampleRestService(mirketAdapterEndpointConfig(), apacheHttpRestAdapterClient, gwTokenProvider);// add this constructor to your implementation. provide this to BaseAdapter constructor. 
    }

    //SOAP SERVICES

    @Bean
    public ExampleSoapService exampleSoapService()
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, InstantiationException {
        EndpointConnectionConfig endpointConnectionConfig = exampleSoapServiceEndpointConfig();
        final ApacheHttpSoapAdapterClient soapAdapterClient = (ApacheHttpSoapAdapterClient) applicationContext.getBean(
                BEAN_APACHE_HTTP_SOAP_CLIENT, "com.turkcell.generated.class.package",
                endpointConnectionConfig, apacheHttpClient());
        return new ExampleSoapService(endpointConnectionConfig, soapAdapterClient);
    }
    //TODO ADD OTHER WAYS FOR SOAP SERVICE INITIALIZATION.


    //General configs
    @Bean
    public EndpointConnectionConfig exampleRestServiceEndpointConfig() {
        final EndpointConnectionConfig endpointConnectionConfig = new EndpointConnectionConfig();
        endpointConnectionConfig.setServiceDirectUrl("https://example.servicedomain.com/example-rest");
        endpointConnectionConfig.setGatewayUrlSubPath("/example-rest ");
        endpointConnectionConfig.setUser("user");
        endpointConnectionConfig.setPassword("password");
        endpointConnectionConfig.setUseGatewayIfEnabled(true);// disable/enable this in here.
        return endpointConnectionConfig;
    }

    @Bean
    public EndpointConnectionConfig exampleSoapServiceEndpointConfig() {
        final EndpointConnectionConfig endpointConnectionConfig = new EndpointConnectionConfig();
        endpointConnectionConfig.setServiceDirectUrl("https://example.servicedomain.com/example-soap");
        endpointConnectionConfig.setGatewayUrlSubPath("/example-soap");
        endpointConnectionConfig.setUser("user");
        endpointConnectionConfig.setPassword("password");
        endpointConnectionConfig.setUseGatewayIfEnabled(true);// disable/enable this in here.
        return endpointConnectionConfig;
    }

    @Bean
    public GatewayConfig gatewayConfig() {
        GatewayConfig gatewayConfig = GatewayConfig.updateAndGetInstance(
                "https://api-gw.ourgw.com",
                "username", "password", "clientId", "clientSecret");

        gatewayConfig.setAuthHeaderName("X-Custom-Auth-Header");
        gatewayConfig.setGWEnabled(true);
        return gatewayConfig;
    }

    @Bean
    public HttpClient apacheHttpClient()
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())  // <--- accepts each certificate
                .build();

        Registry<ConnectionSocketFactory> socketRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register(URIScheme.HTTPS.getId(),
                        new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                //.register(URIScheme.HTTP.getId(), new PlainConnectionSocketFactory())
                .build();

        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(new PoolingHttpClientConnectionManager(socketRegistry))
                .setConnectionManagerShared(true)
                .build();
        return httpClient;
    }
}

```

## DC-Adapters geliştirmelerinde genel standartlarımız

Öncelikle beklentimiz Mikroservis Kod Standartları dokumantasyonunu inceleyerek kod geliştirmelerine başlanılması, geliştirmeler yapılırken bu standartlara dikkat edilmesi.( Kod standartları ->https://confluence.turkcell.com.tr/pages/viewpage.action?pageId=126674602 )

### MS Adapter Modellemeler
 * Generate edilen model ile MS in kullanacağı modeller farklı olmalı. (Detay verecek olursak, MS içinde adapter içinde geliştiricinin oluşturduğu adapter request / response nesneleri olmalı, generated paketler MS içinde kullanılmamalı
 * Bizim adapter için oluşturduğumuz modeller isim standartlarına uygun olmalı. Altyapıda yapılmış isimlendirme hataları adapter ımızda düzenltilmiş olmalıdır. (Örneğin IsXyzDTO, IsXyzMapper altyapı modeli : IsXyz, “_” gibi özel karakterlerle DTO içinde field olmamalı, DTO field isimleri uygun olup getXyzProduct gibi yüklemle CRUD yüklemleriyle başlayanlardan kaçınmalıyız. 
 * DTO class ve field isimlendirmelerinde microservice isimlendirme standartlarına dikkat edilmelidir. (Code standartları 2.madde)

### Model Dönüşümleri
 * Kompleksiteye bağlı olarak Java obje dönüşümü veya Mapstruct kullanılabilir. (Otomatik dönüşümlerin kolaylığı, NPE gibi durumların önüne geçmek adına Mapstruct tavsiye edilir)
 * MapStruct kullanımı yapıldığı durumlarda her bir N adet Req-RespDTO için ayrı ayrı Mapper oluşturulmamalıdır. Mapperlar belli bir domain kuralına göre gruplanabilir.
 * Servislerde Mapperları autowired etmekten kaçınıp Instance ı ile işlem yapılmalı. Her bir Req-Resp DTO için mapper oluşturulduğunda 2*N kadar mapper servise autowired olmamalı. DI ların da bir limiti olmalıdır.
 
### Configuration
 * Soap/Rest Adapter (abstraction) yanına MS için kullanılacak customer interface oluşturulmalıdır.
Örnek : CountryAdapterImpl extends SoapAdapter implements CountryAdapter
Örnek projeler :
https://stash.turkcell.com.tr/git/projects/SH200711/repos/dc-adapters/browse/netflow-adapter/src/main/java/com/turkcell/dcs/netflow
https://stash.turkcell.com.tr/git/projects/SH200711/repos/dc-adapters/browse/atom-adapter/src/main/java/com/turkcell/dcs/atom
 * SoapAdapter’da bulunan constructorlarla gelen yeteneklerin tamamının impl. classında olmasına dikkat edilebilir. (4 constructor ile bu yetenekler oluşturulmalı)
 * Entegrasyon testleri olmalıdır.
 * Wsdl, xsd dosyaları resource altında olmalıdır, build gradle larda altyapı wsdl url leri olmamalıdır.  
 * Generate edilen dosyaları fixed olarak (SOAP için) source kod dışında generated prefixli bir paket altına atıyoruz. (nice to have bir işlem sadece bütünlük içindir)

### Exception Handling
 * İçeride yakalanan exceptionlar loglanmalı, uygun exception türlerine göre çıktılar olmalıdır.
 * Bazı soap istekler response da hatayı dönmek yerine ilgili method bazında custom request veya business exception atabilmektedir. Bu hata tiplerine yakalanarak özel olarak çıkılmalı yutulmamalıdır.
 * Loglama stratejisi başta belirlenip ona göre konfigüre edilmelidir.

### Admin Cms tanımları
 * Admin cms tanımları operasyonla ve integration takımıyla konuşularak uygun formatta olmalıdır