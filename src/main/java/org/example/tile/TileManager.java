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
  private final String mapPath = "/maps/map01.txt";

  public TileManager(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    tile = new Tile[10];
    mapTileNum = new int[gamePanel.maxScreenColumn][gamePanel.maxScreenRow];

    getTileImage();
    loadMap(mapPath);
  }

  public void getTileImage() {
    try {
      tile[0] = new Tile();
      tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));

      tile[1] = new Tile();
      tile[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));

      tile[2] = new Tile();
      tile[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));

      tile[3] = new Tile();
      tile[3].image = ImageIO.read(getClass().getResourceAsStream("/tiles/sand.png"));

      tile[4] = new Tile();
      tile[4].image = ImageIO.read(getClass().getResourceAsStream("/tiles/tree.png"));

      tile[5] = new Tile();
      tile[5].image = ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));

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

      while (col < gamePanel.maxScreenColumn && row < gamePanel.maxScreenRow) {
        String line = br.readLine();

        while (col < gamePanel.maxScreenColumn) {

          String[] numbers = line.split(" ");

          int number = Integer.parseInt(numbers[col]);
          mapTileNum[col][row] = number;
          col++;
        }

        if (col == gamePanel.maxScreenColumn) {
          col = 0;
          row++;
        }
        br.close();
      }
    } catch (Exception e) {

    }
  }

  public void draw(Graphics2D graphics2D) {
    int col = 0;
    int row = 0;
    int x = 0;
    int y = 0;

    while (col < gamePanel.maxScreenColumn && row < gamePanel.maxScreenRow) {

      int tileNum = mapTileNum[col][row];

      graphics2D.drawImage(tile[tileNum].image, x, y, gamePanel.tileSize, gamePanel.tileSize, null);
      col++;
      x += gamePanel.tileSize;

      if (col == gamePanel.maxScreenColumn) {
        col = 0;
        x = 0;
        row++;
        y += gamePanel.tileSize;
      }
    }
  }
}
