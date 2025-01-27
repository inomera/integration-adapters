package com.inomera.middleware.ssl;

import com.inomera.middleware.ssl.pem.PemContent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PrivateKey;
import org.springframework.util.Assert;

/**
 * {@link PrivateKey} content loaded from a file.
 *
 * @param path       the path of the file that contains the content
 * @param privateKey the parsed private key
 */
public record PrivateKeyFile(Path path, PrivateKey privateKey) {

  public PrivateKeyFile {
    Assert.notNull(path, "Path must not be null");
    Assert.isTrue(Files.isRegularFile(path), "Path '%s' must be a regular file".formatted(path));
    Assert.isTrue(privateKey != null, "PrivateKey must not be null");
  }

  @Override
  public String toString() {
    return "'" + this.path + "'";
  }

  /**
   * Load a new {@link PrivateKeyFile} from the given PEM file.
   *
   * @param path               the path of the PEM file
   * @param privateKeyPassword the private key password or {@code null}
   * @return a new {@link PrivateKeyFile} instance
   * @throws IOException on IO error
   */
  static PrivateKeyFile loadFromPemFile(Path path, String privateKeyPassword) throws IOException {
    try {
      PrivateKey privateKey = PemContent.load(path).getPrivateKey(privateKeyPassword);
      return new PrivateKeyFile(path, privateKey);
    } catch (IllegalStateException ex) {
      throw new IllegalStateException("Cannot load private key from PEM file '%s'".formatted(path));
    }
  }
}
