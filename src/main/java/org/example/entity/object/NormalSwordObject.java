package org.example.entity.object;

import static org.example.config.GameEntityNameFactory.NORMAL_SWORD;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.WorldGameTypes;

public class NormalSwordObject extends GameEntity {

  public NormalSwordObject(GamePanel gamePanel) {
    super(gamePanel);
    name = "Normal Sword";
    down1 = setup(NORMAL_SWORD, gamePanel.tileSize, gamePanel.tileSize);
    attackValue = 1;
    attackArea.width = 36;
    attackArea.height = 36;
    description = "[" + name + "]\nAn old sword";
    type = WorldGameTypes.SWORD;

  }
}
