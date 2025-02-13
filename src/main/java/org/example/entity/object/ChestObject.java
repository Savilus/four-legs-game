package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.CHEST;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class ChestObject extends GameEntity {

  public ChestObject(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.CHEST.getName();
    down1 = setup(CHEST, gamePanel.tileSize, gamePanel.tileSize);
  }
}
