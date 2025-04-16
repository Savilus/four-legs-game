package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.DOOR;
import static org.savilusGame.enums.WorldGameTypes.OBSTACLE;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.utils.text.TextManager;

public class Door extends GameEntity {

  private static final String DOOR_DIALOGUE_KEY = "door";
  private static final String OPEN_INFORMATION = "openInformation";

  public Door(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObject.DOOR.name();
    mainImage = setup(DOOR, TILE_SIZE, TILE_SIZE);
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
