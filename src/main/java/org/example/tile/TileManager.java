package org.example.tile;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import org.example.GamePanel;

public class TileManager {

  GamePanel gamePanel;
  Tile[] tile;
  int[][] mapTileNum;
  private final String mapPath = "/maps/world01.txt";

  public TileManager(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    tile = new Tile[10];
    mapTileNum = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];

    getTileImage();
    loadMap(mapPath);
  }

  public void getTileImage() {
    System.out.println("Image loading started");
    try {
      tile[0] = new Tile();
      tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
      System.out.println("Image loading 0");
      tile[1] = new Tile();
      tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
      System.out.println("Image loading 01");
      tile[2] = new Tile();
      tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
      System.out.println("Image loading 02");
      tile[3] = new Tile();
      tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));
      System.out.println("Image loading 03");
      tile[4] = new Tile();
      tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));
      System.out.println("Image loading 04");
      tile[5] = new Tile();
      tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));
      System.out.println("Image loading finished");
    } catch (IOException e) {
      e.printStackTrace();
    }
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

      graphics2D.drawImage(tile[tileNum].image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
      worldCol++;

      if (worldCol == gamePanel.maxWorldCol) {
        worldCol = 0;
        worldRow++;
      }
    }
  }
}
