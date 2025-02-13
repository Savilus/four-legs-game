package org.example.enums;

import static org.example.utils.IdEnumManager.getIdForEnum;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
public enum GameObjectType {
  KEY("Key"),
  DOOR("Door"),
  BOOTS("Boots"),
  HEART("Heart"),
  CHEST("Chest");

  private final String name;
  private final int objectTypeId;

  GameObjectType(String name) {
    this.name = name;
    this.objectTypeId = getIdForEnum(this);
  }

  public static GameObjectType fromString(String text) {
    for (GameObjectType type : GameObjectType.values()) {
      if (type.name.equalsIgnoreCase(text)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown game object: " + text);
  }
}

