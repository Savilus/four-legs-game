package org.example.enums;

import lombok.Getter;

@Getter
public enum GameObjectType {
  KEY("Key"),
  DOOR("Door"),
  BOOTS("Boots"),
  HEART("Heart"),
  CHEST("Chest");

  private final String value;

  GameObjectType(String value) {
    this.value = value;
  }

  public static GameObjectType fromString(String text) {
    for (GameObjectType type : GameObjectType.values()) {
      if (type.value.equalsIgnoreCase(text)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown game object: " + text);
  }
}

