package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.CHEST;
import static org.example.config.GameEntityNameFactory.CHEST_OPENED;
import static org.example.config.GameEntityNameFactory.UNLOCK;
import static org.example.enums.GameStateType.DIALOG_STATE;
import static org.example.enums.WorldGameTypes.OBSTACLE;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class Chest extends GameEntity {

  private static final String OPEN_CHEST_TEXT = "You opened the chest and find a %s !";
  private static final String EMPTY_CHEST_TEXT = "It's empty.";
  private static final String FULL_INVENTORY_TEXT = "\n You cannot carry any more!";
  private static final String COLLECT_ITEM_TEXT = "\n You obtain the %s !";

  GameEntity loot;
  boolean opened = false;

  public Chest(GamePanel gamePanel, GameEntity loot) {
    super(gamePanel);
    this.loot = loot;
    type = OBSTACLE;
    name = GameObjectType.CHEST.getName();
    image = setup(CHEST, gamePanel.tileSize, gamePanel.tileSize);
    image2 = setup(CHEST_OPENED, gamePanel.tileSize, gamePanel.tileSize);
    collision = true;

    solidArea.x = 4;
    solidArea.y = 16;
    solidArea.width = 40;
    solidArea.height = 32;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
  }

  @Override
  public void interact() {
    gamePanel.gameState = DIALOG_STATE;

    if (!opened) {
      gamePanel.playSoundEffect(UNLOCK);
      StringBuilder sb = new StringBuilder();
      sb.append(OPEN_CHEST_TEXT);

      if (!gamePanel.player.canObtainItem(loot)) {
        sb.append(FULL_INVENTORY_TEXT);
      } else {
        sb.append(COLLECT_ITEM_TEXT);
        image = image2;
        opened = true;
      }
      gamePanel.ui.currentDialogue = String.format(sb.toString(), loot.name, loot.name);
    } else {
      gamePanel.ui.currentDialogue = EMPTY_CHEST_TEXT;
    }
  }
}
