package com.inomera.mirketadapter.soap.model;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CountrySoapAdapterProperties implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private String url;
  private String authHeader;
  private String authHeaderValue;
}
