package org.savilusGame.utils;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.DUNGEON_FIRST_FLOR;
import static org.savilusGame.config.GameEntityNameFactory.DUNGEON_SECOND_FLOR;
import static org.savilusGame.config.GameEntityNameFactory.INTERIOR_MAP;
import static org.savilusGame.config.GameEntityNameFactory.MAIN_MAP;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.interactiveObjects.BigRock;
import org.savilusGame.entity.interactiveTile.DestructibleWall;
import org.savilusGame.entity.interactiveTile.InteractiveDryTree;
import org.savilusGame.entity.interactiveTile.InteractiveTile;
import org.savilusGame.entity.interactiveTile.MetalPlate;
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
import org.savilusGame.entity.monster.Bat;
import org.savilusGame.entity.monster.GreenSlime;
import org.savilusGame.entity.monster.Orc;
import org.savilusGame.entity.monster.SkeletonLord;
import org.savilusGame.entity.npc.MerchantNPC;
import org.savilusGame.entity.npc.OldManNPC;
import org.savilusGame.entity.shield.BlueShield;
import org.savilusGame.entity.weapon.Axe;
import org.savilusGame.entity.weapon.PickAxe;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssetSetter {

  static int MAIN_MAP_MONSTER_NUMBER = 20;
  static int SECOND_FLOR_NPC_NUMBER = 1;

  GamePanel gamePanel;

  public void setObject() {
    gamePanel.setMapsObjects(Map.of(
        MAIN_MAP, new GameEntity[]{
            new BronzeCoin(gamePanel).setWorldPosition(TILE_SIZE * 25, TILE_SIZE * 23),
            new BronzeCoin(gamePanel).setWorldPosition(TILE_SIZE * 21, TILE_SIZE * 19),
            new Key(gamePanel).setWorldPosition(TILE_SIZE * 26, TILE_SIZE * 21),
            new Axe(gamePanel).setWorldPosition(TILE_SIZE * 33, TILE_SIZE * 21),
            new BlueShield(gamePanel).setWorldPosition(TILE_SIZE * 35, TILE_SIZE * 21),
            new RedPotion(gamePanel).setWorldPosition(TILE_SIZE * 22, TILE_SIZE * 27),
            new Heart(gamePanel).setWorldPosition(TILE_SIZE * 22, TILE_SIZE * 29),
            new ManaCrystal(gamePanel).setWorldPosition(TILE_SIZE * 22, TILE_SIZE * 31),
            new Door(gamePanel).setWorldPosition(TILE_SIZE * 14, TILE_SIZE * 28),
            new Door(gamePanel).setWorldPosition(TILE_SIZE * 12, TILE_SIZE * 12),
            new Chest(gamePanel).setLoot(new Key(gamePanel)).setWorldPosition(TILE_SIZE * 33, TILE_SIZE * 7),
            new RedPotion(gamePanel).setWorldPosition(TILE_SIZE * 21, TILE_SIZE * 20),
            new RedPotion(gamePanel).setWorldPosition(TILE_SIZE * 20, TILE_SIZE * 20),
            new RedPotion(gamePanel).setWorldPosition(TILE_SIZE * 17, TILE_SIZE * 21),
            new Lantern(gamePanel).setWorldPosition(TILE_SIZE * 18, TILE_SIZE * 20),
            new Tent(gamePanel).setWorldPosition(TILE_SIZE * 19, TILE_SIZE * 20),
            new Chest(gamePanel).setLoot(new Tent(gamePanel)).setWorldPosition(TILE_SIZE * 17, TILE_SIZE * 20)
        },
        DUNGEON_FIRST_FLOR, new GameEntity[]{
            new Chest(gamePanel).setLoot(new PickAxe(gamePanel)).setWorldPosition(TILE_SIZE * 40, TILE_SIZE * 41),
            new Chest(gamePanel).setLoot(new RedPotion(gamePanel)).setWorldPosition(TILE_SIZE * 13, TILE_SIZE * 16),
            new Chest(gamePanel).setLoot(new RedPotion(gamePanel)).setWorldPosition(TILE_SIZE * 26, TILE_SIZE * 34),
            new Chest(gamePanel).setLoot(new RedPotion(gamePanel)).setWorldPosition(TILE_SIZE * 27, TILE_SIZE * 15),
            new IronDoor(gamePanel).setWorldPosition(TILE_SIZE * 18, TILE_SIZE * 23)
        },
        DUNGEON_SECOND_FLOR, new GameEntity[]{
            new IronDoor(gamePanel).setWorldPosition(TILE_SIZE * 25, TILE_SIZE * 15),
            null
        }));
  }

  public void setProjectile() {
    gamePanel.setProjectiles(Map.of(
        MAIN_MAP, new GameEntity[MAIN_MAP_MONSTER_NUMBER + 1],
        INTERIOR_MAP, new GameEntity[1],
        DUNGEON_FIRST_FLOR, new GameEntity[1],
        DUNGEON_SECOND_FLOR, new GameEntity[1]
    ));
  }

  public void setNPC() {
    gamePanel.setMapsNpc(new HashMap<>());
    gamePanel.getMapsNpc().put(MAIN_MAP, new GameEntity[]{
        new OldManNPC(gamePanel).setWorldPosition(TILE_SIZE * 21, TILE_SIZE * 21)
    });
    gamePanel.getMapsNpc().put(INTERIOR_MAP, new GameEntity[]{
        new MerchantNPC(gamePanel).setWorldPosition(TILE_SIZE * 12, TILE_SIZE * 7)
    });
    gamePanel.getMapsNpc().put(DUNGEON_SECOND_FLOR, new GameEntity[SECOND_FLOR_NPC_NUMBER]);
  }


  public void setInteractiveObjects() {
    if (Objects.isNull(gamePanel.getMapsNpc())) {
      gamePanel.setMapsNpc(new HashMap<>());
    }
    gamePanel.getMapsNpc().put(DUNGEON_FIRST_FLOR, new GameEntity[]{
        new BigRock(gamePanel).setWorldPosition(TILE_SIZE * 20, TILE_SIZE * 25),
        new BigRock(gamePanel).setWorldPosition(TILE_SIZE * 11, TILE_SIZE * 18),
        new BigRock(gamePanel).setWorldPosition(TILE_SIZE * 23, TILE_SIZE * 14)
    });
  }

  public void setMonster() {
    gamePanel.setMapsMonsters(Map.of(
        MAIN_MAP, new GameEntity[]{
            new GreenSlime(gamePanel).setWorldPosition(TILE_SIZE * 21, TILE_SIZE * 38),
            new GreenSlime(gamePanel).setWorldPosition(TILE_SIZE * 23, TILE_SIZE * 42),
            new GreenSlime(gamePanel).setWorldPosition(TILE_SIZE * 24, TILE_SIZE * 37),
            new GreenSlime(gamePanel).setWorldPosition(TILE_SIZE * 34, TILE_SIZE * 42),
            new GreenSlime(gamePanel).setWorldPosition(TILE_SIZE * 38, TILE_SIZE * 42),
            new Orc(gamePanel).setWorldPosition(TILE_SIZE * 12, TILE_SIZE * 33)
        },
        DUNGEON_FIRST_FLOR, new GameEntity[]{
            new Bat(gamePanel).setWorldPosition(TILE_SIZE * 34, TILE_SIZE * 39),
            new Bat(gamePanel).setWorldPosition(TILE_SIZE * 36, TILE_SIZE * 25),
            new Bat(gamePanel).setWorldPosition(TILE_SIZE * 39, TILE_SIZE * 26),
            new Bat(gamePanel).setWorldPosition(TILE_SIZE * 28, TILE_SIZE * 11),
            new Bat(gamePanel).setWorldPosition(TILE_SIZE * 10, TILE_SIZE * 19)
        },
        DUNGEON_SECOND_FLOR, new GameEntity[]{
            new SkeletonLord(gamePanel).setWorldPosition(TILE_SIZE * 23, TILE_SIZE * 16)
        }
    ));
  }

  public void setInteractiveTiles() {
    gamePanel.setMapsInteractiveTiles(Map.of(
        MAIN_MAP, new InteractiveTile[]{
            new InteractiveDryTree(gamePanel, 27, 12),
            new InteractiveDryTree(gamePanel, 28, 12),
            new InteractiveDryTree(gamePanel, 29, 12),
            new InteractiveDryTree(gamePanel, 30, 12),
            new InteractiveDryTree(gamePanel, 31, 12),
            new InteractiveDryTree(gamePanel, 32, 12),
            new InteractiveDryTree(gamePanel, 33, 12),
            new InteractiveDryTree(gamePanel, 18, 40),
            new InteractiveDryTree(gamePanel, 17, 40),
            new InteractiveDryTree(gamePanel, 16, 40)
        },
        DUNGEON_FIRST_FLOR, new InteractiveTile[]{
            new DestructibleWall(gamePanel, 18, 30),
            new DestructibleWall(gamePanel, 17, 31),
            new DestructibleWall(gamePanel, 17, 32),
            new DestructibleWall(gamePanel, 17, 34),
            new DestructibleWall(gamePanel, 18, 34),
            new DestructibleWall(gamePanel, 18, 33),
            new DestructibleWall(gamePanel, 10, 22),
            new DestructibleWall(gamePanel, 10, 24),
            new DestructibleWall(gamePanel, 38, 18),
            new DestructibleWall(gamePanel, 38, 19),
            new DestructibleWall(gamePanel, 38, 20),
            new DestructibleWall(gamePanel, 38, 21),
            new DestructibleWall(gamePanel, 18, 13),
            new DestructibleWall(gamePanel, 18, 14),
            new DestructibleWall(gamePanel, 22, 28),
            new DestructibleWall(gamePanel, 30, 28),
            new DestructibleWall(gamePanel, 32, 28),
            new MetalPlate(gamePanel, 20, 22),
            new MetalPlate(gamePanel, 8, 17),
            new MetalPlate(gamePanel, 39, 31)
        }
    ));
  }
}
