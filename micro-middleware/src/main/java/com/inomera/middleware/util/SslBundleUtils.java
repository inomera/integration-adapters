package com.inomera.middleware.util;

import com.inomera.integration.config.model.HttpClientProperties;
import com.inomera.ssl.MultiTrustSSLContextBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundleKey;
import org.springframework.boot.ssl.SslManagerBundle;
import org.springframework.boot.ssl.SslOptions;
import org.springframework.boot.ssl.SslStoreBundle;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import static org.springframework.boot.ssl.SslBundle.DEFAULT_PROTOCOL;

@Slf4j
public final class SslBundleUtils {

  public static SslBundle createSslBundle(HttpClientProperties http, String url) {
    if (http.isSkipSsl()) {
      return SslBundle.of(SslStoreBundle.NONE);
    }
    MultiTrustSSLContextBuilder multiTrustSSLContextBuilder = new MultiTrustSSLContextBuilder();
    try {
      //TODO : load from path, file, text? (some param should be added to adapter config)
      SSLContext sslContext = multiTrustSSLContextBuilder.build();
      //TODO : combine with ssl-forge
//      return SslBundle.of(SslStoreBundle.of(sslContext));
//      SslStoreBundle sslStoreBundle = SslStoreBundle.of(keystore, keystorePassword, trustStore);
//      SslBundleKey sslBundleKey = SslBundleKey.of(keystorePassword, keyAlias);
//      SslOptions sslOptions = SslOptions.of(ciphers, protocols);
      SslStoreBundle sslStoreBundle = SslStoreBundle.of(null, "", null);
      SslBundleKey sslBundleKey = SslBundleKey.of("", "");
      SslOptions sslOptions = SslOptions.of(new String[]{}, new String[]{});
      SslManagerBundle sslManagerBundle = SslManagerBundle.from(sslStoreBundle, sslBundleKey);

      SslBundle sslBundle = SslBundle.of(sslStoreBundle, sslBundleKey, sslOptions, DEFAULT_PROTOCOL,
          sslManagerBundle);
      DefaultSslBundleRegistry sslBundleRegistry = new DefaultSslBundleRegistry();
      sslBundleRegistry.registerBundle("multi-trust-bundle-soap", sslBundle);
      return sslBundle;
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      LOG.error("url:: {} -> SSL Context couldn't be initialized", url, e);
      throw new RuntimeException(e);
    }
  }
}
