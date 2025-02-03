package org.example.utils;

import org.example.GamePanel;
import org.example.entity.Entity;

public class CollisionDetector {

  GamePanel gamePanel;

  public CollisionDetector(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void checkTile(Entity entity) {
    int entityLeftWorldX = entity.worldX + entity.solidArea.x;
    int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
    int entityTopWorldY = entity.worldY + entity.solidArea.y;
    int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

    int entityLeftCol = entityLeftWorldX / gamePanel.tileSize;
    int entityRightCol = entityRightWorldX / gamePanel.tileSize;
    int entityTopRow = entityTopWorldY / gamePanel.tileSize;
    int entityBottomRow = entityBottomWorldY / gamePanel.tileSize;

    int tileNum1, tileNum2;

    switch (entity.getDirection()) {
      case UP:
        entityTopRow = (entityTopWorldY - entity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
        tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityTopRow];
        if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
          entity.collisionOn = true;
        }
        break;
      case DOWN:
        entityBottomRow = (entityBottomWorldY + entity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
        tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
        if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
          entity.collisionOn = true;
        }
        break;
      case LEFT:
        entityLeftCol = (entityLeftWorldX - entity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
        tileNum2 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
        if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
          entity.collisionOn = true;
        }
        break;
      case RIGHT:
        entityRightCol = (entityRightWorldX + entity.speed) / gamePanel.tileSize;
        tileNum1 = gamePanel.tileManager.mapTileNum[entityLeftCol][entityTopRow];
        tileNum2 = gamePanel.tileManager.mapTileNum[entityRightCol][entityBottomRow];
        if (gamePanel.tileManager.tile[tileNum1].collision || gamePanel.tileManager.tile[tileNum2].collision) {
          entity.collisionOn = true;
        }
        break;
    }
  }

  public int checkObject(Entity entity, boolean player) {
    int index = 999;

    for (int i = 0; i < gamePanel.obj.length; i++) {
      if (gamePanel.obj[i] != null) {
        //get entity's solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        // get object's solid area position
        gamePanel.obj[i].solidArea.x = gamePanel.obj[i].worldX + gamePanel.obj[i].solidArea.x;
        gamePanel.obj[i].solidArea.y = gamePanel.obj[i].worldY + gamePanel.obj[i].solidArea.y;
        switch (entity.getDirection()) {
          case UP:
            entity.solidArea.y -= entity.speed;
            if (entity.solidArea.intersects(gamePanel.obj[i].solidArea)) {
              if (gamePanel.obj[i].collision) {
                entity.collisionOn = true;
              }
              if (player) {
                index = i;
              }
            }
            break;
          case DOWN:
            entity.solidArea.y += entity.speed;
            if (entity.solidArea.intersects(gamePanel.obj[i].solidArea)) {
              if (gamePanel.obj[i].collision) {
                entity.collisionOn = true;
              }
              if (player) {
                index = i;
              }
            }
            break;
          case LEFT:
            entity.solidArea.x -= entity.speed;
            if (entity.solidArea.intersects(gamePanel.obj[i].solidArea)) {
              if (gamePanel.obj[i].collision) {
                entity.collisionOn = true;
              }
              if (player) {
                index = i;
              }
            }
            break;
          case RIGHT:
            entity.solidArea.x += entity.speed;
            if (entity.solidArea.intersects(gamePanel.obj[i].solidArea)) {
              if (gamePanel.obj[i].collision) {
                entity.collisionOn = true;
              }
              if (player) {
                index = i;
              }
            }
            break;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gamePanel.obj[i].solidArea.x = gamePanel.obj[i].solidAreaDefaultX;
        gamePanel.obj[i].solidArea.y = gamePanel.obj[i].solidAreaDefaultY;
      }
    }

    return index;
  }

  // NPC OR MONSTER COLLISION
  public int checkEntity(Entity entity, Entity[] target) {
    int index = 999;

    for (int i = 0; i < target.length; i++) {
      if (target[i] != null) {
        //get entity's solid area position
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        // get target solid area position
        target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
        target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;
        switch (entity.getDirection()) {
          case UP:
            entity.solidArea.y -= entity.speed;
            if (entity.solidArea.intersects(target[i].solidArea)) {
              entity.collisionOn = true;
              index = i;
            }
            break;
          case DOWN:
            entity.solidArea.y += entity.speed;
            if (entity.solidArea.intersects(target[i].solidArea)) {
              entity.collisionOn = true;
              index = i;
            }
            break;
          case LEFT:
            entity.solidArea.x -= entity.speed;
            if (entity.solidArea.intersects(target[i].solidArea)) {
              entity.collisionOn = true;
              index = i;
            }
            break;
          case RIGHT:
            entity.solidArea.x += entity.speed;
            if (entity.solidArea.intersects(target[i].solidArea)) {
              entity.collisionOn = true;
              index = i;
            }
            break;
        }
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gamePanel.npc[i].solidArea.x = gamePanel.npc[i].solidAreaDefaultX;
        gamePanel.npc[i].solidArea.y = gamePanel.npc[i].solidAreaDefaultY;
      }
    }
    return index;
  }

  public void checkPlayer(Entity entity) {
    //get entity's solid area position
    entity.solidArea.x = entity.worldX + entity.solidArea.x;
    entity.solidArea.y = entity.worldY + entity.solidArea.y;
    // get player solid area position
    gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
    gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;

    switch (entity.getDirection()) {
      case UP:
        entity.solidArea.y -= entity.speed;
        if (entity.solidArea.intersects(gamePanel.player.solidArea)) {
          entity.collisionOn = true;
        }
        break;
      case DOWN:
        entity.solidArea.y += entity.speed;
        if (entity.solidArea.intersects(gamePanel.player.solidArea)) {
          entity.collisionOn = true;
        }
        break;
      case LEFT:
        entity.solidArea.x -= entity.speed;
        if (entity.solidArea.intersects(gamePanel.player.solidArea)) {
          entity.collisionOn = true;
        }
        break;
      case RIGHT:
        entity.solidArea.x += entity.speed;
        if (entity.solidArea.intersects(gamePanel.player.solidArea)) {
          entity.collisionOn = true;
        }
        break;
    }
    entity.solidArea.x = entity.solidAreaDefaultX;
    entity.solidArea.y = entity.solidAreaDefaultY;
    gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
    gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
  }
}
