package org.example.entity.shield;

import static org.example.config.GameEntityNameFactory.WOODEN_SHIELD;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;
import org.example.utils.text.TextManager;

public class WoodShield extends GameEntity {

  public WoodShield(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.WOOD_SHIELD.getName();
    down1 = setup(WOODEN_SHIELD, gamePanel.tileSize, gamePanel.tileSize);
    defenseValue = 1;
    description = String.format(TextManager.getItemDescription(GameObjectType.WOOD_SHIELD.getTextKey()), name);
    type = WorldGameTypes.SHIELD;
    price = 10;

  }
}
