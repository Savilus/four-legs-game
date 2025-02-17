package org.example.entity;

import org.example.GamePanel;
import org.example.enums.DirectionType;

public abstract class Projectile extends GameEntity {

  GameEntity owner;
  public int useCost;

  protected Projectile(GamePanel gamePanel) {
    super(gamePanel);
  }

  public abstract void substractResource(GameEntity user);
  public abstract boolean haveResource(GameEntity user);

  public void set(int worldX, int worldY, DirectionType direction, boolean alive, GameEntity owner) {
    this.worldX = worldX;
    this.worldY = worldY;
    this.direction = direction;
    this.alive = alive;
    this.owner = owner;
    this.currentLife = this.maxLife;
  }

  public void update() {

    if (owner == gamePanel.player) {
      int monsterIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.monsters);
      if (monsterIndex != 999) {
        gamePanel.player.damageMonster(monsterIndex, attack);
        generateParticle(owner.projectile, gamePanel.monsters[monsterIndex]);
        alive = false;
      }
    } else {
      boolean contactPlayer = gamePanel.collisionDetector.checkPlayer(this);
      if (!gamePanel.player.invincible && contactPlayer) {
        damagePlayer(attackValue);
        generateParticle(owner.projectile, gamePanel.player);
        alive = false;
      }
    }
    switch (getDirection()) {
      case UP -> worldY -= speed;
      case DOWN -> worldY += speed;
      case LEFT -> worldX -= speed;
      case RIGHT -> worldX += speed;
    }

    currentLife--;
    if (currentLife <= 0) {
      alive = false;
    }

    spriteCounter++;
    if (spriteCounter > 12) {
      if (spriteNum == 1) {
        spriteNum = 2;
      } else if (spriteNum == 2) {
        spriteNum = 1;
      }
      spriteCounter = 0;
    }
  }
}
