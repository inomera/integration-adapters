package com.inomera.mirketadapter.fault;

import com.inomera.integration.fault.AdapterException;
import com.inomera.integration.model.AdapterStatus;

public class CountryAdapterException extends AdapterException {

  private String code;
  private String message;

  public CountryAdapterException(AdapterStatus adapterStatus) {
    super(adapterStatus);
  }

  public CountryAdapterException(String code, String message) {
    super(AdapterStatus.createStatusFailedAsTechnical());
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
