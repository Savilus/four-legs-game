package org.savilusGame.entity.shield;

import static org.savilusGame.config.GameEntityNameFactory.WOODEN_SHIELD;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObjectType;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

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
