package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.KEY;
import static org.savilusGame.config.GameEntityNameFactory.UNLOCK;
import static org.savilusGame.enums.WorldGameTypes.CONSUMABLE;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;
import static org.savilusGame.utils.CollisionDetector.INIT_INDEX;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.utils.text.TextManager;

public class Key extends GameEntity {

  private static final String OPEN_DOOR_TEXT_KEY = "openDoor";
  private static final String WRONG_USE_TEXT_KEY = "wrongUse";

  public Key(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObject.KEY.getName();
    type = CONSUMABLE;
    stackable = true;
    mainImage = setup(KEY, TILE_SIZE, TILE_SIZE);
    down1 = setup(KEY, TILE_SIZE, TILE_SIZE);
    description = String.format(TextManager.getItemDescription(GameObject.KEY.getTextKey()), name);
    dialogues = TextManager.getItemTexts(GameObject.KEY.getTextKey(), name);
    price = 1;
  }

  @Override
  public boolean use(GameEntity gameEntity) {
    int objIndex = getDetected(gameEntity, gamePanel.getMapsObjects(), GameObject.DOOR.name());

    if (objIndex != INIT_INDEX) {
      startDialogue(this, OPEN_DOOR_TEXT_KEY);
      gamePanel.playSoundEffect(UNLOCK);
      gamePanel.getMapsObjects().get(CURRENT_MAP)[objIndex] = null;
      return true;
    } else {
      startDialogue(this, WRONG_USE_TEXT_KEY);
      return false;
    }
  }
}
