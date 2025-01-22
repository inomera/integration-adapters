package com.inomera.mirketadapter.rest;

import com.inomera.integration.client.HttpRestAdapterClient;
import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.integration.constant.HttpMethod;
import com.inomera.integration.interceptor.IntegrationAdapterInterceptor;
import com.inomera.integration.model.AdapterResponse;
import com.inomera.integration.model.AdapterStatus;
import com.inomera.integration.model.HttpAdapterResponse;
import com.inomera.integration.type.RestAdapter;
import com.inomera.mirketadapter.rest.model.MirketRestAdapterProperties;
import com.inomera.mirketadapter.rest.model.MirketRestAdapterRequest;
import com.inomera.mirketadapter.rest.rto.FirstRestResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MirketAdapterImpl extends RestAdapter<AdapterConfig> implements
    MirketAdapter {

  public MirketAdapterImpl(Supplier<AdapterConfig> configSupplierFunc,
      HttpRestAdapterClient httpAdapterClient) {
    super(configSupplierFunc, httpAdapterClient);
  }

  public MirketAdapterImpl(Supplier<AdapterConfig> adapterConfigDataSupplier,
      HttpRestAdapterClient httpRestAdapterClient, List<IntegrationAdapterInterceptor> interceptors)
      throws InstantiationException {
    super(adapterConfigDataSupplier, httpRestAdapterClient, interceptors);
  }

  @Override
  public <I, O> AdapterResponse<O> getFirst(MirketRestAdapterRequest adapterRequest,
      MirketRestAdapterProperties mirketProps, Function<I, O> resultMapperFunction) {
    final Map<String, String> headers = new HashMap();
    headers.put("X-GW-API-KEY", "3cf9ff3b-c2d0-4813-8f31-cd940490923f");
    headers.put("Host", "api.mirket.inomera.com");
    final AdapterResponse<FirstRestResponse> ar = executeGeneric("/v10/first", HttpMethod.GET,
        headers, null, FirstRestResponse.class, null);
    return (AdapterResponse<O>) ar;
  }

  @Override
  public AdapterResponse<String> getFirst(MirketRestAdapterRequest adapterRequest,
      MirketRestAdapterProperties mirketProps) {
    final Map<String, String> headers = new HashMap();
    headers.put("X-GW-API-KEY", "3cf9ff3b-c2d0-4813-8f31-cd940490923f");
    headers.put("Host", "api.mirket.inomera.com");
    return get("", headers, null,
        FirstRestResponse.class, httpAdapterResponse -> {

          final FirstRestResponse first = httpAdapterResponse.body();
          if (first.getHeaders().containsKey("X-Forwarded-Host")) {
            return new AdapterResponse<>(AdapterStatus.createSuccess(),
                first.getHeaders().get("X-Forwarded-Host"));
          }

          return new AdapterResponse<>(AdapterStatus.createStatusFailedAsBusiness());
        });
  }

  @Override
  public AdapterResponse<String> getFirstGeneric(MirketRestAdapterRequest adapterRequest,
      MirketRestAdapterProperties mirketProps) {
    final Map<String, String> headers = new HashMap();
    headers.put("X-GW-API-KEY", "3cf9ff3b-c2d0-4813-8f31-cd940490923f");
    return executeGeneric("/v10/first", HttpMethod.GET, headers, null,
        FirstRestResponse.class, (httpAdapterResponse) -> {
          if (httpAdapterResponse != null) {
            final FirstRestResponse first = FirstRestResponse.class.cast(
                httpAdapterResponse.body());
            if (first.getHeaders().containsKey("X-Forwarded-For")) {
              return new AdapterResponse<>(AdapterStatus.createSuccess(),
                  first.getHeaders().get("X-Forwarded-For"));
            }
          }
          return new AdapterResponse<>(AdapterStatus.createStatusFailedAsBusiness(), "");
        });
  }

  @Override
  protected AdapterStatus checkStatusDefaultIsHttp200(HttpAdapterResponse<?> httpAdapterResponse) {
    final int statusCode = httpAdapterResponse.statusCode();
    if (statusCode < 200 || statusCode >= 300) {
      return AdapterStatus.createStatusFailedAsTechnical();
    }
    if (statusCode == 200) {
      final FirstRestResponse firstRestResponse = (FirstRestResponse) httpAdapterResponse.body();
      if ("false".equals(firstRestResponse.getResult())) {
        return AdapterStatus.createStatusFailedAsBusiness();
      }
    }
    return AdapterStatus.createSuccess();
  }

  @Override
  protected <T> AdapterResponse<T> handleExceptionWithDefault(Exception e) {
    LOG.error("Mirket Adapter Exception : " + e.getMessage(), e);
    return super.handleExceptionWithDefault(e);
  }
}
