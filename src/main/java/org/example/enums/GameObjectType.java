package org.example.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameObjectType {
  // TODO: some classes does not use GameObjectType and some does, fix it or think of something else. You can also add some parameters to this class
  KEY("Key"),
  DOOR("Door"),
  BOOTS("Boots"),
  HEART("Heart"),
  CHEST("Chest"),
  LANTERN("Lantern"),
  TENT("Tent"),
  RED_POTION("Red Potion"),
  BRONZE_COIN("Bronze Coin"),
  MANA_CRYSTAL("Mana Crystal"),

  // WEAPONS
  NORMAL_SWORD("Normal Sword"),
  AXE("Woodcutter's Axe"),

  // SHIELDS
  BLUE_SHIELD("Blue Shield"),
  WOOD_SHIELD("Wood Shield");


  private final String name;

  public static GameObjectType getObjectFromName(String name) {
    for (GameObjectType type : GameObjectType.values()) {
      if (type.name.equalsIgnoreCase(name)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown game object: " + name);
  }
}

