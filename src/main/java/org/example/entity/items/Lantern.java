package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.LANTERN;
import static org.example.enums.WorldGameTypes.LIGHTING;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class Lantern extends GameEntity {

  private static final String LANTERN_DESCRIPTION = "[Lantern] \n Illuminates your \n surrondings.";

  public Lantern(GamePanel gamePanel) {
    super(gamePanel);

    type = LIGHTING;
    name = GameObjectType.LANTERN.getName();
    image = setup(LANTERN, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(LANTERN, gamePanel.tileSize, gamePanel.tileSize);
    description = LANTERN_DESCRIPTION;
    price = 200;
    lightRadius = 450;
  }
}
