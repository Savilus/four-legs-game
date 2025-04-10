package org.savilusGame.entity.interactiveTile;

import static org.savilusGame.config.GameEntityNameFactory.CHIP_WALL;
import static org.savilusGame.config.GameEntityNameFactory.DESTRUCTIBLE_WALL;
import static org.savilusGame.enums.WorldGameTypes.PICKAXE;

import java.awt.*;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;

public class DestructibleWall extends InteractiveTile {

  public DestructibleWall(GamePanel gamePanel, int col, int row) {
    super(gamePanel);
    currentLife = 1;
    this.worldX = gamePanel.tileSize * col;
    this.worldY = gamePanel.tileSize * row;

    image = setup(DESTRUCTIBLE_WALL, gamePanel.tileSize, gamePanel.tileSize);
    setDestructible(true);
  }

  @Override
  public Color getParticleColor() {
    return new Color(65, 65, 65);
  }

  @Override
  public int getParticleSize() {
    return 6;  // 6 pixels
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
  public void playSoundEffect() {
    gamePanel.playSoundEffect(CHIP_WALL);
  }

  @Override
  public boolean isCorrectItem(GameEntity item) {
    return item.currentWeapon.type == PICKAXE;
  }
}

