package org.example.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum DirectionType {
  UP("up"),
  DOWN("down"),
  LEFT("left"),
  RIGHT("right"),
  ANY("any");

  private final String value;

  public static DirectionType fromString(String text) {
    for (DirectionType type : DirectionType.values()) {
      if (type.value.equalsIgnoreCase(text)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown direction: " + text);
  }

  public DirectionType getOpposite() {
    return switch (this) {
      case UP -> DOWN;
      case DOWN -> UP;
      case LEFT -> RIGHT;
      case RIGHT -> LEFT;
      case ANY -> ANY;
    };
  }
}
