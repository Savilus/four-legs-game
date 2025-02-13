package org.example.utils;

import org.example.GamePanel;
import org.example.entity.NPC;
import org.example.entity.monster.GreenSlime;
import org.example.entity.object.AxeObject;
import org.example.entity.object.BlueShieldObject;
import org.example.entity.object.BootsObject;
import org.example.entity.object.DoorObject;
import org.example.entity.object.KeyObject;
import org.example.entity.object.RedPotionObject;

public class AssetSetter {

  GamePanel gamePanel;

  public AssetSetter(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void setObject() {
    int i = 0;
    gamePanel.obj[i] = new KeyObject(gamePanel);
    gamePanel.obj[i].worldX = gamePanel.tileSize * 25;
    gamePanel.obj[i].worldY = gamePanel.tileSize * 23;
    i++;
    gamePanel.obj[i] = new KeyObject(gamePanel);
    gamePanel.obj[i].worldX = gamePanel.tileSize * 21;
    gamePanel.obj[i].worldY = gamePanel.tileSize * 19;
    i++;
    gamePanel.obj[i] = new KeyObject(gamePanel);
    gamePanel.obj[i].worldX = gamePanel.tileSize * 26;
    gamePanel.obj[i].worldY = gamePanel.tileSize * 21;
    i++;
    gamePanel.obj[i] = new AxeObject(gamePanel);
    gamePanel.obj[i].worldX = gamePanel.tileSize * 33;
    gamePanel.obj[i].worldY = gamePanel.tileSize * 21;
    i++;
    gamePanel.obj[i] = new BlueShieldObject(gamePanel);
    gamePanel.obj[i].worldX = gamePanel.tileSize * 35;
    gamePanel.obj[i].worldY = gamePanel.tileSize * 21;
    i++;
    gamePanel.obj[i] = new RedPotionObject(gamePanel);
    gamePanel.obj[i].worldX = gamePanel.tileSize * 22;
    gamePanel.obj[i].worldY = gamePanel.tileSize * 27;
  }

  public void setNPC() {
    gamePanel.npc[0] = new NPC(gamePanel);
    gamePanel.npc[0].worldX = gamePanel.tileSize * 21;
    gamePanel.npc[0].worldY = gamePanel.tileSize * 21;
  }

  public void setMonster() {
    int i = 0;
    gamePanel.monsters[i] = new GreenSlime(gamePanel);
    gamePanel.monsters[i].worldX = gamePanel.tileSize * 21;
    gamePanel.monsters[i].worldY = gamePanel.tileSize * 38;
    i++;
    gamePanel.monsters[i] = new GreenSlime(gamePanel);
    gamePanel.monsters[i].worldX = gamePanel.tileSize * 23;
    gamePanel.monsters[i].worldY = gamePanel.tileSize * 42;
    i++;
    gamePanel.monsters[i] = new GreenSlime(gamePanel);
    gamePanel.monsters[i].worldX = gamePanel.tileSize * 24;
    gamePanel.monsters[i].worldY = gamePanel.tileSize * 37;
    i++;
    gamePanel.monsters[i] = new GreenSlime(gamePanel);
    gamePanel.monsters[i].worldX = gamePanel.tileSize * 34;
    gamePanel.monsters[i].worldY = gamePanel.tileSize * 42;
    i++;
    gamePanel.monsters[i] = new GreenSlime(gamePanel);
    gamePanel.monsters[i].worldX = gamePanel.tileSize * 38;
    gamePanel.monsters[i].worldY = gamePanel.tileSize * 42;
  }
}
