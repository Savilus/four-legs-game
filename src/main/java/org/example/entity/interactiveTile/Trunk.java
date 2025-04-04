package org.example.entity.interactiveTile;

import static org.example.config.GameEntityNameFactory.TRUNK;

import org.example.GamePanel;

public class Trunk extends InteractiveTile {

  public Trunk(GamePanel gamePanel, int col, int row) {
    super(gamePanel);

    this.worldX = gamePanel.tileSize * col;
    this.worldY = gamePanel.tileSize * row;

    image = setup(TRUNK, gamePanel.tileSize, gamePanel.tileSize);

    solidArea.x = 0;
    solidArea.y = 0;
    solidArea.width = 0;
    solidArea.height = 0;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;

  }
}
