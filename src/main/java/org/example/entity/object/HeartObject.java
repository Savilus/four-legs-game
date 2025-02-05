package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.HEART_BLANK;
import static org.example.config.GameEntityNameFactory.HEART_FULL;
import static org.example.config.GameEntityNameFactory.HEART_HALF;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class HeartObject extends GameEntity {

  public HeartObject(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.HEART.getValue();
    image = setup(HEART_FULL);
    image2 = setup(HEART_HALF);
    image3 = setup(HEART_BLANK);

  }
}
