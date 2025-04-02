package org.example.utils;

import static org.example.config.GameEntityNameFactory.DUNGEON_FIRST_FLOR;
import static org.example.config.GameEntityNameFactory.DUNGEON_SECOND_FLOR;
import static org.example.config.GameEntityNameFactory.INTERIOR_MAP;
import static org.example.config.GameEntityNameFactory.MAIN_MAP;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.entity.interactiveObjects.BigRock;
import org.example.entity.interactiveTile.DestructibleWall;
import org.example.entity.interactiveTile.InteractiveDryTree;
import org.example.entity.interactiveTile.InteractiveTile;
import org.example.entity.interactiveTile.MetalPlate;
import org.example.entity.items.BronzeCoin;
import org.example.entity.items.Chest;
import org.example.entity.items.Door;
import org.example.entity.items.Heart;
import org.example.entity.items.IronDoor;
import org.example.entity.items.Key;
import org.example.entity.items.Lantern;
import org.example.entity.items.ManaCrystal;
import org.example.entity.items.RedPotion;
import org.example.entity.items.Tent;
import org.example.entity.monster.GreenSlime;
import org.example.entity.monster.Orc;
import org.example.entity.npc.MerchantNPC;
import org.example.entity.npc.OldManNPC;
import org.example.entity.shield.BlueShield;
import org.example.entity.weapon.Axe;
import org.example.entity.weapon.PickAxe;

public class AssetSetter {

  private static final int MAIN_MAP_NPC_NUMBER = 2;
  private static final int MAIN_MAP_MONSTER_NUMBER = 20;
  private static final int INTERIOR_MAP_NPC_NUMBER = 1;
  private static final int FIRST_FLOR_DUNGEON_INTERACTIVE_NUMBER = 5;

  GamePanel gamePanel;

  public AssetSetter(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void setObject() {
    gamePanel.mapsObjects.put(MAIN_MAP, new GameEntity[20]);
    int i = 0;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new BronzeCoin(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 25;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 23;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new BronzeCoin(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 21;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 19;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new Key(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 26;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 21;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new Axe(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 33;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 21;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new BlueShield(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 35;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 21;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new RedPotion(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 22;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 27;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new Heart(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 22;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 29;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new ManaCrystal(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 22;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 31;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new Door(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 14;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 28;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new Door(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 12;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 12;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new Chest(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].setLoot(new Key(gamePanel));
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 33;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 7;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new RedPotion(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 21;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 20;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new RedPotion(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 20;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 20;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new RedPotion(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 17;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 21;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new Lantern(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 18;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 20;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new Tent(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 19;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 20;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP)[i] = new Chest(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP)[i].setLoot(new Tent(gamePanel));
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 17;
    gamePanel.mapsObjects.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 20;

    gamePanel.mapsObjects.put(DUNGEON_FIRST_FLOR, new GameEntity[20]);
    i = 0;

    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i] = new Chest(gamePanel);
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].setLoot(new PickAxe(gamePanel));
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].worldX = gamePanel.tileSize * 40;
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].worldY = gamePanel.tileSize * 41;
    i++;
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i] = new Chest(gamePanel);
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].setLoot(new RedPotion(gamePanel));
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].worldX = gamePanel.tileSize * 13;
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].worldY = gamePanel.tileSize * 16;
    i++;
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i] = new Chest(gamePanel);
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].setLoot(new RedPotion(gamePanel));
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].worldX = gamePanel.tileSize * 26;
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].worldY = gamePanel.tileSize * 34;
    i++;
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i] = new Chest(gamePanel);
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].setLoot(new RedPotion (gamePanel));
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].worldX = gamePanel.tileSize * 27;
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].worldY = gamePanel.tileSize * 15;
    i++;
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i] = new IronDoor(gamePanel);
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].worldX = gamePanel.tileSize * 18;
    gamePanel.mapsObjects.get(DUNGEON_FIRST_FLOR)[i].worldY = gamePanel.tileSize * 23;

  }

  public void setProjectile() {
    gamePanel.projectiles.put(MAIN_MAP, new GameEntity[MAIN_MAP_MONSTER_NUMBER + 1]);
    gamePanel.projectiles.put(INTERIOR_MAP, new GameEntity[1]);
    gamePanel.projectiles.put(DUNGEON_FIRST_FLOR, new GameEntity[1]);
    gamePanel.projectiles.put(DUNGEON_SECOND_FLOR, new GameEntity[1]);
  }

  public void setNPC() {
    gamePanel.mapsNpc.put(MAIN_MAP, new GameEntity[MAIN_MAP_NPC_NUMBER]);
    gamePanel.mapsNpc.get(MAIN_MAP)[0] = new OldManNPC(gamePanel);
    gamePanel.mapsNpc.get(MAIN_MAP)[0].worldX = gamePanel.tileSize * 21;
    gamePanel.mapsNpc.get(MAIN_MAP)[0].worldY = gamePanel.tileSize * 21;

    gamePanel.mapsNpc.put(INTERIOR_MAP, new GameEntity[INTERIOR_MAP_NPC_NUMBER]);
    gamePanel.mapsNpc.get(INTERIOR_MAP)[0] = new MerchantNPC(gamePanel);
    gamePanel.mapsNpc.get(INTERIOR_MAP)[0].worldX = gamePanel.tileSize * 12;
    gamePanel.mapsNpc.get(INTERIOR_MAP)[0].worldY = gamePanel.tileSize * 7;
  }

  public void setInteractiveObjects() {
    int objectIndex = 0;
    gamePanel.mapsNpc.put(DUNGEON_FIRST_FLOR, new GameEntity[FIRST_FLOR_DUNGEON_INTERACTIVE_NUMBER]);
    gamePanel.mapsNpc.get(DUNGEON_FIRST_FLOR)[objectIndex] = new BigRock(gamePanel);
    gamePanel.mapsNpc.get(DUNGEON_FIRST_FLOR)[objectIndex].worldX = gamePanel.tileSize * 20;
    gamePanel.mapsNpc.get(DUNGEON_FIRST_FLOR)[objectIndex].worldY = gamePanel.tileSize * 25;
    objectIndex++;
    gamePanel.mapsNpc.get(DUNGEON_FIRST_FLOR)[objectIndex] = new BigRock(gamePanel);
    gamePanel.mapsNpc.get(DUNGEON_FIRST_FLOR)[objectIndex].worldX = gamePanel.tileSize * 11;
    gamePanel.mapsNpc.get(DUNGEON_FIRST_FLOR)[objectIndex].worldY = gamePanel.tileSize * 18;
    objectIndex++;
    gamePanel.mapsNpc.get(DUNGEON_FIRST_FLOR)[objectIndex] = new BigRock(gamePanel);
    gamePanel.mapsNpc.get(DUNGEON_FIRST_FLOR)[objectIndex].worldX = gamePanel.tileSize * 23;
    gamePanel.mapsNpc.get(DUNGEON_FIRST_FLOR)[objectIndex].worldY = gamePanel.tileSize * 14;
  }

  public void setMonster() {
    gamePanel.mapsMonsters.put(MAIN_MAP, new GameEntity[MAIN_MAP_MONSTER_NUMBER]);
    int i = 0;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i] = new GreenSlime(gamePanel);
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 21;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 38;
    i++;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i] = new GreenSlime(gamePanel);
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 23;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 42;
    i++;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i] = new GreenSlime(gamePanel);
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 24;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 37;
    i++;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i] = new GreenSlime(gamePanel);
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 34;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 42;
    i++;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i] = new GreenSlime(gamePanel);
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 38;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 42;
    i++;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i] = new Orc(gamePanel);
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldX = gamePanel.tileSize * 12;
    gamePanel.mapsMonsters.get(MAIN_MAP)[i].worldY = gamePanel.tileSize * 33;
  }

  public void setInteractiveTiles() {
    gamePanel.mapsInteractiveTiles.put(MAIN_MAP, new InteractiveTile[50]);
    int i = 0;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP)[i] = new InteractiveDryTree(gamePanel, 27, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP)[i] = new InteractiveDryTree(gamePanel, 28, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP)[i] = new InteractiveDryTree(gamePanel, 29, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP)[i] = new InteractiveDryTree(gamePanel, 30, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP)[i] = new InteractiveDryTree(gamePanel, 31, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP)[i] = new InteractiveDryTree(gamePanel, 32, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP)[i] = new InteractiveDryTree(gamePanel, 33, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP)[i] = new InteractiveDryTree(gamePanel, 18, 40);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP)[i] = new InteractiveDryTree(gamePanel, 17, 40);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP)[i] = new InteractiveDryTree(gamePanel, 16, 40);

    gamePanel.mapsInteractiveTiles.put(DUNGEON_FIRST_FLOR, new InteractiveTile[50]);
    i = 0;

    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 18, 30);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 17, 31);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 17, 32);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 17, 34);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 18, 34);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 18, 33);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 10, 22);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 10, 24);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 38, 18);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 38, 19);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 38, 20);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 38, 21);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 18, 13);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 18, 14);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 22, 28);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 30, 28);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new DestructibleWall(gamePanel, 32, 28);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new MetalPlate(gamePanel, 20, 22);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new MetalPlate(gamePanel, 8, 17);
    i++;
    gamePanel.mapsInteractiveTiles.get(DUNGEON_FIRST_FLOR)[i] = new MetalPlate(gamePanel, 39, 31);


  }
}
