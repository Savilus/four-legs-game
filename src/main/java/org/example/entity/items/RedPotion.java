package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.POWER_UP;
import static org.example.config.GameEntityNameFactory.RED_POTION;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;
import org.example.utils.text.TextManager;

public class RedPotion extends GameEntity {

  private static final String USE = "use";

  private final int healingValue = 5;

  public RedPotion(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.RED_POTION.getName();
    dialogues = TextManager.getItemTexts(GameObjectType.RED_POTION.getTextKey(), name);
    type = WorldGameTypes.CONSUMABLE;
    stackable = true;
    image = setup(RED_POTION, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(RED_POTION, gamePanel.tileSize, gamePanel.tileSize);
    description = String.format(TextManager.getItemDescription(GameObjectType.RED_POTION.getTextKey()), name, healingValue);
    price = 3;
  }

  @Override
  public boolean use(GameEntity gameEntity) {
    startDialogue(this, USE);
    gameEntity.currentLife += healingValue;
    gamePanel.playSoundEffect(POWER_UP);
    return true;
  }
}
