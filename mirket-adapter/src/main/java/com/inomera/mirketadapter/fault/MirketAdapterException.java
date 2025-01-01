package com.inomera.mirketadapter.fault;

import com.inomera.integration.fault.AdapterException;
import com.inomera.integration.model.AdapterStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MirketAdapterException extends AdapterException {

  private String code;
  private String message;

  public MirketAdapterException(String code, String message) {
    super(AdapterStatus.createStatusFailedAsTechnical());
    this.code = code;
    this.message = message;
  }

  public MirketAdapterException(AdapterStatus adapterStatus) {
    super(adapterStatus);
  }

  public MirketAdapterException(AdapterStatus adapterStatus, String code, String message) {
    super(adapterStatus);
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
