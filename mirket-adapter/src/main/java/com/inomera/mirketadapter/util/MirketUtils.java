package com.inomera.mirketadapter.util;

import java.util.Base64;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MirketUtils {

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
}
