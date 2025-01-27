package com.inomera.middleware.ssl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.X509Certificate;
import java.util.List;

import com.inomera.middleware.ssl.pem.PemContent;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * A certificate file that contains {@link X509Certificate X509 certificates}.
 *
 * @param path the path of the file that contains the content
 * @param certificates the certificates contained in the file
 */
public record CertificateFile(Path path, List<X509Certificate> certificates) {

  public CertificateFile {
    Assert.notNull(path, "Path must not be null");
    Assert.isTrue(Files.isRegularFile(path), "Path '%s' must be a regular file".formatted(path));
    Assert.isTrue(!CollectionUtils.isEmpty(certificates), "Certificates must not be empty");
  }

  /**
   * Return the leaf certificate which by convention is the first element in
   * {@link #certificates()}.
   * @return the primary certificate
   */
  public X509Certificate leafCertificate() {
    return certificates().get(0);
  }

  @Override
  public String toString() {
    return "'" + this.path + "'";
  }

  /**
   * Load a new {@link CertificateFile} from the given PEM file.
   * @param path the path of the PEM file
   * @return a new {@link CertificateFile} instance
   * @throws IOException on IO error
   */
  static CertificateFile loadFromPemFile(Path path) throws IOException {
    try {
      List<X509Certificate> certificates = PemContent.load(path).getCertificates();
      return new CertificateFile(path, certificates);
    }
    catch (IllegalStateException ex) {
      throw new IllegalStateException("Cannot load certificates from PEM file '%s'".formatted(path));
    }
  }

}

