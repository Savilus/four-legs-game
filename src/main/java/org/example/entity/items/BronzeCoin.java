package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.BRONZE_COIN;
import static org.example.config.GameEntityNameFactory.COIN;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;

public class BronzeCoin extends GameEntity {
  private static final String COIN_TEXT = "Coin ";

  public BronzeCoin(GamePanel gamePanel) {
    super(gamePanel);

    type = WorldGameTypes.PICK_UP;
    name = GameObjectType.BRONZE_COIN.getName();
    value = 1;
    image = setup(BRONZE_COIN, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(BRONZE_COIN, gamePanel.tileSize, gamePanel.tileSize);
  }

  @Override
  public boolean use(GameEntity entity) {
    gamePanel.playSoundEffect(COIN);
    gamePanel.ui.addMessage(COIN_TEXT + value);
    gamePanel.player.money += value;
    return true;
  }
}
