package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.BOOTS;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class BootsObject extends GameEntity {

  public BootsObject(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.BOOTS.getValue();
    down1 = setup(BOOTS, gamePanel.tileSize, gamePanel.tileSize);

  }
}
