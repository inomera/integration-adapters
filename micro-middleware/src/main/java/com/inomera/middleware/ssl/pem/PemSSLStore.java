package com.inomera.middleware.ssl.pem;

import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;
import org.springframework.util.Assert;

/**
 * An individual trust or key store that has been loaded from PEM content.
 */
public interface PemSSLStore {

  /**
   * The key store type, for example {@code JKS} or {@code PKCS11}. A {@code null} value will use
   * {@link KeyStore#getDefaultType()}).
   *
   * @return the key store type
   */
  String type();

  /**
   * The alias used when setting entries in the {@link KeyStore}.
   *
   * @return the alias
   */
  String alias();

  /**
   * the password used
   * {@link KeyStore#setKeyEntry(String, java.security.Key, char[],
   * java.security.cert.Certificate[]) setting key entries} in the {@link KeyStore}.
   *
   * @return the password
   */
  String password();

  /**
   * The certificates for this store. When a {@link #privateKey() private key} is present the
   * returned value is treated as a certificate chain, otherwise it is treated a list of
   * certificates that should all be registered.
   *
   * @return the X509 certificates
   */
  List<X509Certificate> certificates();

  /**
   * The private key for this store or {@code null}.
   *
   * @return the private key
   */
  PrivateKey privateKey();

  /**
   * Return a new {@link PemSSLStore} instance with a new alias.
   *
   * @param alias the new alias
   * @return a new {@link PemSSLStore} instance
   */
  default PemSSLStore withAlias(String alias) {
    return of(type(), alias, password(), certificates(), privateKey());
  }

  /**
   * Return a new {@link PemSSLStore} instance with a new password.
   *
   * @param password the new password
   * @return a new {@link PemSSLStore} instance
   */
  default PemSSLStore withPassword(String password) {
    return of(type(), alias(), password, certificates(), privateKey());
  }

  /**
   * Return a {@link PemSSLStore} instance loaded using the given {@link PemSSLStoreDetails}.
   *
   * @param details the PEM store details
   * @return a loaded {@link PemSSLStore} or {@code null}.
   * @throws IOException on IO error
   */
  static PemSSLStore load(PemSSLStoreDetails details) throws IOException {
    if (details == null || details.isEmpty()) {
      return null;
    }
    return new LoadedPemSslStore(details);
  }

  /**
   * Factory method that can be used to create a new {@link PemSSLStore} with the given values.
   *
   * @param type         the key store type
   * @param certificates the certificates for this store
   * @param privateKey   the private key
   * @return a new {@link PemSSLStore} instance
   */
  static PemSSLStore of(String type, List<X509Certificate> certificates, PrivateKey privateKey) {
    return of(type, null, null, certificates, privateKey);
  }

  /**
   * Factory method that can be used to create a new {@link PemSSLStore} with the given values.
   *
   * @param certificates the certificates for this store
   * @param privateKey   the private key
   * @return a new {@link PemSSLStore} instance
   */
  static PemSSLStore of(List<X509Certificate> certificates, PrivateKey privateKey) {
    return of(null, null, null, certificates, privateKey);
  }

  /**
   * Factory method that can be used to create a new {@link PemSSLStore} with the given values.
   *
   * @param type         the key store type
   * @param alias        the alias used when setting entries in the {@link KeyStore}
   * @param password     the password used
   *                     {@link KeyStore#setKeyEntry(String, java.security.Key, char[],
   *                     java.security.cert.Certificate[]) setting key entries} in the
   *                     {@link KeyStore}
   * @param certificates the certificates for this store
   * @param privateKey   the private key
   * @return a new {@link PemSSLStore} instance
   */
  static PemSSLStore of(String type, String alias, String password,
                        List<X509Certificate> certificates,
                        PrivateKey privateKey) {
    Assert.notEmpty(certificates, "Certificates must not be empty");
    return new PemSSLStore() {

      @Override
      public String type() {
        return type;
      }

      @Override
      public String alias() {
        return alias;
      }

      @Override
      public String password() {
        return password;
      }

      @Override
      public List<X509Certificate> certificates() {
        return certificates;
      }

      @Override
      public PrivateKey privateKey() {
        return privateKey;
      }

    };
  }
}
