package org.savilusGame.entity.weapon;

import static org.savilusGame.config.GameEntityNameFactory.NORMAL_SWORD;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObjectType;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class NormalSword extends GameEntity {

  public NormalSword(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.NORMAL_SWORD.getName();
    down1 = setup(NORMAL_SWORD, gamePanel.tileSize, gamePanel.tileSize);
    attackValue = 1;
    attackArea.width = 36;
    attackArea.height = 36;
    description = String.format(TextManager.getItemDescription(GameObjectType.NORMAL_SWORD.getTextKey()), name);
    type = WorldGameTypes.SWORD;
    price = 10;
    knockBackPower = 2;
    firstAttackMotionDuration = 5;
    secondAttackMotionDuration = 25;

  }
}
