package org.savilusGame.entity.items;

import static org.savilusGame.config.GameEntityNameFactory.SLEEP;
import static org.savilusGame.config.GameEntityNameFactory.TENT;
import static org.savilusGame.enums.GameStateType.SLEEP_STATE;
import static org.savilusGame.enums.WorldGameTypes.CONSUMABLE;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObjectType;
import org.savilusGame.utils.text.TextManager;

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
