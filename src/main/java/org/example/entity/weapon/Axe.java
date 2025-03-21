package org.example.entity.weapon;

import static org.example.config.GameEntityNameFactory.AXE;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;

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
    description = "[" + name + "] \n A bit rusty axe but still \n cut some trees";
    type = WorldGameTypes.AXE;
    knockBackPower = 5;
    firstAttackMotionDuration = 20;
    secondAttackMotionDuration = 40;
  }
}
