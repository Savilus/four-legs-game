package org.example.utils.event;

import static org.example.config.GameEntityNameFactory.DUNGEON_FIRST_FLOR;
import static org.example.config.GameEntityNameFactory.DUNGEON_SECOND_FLOR;
import static org.example.config.GameEntityNameFactory.INTERIOR_MAP;
import static org.example.config.GameEntityNameFactory.MAIN_MAP;
import static org.example.config.GameEntityNameFactory.POWER_UP;
import static org.example.config.GameEntityNameFactory.RECEIVE_DAMAGE;
import static org.example.config.GameEntityNameFactory.STAIRS;
import static org.example.enums.GameStateType.DIALOG_STATE;
import static org.example.enums.GameStateType.TRANSITION_STATE;

import java.util.HashMap;
import java.util.Map;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.AreaType;
import org.example.enums.DirectionType;
import org.example.enums.GameStateType;

public class EventHandler {

  private static final String DAMAGE_PIT = "damagePit";
  private static final String HEALING_POOL = "healingPool";

  GamePanel gamePanel;
  Map<String, EventRect[][]> eventRect = new HashMap<>();
  public String tempMap;
  public int tempCol, tempRow;

  public int previousEventX, previousEventY;
  boolean canTouchEvent = true;

  public EventHandler(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    eventRect.put(MAIN_MAP, new EventRect[gamePanel.maxWorldCol][gamePanel.maxWorldRow]);
    int col = 0;
    int row = 0;
    while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
      eventRect.get(MAIN_MAP)[col][row] = new EventRect();
      eventRect.get(MAIN_MAP)[col][row].x = 23;
      eventRect.get(MAIN_MAP)[col][row].y = 23;
      eventRect.get(MAIN_MAP)[col][row].width = 2;
      eventRect.get(MAIN_MAP)[col][row].height = 2;
      eventRect.get(MAIN_MAP)[col][row].eventRectDefaultX = eventRect.get(MAIN_MAP)[col][row].x;
      eventRect.get(MAIN_MAP)[col][row].eventRectDefaultY = eventRect.get(MAIN_MAP)[col][row].y;

      col++;
      if (col == gamePanel.maxWorldCol) {
        col = 0;
        row++;
      }
    }
  }

  public void checkEvent() {
    // Check if the player character is more than 1 tile away from the last event
    int xDistance = Math.abs(gamePanel.player.worldX - previousEventX);
    int yDistance = Math.abs(gamePanel.player.worldY - previousEventY);
    int distance = Math.max(xDistance, yDistance);

    if (distance > gamePanel.tileSize) {
      canTouchEvent = true;
    }

    if (canTouchEvent && gamePanel.tileManager.currentMap.equals(MAIN_MAP)) {
      if (hit(27, 16, DirectionType.RIGHT)) damagePit(DIALOG_STATE);
      else if (hit(23, 12, DirectionType.UP)) healingPool(DIALOG_STATE);
      else if (hit(10, 39, DirectionType.ANY)) mapTeleport(INTERIOR_MAP, 12, 13, AreaType.INDOOR);
      else if (hit(12, 9, DirectionType.ANY)) mapTeleport(DUNGEON_FIRST_FLOR, 9, 41, AreaType.DUNGEON);
    }

    if (canTouchEvent && gamePanel.tileManager.currentMap.equals(DUNGEON_FIRST_FLOR)) {
      if (hit(8, 7, DirectionType.ANY)) mapTeleport(DUNGEON_SECOND_FLOR, 26, 41, AreaType.DUNGEON);
      else if (hit(9, 41, DirectionType.ANY)) mapTeleport(MAIN_MAP, 12, 10, AreaType.OUTSIDE);
    }

    if (canTouchEvent && gamePanel.tileManager.currentMap.equals(DUNGEON_SECOND_FLOR)) {
      if (hit(26, 41, DirectionType.ANY)) mapTeleport(DUNGEON_FIRST_FLOR, 8, 7, AreaType.DUNGEON);
    }

    if (canTouchEvent && gamePanel.tileManager.currentMap.equals(INTERIOR_MAP)) {
      if (hit(12, 13, DirectionType.ANY)) mapTeleport(MAIN_MAP, 10, 39, AreaType.OUTSIDE);
      else if (hit(12, 9, DirectionType.UP)) speak(gamePanel.mapsNpc.get(INTERIOR_MAP)[0]);
    }
  }

  private void speak(GameEntity gameEntity) {
    if (gamePanel.keyHandler.enterPressed) {
      gamePanel.gameState = DIALOG_STATE;
      gamePanel.player.attackCanceled = true;
      gameEntity.speak();
    }
  }

  public boolean hit(int col, int row, DirectionType requiredDirection) {
    boolean hit = false;
    gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
    gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;
    eventRect.get(MAIN_MAP)[col][row].x = col * gamePanel.tileSize + eventRect.get(MAIN_MAP)[col][row].x;
    eventRect.get(MAIN_MAP)[col][row].y = row * gamePanel.tileSize + eventRect.get(MAIN_MAP)[col][row].y;

    if (gamePanel.player.solidArea.intersects(eventRect.get(MAIN_MAP)[col][row]) && !eventRect.get(MAIN_MAP)[col][row].oneTimeEventDone) {
      if (gamePanel.player.direction.equals(requiredDirection) || requiredDirection.equals(DirectionType.ANY))
        hit = true;

      previousEventX = gamePanel.player.worldX;
      previousEventY = gamePanel.player.worldY;
    }

    gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
    gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
    eventRect.get(MAIN_MAP)[col][row].x = eventRect.get(MAIN_MAP)[col][row].eventRectDefaultX;
    eventRect.get(MAIN_MAP)[col][row].y = eventRect.get(MAIN_MAP)[col][row].eventRectDefaultY;
    return hit;
  }

  private void mapTeleport(String teleportToMap, int col, int row, AreaType areaType) {
    gamePanel.gameState = TRANSITION_STATE;
    gamePanel.nextArea = areaType;
    tempMap = teleportToMap;
    tempCol = col;
    tempRow = row;
    canTouchEvent = false;
    gamePanel.playSoundEffect(STAIRS);
  }

  private void damagePit(GameStateType gameState) {
    gamePanel.gameState = gameState;
    gamePanel.player.startDialogue(gamePanel.player, DAMAGE_PIT);
    gamePanel.player.currentLife -= 1;
    gamePanel.playSoundEffect(RECEIVE_DAMAGE);
//    eventRect[col][row].eventDone = true;
    canTouchEvent = false;
  }

  private void healingPool(GameStateType gameState) {

    if (gamePanel.keyHandler.enterPressed) {
      gamePanel.gameState = gameState;
      gamePanel.player.attackCanceled = true;
      gamePanel.playSoundEffect(POWER_UP);
      gamePanel.player.startDialogue(gamePanel.player, HEALING_POOL);
      gamePanel.player.currentLife = gamePanel.player.maxLife;
      gamePanel.player.mana = gamePanel.player.maxMana;
      gamePanel.assetSetter.setMonster();
      gamePanel.saveLoad.save();
    }
  }

}
