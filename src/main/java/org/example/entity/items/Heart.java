package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.HEART_BLANK;
import static org.example.config.GameEntityNameFactory.HEART_FULL;
import static org.example.config.GameEntityNameFactory.HEART_HALF;
import static org.example.config.GameEntityNameFactory.POWER_UP;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;
import org.example.utils.text.TextManager;

public class Heart extends GameEntity {

  private static final String LIFE = "life";
  private static final String UI_MESSAGES = "uiMessages";

  public Heart(GamePanel gamePanel) {
    super(gamePanel);
    type = WorldGameTypes.PICK_UP;
    name = GameObjectType.HEART.getName();
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
    gamePanel.ui.addMessage(TextManager.getUiText(UI_MESSAGES, LIFE) + value);
    entity.currentLife += value;
    return true;
  }
}
