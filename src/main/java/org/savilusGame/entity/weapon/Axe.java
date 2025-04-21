package org.savilusGame.entity.weapon;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.AXE;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class Axe extends GameEntity {

  public Axe(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObject.AXE.getName();
    mainImage = setup(AXE, TILE_SIZE, TILE_SIZE);
    down1 = setup(AXE, TILE_SIZE, TILE_SIZE);
    attackValue = 2;
    attackArea.width = 30;
    attackArea.height = 30;
    price = 40;
    description = String.format(TextManager.getItemDescription(GameObject.AXE.getTextKey()), name);
    type = WorldGameTypes.AXE;
    knockBackPower = 6;
    firstAttackMotionDuration = 25;
    secondAttackMotionDuration = 45;
  }
}
