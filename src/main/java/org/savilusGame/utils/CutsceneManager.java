package org.savilusGame.utils;

import static org.savilusGame.GamePanel.TILE_SIZE;
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
import org.savilusGame.enums.MonsterObjectType;

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
    var mapNpcs = gamePanel.mapsNpc.get(CURRENT_MAP);

    if (scenePhase == 0) {
      gamePanel.bossBattleOn = true;
      var mapObjects = gamePanel.mapsObjects.get(CURRENT_MAP);
      for (int i = 0; i < mapObjects.length; i++) {
        if (Objects.isNull(gamePanel.mapsObjects.get(CURRENT_MAP)[i])) {
          mapObjects[i] = new IronDoor(gamePanel);
          mapObjects[i].worldX = TILE_SIZE * 25;
          mapObjects[i].worldY = TILE_SIZE * 28;
          mapObjects[i].temporaryObject = true;
          gamePanel.playSoundEffect(DOOR_OPEN);
          break;
        }
      }

      for (int i = 0; i < mapNpcs.length; i++) {
        if (Objects.isNull(mapNpcs[i])) {
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
      if (gamePanel.player.worldY < TILE_SIZE * 16) {
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
          monster.startDialogue(monster, CUTSCENE_DIALOGUE);
          gamePanel.ui.drawDialogueScreen();
          break;
        }
      }
    }

    if (scenePhase == 4) {
      for (int i = 0; i < mapNpcs.length; i++) {
        if (Objects.nonNull(mapNpcs[i]) && mapNpcs[i] instanceof PlayerDummy) {
          gamePanel.player.worldX = mapNpcs[i].worldX;
          gamePanel.player.worldY = mapNpcs[i].worldY;
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
