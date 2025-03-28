package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.DOOR;
import static org.example.enums.WorldGameTypes.OBSTACLE;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.utils.text.TextManager;

public class Door extends GameEntity {

  private static final String DOOR_DIALOGUE_KEY = "door";
  private static final String OPEN_INFORMATION = "openInformation";

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
    dialogues = TextManager.getItemTexts(DOOR_DIALOGUE_KEY);
  }

  @Override
  public void interact() {
    startDialogue(this, OPEN_INFORMATION);
  }
}
