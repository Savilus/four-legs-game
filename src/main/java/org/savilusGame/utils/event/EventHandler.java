package org.savilusGame.utils.event;

import static org.savilusGame.GamePanel.TILE_SIZE;
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
    eventRect.put(MAIN_MAP, new EventRect[gamePanel.getMaxWorldCol()][gamePanel.getMaxWorldRow()]);
    for (int row = 0; row < gamePanel.getMaxWorldRow(); row++) {
      for (int col = 0; col < gamePanel.getMaxWorldCol(); col++) {
        EventRect rect = new EventRect();
        rect.x = 23;
        rect.y = 23;
        rect.width = 2;
        rect.height = 2;
        rect.eventRectDefaultX = rect.x;
        rect.eventRectDefaultY = rect.y;

        eventRect.get(MAIN_MAP)[col][row] = rect;
      }
    }
  }

  public void checkEvent() {
    int xDistance = Math.abs(gamePanel.getPlayer().worldX - previousEventX);
    int yDistance = Math.abs(gamePanel.getPlayer().worldY - previousEventY);
    int distance = Math.max(xDistance, yDistance);

    if (distance > TILE_SIZE) {
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
    else if (hit(12, 9, DirectionType.UP)) speak(gamePanel.getMapsNpc().get(INTERIOR_MAP)[0]);
  }

  private void speak(GameEntity gameEntity) {
    if (gamePanel.getKeyHandler().isEnterPressed()) {
      gamePanel.setGameState(DIALOG_STATE);
      gamePanel.getPlayer().attackCanceled = true;
      gameEntity.speak();
    }
  }

  private boolean hit(int col, int row, DirectionType requiredDirection) {
    var playerSolidArea = new Rectangle(
        gamePanel.getPlayer().worldX + gamePanel.getPlayer().solidArea.x,
        gamePanel.getPlayer().worldY + gamePanel.getPlayer().solidArea.y,
        gamePanel.getPlayer().solidArea.width,
        gamePanel.getPlayer().solidArea.height
    );

    var eventArea = new Rectangle(
        col * TILE_SIZE + eventRect.get(MAIN_MAP)[col][row].x,
        row * TILE_SIZE + eventRect.get(MAIN_MAP)[col][row].y,
        eventRect.get(MAIN_MAP)[col][row].width,
        eventRect.get(MAIN_MAP)[col][row].height
    );

    boolean hitResult = playerSolidArea.intersects(eventArea) && !eventRect.get(MAIN_MAP)[col][row].oneTimeEventDone;

    if (hitResult && (gamePanel.getPlayer().direction.equals(requiredDirection) || requiredDirection.equals(DirectionType.ANY))) {
      previousEventX = gamePanel.getPlayer().worldX;
      previousEventY = gamePanel.getPlayer().worldY;
      return true;
    }

    resetPlayerSolidArea();
    resetEventArea(col, row);
    return false;
  }

  private void resetPlayerSolidArea() {
    gamePanel.getPlayer().solidArea.x = gamePanel.getPlayer().solidAreaDefaultX;
    gamePanel.getPlayer().solidArea.y = gamePanel.getPlayer().solidAreaDefaultY;
  }

  private void resetEventArea(int col, int row) {
    eventRect.get(MAIN_MAP)[col][row].x = eventRect.get(MAIN_MAP)[col][row].eventRectDefaultX;
    eventRect.get(MAIN_MAP)[col][row].y = eventRect.get(MAIN_MAP)[col][row].eventRectDefaultY;
  }

  private void mapTeleport(String teleportToMap, int col, int row, AreaType areaType) {
    gamePanel.setGameState(TRANSITION_STATE);
    gamePanel.setNextArea(areaType);
    tempMap = teleportToMap;
    tempCol = col;
    tempRow = row;
    canTouchEvent = false;
    gamePanel.playSoundEffect(STAIRS);
  }

  private void skeletonLord() {
    if (!gamePanel.isBossBattleOn() && !Progress.skeletonLordDefeated) {
      gamePanel.setGameState(CUTSCENE_STATE);
      gamePanel.getCutsceneManager().setSceneNum(gamePanel.getCutsceneManager().getSkeletonLord());
    }
  }

  private void damagePit() {
    gamePanel.setGameState(DIALOG_STATE);
    gamePanel.getPlayer().startDialogue(gamePanel.getPlayer(), DAMAGE_PIT);
    gamePanel.getPlayer().currentLife -= 1;
    gamePanel.playSoundEffect(RECEIVE_DAMAGE);
//    eventRect[col][row].eventDone = true;
    canTouchEvent = false;
  }

  private void healingPool() {
    if (gamePanel.getKeyHandler().isEnterPressed()) {
      gamePanel.setGameState(DIALOG_STATE);
      gamePanel.getPlayer().attackCanceled = true;
      gamePanel.playSoundEffect(POWER_UP);
      gamePanel.getPlayer().startDialogue(gamePanel.getPlayer(), HEALING_POOL);
      gamePanel.getPlayer().currentLife = gamePanel.getPlayer().maxLife;
      gamePanel.getPlayer().mana = gamePanel.getPlayer().maxMana;
      gamePanel.getAssetSetter().setMonster();
      gamePanel.getSaveLoad().save();
    }
  }
}
