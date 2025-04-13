package org.savilusGame.enums;


import org.apache.commons.lang3.StringUtils;
import org.savilusGame.utils.text.TextManager;

import lombok.Getter;

@Getter
public enum GameObject {
  // GAME ITEMS
  KEY("key"),
  DOOR("door"),
  IRON_DOOR("ironDoor"),
  BOOTS("boots"),
  HEART("heart"),
  CHEST("chest"),
  LANTERN("lantern"),
  TENT("tent"),
  RED_POTION("redPotion"),
  BRONZE_COIN("bronzeCoin"),
  MANA_CRYSTAL("manaCrystal"),

  // WEAPONS
  NORMAL_SWORD("normalSword"),
  AXE("axe"),
  PICKAXE("pickAxe"),

  // SHIELDS
  BLUE_SHIELD("blueShield"),
  WOOD_SHIELD("woodShield"),

  // Projectiles
  FIREBALL("fireball"),
  ROCK("rock"),

  // INTERACTIVE
  METAL_PLATE("metalPlate"),
  BIG_ROCK("bigRock");

  private final String name;
  private final String textKey;

  GameObject(String textKey) {
    this.textKey = textKey;
    name = TextManager.getItemName(textKey);
  }

  public static GameObject getObjectFromName(String name) {
    for (GameObject type : GameObject.values()) {
      if (StringUtils.equalsIgnoreCase(type.name, name)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown game object: " + name);
  }
}

