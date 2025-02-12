package org.example.utils;

import org.example.GamePanel;
import org.example.entity.NPC;
import org.example.entity.monster.GreenSlime;
import org.example.entity.object.BootsObject;
import org.example.entity.object.KeyObject;

public class AssetSetter {

  GamePanel gamePanel;

  public AssetSetter(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void setObject() {
    gamePanel.obj[0] = new BootsObject(gamePanel);
    gamePanel.obj[0].worldX = gamePanel.tileSize * 25;
    gamePanel.obj[0].worldY = gamePanel.tileSize * 19;
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
