package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.IRON_DOOR;
import static org.example.enums.WorldGameTypes.OBSTACLE;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.utils.text.TextManager;

public class IronDoor extends GameEntity {

  private static final String IRON_DOOR_DIALOGUE_KEY = "ironDoor";
  private static final String OPEN_INFORMATION = "openInformation";

  public IronDoor(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.IRON_DOOR.getName();
    image = setup(IRON_DOOR, gamePanel.tileSize, gamePanel.tileSize);
    collision = true;
    type = OBSTACLE;

    solidArea.x = 0;
    solidArea.y = 16;
    solidArea.width = 48;
    solidArea.height = 32;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    dialogues = TextManager.getItemTexts(IRON_DOOR_DIALOGUE_KEY);
  }

  @Override
  public void interact() {
    startDialogue(this, OPEN_INFORMATION);
  }
}
