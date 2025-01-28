package com.inomera.middleware.ssl.pem;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * {@link PemSSLStore} loaded from {@link PemSSLStoreDetails}.
 *
 * @see PemSSLStore#load(PemSSLStoreDetails)
 */
final class LoadedPemSslStore implements PemSSLStore {

  private final PemSSLStoreDetails details;

  private final List<X509Certificate> certificates;

  private final PrivateKey privateKey;

  LoadedPemSslStore(PemSSLStoreDetails details) throws IOException {
    Assert.notNull(details, "Details must not be null");
    this.details = details;
    this.certificates = loadCertificates(details);
    this.privateKey = loadPrivateKey(details);
  }

  private static List<X509Certificate> loadCertificates(PemSSLStoreDetails details) throws IOException {
    PemContent pemContent = PemContent.load(details.certificates());
    if (pemContent == null) {
      return null;
    }
    List<X509Certificate> certificates = pemContent.getCertificates();
    Assert.state(!CollectionUtils.isEmpty(certificates), "Loaded certificates are empty");
    return certificates;
  }

  private static PrivateKey loadPrivateKey(PemSSLStoreDetails details) throws IOException {
    PemContent pemContent = PemContent.load(details.privateKey());
    return (pemContent != null) ? pemContent.getPrivateKey(details.privateKeyPassword()) : null;
  }

  @Override
  public String type() {
    return this.details.type();
  }

  @Override
  public String alias() {
    return this.details.alias();
  }

  @Override
  public String password() {
    return this.details.password();
  }

  @Override
  public List<X509Certificate> certificates() {
    return this.certificates;
  }

  @Override
  public PrivateKey privateKey() {
    return this.privateKey;
  }
}
