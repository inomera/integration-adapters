package com.inomera.middleware.util;

import com.inomera.integration.fault.AdapterException;
import com.inomera.integration.model.AdapterStatus;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AdapterDateUtils {

  public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date) {
    if (Objects.isNull(date)) {
      return null;
    }
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
    } catch (DatatypeConfigurationException e) {
      LOG.error("Date conversion exception occurred, {}", e.getMessage(), e);
      throw new AdapterException(e, AdapterStatus.createStatusFailedAsTechnical());
    }
  }

  public static Date convertToXMLGregorianCalendarToDate(
      XMLGregorianCalendar xmlGregorianCalendar) {
    return xmlGregorianCalendar != null ?
        xmlGregorianCalendar.toGregorianCalendar().getTime() : null;
  }

  public static LocalDateTime convertToXMLGregorianCalendarToLocalDate(
      XMLGregorianCalendar xmlGregorianCalendar) {
    if (Objects.isNull(xmlGregorianCalendar)) {
      return null;
    }

    return xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().toLocalDateTime();
  }


  public static XMLGregorianCalendar convertfromLocalDateTimeToXMLGregorianCalendar(
      LocalDateTime localDateTime) {
    ZoneId zoneId = ZoneId.systemDefault();
    GregorianCalendar gregorianCalendar = GregorianCalendar.from(localDateTime.atZone(zoneId));
    try {
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    } catch (DatatypeConfigurationException e) {
      LOG.error("Date conversion exception occurred, {}", e.getMessage(), e);
      throw new AdapterException(e, AdapterStatus.createStatusFailedAsTechnical());
    }
  }

  public static String toString(Date date, String format) {
    if (date == null) {
      return StringUtils.EMPTY;
    }
    final DateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.format(date);
  }

  public static Date fromString(String str, String format) {
    DateFormat dateFormat = new SimpleDateFormat(format);
    try {
      return dateFormat.parse(str);
    } catch (ParseException e) {
      LOG.error("Date conversion exception occurred, {}", e.getMessage(), e);
    }
    return null;
  }

}
