package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.POWER_UP;
import static org.savilusGame.config.GameEntityNameFactory.RED_POTION;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class RedPotion extends GameEntity {

  private static final String USE = "use";

  private final int healingValue = 5;

  public RedPotion(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObject.RED_POTION.getName();
    dialogues = TextManager.getItemTexts(GameObject.RED_POTION.getTextKey(), name);
    type = WorldGameTypes.CONSUMABLE;
    stackable = true;
    mainImage = setup(RED_POTION, TILE_SIZE, TILE_SIZE);
    down1 = setup(RED_POTION, TILE_SIZE, TILE_SIZE);
    description = String.format(TextManager.getItemDescription(GameObject.RED_POTION.getTextKey()), name, healingValue);
    price = 3;
  }

  @Override
  public boolean use(GameEntity gameEntity) {
    startDialogue(this, USE);
    gameEntity.setCurrentLife(gameEntity.getCurrentLife() + healingValue);
    gamePanel.playSoundEffect(POWER_UP);
    return true;
  }
}
