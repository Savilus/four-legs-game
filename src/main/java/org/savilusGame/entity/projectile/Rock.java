package org.savilusGame.entity.projectile;

import static org.savilusGame.config.GameEntityNameFactory.ROCK_ATTACK;

import java.awt.*;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObjectType;

public class Rock extends Projectile {

  public Rock(GamePanel gamePanel) {
    super(gamePanel);

    name = GameObjectType.ROCK.getName();
    speed = 8;
    maxLife = 50;
    currentLife = maxLife;
    attack = 1;
    useCost = 1;
    alive = false;
    getImage();
  }

  private void getImage() {
    up1 = setup(ROCK_ATTACK, gamePanel.tileSize, gamePanel.tileSize);
    up2 = setup(ROCK_ATTACK, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(ROCK_ATTACK, gamePanel.tileSize, gamePanel.tileSize);
    down2 = setup(ROCK_ATTACK, gamePanel.tileSize, gamePanel.tileSize);
    left1 = setup(ROCK_ATTACK, gamePanel.tileSize, gamePanel.tileSize);
    left2 = setup(ROCK_ATTACK, gamePanel.tileSize, gamePanel.tileSize);
    right1 = setup(ROCK_ATTACK, gamePanel.tileSize, gamePanel.tileSize);
    right2 = setup(ROCK_ATTACK, gamePanel.tileSize, gamePanel.tileSize);
  }

  @Override
  public Color getParticleColor() {
    return new Color(40, 50, 0);
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
    user.ammo -= useCost;
  }

  @Override
  public boolean haveResource(GameEntity user) {
    return user.ammo >= useCost;
  }

}
