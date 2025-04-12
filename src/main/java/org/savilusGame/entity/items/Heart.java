package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.HEART_BLANK;
import static org.savilusGame.config.GameEntityNameFactory.HEART_FULL;
import static org.savilusGame.config.GameEntityNameFactory.HEART_HALF;
import static org.savilusGame.config.GameEntityNameFactory.POWER_UP;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObjectType;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class Heart extends GameEntity {

  private static final String LIFE = "life";
  private static final String UI_MESSAGES = "uiMessages";

  public Heart(GamePanel gamePanel) {
    super(gamePanel);
    type = WorldGameTypes.PICK_UP;
    name = GameObjectType.HEART.getName();
    value = 2;
    down1 = setup(HEART_FULL, TILE_SIZE, TILE_SIZE);

    name = GameObjectType.HEART.getName();
    image = setup(HEART_FULL, TILE_SIZE, TILE_SIZE);
    image2 = setup(HEART_HALF, TILE_SIZE, TILE_SIZE);
    image3 = setup(HEART_BLANK, TILE_SIZE, TILE_SIZE);
  }

  @Override
  public boolean use(GameEntity entity) {
    gamePanel.playSoundEffect(POWER_UP);
    gamePanel.getUi().addMessage(TextManager.getUiText(UI_MESSAGES, LIFE) + value);
    entity.currentLife += value;
    return true;
  }
}
