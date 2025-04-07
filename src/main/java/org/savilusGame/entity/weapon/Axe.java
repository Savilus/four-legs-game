package org.savilusGame.entity.weapon;

import static org.savilusGame.config.GameEntityNameFactory.AXE;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObjectType;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class Axe extends GameEntity {

  public Axe(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.AXE.getName();
    image = setup(AXE, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(AXE, gamePanel.tileSize, gamePanel.tileSize);
    attackValue = 2;
    attackArea.width = 30;
    attackArea.height = 30;
    price = 25;
    description = String.format(TextManager.getItemDescription(GameObjectType.AXE.getTextKey()), name);
    type = WorldGameTypes.AXE;
    knockBackPower = 5;
    firstAttackMotionDuration = 20;
    secondAttackMotionDuration = 40;
  }
}
