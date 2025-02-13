package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.ROCK_ATTACK;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.entity.Projectile;

public class RockObject extends Projectile {

  public RockObject(GamePanel gamePanel) {
    super(gamePanel);

    name = "Rock";
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
  public void substractResource(GameEntity user) {
    user.ammo -= useCost;
  }

  @Override
  public boolean haveResource(GameEntity user) {
    return user.ammo >= useCost;
  }

}
