package com.inomera.middleware.ssl.pem;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import org.springframework.boot.ssl.SslStoreBundle;
import org.springframework.boot.ssl.pem.PemSslStoreBundle;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link SslStoreBundle} backed by PEM-encoded certificates and private keys.
 */
public class PemSSLStoreBundle implements SslStoreBundle {

  private static final String DEFAULT_KEY_ALIAS = "ssl";

  private final KeyStore keyStore;

  private final KeyStore trustStore;

  /**
   * Create a new {@link PemSslStoreBundle} instance.
   *
   * @param pemKeyStore   the PEM key store
   * @param pemTrustStore the PEM trust store
   * @since 3.2.0
   */
  public PemSSLStoreBundle(PemSSLStore pemKeyStore, PemSSLStore pemTrustStore) {
    this(pemKeyStore, pemTrustStore, null);
  }

  private PemSSLStoreBundle(PemSSLStore pemKeyStore, PemSSLStore pemTrustStore,
      String alias) {
    this.keyStore = createKeyStore("key", pemKeyStore, alias);
    this.trustStore = createKeyStore("trust", pemTrustStore, alias);
  }

  @Override
  public KeyStore getKeyStore() {
    return this.keyStore;
  }

  @Override
  public String getKeyStorePassword() {
    return null;
  }

  @Override
  public KeyStore getTrustStore() {
    return this.trustStore;
  }

  private KeyStore createKeyStore(String name, PemSSLStore pemSslStore, String alias) {
    if (pemSslStore == null) {
      return null;
    }
    try {
      Assert.notEmpty(pemSslStore.certificates(), "Certificates must not be empty");
      alias = (pemSslStore.alias() != null) ? pemSslStore.alias() : alias;
      alias = (alias != null) ? alias : DEFAULT_KEY_ALIAS;
      KeyStore store = createKeyStore(pemSslStore.type());
      List<X509Certificate> certificates = pemSslStore.certificates();
      PrivateKey privateKey = pemSslStore.privateKey();
      if (privateKey != null) {
        addPrivateKey(store, privateKey, alias, pemSslStore.password(), certificates);
      } else {
        addCertificates(store, certificates, alias);
      }
      return store;
    } catch (Exception ex) {
      throw new IllegalStateException(
          "Unable to create %s store: %s".formatted(name, ex.getMessage()), ex);
    }
  }

  private static KeyStore createKeyStore(String type)
      throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
    KeyStore store = KeyStore.getInstance(
        StringUtils.hasText(type) ? type : KeyStore.getDefaultType());
    store.load(null);
    return store;
  }

  private static void addPrivateKey(KeyStore keyStore, PrivateKey privateKey, String alias,
      String keyPassword,
      List<X509Certificate> certificateChain) throws KeyStoreException {
    keyStore.setKeyEntry(alias, privateKey,
        (keyPassword != null) ? keyPassword.toCharArray() : null,
        certificateChain.toArray(X509Certificate[]::new));
  }

  private static void addCertificates(KeyStore keyStore, List<X509Certificate> certificates,
      String alias)
      throws KeyStoreException {
    for (int index = 0; index < certificates.size(); index++) {
      String entryAlias = alias + ((certificates.size() == 1) ? "" : "-" + index);
      X509Certificate certificate = certificates.get(index);
      keyStore.setCertificateEntry(entryAlias, certificate);
    }
  }
}
