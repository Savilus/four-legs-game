package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.MANA_CRYSTAL_BLANK;
import static org.example.config.GameEntityNameFactory.MANA_CRYSTAL_FULL;
import static org.example.config.GameEntityNameFactory.POWER_UP;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.WorldGameTypes;

public class ManaCrystalObject extends GameEntity {

  public ManaCrystalObject(GamePanel gamePanel) {
    super(gamePanel);
    type = WorldGameTypes.PICK_UP;
    name = "Mana Crystal";
    value = 1;
    down1 = setup(MANA_CRYSTAL_FULL, gamePanel.tileSize, gamePanel.tileSize);
    image = setup(MANA_CRYSTAL_FULL, gamePanel.tileSize, gamePanel.tileSize);
    image2 = setup(MANA_CRYSTAL_BLANK, gamePanel.tileSize, gamePanel.tileSize);
  }

  @Override
  public boolean use(GameEntity entity) {
    gamePanel.playSoundEffect(POWER_UP);
    gamePanel.ui.addMessage("Mana +" + value);
    entity.mana += value;
    return true;
  }
}
