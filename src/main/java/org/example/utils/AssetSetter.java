package org.example.utils;

import org.example.GamePanel;
import org.example.entity.NPC;
import org.example.entity.monster.GreenSlime;
import org.example.entity.object.DoorObject;

public class AssetSetter {

  GamePanel gamePanel;

  public AssetSetter(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void setObject() {

  }

  public void setNPC() {
    gamePanel.npc[0] = new NPC(gamePanel);
    gamePanel.npc[0].worldX = gamePanel.tileSize * 9;
    gamePanel.npc[0].worldY = gamePanel.tileSize * 10;
  }

  public void setMonster(){
    gamePanel.monsters[0] = new GreenSlime(gamePanel);
    gamePanel.monsters[0].worldX = gamePanel.tileSize * 11;
    gamePanel.monsters[0].worldY = gamePanel.tileSize * 10;

    gamePanel.monsters[1] = new GreenSlime(gamePanel);
    gamePanel.monsters[1].worldX = gamePanel.tileSize * 11;
    gamePanel.monsters[1].worldY = gamePanel.tileSize * 11;
  }
}
