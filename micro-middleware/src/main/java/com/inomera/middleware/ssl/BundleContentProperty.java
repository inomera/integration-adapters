package com.inomera.middleware.ssl;

import com.inomera.middleware.ssl.pem.PemContent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.util.Assert;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

/**
 * Helper utility to manage a single bundle content configuration property. May possibly contain PEM
 * content, a location or a directory search pattern.
 *
 * @param name  the configuration property name (excluding any prefix)
 * @param value the configuration property value
 */
record BundleContentProperty(String name, String value) {

  /**
   * Return a {@link DirectoryStream} of paths that match the property value when when treated as a
   * glob pattern.
   *
   * @return the matching paths
   * @throws IOException           on IO error
   * @throws IllegalStateException if the value is not a directory glob pattern
   */
  DirectoryStream<Path> getDirectoryGlobMatches() throws IOException {
    Path path = toPath();
    Assert.state(isDirectoryGlob(path),
        () -> "Property '%s' must contain a directory glob pattern".formatted(name()));
    return Files.newDirectoryStream(path.getParent(), path.getFileName().toString());
  }

  /**
   * Assert that the property value is not a directory glob pattern.
   */
  void assertIsNotDirectoryGlob() {
    Assert.state(!isDirectoryGlob(),
        () -> "Property '%s' cannot contain a directory glob pattern".formatted(name()));
  }

  /**
   * Return if the property value is a directory glob pattern.
   *
   * @return if the value is a directory glob pattern.
   */
  boolean isDirectoryGlob() {
    if (hasValue() && !isPemContent()) {
      try {
        return isDirectoryGlob(toPath());
      } catch (Exception ex) {
        return false;
      }
    }
    return false;
  }

  private boolean isDirectoryGlob(Path path) {
    return path.getFileName().toString().contains("*");
  }

  /**
   * Return if the property value is PEM content.
   *
   * @return if the value is PEM content
   */
  boolean isPemContent() {
    return PemContent.isPresentInText(this.value);
  }

  /**
   * Return if there is any property value present.
   *
   * @return if the value is present
   */
  boolean hasValue() {
    return StringUtils.hasText(this.value);
  }

  private URL toUrl() throws FileNotFoundException {
    Assert.state(!isPemContent(), "Value contains PEM content");
    return ResourceUtils.getURL(this.value);
  }

  private Path toPath() {
    try {
      URL url = toUrl();
      Assert.state(isFileUrl(url), () -> "Value '%s' is not a file URL".formatted(url));
      return Path.of(url.toURI()).toAbsolutePath();
    } catch (Exception ex) {
      throw new IllegalStateException(
          "Unable to convert '%s' property to a path".formatted(this.name), ex);
    }
  }

  private boolean isFileUrl(URL url) {
    return "file".equalsIgnoreCase(url.getProtocol());
  }
}

