package org.example.enums;

import static org.example.utils.IdEnumManager.getIdForEnum;

import lombok.Getter;

@Getter
public enum GameStateType {
  TITLE_STATE("titleState"),
  PLAY_STATE("playState"),
  PAUSE_STATE("pauseState"),
  DIALOG_STATE("dialogState"),
  CHARACTER_STATE("characterState");

  private final String name;
  private final int gameStateId;

  GameStateType(String name) {
    this.name = name;
    this.gameStateId = getIdForEnum(this);
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
