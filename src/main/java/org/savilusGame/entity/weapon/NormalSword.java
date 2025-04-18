package org.savilusGame.entity.weapon;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.NORMAL_SWORD;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class NormalSword extends GameEntity {

  public NormalSword(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObject.NORMAL_SWORD.getName();
    down1 = setup(NORMAL_SWORD, TILE_SIZE, TILE_SIZE);
    attackValue = 1;
    attackArea.width = 36;
    attackArea.height = 36;
    description = String.format(TextManager.getItemDescription(GameObject.NORMAL_SWORD.getTextKey()), name);
    type = WorldGameTypes.SWORD;
    price = 15;
    knockBackPower = 2;
    firstAttackMotionDuration = 5;
    secondAttackMotionDuration = 25;
  }
}
