package org.savilusGame.entity.shield;

import static org.savilusGame.config.GameEntityNameFactory.BLUE_SHIELD;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObjectType;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

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
