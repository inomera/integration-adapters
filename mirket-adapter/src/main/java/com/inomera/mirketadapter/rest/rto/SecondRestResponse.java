package com.inomera.mirketadapter.rest.rto;

import java.io.Serializable;
import java.util.HashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SecondRestResponse implements Serializable {

  private static final long serialVersionUID = 1L;
  private String result;
  private Headers headers;

  @NoArgsConstructor
  @Getter
  @Setter
  public static class Headers extends HashMap<String, String> {

  }
}
