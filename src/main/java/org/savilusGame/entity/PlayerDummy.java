package org.savilusGame.entity;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.BOY_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_UP1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_UP2;

import org.savilusGame.GamePanel;

public class PlayerDummy extends GameEntity {
  private static final String NAME = "PlayerDummy";

  public PlayerDummy(GamePanel gamePanel) {
    super(gamePanel);
    name = NAME;
    getImage();
  }

  public void getImage() {
    up1 = setup(BOY_UP1, TILE_SIZE, TILE_SIZE);
    up2 = setup(BOY_UP2, TILE_SIZE, TILE_SIZE);
    down1 = setup(BOY_DOWN1, TILE_SIZE, TILE_SIZE);
    down2 = setup(BOY_DOWN2, TILE_SIZE, TILE_SIZE);
    left1 = setup(BOY_LEFT1, TILE_SIZE, TILE_SIZE);
    up1 = setup(BOY_UP1, TILE_SIZE, TILE_SIZE);
    left2 = setup(BOY_LEFT2, TILE_SIZE, TILE_SIZE);
    right1 = setup(BOY_RIGHT1, TILE_SIZE, TILE_SIZE);
    right2 = setup(BOY_RIGHT2, TILE_SIZE, TILE_SIZE);
  }
}
