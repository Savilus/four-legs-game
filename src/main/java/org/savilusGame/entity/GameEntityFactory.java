package org.savilusGame.entity;

import static org.savilusGame.enums.GameObjectType.AXE;
import static org.savilusGame.enums.GameObjectType.BLUE_SHIELD;
import static org.savilusGame.enums.GameObjectType.BOOTS;
import static org.savilusGame.enums.GameObjectType.BRONZE_COIN;
import static org.savilusGame.enums.GameObjectType.CHEST;
import static org.savilusGame.enums.GameObjectType.DOOR;
import static org.savilusGame.enums.GameObjectType.FIREBALL;
import static org.savilusGame.enums.GameObjectType.HEART;
import static org.savilusGame.enums.GameObjectType.IRON_DOOR;
import static org.savilusGame.enums.GameObjectType.KEY;
import static org.savilusGame.enums.GameObjectType.LANTERN;
import static org.savilusGame.enums.GameObjectType.MANA_CRYSTAL;
import static org.savilusGame.enums.GameObjectType.NORMAL_SWORD;
import static org.savilusGame.enums.GameObjectType.PICKAXE;
import static org.savilusGame.enums.GameObjectType.RED_POTION;
import static org.savilusGame.enums.GameObjectType.ROCK;
import static org.savilusGame.enums.GameObjectType.TENT;
import static org.savilusGame.enums.GameObjectType.WOOD_SHIELD;

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
import org.savilusGame.enums.GameObjectType;

import lombok.AllArgsConstructor;

public class GameEntityFactory {

  private final GamePanel gamePanel;
  private final Map<GameObjectType, Supplier<GameEntity>> entityMap = new EnumMap<>(GameObjectType.class);

  public GameEntityFactory(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    initMap();
  }

  public GameEntity getGameEntity(String itemName) {
    GameObjectType type = GameObjectType.getObjectFromName(itemName);
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

