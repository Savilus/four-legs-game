package org.example.tile;

import static org.example.config.GameEntityNameFactory.EARTH;
import static org.example.config.GameEntityNameFactory.GRASS0;
import static org.example.config.GameEntityNameFactory.GRASS1;
import static org.example.config.GameEntityNameFactory.MAP_PATH;
import static org.example.config.GameEntityNameFactory.ROAD0;
import static org.example.config.GameEntityNameFactory.ROAD1;
import static org.example.config.GameEntityNameFactory.ROAD10;
import static org.example.config.GameEntityNameFactory.ROAD11;
import static org.example.config.GameEntityNameFactory.ROAD12;
import static org.example.config.GameEntityNameFactory.ROAD2;
import static org.example.config.GameEntityNameFactory.ROAD3;
import static org.example.config.GameEntityNameFactory.ROAD4;
import static org.example.config.GameEntityNameFactory.ROAD5;
import static org.example.config.GameEntityNameFactory.ROAD6;
import static org.example.config.GameEntityNameFactory.ROAD7;
import static org.example.config.GameEntityNameFactory.ROAD8;
import static org.example.config.GameEntityNameFactory.ROAD9;
import static org.example.config.GameEntityNameFactory.TREE;
import static org.example.config.GameEntityNameFactory.WALL;
import static org.example.config.GameEntityNameFactory.WATER0;
import static org.example.config.GameEntityNameFactory.WATER1;
import static org.example.config.GameEntityNameFactory.WATER10;
import static org.example.config.GameEntityNameFactory.WATER11;
import static org.example.config.GameEntityNameFactory.WATER12;
import static org.example.config.GameEntityNameFactory.WATER13;
import static org.example.config.GameEntityNameFactory.WATER2;
import static org.example.config.GameEntityNameFactory.WATER3;
import static org.example.config.GameEntityNameFactory.WATER4;
import static org.example.config.GameEntityNameFactory.WATER5;
import static org.example.config.GameEntityNameFactory.WATER6;
import static org.example.config.GameEntityNameFactory.WATER7;
import static org.example.config.GameEntityNameFactory.WATER8;
import static org.example.config.GameEntityNameFactory.WATER9;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.GamePanel;
import org.example.utils.UtilityTool;

public class TileManager {

  GamePanel gamePanel;
  public Tile[] tile;
  public int[][] mapTileNum;

  public TileManager(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    tile = new Tile[50];
    mapTileNum = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];

    getTileImage();
    loadMap(MAP_PATH);
  }

  public void getTileImage() {

    setUp(10, GRASS0, false);
    setUp(11, GRASS1, false);
    setUp(12, WATER0, true);
    setUp(13, WATER1, true);
    setUp(14, WATER2, true);
    setUp(15, WATER3, true);
    setUp(16, WATER4, true);
    setUp(17, WATER5, true);
    setUp(18, WATER6, true);
    setUp(19, WATER7, true);
    setUp(20, WATER8, true);
    setUp(21, WATER9, true);
    setUp(22, WATER10, true);
    setUp(23, WATER11, true);
    setUp(24, WATER12, true);
    setUp(25, WATER13, true);
    setUp(26, ROAD0, false);
    setUp(27, ROAD1, false);
    setUp(28, ROAD2, false);
    setUp(29, ROAD3, false);
    setUp(30, ROAD4, false);
    setUp(31, ROAD5, false);
    setUp(32, ROAD6, false);
    setUp(33, ROAD7, false);
    setUp(34, ROAD8, false);
    setUp(35, ROAD9, false);
    setUp(36, ROAD10, false);
    setUp(37, ROAD11, false);
    setUp(38, ROAD12, false);
    setUp(39, EARTH, false);
    setUp(40, WALL, true);
    setUp(41, TREE, true);

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
        graphics2D.drawImage(tile[tileNum].image, screenX, screenY, null);
      }
      worldCol++;

      if (worldCol == gamePanel.maxWorldCol) {
        worldCol = 0;
        worldRow++;
      }
    }
  }

  private void setUp(int index, String imageName, boolean collision) {
    if (index <= 9) {
      return;
    }
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
