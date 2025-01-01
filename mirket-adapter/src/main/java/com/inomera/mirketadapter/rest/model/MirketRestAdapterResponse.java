package com.inomera.mirketadapter.rest.model;

import com.inomera.mirketadapter.rest.rto.FirstRestResponse;
import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MirketRestAdapterResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private String result;
  private Map<String, String> headers;

  public MirketRestAdapterResponse(FirstRestResponse firstResponse) {
    this.result = firstResponse.getResult();
    this.headers = firstResponse.getHeaders();
  }
}
