package org.example.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameObjectType {
  KEY("Key"),
  DOOR("Door"),
  BOOTS("Boots"),
  HEART("Heart"),
  CHEST("Chest");

  private final String name;

  public static GameObjectType fromString(String text) {
    for (GameObjectType type : GameObjectType.values()) {
      if (type.name.equalsIgnoreCase(text)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown game object: " + text);
  }
}

