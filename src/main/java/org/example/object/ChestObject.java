package org.example.object;

import static org.example.config.GameNameFactory.CHEST;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class ChestObject extends GameEntity {

  public ChestObject(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.CHEST.getValue();
    down1 = setup(CHEST);
  }
}
