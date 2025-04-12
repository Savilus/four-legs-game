package org.savilusGame.utils;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;

import java.util.Objects;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.DirectionType;

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

    int entityLeftCol = entityLeftWorldX / TILE_SIZE;
    int entityRightCol = entityRightWorldX / TILE_SIZE;
    int entityTopRow = entityTopWorldY / TILE_SIZE;
    int entityBottomRow = entityBottomWorldY / TILE_SIZE;

    int tileNum1, tileNum2;

    // Use a temporal direction when it's being knock backed
    DirectionType direction = gameEntity.direction;
    if (gameEntity.knockBack) {
      direction = gameEntity.knockBackDirection;
    }
    var currentMapTiles = gamePanel.getTileManager().getGameMaps().get(CURRENT_MAP);
    switch (direction) {
      case UP -> {
        entityTopRow = (entityTopWorldY - gameEntity.speed) / TILE_SIZE;
        tileNum1 = currentMapTiles[entityLeftCol][entityTopRow];
        tileNum2 = currentMapTiles[entityRightCol][entityTopRow];
        if (gamePanel.getTileManager().getTile()[tileNum1].collision() || gamePanel.getTileManager().getTile()[tileNum2].collision()) {
          gameEntity.collisionOn = true;
        }
      }
      case DOWN -> {
        entityBottomRow = (entityBottomWorldY + gameEntity.speed) / TILE_SIZE;
        tileNum1 = currentMapTiles[entityLeftCol][entityBottomRow];
        tileNum2 = currentMapTiles[entityRightCol][entityBottomRow];
        if (gamePanel.getTileManager().getTile()[tileNum1].collision() || gamePanel.getTileManager().getTile()[tileNum2].collision()) {
          gameEntity.collisionOn = true;
        }
      }
      case LEFT -> {
        entityLeftCol = (entityLeftWorldX - gameEntity.speed) / TILE_SIZE;
        tileNum1 = currentMapTiles[entityLeftCol][entityTopRow];
        tileNum2 = currentMapTiles[entityLeftCol][entityBottomRow];
        if (gamePanel.getTileManager().getTile()[tileNum1].collision() || gamePanel.getTileManager().getTile()[tileNum2].collision()) {
          gameEntity.collisionOn = true;
        }
      }
      case RIGHT -> {
        entityRightCol = (entityRightWorldX + gameEntity.speed) / TILE_SIZE;
        tileNum1 = currentMapTiles[entityLeftCol][entityTopRow];
        tileNum2 = currentMapTiles[entityRightCol][entityBottomRow];
        if (gamePanel.getTileManager().getTile()[tileNum1].collision() || gamePanel.getTileManager().getTile()[tileNum2].collision()) {
          gameEntity.collisionOn = true;
        }
      }
    }
  }

  public int checkObject(GameEntity gameEntity, boolean player) {
    if (Objects.isNull(gamePanel.getMapsObjects().get(CURRENT_MAP)))
      return INIT_INDEX;

    int index = INIT_INDEX;

    DirectionType direction = gameEntity.direction;
    if (gameEntity.knockBack) {
      direction = gameEntity.knockBackDirection;
    }

    for (int i = 0; i < gamePanel.getMapsObjects().get(CURRENT_MAP).length; i++) {
      var mapObject = gamePanel.getMapsObjects().get(CURRENT_MAP)[i];
      if (Objects.nonNull(mapObject)) {
        //get entity's solid area position
        gameEntity.solidArea.x = gameEntity.worldX + gameEntity.solidArea.x;
        gameEntity.solidArea.y = gameEntity.worldY + gameEntity.solidArea.y;
        // get object's solid area position
        mapObject.solidArea.x = mapObject.worldX + mapObject.solidArea.x;
        mapObject.solidArea.y = mapObject.worldY + mapObject.solidArea.y;
        checkGameEntityCollision(gameEntity, direction);
        if (gameEntity.solidArea.intersects(mapObject.solidArea)) {
          if (mapObject.collision) {
            gameEntity.collisionOn = true;
          }
          if (player) {
            index = i;
          }
        }
        gameEntity.solidArea.x = gameEntity.solidAreaDefaultX;
        gameEntity.solidArea.y = gameEntity.solidAreaDefaultY;
        mapObject.solidArea.x = mapObject.solidAreaDefaultX;
        mapObject.solidArea.y = mapObject.solidAreaDefaultY;
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
      if (Objects.nonNull(target[targetIndex])) {
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
    gamePanel.getPlayer().solidArea.x = gamePanel.getPlayer().worldX + gamePanel.getPlayer().solidArea.x;
    gamePanel.getPlayer().solidArea.y = gamePanel.getPlayer().worldY + gamePanel.getPlayer().solidArea.y;

    checkGameEntityCollision(gameEntity, gameEntity.direction);
    if (gameEntity.solidArea.intersects(gamePanel.getPlayer().solidArea)) {
      gameEntity.collisionOn = true;
      contactPlayer = true;
    }
    gameEntity.solidArea.x = gameEntity.solidAreaDefaultX;
    gameEntity.solidArea.y = gameEntity.solidAreaDefaultY;
    gamePanel.getPlayer().solidArea.x = gamePanel.getPlayer().solidAreaDefaultX;
    gamePanel.getPlayer().solidArea.y = gamePanel.getPlayer().solidAreaDefaultY;

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
