package org.example.entity;

import org.example.GamePanel;
import org.example.entity.items.Boots;
import org.example.entity.items.BronzeCoin;
import org.example.entity.items.Chest;
import org.example.entity.items.Door;
import org.example.entity.items.Heart;
import org.example.entity.items.Key;
import org.example.entity.items.Lantern;
import org.example.entity.items.ManaCrystal;
import org.example.entity.items.RedPotion;
import org.example.entity.items.Tent;
import org.example.entity.projectile.Fireball;
import org.example.entity.projectile.Rock;
import org.example.entity.shield.BlueShield;
import org.example.entity.shield.WoodShield;
import org.example.entity.weapon.Axe;
import org.example.entity.weapon.NormalSword;
import org.example.enums.GameObjectType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EntityGenerator {

  GamePanel gamePanel;

  public GameEntity getGameEntity(String itemName) {
    GameEntity gameObject = null;
    // TODO: change for more flexible solution
    switch (GameObjectType.getObjectFromName(itemName)) {
      case AXE -> gameObject = new Axe(gamePanel);
      case NORMAL_SWORD -> gameObject = new NormalSword(gamePanel);
      case BLUE_SHIELD -> gameObject = new BlueShield(gamePanel);
      case WOOD_SHIELD -> gameObject = new WoodShield(gamePanel);
      case BOOTS -> gameObject = new Boots(gamePanel);
      case KEY -> gameObject = new Key(gamePanel);
      case LANTERN -> gameObject = new Lantern(gamePanel);
      case TENT -> gameObject = new Tent(gamePanel);
      case RED_POTION -> gameObject = new RedPotion(gamePanel);
      case CHEST -> gameObject = new Chest(gamePanel);
      case MANA_CRYSTAL -> gameObject = new ManaCrystal(gamePanel);
      case BRONZE_COIN -> gameObject = new BronzeCoin(gamePanel);
      case HEART -> gameObject = new Heart(gamePanel);
      case DOOR -> gameObject = new Door(gamePanel);
      case FIREBALL -> gameObject = new Fireball(gamePanel);
      case ROCK -> gameObject = new Rock(gamePanel);
    }
    return gameObject;
  }
}
