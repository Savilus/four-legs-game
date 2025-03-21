package org.example.entity.shield;

import static org.example.config.GameEntityNameFactory.WOODEN_SHIELD;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;

public class WoodShield extends GameEntity {

  public WoodShield(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.WOOD_SHIELD.getName();
    down1 = setup(WOODEN_SHIELD, gamePanel.tileSize, gamePanel.tileSize);
    defenseValue = 1;
    description = "[" + name + "]\nMade by wood.";
    type = WorldGameTypes.SHIELD;
    price = 10;

  }
}
