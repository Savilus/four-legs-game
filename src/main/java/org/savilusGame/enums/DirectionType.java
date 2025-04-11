package org.savilusGame.enums;

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
