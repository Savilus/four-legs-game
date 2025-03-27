package org.example.entity.shield;

import static org.example.config.GameEntityNameFactory.BLUE_SHIELD;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;
import org.example.utils.text.TextManager;

public class BlueShield extends GameEntity {

  public BlueShield(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.BLUE_SHIELD.getName();
    down1 = setup(BLUE_SHIELD, gamePanel.tileSize, gamePanel.tileSize);
    image = setup(BLUE_SHIELD, gamePanel.tileSize, gamePanel.tileSize);
    defenseValue = 2;
    description = String.format(TextManager.getItemDescription(GameObjectType.BLUE_SHIELD.getTextKey()), name);
    type = WorldGameTypes.SHIELD;
    price = 25;
  }
}
