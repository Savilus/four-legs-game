package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.LANTERN;
import static org.savilusGame.enums.WorldGameTypes.LIGHTING;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.utils.text.TextManager;

public class Lantern extends GameEntity {

  public Lantern(GamePanel gamePanel) {
    super(gamePanel);

    type = LIGHTING;
    name = GameObject.LANTERN.getName();
    image = setup(LANTERN, TILE_SIZE, TILE_SIZE);
    down1 = setup(LANTERN, TILE_SIZE, TILE_SIZE);
    description = String.format(TextManager.getItemDescription(GameObject.LANTERN.getTextKey()), name);
    price = 200;
    lightRadius = 450;
  }
}
