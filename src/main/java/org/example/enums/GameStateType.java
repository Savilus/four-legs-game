package org.example.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameStateType {
  TITLE_STATE("titleState"),
  PLAY_STATE("playState"),
  PAUSE_STATE("pauseState"),
  DIALOG_STATE("dialogState"),
  CHARACTER_STATE("characterState"),
  OPTIONS_STATE("optionsState");

  private final String name;

  public static GameStateType fromString(String text) {
    for (GameStateType type : GameStateType.values()) {
      if (type.name.equalsIgnoreCase(text)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown game object: " + text);
  }
}
