package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.LANTERN;
import static org.example.enums.WorldGameTypes.LIGHTING;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.utils.text.TextManager;

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
