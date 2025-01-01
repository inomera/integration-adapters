package com.inomera.middleware.util;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import com.inomera.integration.fault.AdapterException;
import com.inomera.integration.model.AdapterStatus;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class XmlStringUtils {

  private static final char MASK_CHAR = '*';

  /*
   * Example input/output for the regular expression used in this method:
   */
  public static String maskXmlTagContent(String content, String tag) {
    final String regex = "(<([^>]*:)?" + tag + ".*?>)(.*?)(</([^>]*:)?" + tag + ">)";
    final Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
    final Matcher matcher = pattern.matcher(content);

    while (matcher.find()) {
      final var fullMatch = matcher.group();
      final var maskValue = matcher.group(3);
      final var maskedValue = StringUtils.repeat(MASK_CHAR, StringUtils.length(maskValue));
      final var replacement = matcher.group(1) + maskedValue + matcher.group(4);
      content = StringUtils.replace(content, fullMatch, replacement);
    }

    return content;
  }

  public static Element parseDocument(String content) throws AdapterException {
    final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, EMPTY);
    documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, EMPTY);

    try {
      DocumentBuilder db = documentBuilderFactory.newDocumentBuilder();
      InputSource is = new InputSource();
      try (StringReader characterStream = new StringReader(content)) {
        is.setCharacterStream(characterStream);
        Document doc = db.parse(is);
        return doc.getDocumentElement();
      }
    } catch (SAXException | IOException | ParserConfigurationException e) {
      LOG.error("Error occurred when parse document. {}", e.getMessage(), e);
      throw new AdapterException(AdapterStatus.createStatusFailedAsTechnical());
    }
  }
}