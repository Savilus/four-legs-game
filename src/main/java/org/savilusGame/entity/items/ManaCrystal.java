package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.MANA_CRYSTAL_BLANK;
import static org.savilusGame.config.GameEntityNameFactory.MANA_CRYSTAL_FULL;
import static org.savilusGame.config.GameEntityNameFactory.POWER_UP;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class ManaCrystal extends GameEntity {

  private static final String MANA = "mana";
  private static final String UI_MESSAGES = "uiMessages";

  public ManaCrystal(GamePanel gamePanel) {
    super(gamePanel);
    type = WorldGameTypes.PICK_UP;
    name = GameObject.MANA_CRYSTAL.getName();
    value = 1;
    down1 = setup(MANA_CRYSTAL_FULL, TILE_SIZE, TILE_SIZE);
    image = setup(MANA_CRYSTAL_FULL, TILE_SIZE, TILE_SIZE);
    image2 = setup(MANA_CRYSTAL_BLANK, TILE_SIZE, TILE_SIZE);
  }

  @Override
  public boolean use(GameEntity entity) {
    gamePanel.playSoundEffect(POWER_UP);
    gamePanel.getUi().addMessage(TextManager.getUiText(UI_MESSAGES, MANA) + value);
    entity.mana += value;
    return true;
  }
}
