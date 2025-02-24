package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.POWER_UP;
import static org.example.config.GameEntityNameFactory.RED_POTION;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameStateType;
import org.example.enums.WorldGameTypes;

public class RedPotionObject extends GameEntity {

  private final int healingValue = 5;

  public RedPotionObject(GamePanel gamePanel) {
    super(gamePanel);
    type = WorldGameTypes.CONSUMABLE;
    name = "Red Potion";
    image = setup(RED_POTION, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(RED_POTION, gamePanel.tileSize, gamePanel.tileSize);
    description = "[" + name + "]\nHeals your life by " + healingValue + ".";
    price = 3;
  }

  @Override
  public void use(GameEntity gameEntity) {
    gamePanel.gameState = GameStateType.DIALOG_STATE;
    gamePanel.ui.currentDialogue = "You drink the " + name + "!\n" +
        "You feel much better!";
    gameEntity.currentLife += healingValue;
    gamePanel.playSoundEffect(POWER_UP);
  }
}
