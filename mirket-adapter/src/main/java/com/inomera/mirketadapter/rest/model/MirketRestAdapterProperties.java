package com.inomera.mirketadapter.rest.model;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MirketRestAdapterProperties implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  private String authHeader;
  private String authHeaderValue;

}
