package org.example.entity.weapon;

import static org.example.config.GameEntityNameFactory.PICK_AXE;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;
import org.example.utils.text.TextManager;

public class PickAxe extends GameEntity {

  public PickAxe(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.PICKAXE.getName();
    down1 = setup(PICK_AXE, gamePanel.tileSize, gamePanel.tileSize);
    attackValue = 2;
    attackArea.width = 30;
    attackArea.height = 30;
    price = 75;
    description = String.format(TextManager.getItemDescription(GameObjectType.PICKAXE.getTextKey()), name);
    type = WorldGameTypes.PICKAXE;
    knockBackPower = 10;
    firstAttackMotionDuration = 10;
    secondAttackMotionDuration = 20;
  }
}
