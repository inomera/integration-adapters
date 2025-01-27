package com.inomera.middleware.ssl.pem;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StreamUtils;

/**
 * PEM encoded content that can provide {@link X509Certificate certificates} and
 * {@link PrivateKey private keys}.
 */
public class PemContent {

  private static final Pattern PEM_HEADER = Pattern.compile("-+BEGIN\\s+[^-]*-+",
      Pattern.CASE_INSENSITIVE);

  private static final Pattern PEM_FOOTER = Pattern.compile("-+END\\s+[^-]*-+",
      Pattern.CASE_INSENSITIVE);

  private String text;

  private PemContent(String text) {
    this.text = text;
  }

  /**
   * Parse and return all {@link X509Certificate certificates} from the PEM content. Most PEM files
   * either contain a single certificate or a certificate chain.
   *
   * @return the certificates
   * @throws IllegalStateException if no certificates could be loaded
   */
  public List<X509Certificate> getCertificates() {
    return PemCertificateParser.parse(this.text);
  }

  /**
   * Parse and return the {@link PrivateKey private keys} from the PEM content.
   *
   * @return the private keys
   * @throws IllegalStateException if no private key could be loaded
   */
  public PrivateKey getPrivateKey() {
    return getPrivateKey(null);
  }

  /**
   * Parse and return the {@link PrivateKey private keys} from the PEM content or {@code null} if
   * there is no private key.
   *
   * @param password the password to decrypt the private keys or {@code null}
   * @return the private keys
   */
  public PrivateKey getPrivateKey(String password) {
    return PemPrivateKeyParser.parse(this.text, password);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    return Objects.equals(this.text, ((PemContent) obj).text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.text);
  }

  @Override
  public String toString() {
    return this.text;
  }

  /**
   * Load {@link PemContent} from the given content (either the PEM
   * content itself or a reference to the resource to load).
   *
   * @param content the content to load
   * @return a new {@link PemContent} instance
   * @throws IOException on IO error
   */
  public static PemContent load(String content) throws IOException {
    if (content == null) {
      return null;
    }
    if (isPresentInText(content)) {
      return new PemContent(content);
    }
    try {
      return load(ResourceUtils.getURL(content));
    } catch (IOException | UncheckedIOException ex) {
      throw new IOException("Error reading certificate or key from file '%s'".formatted(content),
          ex);
    }
  }

  /**
   * Load {@link PemContent} from the given {@link URL}.
   *
   * @param url the URL to load content from
   * @return the loaded PEM content
   * @throws IOException on IO error
   */
  public static PemContent load(URL url) throws IOException {
    Assert.notNull(url, "Url must not be null");
    try (InputStream in = url.openStream()) {
      return load(in);
    }
  }

  /**
   * Load {@link PemContent} from the given {@link Path}.
   *
   * @param path a path to load the content from
   * @return the loaded PEM content
   * @throws IOException on IO error
   */
  public static PemContent load(Path path) throws IOException {
    Assert.notNull(path, "Path must not be null");
    try (InputStream in = Files.newInputStream(path, StandardOpenOption.READ)) {
      return load(in);
    }
  }

  private static PemContent load(InputStream in) throws IOException {
    return of(StreamUtils.copyToString(in, StandardCharsets.UTF_8));
  }

  /**
   * Return a new {@link PemContent} instance containing the given text.
   *
   * @param text the text containing PEM encoded content
   * @return a new {@link PemContent} instance
   */
  public static PemContent of(String text) {
    return (text != null) ? new PemContent(text) : null;
  }

  /**
   * Return if PEM content is present in the given text.
   *
   * @param text the text to check
   * @return if the text includes PEM encoded content.
   */
  public static boolean isPresentInText(String text) {
    return text != null && PEM_HEADER.matcher(text).find() && PEM_FOOTER.matcher(text).find();
  }
}
