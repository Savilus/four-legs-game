package org.savilusGame.utils;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;

import java.util.Objects;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.Direction;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CollisionDetector {
  public final static int INIT_INDEX = 999;

  private final GamePanel gamePanel;

  public void checkTile(GameEntity gameEntity) {
    int entityLeftWorldX = getXSolidArea(gameEntity);
    int entityRightWorldX = getXSolidArea(gameEntity) + gameEntity.getSolidArea().width;
    int entityTopWorldY = getYSolidArea(gameEntity);
    int entityBottomWorldY = getYSolidArea(gameEntity) + gameEntity.getSolidArea().height;

    int entityLeftCol = entityLeftWorldX / TILE_SIZE;
    int entityRightCol = entityRightWorldX / TILE_SIZE;
    int entityTopRow = entityTopWorldY / TILE_SIZE;
    int entityBottomRow = entityBottomWorldY / TILE_SIZE;

    int tileNum1, tileNum2;

    // Use a temporal direction when it's being knock backed
    Direction direction = gameEntity.getDirection();
    if (gameEntity.isKnockBack()) {
      direction = gameEntity.getKnockBackDirection();
    }
    var currentMapTiles = gamePanel.getTileManager().getGameMaps().get(CURRENT_MAP);
    switch (direction) {
      case UP -> {
        entityTopRow = (entityTopWorldY - gameEntity.getSpeed()) / TILE_SIZE;
        tileNum1 = currentMapTiles[entityLeftCol][entityTopRow];
        tileNum2 = currentMapTiles[entityRightCol][entityTopRow];
        if (gamePanel.getTileManager().getTile()[tileNum1].collision() || gamePanel.getTileManager().getTile()[tileNum2].collision()) {
          gameEntity.setCollisionOn(true);
        }
      }
      case DOWN -> {
        entityBottomRow = (entityBottomWorldY + gameEntity.getSpeed()) / TILE_SIZE;
        tileNum1 = currentMapTiles[entityLeftCol][entityBottomRow];
        tileNum2 = currentMapTiles[entityRightCol][entityBottomRow];
        if (gamePanel.getTileManager().getTile()[tileNum1].collision() || gamePanel.getTileManager().getTile()[tileNum2].collision()) {
          gameEntity.setCollisionOn(true);
        }
      }
      case LEFT -> {
        entityLeftCol = (entityLeftWorldX - gameEntity.getSpeed()) / TILE_SIZE;
        tileNum1 = currentMapTiles[entityLeftCol][entityTopRow];
        tileNum2 = currentMapTiles[entityLeftCol][entityBottomRow];
        if (gamePanel.getTileManager().getTile()[tileNum1].collision() || gamePanel.getTileManager().getTile()[tileNum2].collision()) {
          gameEntity.setCollisionOn(true);
        }
      }
      case RIGHT -> {
        entityRightCol = (entityRightWorldX + gameEntity.getSpeed()) / TILE_SIZE;
        tileNum1 = currentMapTiles[entityLeftCol][entityTopRow];
        tileNum2 = currentMapTiles[entityRightCol][entityBottomRow];
        if (gamePanel.getTileManager().getTile()[tileNum1].collision() || gamePanel.getTileManager().getTile()[tileNum2].collision()) {
          gameEntity.setCollisionOn(true);
        }
      }
    }
  }

  public int checkObject(GameEntity gameEntity, boolean player) {
    if (Objects.isNull(gamePanel.getMapsObjects().get(CURRENT_MAP)))
      return INIT_INDEX;

    int index = INIT_INDEX;

    Direction direction = gameEntity.getDirection();
    if (gameEntity.isKnockBack()) {
      direction = gameEntity.getKnockBackDirection();
    }

    for (int i = 0; i < gamePanel.getMapsObjects().get(CURRENT_MAP).length; i++) {
      var mapObject = gamePanel.getMapsObjects().get(CURRENT_MAP)[i];
      if (Objects.nonNull(mapObject)) {
        //get entity's solid area position
        gameEntity.getSolidArea().x = getXSolidArea(gameEntity);
        gameEntity.getSolidArea().y = getYSolidArea(gameEntity);
        // get object's solid area position
        mapObject.getSolidArea().x = mapObject.getWorldX() + mapObject.getSolidArea().x;
        mapObject.getSolidArea().y = mapObject.getWorldY() + mapObject.getSolidArea().y;
        checkGameEntityCollision(gameEntity, direction);
        if (gameEntity.getSolidArea().intersects(mapObject.getSolidArea())) {
          if (mapObject.isCollision()) {
            gameEntity.setCollisionOn(true);
          }
          if (player) {
            index = i;
          }
        }
        gameEntity.getSolidArea().x = gameEntity.getSolidAreaDefaultX();
        gameEntity.getSolidArea().y = gameEntity.getSolidAreaDefaultY();
        mapObject.getSolidArea().x = mapObject.getSolidAreaDefaultX();
        mapObject.getSolidArea().y = mapObject.getSolidAreaDefaultY();
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
    Direction direction = gameEntity.getDirection();
    if (gameEntity.isKnockBack()) {
      direction = gameEntity.getKnockBackDirection();
    }

    for (int targetIndex = 0; targetIndex < target.length; targetIndex++) {
      if (Objects.nonNull(target[targetIndex])) {
        //get entity's solid area position
        gameEntity.getSolidArea().x = getXSolidArea(gameEntity);
        gameEntity.getSolidArea().y = getYSolidArea(gameEntity);
        // get target solid area position
        target[targetIndex].getSolidArea().x = target[targetIndex].getWorldX() + target[targetIndex].getSolidArea().x;
        target[targetIndex].getSolidArea().y = target[targetIndex].getWorldY() + target[targetIndex].getSolidArea().y;
        checkGameEntityCollision(gameEntity, direction);
        if (gameEntity.getSolidArea().intersects(target[targetIndex].getSolidArea()) && target[targetIndex] != gameEntity) {
          gameEntity.setCollisionOn(true);
          index = targetIndex;
        }

        gameEntity.getSolidArea().x = gameEntity.getSolidAreaDefaultX();
        gameEntity.getSolidArea().y = gameEntity.getSolidAreaDefaultY();
        target[targetIndex].getSolidArea().x = target[targetIndex].getSolidAreaDefaultX();
        target[targetIndex].getSolidArea().y = target[targetIndex].getSolidAreaDefaultY();
      }
    }
    return index;
  }

  public boolean checkPlayer(GameEntity gameEntity) {
    boolean contactPlayer = false;
    //get entity's solid area position
    gameEntity.getSolidArea().x = getXSolidArea(gameEntity);
    gameEntity.getSolidArea().y = getYSolidArea(gameEntity);
    // get player solid area position
    gamePanel.getPlayer().getSolidArea().x = gamePanel.getPlayer().getWorldX() + gamePanel.getPlayer().getSolidArea().x;
    gamePanel.getPlayer().getSolidArea().y = gamePanel.getPlayer().getWorldY() + gamePanel.getPlayer().getSolidArea().y;

    checkGameEntityCollision(gameEntity, gameEntity.getDirection());
    if (gameEntity.getSolidArea().intersects(gamePanel.getPlayer().getSolidArea())) {
      gameEntity.setCollisionOn(true);
      contactPlayer = true;
    }
    gameEntity.getSolidArea().x = gameEntity.getSolidAreaDefaultX();
    gameEntity.getSolidArea().y = gameEntity.getSolidAreaDefaultY();
    gamePanel.getPlayer().getSolidArea().x = gamePanel.getPlayer().getSolidAreaDefaultX();
    gamePanel.getPlayer().getSolidArea().y = gamePanel.getPlayer().getSolidAreaDefaultY();

    return contactPlayer;
  }

  private int getYSolidArea(GameEntity gameEntity){
   return gameEntity.getWorldY() + gameEntity.getSolidArea().y;
  }

  private int getXSolidArea(GameEntity gameEntity){
    return gameEntity.getWorldX() + gameEntity.getSolidArea().x;
  }

  private void checkGameEntityCollision(GameEntity gameEntity, Direction direction) {
    switch (direction) {
      case UP -> gameEntity.getSolidArea().y -= gameEntity.getSpeed();
      case DOWN -> gameEntity.getSolidArea().y += gameEntity.getSpeed();
      case LEFT -> gameEntity.getSolidArea().x -= gameEntity.getSpeed();
      case RIGHT -> gameEntity.getSolidArea().x += gameEntity.getSpeed();
    }
  }
}
