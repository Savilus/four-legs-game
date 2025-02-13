package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.MANA_CRYSTAL_BLANK;
import static org.example.config.GameEntityNameFactory.MANA_CRYSTAL_FULL;

import org.example.GamePanel;
import org.example.entity.GameEntity;

public class ManaCrystalObject extends GameEntity {

  public ManaCrystalObject(GamePanel gamePanel) {
    super(gamePanel);
    name = "Mana Crystal";
    image = setup(MANA_CRYSTAL_FULL, gamePanel.tileSize, gamePanel.tileSize);
    image2 = setup(MANA_CRYSTAL_BLANK, gamePanel.tileSize, gamePanel.tileSize);
  }
}
