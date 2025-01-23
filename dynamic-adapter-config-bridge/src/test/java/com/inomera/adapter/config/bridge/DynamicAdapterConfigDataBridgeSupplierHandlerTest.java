package com.inomera.adapter.config.bridge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inomera.adapter.config.bridge.exception.AdapterConfigException;
import com.inomera.integration.config.model.AdapterConfig;
import com.inomera.integration.config.model.AdapterProperties;
import com.inomera.telco.commons.config.ConfigurationHolder;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DynamicAdapterConfigDataBridgeSupplierHandlerTest {

  @InjectMocks
  private DynamicAdapterConfigDataBridgeSupplierHandler handler;

  @Mock
  private ConfigurationHolder configurationHolder;

  private AutoCloseable autoCloseable;
  @BeforeEach
  void setUp() {
    autoCloseable = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void destroy() throws Exception {
    autoCloseable.close();
  }


  @Test
  void testGetConfigV1_Success() {
    String key = "testKey";
    AdapterProperties mockAdapterProperties = new AdapterProperties();
    mockAdapterProperties.setAuth(null);

    Map<String, Object> mockAdapterPropertiesMap = Map.of("auth", Map.of("type", "NONE"));

    when(configurationHolder.getJsonObjectProperty(eq(key), eq(Map.class))).thenReturn(mockAdapterPropertiesMap);
    when(configurationHolder.getJsonObjectProperty(eq(key), eq(AdapterProperties.class))).thenReturn(mockAdapterProperties);

    AdapterConfig result = handler.getConfigV1(key);

    assertNotNull(result);
    assertEquals(key, result.getKey());
    assertNotNull(result.getAdapterProperties());
    verify(configurationHolder, times(1)).getJsonObjectProperty(eq(key), eq(Map.class));
  }

  @Test
  void testGetConfigV1_KeyNotFound_ThrowsException() {
    String key = "testKey";

    when(configurationHolder.getJsonObjectProperty(eq(key), eq(Map.class))).thenReturn(null);

    AdapterConfigException exception = assertThrows(AdapterConfigException.class, () -> handler.getConfigV1(key));
    assertEquals("Adapter config for key 'testKey' is not found", exception.getCause().getMessage());

    verify(configurationHolder, times(1)).getJsonObjectProperty(eq(key), eq(Map.class));
  }

  @Test
  void testGetConfig_StringProperty_Success() {
    String key = "stringKey";
    String expectedValue = "testValue";

    when(configurationHolder.getStringProperty(eq(key))).thenReturn(expectedValue);

    String result = handler.getConfig(key, String.class);

    assertNotNull(result);
    assertEquals(expectedValue, result);
    verify(configurationHolder, times(1)).getStringProperty(eq(key));
  }

  @Test
  void testGetConfig_JsonObject_Success() {
    String key = "jsonKey";
    AdapterProperties expectedObject = new AdapterProperties();

    when(configurationHolder.getJsonObjectProperty(eq(key), eq(AdapterProperties.class))).thenReturn(expectedObject);

    AdapterProperties result = handler.getConfig(key, AdapterProperties.class);

    assertNotNull(result);
    assertEquals(expectedObject, result);
    verify(configurationHolder, times(1)).getJsonObjectProperty(eq(key), eq(AdapterProperties.class));
  }

  @Test
  void testGetConfigs_Success() {
    String key = "listKey";
    AdapterProperties mockObject1 = new AdapterProperties();
    AdapterProperties mockObject2 = new AdapterProperties();

    when(configurationHolder.getJsonListProperty(eq(key), eq(AdapterProperties.class)))
        .thenReturn(List.of(mockObject1, mockObject2));

    List<AdapterProperties> result = handler.getConfigs(key, AdapterProperties.class);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(configurationHolder, times(1)).getJsonListProperty(eq(key), eq(AdapterProperties.class));
  }

  @Test
  void testGetConfigV1_AuthExtraction_ThrowsException() {
    String key = "testKey";
    Map<String, Object> mockAdapterPropertiesMap = Map.of();

    when(configurationHolder.getJsonObjectProperty(eq(key), eq(Map.class))).thenReturn(mockAdapterPropertiesMap);

    AdapterConfigException exception = assertThrows(AdapterConfigException.class, () -> handler.getConfigV1(key));
    assertEquals("'auth' section is missing in adapter properties", exception.getCause().getMessage());

    verify(configurationHolder, times(1)).getJsonObjectProperty(eq(key), eq(Map.class));
  }

}
