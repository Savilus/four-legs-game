package org.savilusGame.utils;

import static io.vavr.Predicates.instanceOf;
import static org.savilusGame.config.GameEntityNameFactory.DOOR_OPEN;
import static org.savilusGame.config.GameEntityNameFactory.FINAL_BATTLE;
import static org.savilusGame.enums.GameStateType.PLAY_STATE;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;

import java.awt.*;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.PlayerDummy;
import org.savilusGame.entity.items.IronDoor;
import org.savilusGame.enums.GameStateType;
import org.savilusGame.enums.MonsterObjectType;

public class CutsceneManager {
  private static final String CUTSCENE_DIALOGUE = "cutsceneDialogue";

  GamePanel gamePanel;
  Graphics2D graphics2D;
  public int sceneNum;
  public int scenePhase;

  //scene number
  public final int NA = 0;
  public final int skeletonLord = 1;

  public CutsceneManager(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void draw(Graphics2D graphics2D) {
    this.graphics2D = graphics2D;

    switch (sceneNum) {
      case skeletonLord -> skeletonLordScene();
    }
  }

  private void skeletonLordScene() {
    if (scenePhase == 0) {
      gamePanel.bossBattleOn = true;
      var mapObjects = gamePanel.mapsObjects.get(CURRENT_MAP);
      for (int i = 0; i < mapObjects.length; i++) {
        if (Objects.isNull(gamePanel.mapsObjects.get(CURRENT_MAP)[i])) {
          mapObjects[i] = new IronDoor(gamePanel);
          mapObjects[i].worldX = gamePanel.tileSize * 25;
          mapObjects[i].worldY = gamePanel.tileSize * 28;
          mapObjects[i].temporaryObject = true;
          gamePanel.playSoundEffect(DOOR_OPEN);
          break;
        }
      }

      for (int i = 0; i < gamePanel.mapsNpc.get(CURRENT_MAP).length; i++) {
        if (Objects.isNull(gamePanel.mapsNpc.get(CURRENT_MAP)[i])) {
          PlayerDummy dummy = new PlayerDummy(gamePanel);
          dummy.worldX = gamePanel.player.worldX;
          dummy.worldY = gamePanel.player.worldY;
          dummy.direction = gamePanel.player.direction;

          gamePanel.mapsNpc.get(CURRENT_MAP)[i] = dummy;
          break;
        }
      }

      gamePanel.player.drawing = false;
      scenePhase++;
    }
    if (scenePhase == 1) {
      gamePanel.player.worldY -= 2;

      if (gamePanel.player.worldY < gamePanel.tileSize * 16) {
        scenePhase++;
      }
    }
    if (scenePhase == 2) {
      var monsters = gamePanel.mapsMonsters.get(CURRENT_MAP);
      for (GameEntity monster : monsters) {
        if (Objects.nonNull(monster) &&
            StringUtils.equalsIgnoreCase(monster.name, MonsterObjectType.SKELETON_LORD.getName())) {
          monster.sleep = false;
          gamePanel.ui.npc = monster;
          scenePhase++;
          break;
        }

      }
    }
    if (scenePhase == 3) {
      var monsters = gamePanel.mapsMonsters.get(CURRENT_MAP);
      for (GameEntity monster : monsters) {
        if (Objects.nonNull(monster) &&
            StringUtils.equalsIgnoreCase(monster.name, MonsterObjectType.SKELETON_LORD.getName())) {
          monster.startDialogue(monster,CUTSCENE_DIALOGUE);
          gamePanel.ui.drawDialogueScreen();
          break;
        }
      }
    }
    if (scenePhase == 4) {
      for(int i = 0; i < gamePanel.mapsNpc.get(CURRENT_MAP).length; i++) {
        if(gamePanel.mapsNpc.get(CURRENT_MAP)[i] != null && gamePanel.mapsNpc.get(CURRENT_MAP)[i] instanceof PlayerDummy) {
          gamePanel.player.worldX = gamePanel.mapsNpc.get(CURRENT_MAP)[i].worldX;
          gamePanel.player.worldY = gamePanel.mapsNpc.get(CURRENT_MAP)[i].worldY;
          gamePanel.mapsNpc.get(CURRENT_MAP)[i] = null;
          break;
        }
      }
      gamePanel.player.drawing = true;
      sceneNum = NA;
      scenePhase = 0;
      gamePanel.gameState = PLAY_STATE;
      gamePanel.stopMusic();
      gamePanel.playMusic(FINAL_BATTLE);
    }
  }
}
