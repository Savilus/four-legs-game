package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.DOOR;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class DoorObject extends GameEntity {

  public DoorObject(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.DOOR.toString();
    image = setup(DOOR, gamePanel.tileSize, gamePanel.tileSize);
    collision = true;

    solidArea.x = 0;
    solidArea.y = 16;
    solidArea.width = 48;
    solidArea.height = 32;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
  }
}
