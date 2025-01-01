package com.inomera.mirketadapter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MirketAdapterErrors {

  public static final String SUCCESS_CODE = "0";
  public static final String SUCCESS_CODE_DESC = "Success";
  public static final String UNKNOWN_ADAPTER_ERR = "UNKNOWN_ADAPTER_ERR";
  public static final String NO_ERROR = "NO_ERROR";

  public static final String CLIENT_PREP_ERR_CODE = "4000";
  public static final String CLIENT_PREP_ERR_CODE_DESC = "EXECUTION_IO_EXCEPTION";

  public static final String SERVER_RESPONSE_CLIENT_HANDLING_CODE = "4500";
  public static final String SERVER_RESPONSE_CLIENT_HANDLING_DESC = "EXECUTION_IO_EXCEPTION";


}
