package org.savilusGame.entity.npc;

import static org.savilusGame.config.GameEntityNameFactory.OLD_MAN_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.OLD_MAN_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.OLD_MAN_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.OLD_MAN_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.OLD_MAN_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.OLD_MAN_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.OLD_MAN_UP1;
import static org.savilusGame.config.GameEntityNameFactory.OLD_MAN_UP2;

import java.util.List;
import java.util.Random;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.DirectionType;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class OldManNPC extends GameEntity {

  private static final String OLD_MAN_DIALOGUES_KEY = "oldManNpc";
  private int dialogueSelector = 0;

  public OldManNPC(GamePanel gamePanel) {
    super(gamePanel);

    direction = DirectionType.DOWN;
    speed = 1;
    type = WorldGameTypes.INTERACTIVE;
    getPlayerImage();
    dialogues = TextManager.getAllDialoguesForTarget(OLD_MAN_DIALOGUES_KEY);
  }

  public void getPlayerImage() {
    up1 = setup(OLD_MAN_UP1, gamePanel.tileSize, gamePanel.tileSize);
    up2 = setup(OLD_MAN_UP2, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(OLD_MAN_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    down2 = setup(OLD_MAN_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    left1 = setup(OLD_MAN_LEFT1, gamePanel.tileSize, gamePanel.tileSize);
    left2 = setup(OLD_MAN_LEFT2, gamePanel.tileSize, gamePanel.tileSize);
    right1 = setup(OLD_MAN_RIGHT1, gamePanel.tileSize, gamePanel.tileSize);
    right2 = setup(OLD_MAN_RIGHT2, gamePanel.tileSize, gamePanel.tileSize);
  }

  @Override
  public void speak() {
    facePlayer();
    List<String> dialogueKeys = TextManager.getDialoguesKeysForTarget(OLD_MAN_DIALOGUES_KEY);
    if (dialogueKeys.isEmpty()) {
      return;
    }
    String currentDialogueKey = dialogueKeys.get(dialogueSelector);
    startDialogue(this, currentDialogueKey);
    dialogueSelector = (dialogueSelector + 1) % dialogueKeys.size();
  }

  @Override
  public void setAction() {
    if (onPath) {
      int goalCol = 12;
      int goalRow = 9;

// Follow Player
//      int goalCol = (gamePanel.player.worldX + gamePanel.player.solidArea.x) / gamePanel.tileSize;
//      int goalRow = (gamePanel.player.worldY + gamePanel.player.solidArea.y) / gamePanel.tileSize;
      searchPath(goalCol, goalRow);
    } else {
      actionLockCounter++;

      if (actionLockCounter == 120) {
        Random random = new Random();
        int randomNumber = random.nextInt(100) + 1;

        if (randomNumber <= 25) direction = DirectionType.UP;
        else if (randomNumber <= 50) direction = DirectionType.DOWN;
        else if (randomNumber <= 75) direction = DirectionType.LEFT;
        else if (randomNumber <= 100) direction = DirectionType.RIGHT;

        actionLockCounter = 0;
      }
    }
  }


}
