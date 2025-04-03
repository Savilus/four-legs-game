package org.example.utils;

import java.util.Objects;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.DirectionType;

public class CollisionDetector {
  public final static int INIT_INDEX = 999;

  GamePanel gamePanel;

  public CollisionDetector(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void checkTile(GameEntity gameEntity) {
    int entityLeftWorldX = gameEntity.worldX + gameEntity.solidArea.x;
    int entityRightWorldX = gameEntity.worldX + gameEntity.solidArea.x + gameEntity.solidArea.width;
    int entityTopWorldY = gameEntity.worldY + gameEntity.solidArea.y;
    int entityBottomWorldY = gameEntity.worldY + gameEntity.solidArea.y + gameEntity.solidArea.height;

    int entityLeftCol = entityLeftWorldX / gamePanel.tileSize;
    int entityRightCol = entityRightWorldX / gamePanel.tileSize;
    int entityTopRow = entityTopWorldY / gamePanel.tileSize;
    int entityBottomRow = entityBottomWorldY / gamePanel.tileSize;

    int tileNum1, tileNum2;

    // Use a temporal direction when it's being knock backed
    DirectionType direction = gameEntity.direction;
    if (gameEntity.knockBack) {
      direction = gameEntity.knockBackDirection;
    }

    switch (direction) {
      case UP -> {
        entityTopRow = (entityTopWorldY - gameEntity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.gameMaps.get(gamePanel.tileManager.currentMap)[entityLeftCol][entityTopRow];
        tileNum2 = gamePanel.tileManager.gameMaps.get(gamePanel.tileManager.currentMap)[entityRightCol][entityTopRow];
        if (gamePanel.tileManager.tile[tileNum1].collision() || gamePanel.tileManager.tile[tileNum2].collision()) {
          gameEntity.collisionOn = true;
        }
      }
      case DOWN -> {
        entityBottomRow = (entityBottomWorldY + gameEntity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.gameMaps.get(gamePanel.tileManager.currentMap)[entityLeftCol][entityBottomRow];
        tileNum2 = gamePanel.tileManager.gameMaps.get(gamePanel.tileManager.currentMap)[entityRightCol][entityBottomRow];
        if (gamePanel.tileManager.tile[tileNum1].collision() || gamePanel.tileManager.tile[tileNum2].collision()) {
          gameEntity.collisionOn = true;
        }
      }
      case LEFT -> {
        entityLeftCol = (entityLeftWorldX - gameEntity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.gameMaps.get(gamePanel.tileManager.currentMap)[entityLeftCol][entityTopRow];
        tileNum2 = gamePanel.tileManager.gameMaps.get(gamePanel.tileManager.currentMap)[entityLeftCol][entityBottomRow];
        if (gamePanel.tileManager.tile[tileNum1].collision() || gamePanel.tileManager.tile[tileNum2].collision()) {
          gameEntity.collisionOn = true;
        }
      }
      case RIGHT -> {
        entityRightCol = (entityRightWorldX + gameEntity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.gameMaps.get(gamePanel.tileManager.currentMap)[entityLeftCol][entityTopRow];
        tileNum2 = gamePanel.tileManager.gameMaps.get(gamePanel.tileManager.currentMap)[entityRightCol][entityBottomRow];
        if (gamePanel.tileManager.tile[tileNum1].collision() || gamePanel.tileManager.tile[tileNum2].collision()) {
          gameEntity.collisionOn = true;
        }
      }
    }
  }

  public int checkObject(GameEntity gameEntity, boolean player) {
    if (Objects.isNull(gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)))
      return INIT_INDEX;

    int index = INIT_INDEX;

    DirectionType direction = gameEntity.direction;
    if (gameEntity.knockBack) {
      direction = gameEntity.knockBackDirection;
    }

    for (int i = 0; i < gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap).length; i++) {
      if (gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i] != null) {
        //get entity's solid area position
        gameEntity.solidArea.x = gameEntity.worldX + gameEntity.solidArea.x;
        gameEntity.solidArea.y = gameEntity.worldY + gameEntity.solidArea.y;
        // get object's solid area position
        gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].solidArea.x = gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].worldX + gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].solidArea.x;
        gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].solidArea.y = gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].worldY + gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].solidArea.y;
        checkGameEntityCollision(gameEntity, direction);
        if (gameEntity.solidArea.intersects(gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].solidArea)) {
          if (gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].collision) {
            gameEntity.collisionOn = true;
          }
          if (player) {
            index = i;
          }
        }
        gameEntity.solidArea.x = gameEntity.solidAreaDefaultX;
        gameEntity.solidArea.y = gameEntity.solidAreaDefaultY;
        gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].solidArea.x = gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].solidAreaDefaultX;
        gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].solidArea.y = gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].solidAreaDefaultY;
      }
    }

    return index;
  }

  // NPC OR MONSTER COLLISION
  public int checkEntity(GameEntity gameEntity, GameEntity[] target) {
    if (Objects.isNull(target))
      return INIT_INDEX;
    int index = INIT_INDEX;

    // Use a temporal direction when it's being knock backed
    DirectionType direction = gameEntity.direction;
    if (gameEntity.knockBack) {
      direction = gameEntity.knockBackDirection;
    }

    for (int targetIndex = 0; targetIndex < target.length; targetIndex++) {
      if (target[targetIndex] != null) {
        //get entity's solid area position
        gameEntity.solidArea.x = gameEntity.worldX + gameEntity.solidArea.x;
        gameEntity.solidArea.y = gameEntity.worldY + gameEntity.solidArea.y;
        // get target solid area position
        target[targetIndex].solidArea.x = target[targetIndex].worldX + target[targetIndex].solidArea.x;
        target[targetIndex].solidArea.y = target[targetIndex].worldY + target[targetIndex].solidArea.y;
        checkGameEntityCollision(gameEntity, direction);
        if (gameEntity.solidArea.intersects(target[targetIndex].solidArea) && target[targetIndex] != gameEntity) {
          gameEntity.collisionOn = true;
          index = targetIndex;
        }

        gameEntity.solidArea.x = gameEntity.solidAreaDefaultX;
        gameEntity.solidArea.y = gameEntity.solidAreaDefaultY;
        target[targetIndex].solidArea.x = target[targetIndex].solidAreaDefaultX;
        target[targetIndex].solidArea.y = target[targetIndex].solidAreaDefaultY;
      }
    }
    return index;
  }

  public boolean checkPlayer(GameEntity gameEntity) {
    boolean contactPlayer = false;
    //get entity's solid area position
    gameEntity.solidArea.x = gameEntity.worldX + gameEntity.solidArea.x;
    gameEntity.solidArea.y = gameEntity.worldY + gameEntity.solidArea.y;
    // get player solid area position
    gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
    gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;

    checkGameEntityCollision(gameEntity, gameEntity.direction);
    if (gameEntity.solidArea.intersects(gamePanel.player.solidArea)) {
      gameEntity.collisionOn = true;
      contactPlayer = true;
    }
    gameEntity.solidArea.x = gameEntity.solidAreaDefaultX;
    gameEntity.solidArea.y = gameEntity.solidAreaDefaultY;
    gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
    gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;

    return contactPlayer;
  }

  private void checkGameEntityCollision(GameEntity gameEntity, DirectionType direction) {
    switch (direction) {
      case UP -> gameEntity.solidArea.y -= gameEntity.speed;
      case DOWN -> gameEntity.solidArea.y += gameEntity.speed;
      case LEFT -> gameEntity.solidArea.x -= gameEntity.speed;
      case RIGHT -> gameEntity.solidArea.x += gameEntity.speed;
    }
  }
}
