package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.BOOTS;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class Boots extends GameEntity {

  public Boots(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.BOOTS.getName();
    down1 = setup(BOOTS, gamePanel.tileSize, gamePanel.tileSize);

  }
}
