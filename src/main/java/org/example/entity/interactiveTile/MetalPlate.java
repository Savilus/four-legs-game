package org.example.entity.interactiveTile;

import static org.example.config.GameEntityNameFactory.METAL_PLATE;

import org.example.GamePanel;
import org.example.enums.GameObjectType;

public class MetalPlate extends InteractiveTile {

  public MetalPlate(GamePanel gamePanel, int col, int row) {
    super(gamePanel);

    this.worldX = gamePanel.tileSize * col;
    this.worldY = gamePanel.tileSize * row;

    name = GameObjectType.METAL_PLATE.getName();
    image = setup(METAL_PLATE, gamePanel.tileSize, gamePanel.tileSize);

    solidArea.x = 0;
    solidArea.y = 0;
    solidArea.width = 0;
    solidArea.height = 0;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;

  }
}
