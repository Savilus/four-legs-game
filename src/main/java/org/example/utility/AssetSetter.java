package org.example.utility;

import org.example.GamePanel;
import org.example.entity.NPC;

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
}
