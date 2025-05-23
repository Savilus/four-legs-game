package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.BOOTS;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;

public class Boots extends GameEntity {

  public Boots(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObject.BOOTS.getName();
    down1 = setup(BOOTS, TILE_SIZE, TILE_SIZE);
  }
}
