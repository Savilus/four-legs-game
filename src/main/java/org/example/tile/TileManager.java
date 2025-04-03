package org.example.tile;

import static org.example.config.GameEntityNameFactory.BLACK_TAIL;
import static org.example.config.GameEntityNameFactory.DUNGEON_FIRST_FLOR;
import static org.example.config.GameEntityNameFactory.DUNGEON_SECOND_FLOR;
import static org.example.config.GameEntityNameFactory.EARTH;
import static org.example.config.GameEntityNameFactory.FLOOR;
import static org.example.config.GameEntityNameFactory.GRASS0;
import static org.example.config.GameEntityNameFactory.GRASS1;
import static org.example.config.GameEntityNameFactory.HUT;
import static org.example.config.GameEntityNameFactory.INTERIOR_MAP;
import static org.example.config.GameEntityNameFactory.MAIN_MAP;
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
import static org.example.config.GameEntityNameFactory.STAIRS_DOWN;
import static org.example.config.GameEntityNameFactory.STAIRS_UP;
import static org.example.config.GameEntityNameFactory.TABLE;
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
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.GamePanel;
import org.example.utils.UtilityTool;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TileManager {
  private static TileManager instance;
  GamePanel gamePanel;
  public String currentMap;
  public Tile[] tile;
  public Map<String, int[][]> gameMaps = new HashMap<>();
  boolean drawPath = true;

  private TileManager(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    this.currentMap = DUNGEON_FIRST_FLOR;
    tile = new Tile[50];

    getTileImage();
    loadMap(INTERIOR_MAP);
    loadMap(MAIN_MAP);
    loadMap(DUNGEON_FIRST_FLOR);
    loadMap(DUNGEON_SECOND_FLOR);
  }

  public static TileManager getInstance(GamePanel gamePanel) {
    if (instance == null) {
      instance = new TileManager(gamePanel);
    }
    return instance;
  }

  public void getTileImage() {

    setUp(49, BLACK_TAIL, true);
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
    setUp(42, HUT, false);
    setUp(43, FLOOR, false);
    setUp(44, TABLE, true);
    setUp(45, STAIRS_DOWN, false);
    setUp(46, STAIRS_UP, false);
  }

  public void loadMap(String mapPath) {
    if (!gameMaps.containsKey(mapPath)) {
      gameMaps.put(mapPath, new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow]);
      Try.withResources(() -> getClass().getResourceAsStream(mapPath))
          .of(is -> {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            int col = 0, row = 0;

            while (row < gamePanel.maxWorldRow) {
              String line = br.readLine();
              if (line == null) break;

              String[] numbers = line.split(" ");

              while (col < gamePanel.maxWorldCol) {
                int number = Integer.parseInt(numbers[col]);
                gameMaps.get(mapPath)[col][row] = number;
                col++;
              }

              col = 0;
              row++;
            }
            return mapPath;
          })
          .onFailure(error -> log.error("Error while loading tile map: {}", error.getMessage()));
    }
  }

  public void draw(Graphics2D graphics2D) {
    int worldCol = 0;
    int worldRow = 0;

    while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {

      int tileNum = gameMaps.get(currentMap)[worldCol][worldRow];

      int worldX = worldCol * gamePanel.tileSize;
      int worldY = worldRow * gamePanel.tileSize;
      int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
      int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

      if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
          worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
          worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
          worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {
        if(Objects.nonNull(tile[tileNum])){
          graphics2D.drawImage(tile[tileNum].image(), screenX, screenY, null);
        }
      }
      worldCol++;

      if (worldCol == gamePanel.maxWorldCol) {
        worldCol = 0;
        worldRow++;
      }
    }

    if (drawPath) {
      graphics2D.setColor(new Color(255, 0, 0, 70));
      for (int i = 0; i < gamePanel.pathFinder.pathList.size(); i++) {
        int worldX = gamePanel.pathFinder.pathList.get(i).col * gamePanel.tileSize;
        int worldY = gamePanel.pathFinder.pathList.get(i).row * gamePanel.tileSize;
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

        graphics2D.fillRect(screenX, screenY, gamePanel.tileSize, gamePanel.tileSize);
      }
    }
  }

  private void setUp(int index, String imageName, boolean collision) {
    if (index <= 9) {
      return;
    }
    Try.run(() -> {
      tile[index] = new Tile(
          UtilityTool.scaleImage(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imageName))),
              gamePanel.tileSize, gamePanel.tileSize), collision
      );
    }).onFailure(error -> log.error("Error while setting up tile: {}", error.getMessage()));
  }
}
