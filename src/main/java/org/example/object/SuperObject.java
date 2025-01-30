package org.example.object;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.example.GamePanel;
import org.example.utility.UtilityTool;

public class SuperObject {

  public BufferedImage image;
  public String name;
  public boolean collision = false;
  public int worldX, worldY;
  public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
  public int solidAreaDefaultX = 0;
  public int solidAreaDefaultY = 0;
  UtilityTool utilityTool = new UtilityTool();
  GamePanel gamePanel;

  public SuperObject(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void draw(Graphics2D graphics2D, GamePanel gamePanel) {

    int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
    int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

    if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
        worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
        worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
        worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {
      graphics2D.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
    }

  }
}
