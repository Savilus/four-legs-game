package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.CHEST;
import static org.example.config.GameEntityNameFactory.CHEST_OPENED;
import static org.example.config.GameEntityNameFactory.UNLOCK;
import static org.example.enums.WorldGameTypes.OBSTACLE;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.utils.text.TextManager;

public class Chest extends GameEntity {

  private static final String FULL_INVENTORY_TEXT_KEY = "fullInventory";
  private static final String COLLECT_ITEM_TEXT_KEY = "collectItem";
  private static final String EMPTY_TEXT_KEY = "empty";

  public Chest(GamePanel gamePanel) {
    super(gamePanel);
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

  public void setLoot(GameEntity loot) {
    this.loot = loot;
    dialogues = TextManager.getItemTexts(GameObjectType.CHEST.getTextKey(), loot.name);
  }

  @Override
  public void interact() {

    if (!opened) {
      gamePanel.playSoundEffect(UNLOCK);
      if (!gamePanel.player.canObtainItem(loot)) {
        startDialogue(this, FULL_INVENTORY_TEXT_KEY);
      } else {
        startDialogue(this, COLLECT_ITEM_TEXT_KEY);
        image = image2;
        opened = true;
      }
    } else {
      startDialogue(this, EMPTY_TEXT_KEY);
    }
  }
}
