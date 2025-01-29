package org.example.tile;

import static org.example.config.GameNameFactory.EARTH;
import static org.example.config.GameNameFactory.GRASS;
import static org.example.config.GameNameFactory.MAP_PATH;
import static org.example.config.GameNameFactory.SAND;
import static org.example.config.GameNameFactory.TREE;
import static org.example.config.GameNameFactory.WALL;
import static org.example.config.GameNameFactory.WATER;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.GamePanel;

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
    try {
      tile[0] = new Tile();
      tile[0].image = loadImage(GRASS);

      tile[1] = new Tile();
      tile[1].image = loadImage(WALL);
      tile[1].collision = true;

      tile[2] = new Tile();
      tile[2].image = loadImage(WATER);
      tile[2].collision = true;

      tile[3] = new Tile();
      tile[3].image = loadImage(EARTH);

      tile[4] = new Tile();
      tile[4].image = loadImage(TREE);
      tile[4].collision = true;

      tile[5] = new Tile();
      tile[5].image = loadImage(SAND);
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
        graphics2D.drawImage(tile[tileNum].image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
      }
      worldCol++;

      if (worldCol == gamePanel.maxWorldCol) {
        worldCol = 0;
        worldRow++;
      }
    }
  }

  private BufferedImage loadImage(String imageName) throws IOException {
    return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imageName)));
  }
}
