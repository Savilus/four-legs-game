package org.example.entity.items;

import static org.example.config.GameEntityNameFactory.MANA_CRYSTAL_BLANK;
import static org.example.config.GameEntityNameFactory.MANA_CRYSTAL_FULL;
import static org.example.config.GameEntityNameFactory.POWER_UP;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;
import org.example.utils.text.TextManager;

public class ManaCrystal extends GameEntity {

  private static final String MANA = "mana";
  private static final String UI_MESSAGES = "uiMessages";

  public ManaCrystal(GamePanel gamePanel) {
    super(gamePanel);
    type = WorldGameTypes.PICK_UP;
    name = GameObjectType.MANA_CRYSTAL.getName();
    value = 1;
    down1 = setup(MANA_CRYSTAL_FULL, gamePanel.tileSize, gamePanel.tileSize);
    image = setup(MANA_CRYSTAL_FULL, gamePanel.tileSize, gamePanel.tileSize);
    image2 = setup(MANA_CRYSTAL_BLANK, gamePanel.tileSize, gamePanel.tileSize);
  }

  @Override
  public boolean use(GameEntity entity) {
    gamePanel.playSoundEffect(POWER_UP);
    gamePanel.ui.addMessage(TextManager.getUiText(UI_MESSAGES, MANA) + value);
    entity.mana += value;
    return true;
  }
}
