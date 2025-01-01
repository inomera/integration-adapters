package com.inomera.middleware.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.inomera.integration.model.AdapterResponse;
import com.inomera.integration.model.AdapterStatus;
import jakarta.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.junit.jupiter.api.Test;

class AdapterUtilsTest {

  @Test
  void shouldGetNormalizedUrl() {
    String normalizedUrl = AdapterUtils.getNormalizedUrl("https://adapter.turkcell.com.tr",
        "/user/notifications");

    assertEquals("https://adapter.turkcell.com.tr/user/notifications", normalizedUrl);
  }

  @Test
  void shouldGetEncodedUserPass() {
    String encodedUserPass = AdapterUtils.getEncodedUserPass("adapter", "pass");
    assertEquals("YWRhcHRlcjpwYXNz", encodedUserPass);
  }

  @Test
  void shouldNullCheck() {
    JAXBElement<String> getUsers = new JAXBElement<>(new QName("getUsers"), String.class,
        "adapter");
    String output = AdapterUtils.nullCheck(getUsers);
    assertEquals("adapter", output);
  }

  @Test
  void shouldReturnNullWhenNullCheckValIsNull() {
    String output = AdapterUtils.nullCheck(null);
    assertNull(output);
  }

  @Test
  void shouldReturnTrueWhenCallSuccessAndHasData() {
    AdapterResponse<String> success = new AdapterResponse<>(
        AdapterStatus.createSuccess(), "success");
    assertTrue(AdapterUtils.isSuccessAndHasData(success));
  }

  @Test
  void shouldReturnFalseWhenCallSuccessAndHasDataIfDataIsNull() {
    AdapterResponse<String> success = new AdapterResponse<>(
        AdapterStatus.createSuccess(), null);
    assertFalse(AdapterUtils.isSuccessAndHasData(success));
  }

  @Test
  void shouldReturnFalseWhenCallSuccessAndHasDataIfResultFail() {
    AdapterResponse<String> fail = new AdapterResponse<>(
        AdapterStatus.createStatusFailedAsTechnical(), null);
    assertFalse(AdapterUtils.isSuccessAndHasData(fail));
  }

  @Test
  void isSuccessAndDoesNotHaveData() {
    AdapterResponse<String> success = new AdapterResponse<>(
        AdapterStatus.createSuccess(), null);
    assertTrue(AdapterUtils.isSuccessAndDoesNotHaveData(success));
  }

  @Test
  void isFailOrDoesNotHaveData() {
    AdapterResponse<String> fail = new AdapterResponse<>(
        AdapterStatus.createStatusFailedAsBusiness(), null);
    assertTrue(AdapterUtils.isFailOrDoesNotHaveData(fail));
  }

  @Test
  void shouldReturnTrueWhenCallSFailOrDoesNotHaveDataIfDataIsNullAndResultSuccess() {
    AdapterResponse<String> fail = new AdapterResponse<>(
        AdapterStatus.createSuccess(), null);
    assertTrue(AdapterUtils.isFailOrDoesNotHaveData(fail));
  }

  @Test
  void isFail() {
    AdapterResponse<String> fail = new AdapterResponse<>(
        AdapterStatus.createStatusFailedAsBusiness(), null);
    assertTrue(AdapterUtils.isFail(fail));
  }

  @Test
  void isSuccess() {
    AdapterResponse<String> success = new AdapterResponse<>(
        AdapterStatus.createSuccess());
    assertTrue(AdapterUtils.isSuccess(success));
  }
}