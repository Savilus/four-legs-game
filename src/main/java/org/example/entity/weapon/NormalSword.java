package org.example.entity.weapon;

import static org.example.config.GameEntityNameFactory.NORMAL_SWORD;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;
import org.example.utils.text.TextManager;

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
