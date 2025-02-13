package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.FIREBALL_DOWN1;
import static org.example.config.GameEntityNameFactory.FIREBALL_DOWN2;
import static org.example.config.GameEntityNameFactory.FIREBALL_LEFT1;
import static org.example.config.GameEntityNameFactory.FIREBALL_LEFT2;
import static org.example.config.GameEntityNameFactory.FIREBALL_RIGHT1;
import static org.example.config.GameEntityNameFactory.FIREBALL_RIGHT2;
import static org.example.config.GameEntityNameFactory.FIREBALL_UP1;
import static org.example.config.GameEntityNameFactory.FIREBALL_UP2;

import org.example.GamePanel;
import org.example.entity.Projectile;

public class FireballObject extends Projectile {

  private final GamePanel gamePanel;

  public FireballObject(GamePanel gamePanel) {
    super(gamePanel);
    this.gamePanel = gamePanel;

    name = "Fireball";
    speed = 5;
    maxLife = 80;
    currentLife = maxLife;
    attack = 2;
    useCost = 1;
    alive = false;
    getImage();
  }

  private void getImage() {
      up1 = setup(FIREBALL_UP1, gamePanel.tileSize, gamePanel.tileSize);
      up2 = setup(FIREBALL_UP2, gamePanel.tileSize, gamePanel.tileSize);
      down1 = setup(FIREBALL_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
      down2 = setup(FIREBALL_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
      left1 = setup(FIREBALL_LEFT1, gamePanel.tileSize, gamePanel.tileSize);
      left2 = setup(FIREBALL_LEFT2, gamePanel.tileSize, gamePanel.tileSize);
      right1 = setup(FIREBALL_RIGHT1, gamePanel.tileSize, gamePanel.tileSize);
      right2 = setup(FIREBALL_RIGHT2, gamePanel.tileSize, gamePanel.tileSize);
  }
}
