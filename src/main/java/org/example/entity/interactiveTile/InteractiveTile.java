package org.example.entity.interactiveTile;

import java.awt.*;

import org.example.GamePanel;
import org.example.entity.GameEntity;

public class InteractiveTile extends GameEntity {

  public boolean destructible = false;

  public InteractiveTile(GamePanel gamePanel) {
    super(gamePanel);
  }

  public boolean isCorrectItem(GameEntity item) {
    return false;
  }

  public void playSoundEffect() {
  }

  public InteractiveTile getDestroyedForm() {
    return null;
  }

  @Override
  public void draw(Graphics2D graphics2D) {
    int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
    int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

    if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
        worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
        worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
        worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {

      graphics2D.drawImage(image, screenX, screenY, null);

    }
  }

  @Override
  public void update() {
    if (invincible) {
      invincibleCounter++;
      if (invincibleCounter > 20) {
        invincible = false;
        invincibleCounter = 0;
      }
    }
  }
}
