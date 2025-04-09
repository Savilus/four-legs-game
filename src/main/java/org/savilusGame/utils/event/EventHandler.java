package org.savilusGame.utils.event;

import static org.savilusGame.config.GameEntityNameFactory.DUNGEON_FIRST_FLOR;
import static org.savilusGame.config.GameEntityNameFactory.DUNGEON_SECOND_FLOR;
import static org.savilusGame.config.GameEntityNameFactory.INTERIOR_MAP;
import static org.savilusGame.config.GameEntityNameFactory.MAIN_MAP;
import static org.savilusGame.config.GameEntityNameFactory.POWER_UP;
import static org.savilusGame.config.GameEntityNameFactory.RECEIVE_DAMAGE;
import static org.savilusGame.config.GameEntityNameFactory.STAIRS;
import static org.savilusGame.enums.GameStateType.CUTSCENE_STATE;
import static org.savilusGame.enums.GameStateType.DIALOG_STATE;
import static org.savilusGame.enums.GameStateType.TRANSITION_STATE;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.savilusGame.GamePanel;
import org.savilusGame.data.Progress;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.AreaType;
import org.savilusGame.enums.DirectionType;
import org.savilusGame.enums.GameStateType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventHandler {

  static final String DAMAGE_PIT = "damagePit";
  static final String HEALING_POOL = "healingPool";

  final GamePanel gamePanel;
  final Map<String, EventRect[][]> eventRect = new HashMap<>();
  String tempMap;
  int tempCol, tempRow;

  int previousEventX, previousEventY;
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
    int xDistance = Math.abs(gamePanel.player.worldX - previousEventX);
    int yDistance = Math.abs(gamePanel.player.worldY - previousEventY);
    int distance = Math.max(xDistance, yDistance);

    if (distance > gamePanel.tileSize) {
      canTouchEvent = true;
    }

    if (canTouchEvent) {
      handleMapEvents(CURRENT_MAP);
    }
  }

  private void handleMapEvents(String currentMap) {
    Map<String, Runnable> mapHandlers = new HashMap<>();
    mapHandlers.put(MAIN_MAP, this::handleMainMapEvents);
    mapHandlers.put(DUNGEON_FIRST_FLOR, this::handleDungeonFirstFloorEvents);
    mapHandlers.put(DUNGEON_SECOND_FLOR, this::handleDungeonSecondFloorEvents);
    mapHandlers.put(INTERIOR_MAP, this::handleInteriorMapEvents);

    Runnable mapHandler = mapHandlers.get(currentMap);
    if (Objects.nonNull(mapHandler)) {
      mapHandler.run();
    }
  }

  private void handleMainMapEvents() {
    if (hit(27, 16, DirectionType.RIGHT)) damagePit();
    else if (hit(23, 12, DirectionType.UP)) healingPool();
    else if (hit(10, 39, DirectionType.ANY)) mapTeleport(INTERIOR_MAP, 12, 13, AreaType.INDOOR);
    else if (hit(12, 9, DirectionType.ANY)) mapTeleport(DUNGEON_FIRST_FLOR, 9, 41, AreaType.DUNGEON);
  }

  private void handleDungeonFirstFloorEvents() {
    if (hit(8, 7, DirectionType.ANY)) mapTeleport(DUNGEON_SECOND_FLOR, 26, 41, AreaType.DUNGEON);
    else if (hit(9, 41, DirectionType.ANY)) mapTeleport(MAIN_MAP, 12, 10, AreaType.OUTSIDE);
  }

  private void handleDungeonSecondFloorEvents() {
    if (hit(26, 41, DirectionType.ANY)) mapTeleport(DUNGEON_FIRST_FLOR, 8, 7, AreaType.DUNGEON);
    if (hit(25, 27, DirectionType.ANY)) skeletonLord();
  }

  private void handleInteriorMapEvents() {
    if (hit(12, 13, DirectionType.ANY)) mapTeleport(MAIN_MAP, 10, 39, AreaType.OUTSIDE);
    else if (hit(12, 9, DirectionType.UP)) speak(gamePanel.mapsNpc.get(INTERIOR_MAP)[0]);
  }

  private void speak(GameEntity gameEntity) {
    if (gamePanel.keyHandler.enterPressed) {
      gamePanel.gameState = DIALOG_STATE;
      gamePanel.player.attackCanceled = true;
      gameEntity.speak();
    }
  }

  private boolean hit(int col, int row, DirectionType requiredDirection) {
    var playerSolidArea = new Rectangle(
        gamePanel.player.worldX + gamePanel.player.solidArea.x,
        gamePanel.player.worldY + gamePanel.player.solidArea.y,
        gamePanel.player.solidArea.width,
        gamePanel.player.solidArea.height
    );

    var eventArea = new Rectangle(
        col * gamePanel.tileSize + eventRect.get(MAIN_MAP)[col][row].x,
        row * gamePanel.tileSize + eventRect.get(MAIN_MAP)[col][row].y,
        eventRect.get(MAIN_MAP)[col][row].width,
        eventRect.get(MAIN_MAP)[col][row].height
    );

    boolean hitResult = playerSolidArea.intersects(eventArea) && !eventRect.get(MAIN_MAP)[col][row].oneTimeEventDone;

    if (hitResult && (gamePanel.player.direction.equals(requiredDirection) || requiredDirection.equals(DirectionType.ANY))) {
      previousEventX = gamePanel.player.worldX;
      previousEventY = gamePanel.player.worldY;
      return true;
    }

    resetPlayerSolidArea();
    resetEventArea(col, row);

    return false;
  }

  private void resetPlayerSolidArea() {
    gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
    gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
  }

  private void resetEventArea(int col, int row) {
    eventRect.get(MAIN_MAP)[col][row].x = eventRect.get(MAIN_MAP)[col][row].eventRectDefaultX;
    eventRect.get(MAIN_MAP)[col][row].y = eventRect.get(MAIN_MAP)[col][row].eventRectDefaultY;
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

  private void skeletonLord() {
    if (!gamePanel.bossBattleOn && !Progress.skeletonLordDefeated) {
      gamePanel.gameState = CUTSCENE_STATE;
      gamePanel.cutsceneManager.sceneNum = gamePanel.cutsceneManager.skeletonLord;
    }
  }

  private void damagePit() {
    gamePanel.gameState = GameStateType.DIALOG_STATE;
    gamePanel.player.startDialogue(gamePanel.player, DAMAGE_PIT);
    gamePanel.player.currentLife -= 1;
    gamePanel.playSoundEffect(RECEIVE_DAMAGE);
//    eventRect[col][row].eventDone = true;
    canTouchEvent = false;
  }

  private void healingPool() {

    if (gamePanel.keyHandler.enterPressed) {
      gamePanel.gameState = GameStateType.DIALOG_STATE;
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
