package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.HEART_BLANK;
import static org.example.config.GameEntityNameFactory.HEART_FULL;
import static org.example.config.GameEntityNameFactory.HEART_HALF;
import static org.example.config.GameEntityNameFactory.POWER_UP;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;

public class Heart extends GameEntity {

  public Heart(GamePanel gamePanel) {
    super(gamePanel);
    type = WorldGameTypes.PICK_UP;
    name = "Heart";
    value = 2;
    down1 = setup(HEART_FULL, gamePanel.tileSize, gamePanel.tileSize);

    name = GameObjectType.HEART.getName();
    image = setup(HEART_FULL, gamePanel.tileSize, gamePanel.tileSize);
    image2 = setup(HEART_HALF, gamePanel.tileSize, gamePanel.tileSize);
    image3 = setup(HEART_BLANK, gamePanel.tileSize, gamePanel.tileSize);

  }

  @Override
  public boolean use(GameEntity entity) {
    gamePanel.playSoundEffect(POWER_UP);
    gamePanel.ui.addMessage("Life +" + value);
    entity.currentLife += value;
    return true;
  }
}
