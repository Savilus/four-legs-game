package org.example.utils;

import static org.example.config.GameEntityNameFactory.INTERIOR_MAP;
import static org.example.config.GameEntityNameFactory.MAIN_MAP_PATH;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.entity.interactiveTile.DryTreeInteractive;
import org.example.entity.interactiveTile.InteractiveTile;
import org.example.entity.monster.GreenSlime;
import org.example.entity.npc.MerchantNPC;
import org.example.entity.npc.OldManNPC;
import org.example.entity.object.AxeObject;
import org.example.entity.object.BlueShieldObject;
import org.example.entity.object.BronzeCoinObject;
import org.example.entity.object.ChestObject;
import org.example.entity.object.DoorObject;
import org.example.entity.object.HeartObject;
import org.example.entity.object.KeyObject;
import org.example.entity.object.ManaCrystalObject;
import org.example.entity.object.RedPotionObject;

public class AssetSetter {

  private static int MAIN_MAP_NPC_NUMBER = 10;
  private static int MAIN_MAP_MONSTER_NUMBER = 20;
  private static int INTERIOR_MAP_NPC_NUMBER = 5;

  GamePanel gamePanel;

  public AssetSetter(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void setObject() {
    gamePanel.mapsObjects.put(MAIN_MAP_PATH, new GameEntity[20]);
    int i = 0;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i] = new BronzeCoinObject(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 25;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 23;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i] = new BronzeCoinObject(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 21;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 19;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i] = new KeyObject(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 26;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 21;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i] = new AxeObject(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 33;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 21;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i] = new BlueShieldObject(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 35;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 21;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i] = new RedPotionObject(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 22;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 27;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i] = new HeartObject(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 22;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 29;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i] = new ManaCrystalObject(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 22;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 31;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i] = new DoorObject(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 14;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 28;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i] = new DoorObject(gamePanel);
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 12;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 12;
    i++;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i] = new ChestObject(gamePanel, new KeyObject(gamePanel));
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 33;
    gamePanel.mapsObjects.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 7;
  }

  public void setProjectile() {
    gamePanel.projectiles.put(MAIN_MAP_PATH, new GameEntity[MAIN_MAP_MONSTER_NUMBER + 1]);
    gamePanel.projectiles.put(INTERIOR_MAP, new GameEntity[1]);
  }

  public void setNPC() {
    gamePanel.mapsNpc.put(MAIN_MAP_PATH, new GameEntity[MAIN_MAP_NPC_NUMBER]);
    gamePanel.mapsNpc.get(MAIN_MAP_PATH)[0] = new OldManNPC(gamePanel);
    gamePanel.mapsNpc.get(MAIN_MAP_PATH)[0].worldX = gamePanel.tileSize * 21;
    gamePanel.mapsNpc.get(MAIN_MAP_PATH)[0].worldY = gamePanel.tileSize * 21;

    gamePanel.mapsNpc.put(INTERIOR_MAP, new GameEntity[INTERIOR_MAP_NPC_NUMBER]);
    gamePanel.mapsNpc.get(INTERIOR_MAP)[0] = new MerchantNPC(gamePanel);
    gamePanel.mapsNpc.get(INTERIOR_MAP)[0].worldX = gamePanel.tileSize * 12;
    gamePanel.mapsNpc.get(INTERIOR_MAP)[0].worldY = gamePanel.tileSize * 7;
  }

  public void setMonster() {
    gamePanel.mapsMonsters.put(MAIN_MAP_PATH, new GameEntity[MAIN_MAP_MONSTER_NUMBER]);
    int i = 0;
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i] = new GreenSlime(gamePanel);
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 21;
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 38;
    i++;
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i] = new GreenSlime(gamePanel);
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 23;
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 42;
    i++;
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i] = new GreenSlime(gamePanel);
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 24;
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 37;
    i++;
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i] = new GreenSlime(gamePanel);
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 34;
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 42;
    i++;
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i] = new GreenSlime(gamePanel);
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i].worldX = gamePanel.tileSize * 38;
    gamePanel.mapsMonsters.get(MAIN_MAP_PATH)[i].worldY = gamePanel.tileSize * 42;
  }

  public void setInteractiveTiles() {
    gamePanel.mapsInteractiveTiles.put(MAIN_MAP_PATH, new InteractiveTile[50]);
    int i = 0;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP_PATH)[i] = new DryTreeInteractive(gamePanel, 27, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP_PATH)[i] = new DryTreeInteractive(gamePanel, 28, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP_PATH)[i] = new DryTreeInteractive(gamePanel, 29, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP_PATH)[i] = new DryTreeInteractive(gamePanel, 30, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP_PATH)[i] = new DryTreeInteractive(gamePanel, 31, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP_PATH)[i] = new DryTreeInteractive(gamePanel, 32, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP_PATH)[i] = new DryTreeInteractive(gamePanel, 33, 12);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP_PATH)[i] = new DryTreeInteractive(gamePanel, 18, 40);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP_PATH)[i] = new DryTreeInteractive(gamePanel, 17, 40);
    i++;
    gamePanel.mapsInteractiveTiles.get(MAIN_MAP_PATH)[i] = new DryTreeInteractive(gamePanel, 16, 40);
  }
}
