package org.savilusGame.entity.projectile;

import static org.savilusGame.config.GameEntityNameFactory.FIREBALL_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.FIREBALL_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.FIREBALL_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.FIREBALL_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.FIREBALL_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.FIREBALL_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.FIREBALL_UP1;
import static org.savilusGame.config.GameEntityNameFactory.FIREBALL_UP2;

import java.awt.*;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObjectType;

public class Fireball extends Projectile {

  public Fireball(GamePanel gamePanel) {
    super(gamePanel);

    name = GameObjectType.FIREBALL.getName();
    speed = 5;
    maxLife = 80;
    currentLife = maxLife;
    attack = 1;
    useCost = 1;
    alive = false;
    knockBackPower = 5;
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

  @Override
  public Color getParticleColor() {
    return new Color(240, 50, 0);
  }

  @Override
  public int getParticleSize() {
    return 10;  // 6 pixels
  }

  @Override
  public int getParticleSpeed() {
    return 1;
  }

  @Override
  public int getParticleMaxLife() {
    return 20;
  }

  @Override
  public void substractResource(GameEntity user) {
    user.mana -= useCost;
  }

  @Override
  public boolean haveResource(GameEntity user) {
    return user.mana >= useCost;
  }
}
