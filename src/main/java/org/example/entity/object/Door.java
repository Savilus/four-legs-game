package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.DOOR;
import static org.example.enums.GameStateType.DIALOG_STATE;
import static org.example.enums.WorldGameTypes.OBSTACLE;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class Door extends GameEntity {

  private static final String OPEN_INFORMATION = "You need a key to open this door.";

  public Door(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.DOOR.name();
    image = setup(DOOR, gamePanel.tileSize, gamePanel.tileSize);
    collision = true;
    type = OBSTACLE;

    solidArea.x = 0;
    solidArea.y = 16;
    solidArea.width = 48;
    solidArea.height = 32;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
  }

  @Override
  public void interact() {
    gamePanel.gameState = DIALOG_STATE;
    gamePanel.ui.currentDialogue = OPEN_INFORMATION;
  }
}
