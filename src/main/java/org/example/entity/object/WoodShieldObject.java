package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.WOODEN_SHIELD;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.WorldGameTypes;

public class WoodShieldObject extends GameEntity {

  public WoodShieldObject(GamePanel gamePanel) {
    super(gamePanel);
    name = "Wood Shield";
    down1 = setup(WOODEN_SHIELD, gamePanel.tileSize, gamePanel.tileSize);
    defenseValue = 1;
    description = "[" + name + "]\nMade by wood.";
    type = WorldGameTypes.SHIELD;

  }
}
