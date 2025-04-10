package org.savilusGame.entity.interactiveTile;

import static org.savilusGame.config.GameEntityNameFactory.DESTROY_TREE;
import static org.savilusGame.config.GameEntityNameFactory.DRY_TREE;
import static org.savilusGame.enums.WorldGameTypes.AXE;

import java.awt.*;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;

public class InteractiveDryTree extends InteractiveTile {

  public InteractiveDryTree(GamePanel gamePanel, int col, int row) {
    super(gamePanel);
    currentLife = 1;
    this.worldX = gamePanel.tileSize * col;
    this.worldY = gamePanel.tileSize * row;

    image = setup(DRY_TREE, gamePanel.tileSize, gamePanel.tileSize);
    setDestructible(true);
  }

  @Override
  public Color getParticleColor() {
    return new Color(65, 50, 30);
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
    gamePanel.playSoundEffect(DESTROY_TREE);
  }

  @Override
  public InteractiveTile getDestroyedForm() {
    return new Trunk(gamePanel, worldX / gamePanel.tileSize, worldY / gamePanel.tileSize);
  }

  @Override
  public boolean isCorrectItem(GameEntity item) {
    return item.currentWeapon.type == AXE;
  }
}
