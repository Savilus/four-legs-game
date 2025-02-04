package org.example.object;

import static org.example.config.GameNameFactory.KEY;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class KeyObject extends GameEntity {

  public KeyObject(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.KEY.getValue();
    down1 = setup(KEY);
  }
}
