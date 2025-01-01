package com.inomera.middleware.util;

import com.inomera.integration.model.AdapterResponse;
import jakarta.xml.bind.JAXBElement;
import java.util.Base64;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdapterUtils {

  public static String getNormalizedUrl(String baseUrl, String endpoint) {
    if (!baseUrl.endsWith("/")) {
      baseUrl = baseUrl.concat("/");
    }
    if (endpoint.startsWith("/")) {
      endpoint = endpoint.substring(1);
    }
    return baseUrl + endpoint;
  }

  public static String getEncodedUserPass(String user, String pass) {
    return Base64.getEncoder().encodeToString((user + ":" + pass).getBytes());
  }

  public static <T> T nullCheck(JAXBElement<T> val) {
    return val == null ? null : val.getValue();
  }

  public static <T> boolean isSuccessAndHasData(AdapterResponse<T> adapterResponse) {
    return adapterResponse.getStatus().isSuccess() && hasData(adapterResponse);
  }

  public static <T> boolean isSuccessAndDoesNotHaveData(AdapterResponse<T> adapterResponse) {
    return adapterResponse.getStatus().isSuccess() && ObjectUtils.isEmpty(
        adapterResponse.getData());
  }

  public static <T> boolean isFailOrDoesNotHaveData(AdapterResponse<T> adapterResponse) {
    return isFail(adapterResponse) || hasNotData(adapterResponse);
  }

  public static <T> boolean isFail(AdapterResponse<T> adapterResponse) {
    return !isSuccess(adapterResponse);
  }

  public static <T> boolean isSuccess(AdapterResponse<T> adapterResponse) {
    return adapterResponse.getStatus().isSuccess();
  }

  private static <T> boolean hasNotData(AdapterResponse<T> adapterResponse) {
    return !hasData(adapterResponse);
  }

  private static <T> boolean hasData(AdapterResponse<T> adapterResponse) {
    return ObjectUtils.isNotEmpty(adapterResponse.getData());
  }

  public static <T> boolean isFailAndNotHaveData(AdapterResponse<T> adapterResponse) {
    return isFail(adapterResponse) && hasNotData(adapterResponse);
  }
}
