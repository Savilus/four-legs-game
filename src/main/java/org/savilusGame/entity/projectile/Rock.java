package org.savilusGame.entity.projectile;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.ROCK_ATTACK;

import java.awt.*;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;

public class Rock extends Projectile {

  public Rock(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObject.ROCK.getName();
    speed = 8;
    maxLife = 50;
    currentLife = maxLife;
    attack = 2;
    alive = false;
    setUseCost(1);
    getImage();
  }

  private void getImage() {
    up1 = setup(ROCK_ATTACK, TILE_SIZE, TILE_SIZE);
    up2 = setup(ROCK_ATTACK, TILE_SIZE, TILE_SIZE);
    down1 = setup(ROCK_ATTACK, TILE_SIZE, TILE_SIZE);
    down2 = setup(ROCK_ATTACK, TILE_SIZE, TILE_SIZE);
    left1 = setup(ROCK_ATTACK, TILE_SIZE, TILE_SIZE);
    left2 = setup(ROCK_ATTACK, TILE_SIZE, TILE_SIZE);
    right1 = setup(ROCK_ATTACK, TILE_SIZE, TILE_SIZE);
    right2 = setup(ROCK_ATTACK, TILE_SIZE, TILE_SIZE);
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
    user.setAmmo(user.getAmmo() - getUseCost());
  }

  @Override
  public boolean haveResource(GameEntity user) {
    return user.getAmmo() >= getUseCost();
  }

}
