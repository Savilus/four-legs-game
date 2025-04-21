package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.CHEST;
import static org.savilusGame.config.GameEntityNameFactory.CHEST_OPENED;
import static org.savilusGame.config.GameEntityNameFactory.UNLOCK;
import static org.savilusGame.enums.WorldGameTypes.OBSTACLE;

import java.util.ArrayList;
import java.util.List;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.utils.text.TextManager;

import lombok.Getter;

@Getter
public class Chest extends GameEntity {

  private static final String FULL_INVENTORY_TEXT_KEY = "fullInventory";
  private static final String COLLECT_ITEM_TEXT_KEY = "collectItem";
  private static final String EMPTY_TEXT_KEY = "empty";

  public Chest(GamePanel gamePanel) {
    super(gamePanel);
    type = OBSTACLE;
    name = GameObject.CHEST.getName();
    mainImage = setup(CHEST, TILE_SIZE, TILE_SIZE);
    mainImage2 = setup(CHEST_OPENED, TILE_SIZE, TILE_SIZE);
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
    return this;
  }

  @Override
  public void interact() {
    if (!opened) {
      dialogues = TextManager.getItemTexts(GameObject.CHEST.getTextKey(), this.loot.getName());
      gamePanel.playSoundEffect(UNLOCK);
      if (!gamePanel.getPlayer().canObtainItem(loot)) {
        startDialogue(this, FULL_INVENTORY_TEXT_KEY);
      } else {
        startDialogue(this, COLLECT_ITEM_TEXT_KEY);
        mainImage = mainImage2;
        opened = true;
      }
    } else {
      startDialogue(this, EMPTY_TEXT_KEY);
    }
  }
}
