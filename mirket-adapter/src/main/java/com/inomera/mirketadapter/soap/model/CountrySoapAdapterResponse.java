package com.inomera.mirketadapter.soap.model;

import com.inomera.mirketadapter.rest.rto.FirstRestResponse;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountrySoapAdapterResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String result;
  private Map<String, String> headers;

  public CountrySoapAdapterResponse(FirstRestResponse firstResponse) {
    this.result = firstResponse.getResult();
    this.headers = firstResponse.getHeaders();
  }
}
