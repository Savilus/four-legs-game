package org.example.utils;

import org.example.GamePanel;
import org.example.entity.GameEntity;

public class CollisionDetector {

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

    switch (gameEntity.getDirection()) {
      case UP -> {
        entityTopRow = (entityTopWorldY - gameEntity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
        tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
        if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
          gameEntity.collisionOn = true;
        }
      }
      case DOWN -> {
        entityBottomRow = (entityBottomWorldY + gameEntity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
        tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
        if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
          gameEntity.collisionOn = true;
        }
      }
      case LEFT -> {
        entityLeftCol = (entityLeftWorldX - gameEntity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
        tileNum2 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
        if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
          gameEntity.collisionOn = true;
        }
      }
      case RIGHT -> {
        entityRightCol = (entityRightWorldX + gameEntity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
        tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
        if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
          gameEntity.collisionOn = true;
        }
      }
    }
  }

  public int checkObject(GameEntity gameEntity, boolean player) {
    int index = 999;

    for (int i = 0; i < gamePanel.obj.length; i++) {
      if (gamePanel.obj[i] != null) {
        //get entity's solid area position
        gameEntity.solidArea.x = gameEntity.worldX + gameEntity.solidArea.x;
        gameEntity.solidArea.y = gameEntity.worldY + gameEntity.solidArea.y;
        // get object's solid area position
        gamePanel.obj[i].solidArea.x = gamePanel.obj[i].worldX + gamePanel.obj[i].solidArea.x;
        gamePanel.obj[i].solidArea.y = gamePanel.obj[i].worldY + gamePanel.obj[i].solidArea.y;
        checkGameEntityCollision(gameEntity);
        if (gameEntity.solidArea.intersects(gamePanel.obj[i].solidArea)) {
          if (gamePanel.obj[i].collision) {
            gameEntity.collisionOn = true;
          }
          if (player) {
            index = i;
          }
        }
        gameEntity.solidArea.x = gameEntity.solidAreaDefaultX;
        gameEntity.solidArea.y = gameEntity.solidAreaDefaultY;
        gamePanel.obj[i].solidArea.x = gamePanel.obj[i].solidAreaDefaultX;
        gamePanel.obj[i].solidArea.y = gamePanel.obj[i].solidAreaDefaultY;
      }
    }

    return index;
  }

  // NPC OR MONSTER COLLISION
  public int checkEntity(GameEntity gameEntity, GameEntity[] target) {
    int index = 999;

    for (int i = 0; i < target.length; i++) {
      if (target[i] != null) {
        //get entity's solid area position
        gameEntity.solidArea.x = gameEntity.worldX + gameEntity.solidArea.x;
        gameEntity.solidArea.y = gameEntity.worldY + gameEntity.solidArea.y;
        // get target solid area position
        target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
        target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;
        checkGameEntityCollision(gameEntity);
        if (gameEntity.solidArea.intersects(target[i].solidArea) && target[i] != gameEntity) {
          gameEntity.collisionOn = true;
          index = i;
        }

        gameEntity.solidArea.x = gameEntity.solidAreaDefaultX;
        gameEntity.solidArea.y = gameEntity.solidAreaDefaultY;
        target[i].solidArea.x = target[i].solidAreaDefaultX;
        target[i].solidArea.y = target[i].solidAreaDefaultY;
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

    checkGameEntityCollision(gameEntity);
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

  private void checkGameEntityCollision(GameEntity gameEntity) {
    switch (gameEntity.getDirection()) {
      case UP -> gameEntity.solidArea.y -= gameEntity.speed;
      case DOWN -> gameEntity.solidArea.y += gameEntity.speed;
      case LEFT -> gameEntity.solidArea.x -= gameEntity.speed;
      case RIGHT -> gameEntity.solidArea.x += gameEntity.speed;
    }
  }
}
