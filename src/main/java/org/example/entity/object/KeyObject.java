package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.KEY;
import static org.example.config.GameEntityNameFactory.UNLOCK;
import static org.example.enums.GameStateType.DIALOG_STATE;
import static org.example.enums.WorldGameTypes.CONSUMABLE;
import static org.example.utils.CollisionDetector.INIT_INDEX;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class KeyObject extends GameEntity {

  private static final String OPEN_DOOR = "You use the %s and open the door.";
  private static final String WRONG_USE = "What are you doing?";

  public KeyObject(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.KEY.getName();
    type = CONSUMABLE;
    image = setup(KEY, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(KEY, gamePanel.tileSize, gamePanel.tileSize);
    description = "[" + name + "]\nIt opens a door";
    price = 1;
  }

  @Override
  public boolean use(GameEntity gameEntity) {
    gamePanel.gameState = DIALOG_STATE;

    int objIndex = getDetected(gameEntity, gamePanel.mapsObjects, GameObjectType.DOOR.name());

    if (objIndex != INIT_INDEX) {
      gamePanel.ui.currentDialogue = String.format(OPEN_DOOR, name);
      gamePanel.playSoundEffect(UNLOCK);
      gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[objIndex] = null;
      return true;
    } else {
      gamePanel.ui.currentDialogue = WRONG_USE;
      return false;
    }
  }
}
