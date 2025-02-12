package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.KEY;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class KeyObject extends GameEntity {

  public KeyObject(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.KEY.getValue();
    down1 = setup(KEY, gamePanel.tileSize, gamePanel.tileSize);
    description = "[" + name + "]\nIt opens a door";
  }
}
