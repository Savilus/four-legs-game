package org.savilusGame.utils;

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

public class AssetSetter {

  private static final int MAIN_MAP_MONSTER_NUMBER = 20;
  private static final int SECOND_FLOR_NPC_NUMBER = 1;

  GamePanel gamePanel;

  public AssetSetter(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void setObject() {
    gamePanel.mapsObjects = Map.of(
        MAIN_MAP, new GameEntity[]{
            new BronzeCoin(gamePanel).setWorldPosition(gamePanel.tileSize * 25, gamePanel.tileSize * 23),
            new BronzeCoin(gamePanel).setWorldPosition(gamePanel.tileSize * 21, gamePanel.tileSize * 19),
            new Key(gamePanel).setWorldPosition(gamePanel.tileSize * 26, gamePanel.tileSize * 21),
            new Axe(gamePanel).setWorldPosition(gamePanel.tileSize * 33, gamePanel.tileSize * 21),
            new BlueShield(gamePanel).setWorldPosition(gamePanel.tileSize * 35, gamePanel.tileSize * 21),
            new RedPotion(gamePanel).setWorldPosition(gamePanel.tileSize * 22, gamePanel.tileSize * 27),
            new Heart(gamePanel).setWorldPosition(gamePanel.tileSize * 22, gamePanel.tileSize * 29),
            new ManaCrystal(gamePanel).setWorldPosition(gamePanel.tileSize * 22, gamePanel.tileSize * 31),
            new Door(gamePanel).setWorldPosition(gamePanel.tileSize * 14, gamePanel.tileSize * 28),
            new Door(gamePanel).setWorldPosition(gamePanel.tileSize * 12, gamePanel.tileSize * 12),
            new Chest(gamePanel).setLoot(new Key(gamePanel)).setWorldPosition(gamePanel.tileSize * 33, gamePanel.tileSize * 7),
            new RedPotion(gamePanel).setWorldPosition(gamePanel.tileSize * 21, gamePanel.tileSize * 20),
            new RedPotion(gamePanel).setWorldPosition(gamePanel.tileSize * 20, gamePanel.tileSize * 20),
            new RedPotion(gamePanel).setWorldPosition(gamePanel.tileSize * 17, gamePanel.tileSize * 21),
            new Lantern(gamePanel).setWorldPosition(gamePanel.tileSize * 18, gamePanel.tileSize * 20),
            new Tent(gamePanel).setWorldPosition(gamePanel.tileSize * 19, gamePanel.tileSize * 20),
            new Chest(gamePanel).setLoot(new Tent(gamePanel)).setWorldPosition(gamePanel.tileSize * 17, gamePanel.tileSize * 20)
        },
        DUNGEON_FIRST_FLOR, new GameEntity[]{
            new Chest(gamePanel).setLoot(new PickAxe(gamePanel)).setWorldPosition(gamePanel.tileSize * 40, gamePanel.tileSize * 41),
            new Chest(gamePanel).setLoot(new RedPotion(gamePanel)).setWorldPosition(gamePanel.tileSize * 13, gamePanel.tileSize * 16),
            new Chest(gamePanel).setLoot(new RedPotion(gamePanel)).setWorldPosition(gamePanel.tileSize * 26, gamePanel.tileSize * 34),
            new Chest(gamePanel).setLoot(new RedPotion(gamePanel)).setWorldPosition(gamePanel.tileSize * 27, gamePanel.tileSize * 15),
            new IronDoor(gamePanel).setWorldPosition(gamePanel.tileSize * 18, gamePanel.tileSize * 23)
        },
        DUNGEON_SECOND_FLOR, new GameEntity[]{
            new IronDoor(gamePanel).setWorldPosition(gamePanel.tileSize * 25, gamePanel.tileSize * 15),
            null
        }
    );
  }

  public void setProjectile() {
    gamePanel.projectiles = Map.of(
        MAIN_MAP, new GameEntity[MAIN_MAP_MONSTER_NUMBER + 1],
        INTERIOR_MAP, new GameEntity[1],
        DUNGEON_FIRST_FLOR, new GameEntity[1],
        DUNGEON_SECOND_FLOR, new GameEntity[1]
    );
  }

  public void setNPC() {
    gamePanel.mapsNpc = new HashMap<>();
    gamePanel.mapsNpc.put(MAIN_MAP, new GameEntity[]{
        new OldManNPC(gamePanel).setWorldPosition(gamePanel.tileSize * 21, gamePanel.tileSize * 21)
    });
    gamePanel.mapsNpc.put(INTERIOR_MAP, new GameEntity[]{
        new MerchantNPC(gamePanel).setWorldPosition(gamePanel.tileSize * 12, gamePanel.tileSize * 7)
    });
    gamePanel.mapsNpc.put(DUNGEON_SECOND_FLOR, new GameEntity[SECOND_FLOR_NPC_NUMBER]);
  }


  public void setInteractiveObjects() {
    if (Objects.isNull(gamePanel.mapsNpc)) {
      gamePanel.mapsNpc = new HashMap<>();
    }
    gamePanel.mapsNpc.put(DUNGEON_FIRST_FLOR, new GameEntity[]{
        new BigRock(gamePanel).setWorldPosition(gamePanel.tileSize * 20, gamePanel.tileSize * 25),
        new BigRock(gamePanel).setWorldPosition(gamePanel.tileSize * 11, gamePanel.tileSize * 18),
        new BigRock(gamePanel).setWorldPosition(gamePanel.tileSize * 23, gamePanel.tileSize * 14)
    });
  }

  public void setMonster() {
    gamePanel.mapsMonsters = Map.of(
        MAIN_MAP, new GameEntity[]{
            new GreenSlime(gamePanel).setWorldPosition(gamePanel.tileSize * 21, gamePanel.tileSize * 38),
            new GreenSlime(gamePanel).setWorldPosition(gamePanel.tileSize * 23, gamePanel.tileSize * 42),
            new GreenSlime(gamePanel).setWorldPosition(gamePanel.tileSize * 24, gamePanel.tileSize * 37),
            new GreenSlime(gamePanel).setWorldPosition(gamePanel.tileSize * 34, gamePanel.tileSize * 42),
            new GreenSlime(gamePanel).setWorldPosition(gamePanel.tileSize * 38, gamePanel.tileSize * 42),
            new Orc(gamePanel).setWorldPosition(gamePanel.tileSize * 12, gamePanel.tileSize * 33)
        },
        DUNGEON_FIRST_FLOR, new GameEntity[]{
            new Bat(gamePanel).setWorldPosition(gamePanel.tileSize * 34, gamePanel.tileSize * 39),
            new Bat(gamePanel).setWorldPosition(gamePanel.tileSize * 36, gamePanel.tileSize * 25),
            new Bat(gamePanel).setWorldPosition(gamePanel.tileSize * 39, gamePanel.tileSize * 26),
            new Bat(gamePanel).setWorldPosition(gamePanel.tileSize * 28, gamePanel.tileSize * 11),
            new Bat(gamePanel).setWorldPosition(gamePanel.tileSize * 10, gamePanel.tileSize * 19)
        },
        DUNGEON_SECOND_FLOR, new GameEntity[]{
            new SkeletonLord(gamePanel).setWorldPosition(gamePanel.tileSize * 23, gamePanel.tileSize * 16)
        }
    );
  }

  public void setInteractiveTiles() {
    gamePanel.mapsInteractiveTiles = Map.of(
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
    );
  }
}
