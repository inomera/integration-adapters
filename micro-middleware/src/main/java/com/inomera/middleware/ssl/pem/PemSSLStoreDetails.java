package com.inomera.middleware.ssl.pem;

import java.security.KeyStore;
import org.springframework.boot.ssl.pem.PemSslStoreBundle;
import org.springframework.util.StringUtils;

/**
 * Details for an individual trust or key store in a {@link PemSslStoreBundle}.
 *
 * @param type               the key store type, for example {@code JKS} or {@code PKCS11}. A
 *                           {@code null} value will use {@link KeyStore#getDefaultType()}).
 * @param alias              the alias used when setting entries in the {@link KeyStore}
 * @param password           the password used
 *                           {@link KeyStore#setKeyEntry(String, java.security.Key, char[],
 *                           java.security.cert.Certificate[]) setting key entries} in the
 *                           {@link KeyStore}
 * @param certificates       the certificates content (either the PEM content itself or or a
 *                           reference to the resource to load). When a
 *                           {@link #privateKey() private key} is present this value is treated as a
 *                           certificate chain, otherwise it is treated a list of certificates that
 *                           should all be registered.
 * @param privateKey         the private key content (either the PEM content itself or a reference
 *                           to the resource to load)
 * @param privateKeyPassword a password used to decrypt an encrypted private key
 * @author Scott Frederick
 * @author Phillip Webb
 * @see PemSSLStore#load(PemSSLStoreDetails)
 * @since 3.1.0
 */
public record PemSSLStoreDetails(String type, String alias, String password, String certificates,
                                 String privateKey,
                                 String privateKeyPassword) {

  /**
   * Create a new {@link org.springframework.boot.ssl.pem.PemSslStoreDetails} instance.
   *
   * @param type               the key store type, for example {@code JKS} or {@code PKCS11}. A
   *                           {@code null} value will use {@link KeyStore#getDefaultType()}).
   * @param alias              the alias used when setting entries in the {@link KeyStore}
   * @param password           the password used
   *                           {@link KeyStore#setKeyEntry(String, java.security.Key, char[],
   *                           java.security.cert.Certificate[]) setting key entries} in the
   *                           {@link KeyStore}
   * @param certificates       the certificate content (either the PEM content itself or a reference
   *                           to the resource to load)
   * @param privateKey         the private key content (either the PEM content itself or a reference
   *                           to the resource to load)
   * @param privateKeyPassword a password used to decrypt an encrypted private key
   * @since 3.2.0
   */
  public PemSSLStoreDetails {
  }

  /**
   * Create a new {@link org.springframework.boot.ssl.pem.PemSslStoreDetails} instance.
   *
   * @param type               the key store type, for example {@code JKS} or {@code PKCS11}. A
   *                           {@code null} value will use {@link KeyStore#getDefaultType()}).
   * @param certificate        the certificate content (either the PEM content itself or a reference
   *                           to the resource to load)
   * @param privateKey         the private key content (either the PEM content itself or a reference
   *                           to the resource to load)
   * @param privateKeyPassword a password used to decrypt an encrypted private key
   */
  public PemSSLStoreDetails(String type, String certificate, String privateKey,
      String privateKeyPassword) {
    this(type, null, null, certificate, privateKey, null);
  }

  /**
   * Create a new {@link org.springframework.boot.ssl.pem.PemSslStoreDetails} instance.
   *
   * @param type        the key store type, for example {@code JKS} or {@code PKCS11}. A
   *                    {@code null} value will use {@link KeyStore#getDefaultType()}).
   * @param certificate the certificate content (either the PEM content itself or a reference to the
   *                    resource to load)
   * @param privateKey  the private key content (either the PEM content itself or a reference to the
   *                    resource to load)
   */
  public PemSSLStoreDetails(String type, String certificate, String privateKey) {
    this(type, certificate, privateKey, null);
  }

  /**
   * Return a new {@link PemSSLStoreDetails} instance with a new private key.
   *
   * @param privateKey the new private key
   * @return a new {@link PemSSLStoreDetails} instance
   */
  public PemSSLStoreDetails withPrivateKey(String privateKey) {
    return new PemSSLStoreDetails(this.type, this.certificates, privateKey,
        this.privateKeyPassword);
  }

  /**
   * Return a new {@link PemSSLStoreDetails} instance with a new private key password.
   *
   * @param password the new private key password
   * @return a new {@link PemSSLStoreDetails} instance
   */
  public PemSSLStoreDetails withPrivateKeyPassword(String password) {
    return new PemSSLStoreDetails(this.type, this.certificates, this.privateKey, password);
  }

  boolean isEmpty() {
    return isEmpty(this.type) && isEmpty(this.certificates) && isEmpty(this.privateKey);
  }

  private boolean isEmpty(String value) {
    return !StringUtils.hasText(value);
  }

  /**
   * Factory method to create a new {@link PemSSLStoreDetails} instance for the given certificate.
   *
   * @param certificate the certificate
   * @return a new {@link PemSSLStoreDetails} instance.
   */
  public static PemSSLStoreDetails forCertificate(String certificate) {
    return new PemSSLStoreDetails(null, certificate, null);
  }
}

