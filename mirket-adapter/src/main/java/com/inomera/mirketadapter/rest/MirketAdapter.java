package com.inomera.mirketadapter.rest;


import com.inomera.integration.model.AdapterResponse;
import com.inomera.mirketadapter.rest.model.MirketRestAdapterProperties;
import com.inomera.mirketadapter.rest.model.MirketRestAdapterRequest;
import java.util.function.Function;

public interface MirketAdapter {

  <I, O> AdapterResponse<O> getFirst(MirketRestAdapterRequest adapterRequest,
      MirketRestAdapterProperties mirketProps, Function<I, O> resultMapperFunction);

  AdapterResponse<String> getFirst(MirketRestAdapterRequest adapterRequest,
      MirketRestAdapterProperties mirketProps);

  AdapterResponse<String> getFirstGeneric(MirketRestAdapterRequest adapterRequest,
      MirketRestAdapterProperties mirketProps);

}
