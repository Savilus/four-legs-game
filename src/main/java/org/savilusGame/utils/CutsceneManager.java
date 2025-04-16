package org.savilusGame.utils;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.DOOR_OPEN;
import static org.savilusGame.config.GameEntityNameFactory.FINAL_BATTLE;
import static org.savilusGame.enums.GameState.PLAY_STATE;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;

import java.awt.*;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.PlayerDummy;
import org.savilusGame.entity.items.IronDoor;
import org.savilusGame.enums.MonsterType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CutsceneManager {
  private static final String CUTSCENE_DIALOGUE = "cutsceneDialogue";

  private final GamePanel gamePanel;
  //  private Graphics2D graphics2D;
  private int sceneNum;
  private int scenePhase;

  //scene number
  private final int NA = 0;
  private final int skeletonLord = 1;

  public CutsceneManager(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void draw(Graphics2D graphics2D) {
//    this.graphics2D = graphics2D;

    switch (sceneNum) {
      case skeletonLord -> skeletonLordScene();
    }
  }

  private void skeletonLordScene() {
    var mapNpcs = gamePanel.getMapsNpc().get(CURRENT_MAP);

    if (scenePhase == 0) {
      gamePanel.setBossBattleOn(true);
      var mapObjects = gamePanel.getMapsObjects().get(CURRENT_MAP);
      for (int i = 0; i < mapObjects.length; i++) {
        if (Objects.isNull(gamePanel.getMapsObjects().get(CURRENT_MAP)[i])) {
          mapObjects[i] = new IronDoor(gamePanel);
          mapObjects[i].setWorldX(TILE_SIZE * 25);
          mapObjects[i].setWorldY(TILE_SIZE * 28);
          mapObjects[i].setTemporaryObject(true);
          gamePanel.playSoundEffect(DOOR_OPEN);
          break;
        }
      }

      for (int i = 0; i < mapNpcs.length; i++) {
        if (Objects.isNull(mapNpcs[i])) {
          PlayerDummy dummy = new PlayerDummy(gamePanel);
          dummy.setWorldX(gamePanel.getPlayer().getWorldX());
          dummy.setWorldY(gamePanel.getPlayer().getWorldY());
          dummy.setDirection(gamePanel.getPlayer().getDirection());

          gamePanel.getMapsNpc().get(CURRENT_MAP)[i] = dummy;
          break;
        }
      }

      gamePanel.getPlayer().setDrawing(false);
      scenePhase++;
    }

    if (scenePhase == 1) {
      gamePanel.getPlayer().setWorldY(gamePanel.getPlayer().getWorldY() - 2);
      if (gamePanel.getPlayer().getWorldY() < TILE_SIZE * 16) {
        scenePhase++;
      }
    }

    if (scenePhase == 2) {
      var monsters = gamePanel.getMapsMonsters().get(CURRENT_MAP);
      for (GameEntity monster : monsters) {
        if (Objects.nonNull(monster) &&
            StringUtils.equalsIgnoreCase(monster.getName(), MonsterType.SKELETON_LORD.getName())) {
          monster.setSleep(false);
          gamePanel.getUi().setNpc(monster);
          scenePhase++;
          break;
        }
      }
    }

    if (scenePhase == 3) {
      var monsters = gamePanel.getMapsMonsters().get(CURRENT_MAP);
      for (GameEntity monster : monsters) {
        if (Objects.nonNull(monster) &&
            StringUtils.equalsIgnoreCase(monster.getName(), MonsterType.SKELETON_LORD.getName())) {
          monster.startDialogue(monster, CUTSCENE_DIALOGUE);
          gamePanel.getUi().drawDialogueScreen();
          break;
        }
      }
    }

    if (scenePhase == 4) {
      for (int i = 0; i < mapNpcs.length; i++) {
        if (Objects.nonNull(mapNpcs[i]) && mapNpcs[i] instanceof PlayerDummy) {
          gamePanel.getPlayer().setWorldX(mapNpcs[i].getWorldX());
          gamePanel.getPlayer().setWorldY(mapNpcs[i].getWorldY());
          gamePanel.getMapsNpc().get(CURRENT_MAP)[i] = null;
          break;
        }
      }

      gamePanel.getPlayer().setDrawing(true);
      sceneNum = NA;
      scenePhase = 0;
      gamePanel.setGameState(PLAY_STATE);
      gamePanel.stopMusic();
      gamePanel.playMusic(FINAL_BATTLE);
    }
  }
}
