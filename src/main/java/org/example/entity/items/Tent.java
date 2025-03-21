package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.SLEEP;
import static org.example.config.GameEntityNameFactory.TENT;
import static org.example.enums.GameStateType.SLEEP_STATE;
import static org.example.enums.WorldGameTypes.CONSUMABLE;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;

public class Tent extends GameEntity {

  private static final String TENT_DESCRIPTION = "[Tent] \\n You can sleep until \\nnext morning.";

  public Tent(GamePanel gamePanel) {
    super(gamePanel);
    type = CONSUMABLE;
    name = GameObjectType.TENT.getName();
    image = setup(TENT, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(TENT, gamePanel.tileSize, gamePanel.tileSize);
    description = TENT_DESCRIPTION;
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
