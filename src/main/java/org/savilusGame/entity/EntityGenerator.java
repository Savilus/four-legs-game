package org.savilusGame.entity;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.items.Boots;
import org.savilusGame.entity.items.BronzeCoin;
import org.savilusGame.entity.items.Chest;
import org.savilusGame.entity.items.Door;
import org.savilusGame.entity.items.Heart;
import org.savilusGame.entity.items.IronDoor;
import org.savilusGame.entity.items.Key;
import org.savilusGame.entity.items.Lantern;
import org.savilusGame.entity.items.ManaCrystal;
import org.savilusGame.entity.items.RedPotion;
import org.savilusGame.entity.items.Tent;
import org.savilusGame.entity.projectile.Fireball;
import org.savilusGame.entity.projectile.Rock;
import org.savilusGame.entity.shield.BlueShield;
import org.savilusGame.entity.shield.WoodShield;
import org.savilusGame.entity.weapon.Axe;
import org.savilusGame.entity.weapon.NormalSword;
import org.savilusGame.entity.weapon.PickAxe;
import org.savilusGame.enums.GameObjectType;

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
      case IRON_DOOR -> gameObject = new IronDoor(gamePanel);
      case FIREBALL -> gameObject = new Fireball(gamePanel);
      case ROCK -> gameObject = new Rock(gamePanel);
      case PICKAXE -> gameObject = new PickAxe(gamePanel);
    }
    return gameObject;
  }
}
