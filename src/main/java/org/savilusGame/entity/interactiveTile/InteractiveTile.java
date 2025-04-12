package org.savilusGame.entity.interactiveTile;

import static org.savilusGame.GamePanel.TILE_SIZE;

import java.awt.*;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InteractiveTile extends GameEntity {

  private boolean destructible = false;

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
    int screenX = worldX - gamePanel.getPlayer().worldX + gamePanel.getPlayer().screenX;
    int screenY = worldY - gamePanel.getPlayer().worldY + gamePanel.getPlayer().screenY;

    if (worldX + TILE_SIZE > gamePanel.getPlayer().worldX - gamePanel.getPlayer().screenX &&
        worldX - TILE_SIZE < gamePanel.getPlayer().worldX + gamePanel.getPlayer().screenX &&
        worldY + TILE_SIZE > gamePanel.getPlayer().worldY - gamePanel.getPlayer().screenY &&
        worldY - TILE_SIZE < gamePanel.getPlayer().worldY + gamePanel.getPlayer().screenY) {

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
