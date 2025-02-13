package org.example.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IdEnumManager {
  private static final AtomicInteger counter = new AtomicInteger(0);
  private static final Map<String, Integer> enumIdMap = new HashMap<>();

  public static int getIdForEnum(Enum<?> e) {
    return enumIdMap.computeIfAbsent(e.name(), k -> counter.incrementAndGet());
  }
}
