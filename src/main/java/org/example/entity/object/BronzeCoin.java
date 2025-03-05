package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.BRONZE_COIN;
import static org.example.config.GameEntityNameFactory.COIN;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.WorldGameTypes;

public class BronzeCoin extends GameEntity {

  public BronzeCoin(GamePanel gamePanel) {
    super(gamePanel);

    type = WorldGameTypes.PICK_UP;
    name = "Bronze Coin";
    value = 1;
    image = setup(BRONZE_COIN, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(BRONZE_COIN, gamePanel.tileSize, gamePanel.tileSize);
  }

  @Override
  public boolean use(GameEntity entity) {
    gamePanel.playSoundEffect(COIN);
    gamePanel.ui.addMessage("Coin +" + value);
    gamePanel.player.money += value;
    return true;
  }
}
