package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.SLEEP;
import static org.savilusGame.config.GameEntityNameFactory.TENT;
import static org.savilusGame.enums.GameState.SLEEP_STATE;
import static org.savilusGame.enums.WorldGameTypes.CONSUMABLE;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.utils.text.TextManager;

public class Tent extends GameEntity {

  public Tent(GamePanel gamePanel) {
    super(gamePanel);
    type = CONSUMABLE;
    name = GameObject.TENT.getName();
    mainImage = setup(TENT, TILE_SIZE, TILE_SIZE);
    down1 = setup(TENT, TILE_SIZE, TILE_SIZE);
    description = String.format(TextManager.getItemDescription(GameObject.TENT.getTextKey()), name);
    price = 200;
    stackable = true;
  }

  @Override
  public boolean use(GameEntity gameEntity) {
    gamePanel.setGameState(SLEEP_STATE);
    gamePanel.playSoundEffect(SLEEP);
    gamePanel.getPlayer().setCurrentLife(gamePanel.getPlayer().getMaxLife());
    gamePanel.getPlayer().setMana(gamePanel.getPlayer().getMaxMana());
    gamePanel.getPlayer().getSleepingImage(mainImage);
    return true;
  }
}
