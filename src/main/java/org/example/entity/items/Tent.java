package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.SLEEP;
import static org.example.config.GameEntityNameFactory.TENT;
import static org.example.enums.GameStateType.SLEEP_STATE;
import static org.example.enums.WorldGameTypes.CONSUMABLE;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.utils.text.TextManager;

public class Tent extends GameEntity {

  public Tent(GamePanel gamePanel) {
    super(gamePanel);
    type = CONSUMABLE;
    name = GameObjectType.TENT.getName();
    image = setup(TENT, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(TENT, gamePanel.tileSize, gamePanel.tileSize);
    description = String.format(TextManager.getItemDescription(GameObjectType.TENT.getTextKey()), name);
    price = 200;
    stackable = true;
  }

  @Override
  public boolean use(GameEntity gameEntity) {
    gamePanel.gameState = SLEEP_STATE;
    gamePanel.playSoundEffect(SLEEP);
    gamePanel.player.currentLife = gamePanel.player.maxLife;
    gamePanel.player.mana = gamePanel.player.maxMana;
    gamePanel.player.getSleepingImage(image);
    return true;
  }
}
