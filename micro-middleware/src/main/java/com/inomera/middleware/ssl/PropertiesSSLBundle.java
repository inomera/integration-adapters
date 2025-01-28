package com.inomera.middleware.ssl;

import com.inomera.integration.config.model.PemSSLBundleProperties;
import com.inomera.integration.config.model.PemSSLBundleProperties.Store;
import com.inomera.integration.config.model.SSLBundleProperties;
import com.inomera.integration.config.model.SSLBundleProperties.Key;
import com.inomera.middleware.ssl.pem.PemSSLStore;
import com.inomera.middleware.ssl.pem.PemSSLStoreBundle;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.springframework.boot.autoconfigure.ssl.PemSslBundleProperties;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundleKey;
import org.springframework.boot.ssl.SslManagerBundle;
import org.springframework.boot.ssl.SslOptions;
import org.springframework.boot.ssl.SslStoreBundle;

/**
 * {@link SslBundle} backed by {@link PemSslBundleProperties}.
 */
public class PropertiesSSLBundle implements SslBundle {

  private final SslStoreBundle stores;

  private final SslBundleKey key;

  private final SslOptions options;

  private final String protocol;

  private final SslManagerBundle managers;

  private PropertiesSSLBundle(SslStoreBundle stores, SSLBundleProperties properties) {
    this.stores = stores;
    this.key = asSslKeyReference(properties.getKey());
    this.options = asSslOptions(properties.getOptions());
    this.protocol = properties.getProtocol();
    this.managers = SslManagerBundle.from(this.stores, this.key);
  }

  private static SslBundleKey asSslKeyReference(Key key) {
    return (key != null) ? SslBundleKey.of(key.getPassword(), key.getAlias()) : SslBundleKey.NONE;
  }

  private static SslOptions asSslOptions(SSLBundleProperties.Options options) {
    return (options != null) ? SslOptions.of(options.getCiphers(), options.getEnabledProtocols())
        : SslOptions.NONE;
  }

  @Override
  public SslStoreBundle getStores() {
    return this.stores;
  }

  @Override
  public SslBundleKey getKey() {
    return this.key;
  }

  @Override
  public SslOptions getOptions() {
    return this.options;
  }

  @Override
  public String getProtocol() {
    return this.protocol;
  }

  @Override
  public SslManagerBundle getManagers() {
    return this.managers;
  }

  /**
   * Get an {@link SslBundle} for the given {@link PemSslBundleProperties}.
   *
   * @param properties the source properties
   * @return an {@link SslBundle} instance
   */
  public static SslBundle get(PemSSLBundleProperties properties) {
    return get(null, properties, new DefaultPemSSLStoreFactory());
  }

  static SslBundle get(String bundleName, PemSSLBundleProperties properties,
      PemSSLStoreFactory storeFactory) {
    try {
      PemSSLStore keyStore = getPemSSLStore(bundleName, storeFactory, "keystore",
          properties.getKeystore());
      if (keyStore != null) {
        keyStore = keyStore.withAlias(properties.getKey().getAlias())
            .withPassword(properties.getKey().getPassword());
      }
      PemSSLStore trustStore = getPemSSLStore(bundleName, storeFactory, "truststore",
          properties.getTruststore());
      SslStoreBundle storeBundle = new PemSSLStoreBundle(keyStore, trustStore);
      return new PropertiesSSLBundle(storeBundle, properties);
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  private static PemSSLStore getPemSSLStore(String bundleName, PemSSLStoreFactory storeFactory,
      String storePropertyName,
      Store storeProperties)
      throws IOException {
    return storeFactory.getPemSSLStore(bundleName, storePropertyName, storeProperties);
  }
}
