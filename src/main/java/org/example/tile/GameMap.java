package org.example.tile;

import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.example.GamePanel;

public class GameMap {
  private final static String CLOSE_MAP_TEXT = "Press M to close.";

  private final GamePanel gamePanel;
  private final TileManager tileManager;
  Map<String, BufferedImage> worldGameMap = new HashMap<>();
  public boolean miniMapOn = false;

  public GameMap(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    this.tileManager = TileManager.getInstance(gamePanel);
    createWorldMap();
  }

  public void createWorldMap() {
    int worldMapWidth = gamePanel.tileSize * gamePanel.maxWorldCol;
    int worldMapHeight = gamePanel.tileSize * gamePanel.maxWorldRow;

    worldGameMap.put(tileManager.currentMap, new BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB));
    var graphics2D = worldGameMap.get(tileManager.currentMap).createGraphics();

    int col = 0;
    int row = 0;

    while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
      int tileNum = tileManager.gameMaps.get(tileManager.currentMap)[col][row];
      int x = gamePanel.tileSize * col;
      int y = gamePanel.tileSize * row;
      graphics2D.drawImage(tileManager.tile[tileNum].image(), x, y, null);

      col++;
      if (col == gamePanel.maxWorldCol) {
        col = 0;
        row++;
      }
    }
    graphics2D.dispose();
  }

  public void drawFullMapScreen(Graphics2D graphics2D) {
    graphics2D.setColor(BLACK);
    graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

    // Draw map
    int width = 500;
    int height = 500;
    int x = gamePanel.screenWidth / 2 - width / 2;
    int y = gamePanel.screenHeight / 2 - height / 2;
    graphics2D.drawImage(worldGameMap.get(tileManager.currentMap), x, y, width, height, null);
    System.out.println(tileManager.currentMap);
    // Draw Player
    double scale = (double) (gamePanel.tileSize * gamePanel.maxWorldCol) / width;
    int playerX = (int) (x + gamePanel.player.worldX / scale);
    int playerY = (int) (y + gamePanel.player.worldY / scale);
    int playerSize = gamePanel.tileSize / 3;
    graphics2D.drawImage(gamePanel.player.down1, playerX, playerY, playerSize, playerSize, null);

    // Hint
    graphics2D.setFont(gamePanel.ui.maruMonica.deriveFont(32F));
    graphics2D.setColor(WHITE);
    graphics2D.drawString(CLOSE_MAP_TEXT, 750, 550);

  }

  public void drawMiniMap(Graphics2D graphics2D) {
    if (miniMapOn) {
      int diameter = 200;
      int x = gamePanel.screenWidth - diameter - 15;
      int y = 15;

      Shape originalClip = graphics2D.getClip();

      Ellipse2D circleClip = new Ellipse2D.Float(x, y, diameter, diameter);
      graphics2D.setClip(circleClip);

      graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8F));
      graphics2D.drawImage(worldGameMap.get(tileManager.currentMap), x, y, diameter, diameter, null);

      // Draw player
      double scale = (double) (gamePanel.tileSize * gamePanel.maxWorldCol) / diameter;
      int playerX = (int) (x + gamePanel.player.worldX / scale);
      int playerY = (int) (y + gamePanel.player.worldY / scale);
      int playerSize = gamePanel.tileSize / 3;
      graphics2D.drawImage(gamePanel.player.down1, playerX - 6, playerY - 6, playerSize, playerSize, null);

      graphics2D.setClip(originalClip);

      graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));

      graphics2D.setColor(Color.WHITE);
      graphics2D.setStroke(new BasicStroke(3));
      graphics2D.draw(circleClip);
    }
  }

}
