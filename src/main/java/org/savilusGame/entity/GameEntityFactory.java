package org.savilusGame.entity;

import static org.savilusGame.enums.GameObject.AXE;
import static org.savilusGame.enums.GameObject.BLUE_SHIELD;
import static org.savilusGame.enums.GameObject.BOOTS;
import static org.savilusGame.enums.GameObject.BRONZE_COIN;
import static org.savilusGame.enums.GameObject.CHEST;
import static org.savilusGame.enums.GameObject.DOOR;
import static org.savilusGame.enums.GameObject.FIREBALL;
import static org.savilusGame.enums.GameObject.HEART;
import static org.savilusGame.enums.GameObject.IRON_DOOR;
import static org.savilusGame.enums.GameObject.KEY;
import static org.savilusGame.enums.GameObject.LANTERN;
import static org.savilusGame.enums.GameObject.MANA_CRYSTAL;
import static org.savilusGame.enums.GameObject.NORMAL_SWORD;
import static org.savilusGame.enums.GameObject.PICKAXE;
import static org.savilusGame.enums.GameObject.RED_POTION;
import static org.savilusGame.enums.GameObject.ROCK;
import static org.savilusGame.enums.GameObject.TENT;
import static org.savilusGame.enums.GameObject.WOOD_SHIELD;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

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
import org.savilusGame.enums.GameObject;

public class GameEntityFactory {

  private final GamePanel gamePanel;
  private final Map<GameObject, Supplier<GameEntity>> entityMap = new EnumMap<>(GameObject.class);

  public GameEntityFactory(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    initMap();
  }

  public GameEntity getGameEntity(String itemName) {
    GameObject type = GameObject.getObjectFromName(itemName);
    Supplier<GameEntity> supplier = entityMap.get(type);
    return supplier != null ? supplier.get() : null;
  }

  private void initMap() {
    entityMap.put(AXE, () -> new Axe(gamePanel));
    entityMap.put(NORMAL_SWORD, () -> new NormalSword(gamePanel));
    entityMap.put(BLUE_SHIELD, () -> new BlueShield(gamePanel));
    entityMap.put(WOOD_SHIELD, () -> new WoodShield(gamePanel));
    entityMap.put(BOOTS, () -> new Boots(gamePanel));
    entityMap.put(KEY, () -> new Key(gamePanel));
    entityMap.put(LANTERN, () -> new Lantern(gamePanel));
    entityMap.put(TENT, () -> new Tent(gamePanel));
    entityMap.put(RED_POTION, () -> new RedPotion(gamePanel));
    entityMap.put(CHEST, () -> new Chest(gamePanel));
    entityMap.put(MANA_CRYSTAL, () -> new ManaCrystal(gamePanel));
    entityMap.put(BRONZE_COIN, () -> new BronzeCoin(gamePanel));
    entityMap.put(HEART, () -> new Heart(gamePanel));
    entityMap.put(DOOR, () -> new Door(gamePanel));
    entityMap.put(IRON_DOOR, () -> new IronDoor(gamePanel));
    entityMap.put(FIREBALL, () -> new Fireball(gamePanel));
    entityMap.put(ROCK, () -> new Rock(gamePanel));
    entityMap.put(PICKAXE, () -> new PickAxe(gamePanel));
  }
}

