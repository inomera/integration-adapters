package com.inomera.middleware.util;

import com.inomera.integration.config.model.HttpClientProperties;
import com.inomera.integration.config.model.PemSSLBundleProperties;
import com.inomera.integration.config.model.SSLProperties;
import com.inomera.middleware.ssl.PropertiesSSLBundle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslStoreBundle;
import org.springframework.util.Assert;

@Slf4j
public final class SslBundleUtils {

  public static SslBundle createSslBundle(HttpClientProperties http, String url) {
    if (http.isSkipSsl()) {
      return SslBundle.of(SslStoreBundle.NONE);
    }
    SSLProperties ssl = http.getSsl();
    Assert.notNull(ssl, "SSL properties are not defined");

    PemSSLBundleProperties pem = ssl.getPem();
    Assert.notNull(pem, "PEM SSL properties are not defined");

    return PropertiesSSLBundle.get(pem);
  }
}
