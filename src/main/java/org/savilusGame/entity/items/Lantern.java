package org.savilusGame.entity.items;

import static org.savilusGame.config.GameEntityNameFactory.LANTERN;
import static org.savilusGame.enums.WorldGameTypes.LIGHTING;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObjectType;
import org.savilusGame.utils.text.TextManager;

public class Lantern extends GameEntity {

  public Lantern(GamePanel gamePanel) {
    super(gamePanel);

    type = LIGHTING;
    name = GameObjectType.LANTERN.getName();
    image = setup(LANTERN, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(LANTERN, gamePanel.tileSize, gamePanel.tileSize);
    description = String.format(TextManager.getItemDescription(GameObjectType.LANTERN.getTextKey()), name);
    price = 200;
    lightRadius = 450;
  }
}
