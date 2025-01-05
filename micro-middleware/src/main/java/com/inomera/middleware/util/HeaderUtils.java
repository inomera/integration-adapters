package com.inomera.middleware.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HeaderUtils {

  public static Map<String, List<String>> convertToListMap(Map<String, String> headers) {
    return headers.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> Collections.singletonList(e.getValue())));
  }

  public static Map<String, String> flattenListMap(Map<String, List<String>> headers) {
    return headers.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> Optional.of(e.getValue())
                .filter(a -> !a.isEmpty())
                .stream()
                .flatMap(Collection::stream)
                .findFirst()
                .get()));
  }
}
