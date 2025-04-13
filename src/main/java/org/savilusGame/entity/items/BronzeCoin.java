package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.BRONZE_COIN;
import static org.savilusGame.config.GameEntityNameFactory.COIN;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.enums.WorldGameTypes;

public class BronzeCoin extends GameEntity {
  private static final String COIN_TEXT = "Coin ";

  public BronzeCoin(GamePanel gamePanel) {
    super(gamePanel);

    type = WorldGameTypes.PICK_UP;
    name = GameObject.BRONZE_COIN.getName();
    value = 1;
    image = setup(BRONZE_COIN, TILE_SIZE, TILE_SIZE);
    down1 = setup(BRONZE_COIN, TILE_SIZE, TILE_SIZE);
  }

  @Override
  public boolean use(GameEntity entity) {
    gamePanel.playSoundEffect(COIN);
    gamePanel.getUi().addMessage(COIN_TEXT + value);
    gamePanel.getPlayer().money += value;
    return true;
  }
}
