package org.example.enums;

import lombok.Getter;

@Getter
public enum DirectionType {
  UP("up"),
  DOWN("down"),
  LEFT("left"),
  RIGHT("right"),
  ANY("any");

  private final String value;

  DirectionType(String value) {
    this.value = value;
  }

  public static DirectionType fromString(String text) {
    for (DirectionType type : DirectionType.values()) {
      if (type.value.equalsIgnoreCase(text)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown direction: " + text);
  }
}
