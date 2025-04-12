package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.CHEST;
import static org.savilusGame.config.GameEntityNameFactory.CHEST_OPENED;
import static org.savilusGame.config.GameEntityNameFactory.UNLOCK;
import static org.savilusGame.enums.WorldGameTypes.OBSTACLE;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObjectType;
import org.savilusGame.utils.text.TextManager;

public class Chest extends GameEntity {

  private static final String FULL_INVENTORY_TEXT_KEY = "fullInventory";
  private static final String COLLECT_ITEM_TEXT_KEY = "collectItem";
  private static final String EMPTY_TEXT_KEY = "empty";

  public Chest(GamePanel gamePanel) {
    super(gamePanel);
    type = OBSTACLE;
    name = GameObjectType.CHEST.getName();
    image = setup(CHEST, TILE_SIZE, TILE_SIZE);
    image2 = setup(CHEST_OPENED, TILE_SIZE, TILE_SIZE);
    collision = true;

    solidArea.x = 4;
    solidArea.y = 16;
    solidArea.width = 40;
    solidArea.height = 32;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
  }

  public Chest setLoot(GameEntity loot) {
    this.loot = loot;
    dialogues = TextManager.getItemTexts(GameObjectType.CHEST.getTextKey(), loot.name);
    return this;
  }

  @Override
  public void interact() {
    if (!opened) {
      gamePanel.playSoundEffect(UNLOCK);
      if (!gamePanel.getPlayer().canObtainItem(loot)) {
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
