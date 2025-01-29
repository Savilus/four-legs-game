package org.example.tile;

import static org.example.config.GameNameFactory.EARTH;
import static org.example.config.GameNameFactory.GRASS;
import static org.example.config.GameNameFactory.MAP_PATH;
import static org.example.config.GameNameFactory.SAND;
import static org.example.config.GameNameFactory.TREE;
import static org.example.config.GameNameFactory.WALL;
import static org.example.config.GameNameFactory.WATER;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.GamePanel;
import org.example.UtilityTool;

public class TileManager {

  GamePanel gamePanel;
  public Tile[] tile;
  public int[][] mapTileNum;

  public TileManager(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    tile = new Tile[10];
    mapTileNum = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];

    getTileImage();
    loadMap(MAP_PATH);
  }

  public void getTileImage() {
    setUp(0, GRASS, false);
    setUp(1, WALL, true);
    setUp(2, WATER, true);
    setUp(3, EARTH, false);
    setUp(4, TREE, true);
    setUp(5, SAND, false);
  }

  public void loadMap(String mapPath) {
    try {
      InputStream is = getClass().getResourceAsStream(mapPath);
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      int col = 0;
      int row = 0;

      while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
        String line = br.readLine();

        while (col < gamePanel.maxWorldCol) {

          String[] numbers = line.split(" ");

          int number = Integer.parseInt(numbers[col]);
          mapTileNum[col][row] = number;
          col++;
        }

        if (col == gamePanel.maxWorldCol) {
          col = 0;
          row++;
        }
      }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void draw(Graphics2D graphics2D) {
    int worldCol = 0;
    int worldRow = 0;

    while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {

      int tileNum = mapTileNum[worldCol][worldRow];

      int worldX = worldCol * gamePanel.tileSize;
      int worldY = worldRow * gamePanel.tileSize;
      int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
      int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

      if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
          worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
          worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
          worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {
        graphics2D.drawImage(tile[tileNum].image, screenX, screenY,null);
      }
      worldCol++;

      if (worldCol == gamePanel.maxWorldCol) {
        worldCol = 0;
        worldRow++;
      }
    }
  }

  private void setUp(int index, String imageName, boolean collision) {
    UtilityTool utilityTool = new UtilityTool();
    try {
      tile[index] = new Tile();
      tile[index].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imageName)));
      tile[index].image = utilityTool.scaleImage(tile[index].image, gamePanel.tileSize, gamePanel.tileSize);
      tile[index].collision = collision;
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
