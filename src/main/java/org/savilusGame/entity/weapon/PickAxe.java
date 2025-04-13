package org.savilusGame.entity.weapon;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.PICK_AXE;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class PickAxe extends GameEntity {

  public PickAxe(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObject.PICKAXE.getName();
    down1 = setup(PICK_AXE, TILE_SIZE, TILE_SIZE);
    attackValue = 2;
    attackArea.width = 30;
    attackArea.height = 30;
    price = 75;
    description = String.format(TextManager.getItemDescription(GameObject.PICKAXE.getTextKey()), name);
    type = WorldGameTypes.PICKAXE;
    knockBackPower = 10;
    firstAttackMotionDuration = 10;
    secondAttackMotionDuration = 20;
  }
}
