package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.BLUE_SHIELD;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.WorldGameTypes;

public class BlueShield extends GameEntity {

  public BlueShield(GamePanel gamePanel) {
    super(gamePanel);
    name = "Blue Shield";
    down1 = setup(BLUE_SHIELD, gamePanel.tileSize, gamePanel.tileSize);
    image = setup(BLUE_SHIELD, gamePanel.tileSize, gamePanel.tileSize);
    defenseValue = 2;
    description = "[" + name + "]\nA shiny blue shield.";
    type = WorldGameTypes.SHIELD;
    price = 25;
  }
}
