package org.example.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameObjectType {
  // TODO: some classes does not use GameObjectType and some does, fix it or think of something else. You can also add some parameters to this class
  // GAME ITEMS
  KEY("Key", "key"),
  DOOR("Door", "door"),
  BOOTS("Boots", "boots"),
  HEART("Heart", "heart"),
  CHEST("Chest", "chest"),
  LANTERN("Lantern", "lantern"),
  TENT("Tent", "tent"),
  RED_POTION("Red Potion", "redPotion"),
  BRONZE_COIN("Bronze Coin", "bronzeCoin"),
  MANA_CRYSTAL("Mana Crystal", "manaCrystal"),

  // WEAPONS
  NORMAL_SWORD("Normal Sword", "normalSword"),
  AXE("Woodcutter's Axe", "axe"),

  // SHIELDS
  BLUE_SHIELD("Blue Shield", "blueShield"),
  WOOD_SHIELD("Wood Shield", "woodShield"),

  // Projectiles
  FIREBALL("Fireball", "fireball"),
  ROCK("Rock", "rock");


  private final String name;
  private final String textKey;

  public static GameObjectType getObjectFromName(String name) {
    for (GameObjectType type : GameObjectType.values()) {
      if (type.name.equalsIgnoreCase(name)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown game object: " + name);
  }
}

