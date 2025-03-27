package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.KEY;
import static org.example.config.GameEntityNameFactory.UNLOCK;
import static org.example.enums.WorldGameTypes.CONSUMABLE;
import static org.example.utils.CollisionDetector.INIT_INDEX;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.utils.text.TextManager;

public class Key extends GameEntity {

  private static final String OPEN_DOOR_TEXT_KEY = "openDoor";
  private static final String WRONG_USE_TEXT_KEY = "wrongUse";

  public Key(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.KEY.getName();
    type = CONSUMABLE;
    stackable = true;
    image = setup(KEY, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(KEY, gamePanel.tileSize, gamePanel.tileSize);
    description = String.format(TextManager.getItemDescription(GameObjectType.KEY.getTextKey()), name);
    dialogues = TextManager.getItemTexts(GameObjectType.KEY.getTextKey(), name);
    price = 1;
  }

  @Override
  public boolean use(GameEntity gameEntity) {
    int objIndex = getDetected(gameEntity, gamePanel.mapsObjects, GameObjectType.DOOR.name());

    if (objIndex != INIT_INDEX) {
      startDialogue(this, OPEN_DOOR_TEXT_KEY);
      gamePanel.playSoundEffect(UNLOCK);
      gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[objIndex] = null;
      return true;
    } else {
      startDialogue(this, WRONG_USE_TEXT_KEY);
      return false;
    }
  }
}
