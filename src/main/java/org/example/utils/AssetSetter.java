package org.example.utils;

import org.example.GamePanel;
import org.example.entity.NPC;
import org.example.entity.monster.GreenSlime;

public class AssetSetter {

  GamePanel gamePanel;

  public AssetSetter(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void setObject() {

  }

  public void setNPC() {
    gamePanel.npc[0] = new NPC(gamePanel);
    gamePanel.npc[0].worldX = gamePanel.tileSize * 21;
    gamePanel.npc[0].worldY = gamePanel.tileSize * 21;
  }

  public void setMonster() {
    gamePanel.monsters[0] = new GreenSlime(gamePanel);
    gamePanel.monsters[0].worldX = gamePanel.tileSize * 23;
    gamePanel.monsters[0].worldY = gamePanel.tileSize * 36;

    gamePanel.monsters[1] = new GreenSlime(gamePanel);
    gamePanel.monsters[1].worldX = gamePanel.tileSize * 23;
    gamePanel.monsters[1].worldY = gamePanel.tileSize * 37;
  }
}
