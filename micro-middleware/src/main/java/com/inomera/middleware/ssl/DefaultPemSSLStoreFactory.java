package com.inomera.middleware.ssl;

import com.inomera.integration.config.model.PemSSLBundleProperties.Store;
import com.inomera.middleware.ssl.pem.PemContent;
import com.inomera.middleware.ssl.pem.PemSSLStore;
import com.inomera.middleware.ssl.pem.PemSSLStoreDetails;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.util.Assert;
import org.springframework.util.function.ThrowingFunction;

/**
 * Default {@link PemSSLStoreFactory} used to create {@link PemSSLStore} instances from
 * {@link Store store properties} taking into consideration*
 */
public class DefaultPemSSLStoreFactory implements PemSSLStoreFactory {

  /**
   * Create a new {@link PemSSLStore} instance based on the given properties.
   *
   * @param bundleName        the bundle name being created
   * @param storePropertyName the property name prefix
   * @param storeProperties   the properties to use
   * @return a new {@link PemSSLStore} instance
   */
  @Override
  public PemSSLStore getPemSSLStore(String bundleName, String storePropertyName,
      Store storeProperties) {
    try {
      return getPemSslStore(bundleName, storeProperties,
          new BundleContentProperty(storePropertyName + ".certificate",
              storeProperties.getCertificate()),
          new BundleContentProperty(storePropertyName + ".private-key",
              storeProperties.getPrivateKey()));
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  private PemSSLStore getPemSslStore(String bundleName, Store properties,
      BundleContentProperty certificateProperty, BundleContentProperty privateKeyProperty)
      throws IOException {
    if (!certificateProperty.isDirectoryGlob()) {
      PemSSLStoreDetails details = new PemSSLStoreDetails(properties.getType(),
          properties.getCertificate(),
          properties.getPrivateKey(), properties.getPrivateKeyPassword());
      return PemSSLStore.load(details);
    }
    privateKeyProperty.assertIsNotDirectoryGlob();
    return PemSSLStore.of(properties.getType(), getCertificates(certificateProperty),
        loadPrivateKey(properties));
  }

  private List<X509Certificate> getCertificates(BundleContentProperty certificateProperty)
      throws IOException {
    List<CertificateFile> certificateFiles = getFiles(certificateProperty,
        CertificateFile::loadFromPemFile);
    return mergeCertificates(certificateFiles);
  }

  private <F> List<F> getFiles(BundleContentProperty bundleContentProperty,
      ThrowingFunction<Path, F> fileFactory)
      throws IOException {
    try (DirectoryStream<Path> paths = bundleContentProperty.getDirectoryGlobMatches()) {
      return StreamSupport.stream(paths.spliterator(), false)
          .sorted()
          .filter(Files::isRegularFile)
          .map(fileFactory)
          .toList();
    }
  }

  public static List<X509Certificate> mergeCertificates(List<CertificateFile> certificateFiles) {
    return certificateFiles.stream()
        .flatMap(certFile -> certFile.certificates().stream())
        .collect(Collectors.toList());
  }

  private static PrivateKey loadPrivateKey(Store properties) throws IOException {
    PemContent pemContent = PemContent.load(properties.getPrivateKey());
    return (pemContent != null) ? pemContent.getPrivateKey(properties.getPrivateKeyPassword())
        : null;
  }
}
