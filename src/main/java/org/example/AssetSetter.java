package org.example;

import org.example.object.BootsObject;
import org.example.object.ChestObject;
import org.example.object.DoorObject;
import org.example.object.KeyObject;

public class AssetSetter {

  GamePanel gamePanel;

  public AssetSetter(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void setObject() {

    gamePanel.obj[0] = new KeyObject(gamePanel);
    gamePanel.obj[0].worldX = 23 * gamePanel.tileSize;
    gamePanel.obj[0].worldY = 7 * gamePanel.tileSize;

    gamePanel.obj[1] = new KeyObject(gamePanel);
    gamePanel.obj[1].worldX = 23 * gamePanel.tileSize;
    gamePanel.obj[1].worldY = 40 * gamePanel.tileSize;

    gamePanel.obj[2] = new KeyObject(gamePanel);
    gamePanel.obj[2].worldX = 38 * gamePanel.tileSize;
    gamePanel.obj[2].worldY = 8 * gamePanel.tileSize;

    gamePanel.obj[3] = new DoorObject(gamePanel);
    gamePanel.obj[3].worldX = 10 * gamePanel.tileSize;
    gamePanel.obj[3].worldY = 11 * gamePanel.tileSize;

    gamePanel.obj[4] = new DoorObject(gamePanel);
    gamePanel.obj[4].worldX = 8 * gamePanel.tileSize;
    gamePanel.obj[4].worldY = 28 * gamePanel.tileSize;

    gamePanel.obj[5] = new DoorObject(gamePanel);
    gamePanel.obj[5].worldX = 12 * gamePanel.tileSize;
    gamePanel.obj[5].worldY = 22 * gamePanel.tileSize;

    gamePanel.obj[6] = new ChestObject(gamePanel);
    gamePanel.obj[6].worldX = 10 * gamePanel.tileSize;
    gamePanel.obj[6].worldY = 7 * gamePanel.tileSize;

    gamePanel.obj[7] = new BootsObject(gamePanel);
    gamePanel.obj[7].worldX = 37 * gamePanel.tileSize;
    gamePanel.obj[7].worldY = 42 * gamePanel.tileSize;

  }
}
