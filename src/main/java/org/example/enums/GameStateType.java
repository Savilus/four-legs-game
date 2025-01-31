package org.example.enums;

import lombok.Getter;

@Getter
public enum GameStateType {
  TITLE_STATE("titleState", 0),
  PLAY_STATE("playState", 1),
  PAUSE_STATE("pauseState", 2),
  DIALOG_STATE("dialogState", 3);

  private final String name;
  private final int stateId;

  GameStateType(String name, int stateId) {
    this.name = name;
    this.stateId = stateId;
  }

  public static GameStateType fromString(String text) {
    for (GameStateType type : GameStateType.values()) {
      if (type.name.equalsIgnoreCase(text)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown game object: " + text);
  }
}
