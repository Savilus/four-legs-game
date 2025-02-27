package org.example.entity.npc;

import static org.example.config.GameEntityNameFactory.OLD_MAN_DOWN1;
import static org.example.config.GameEntityNameFactory.OLD_MAN_DOWN2;
import static org.example.config.GameEntityNameFactory.OLD_MAN_LEFT1;
import static org.example.config.GameEntityNameFactory.OLD_MAN_LEFT2;
import static org.example.config.GameEntityNameFactory.OLD_MAN_RIGHT1;
import static org.example.config.GameEntityNameFactory.OLD_MAN_RIGHT2;
import static org.example.config.GameEntityNameFactory.OLD_MAN_UP1;
import static org.example.config.GameEntityNameFactory.OLD_MAN_UP2;

import java.util.Random;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.DirectionType;
import org.example.enums.WorldGameTypes;

public class OldManNPC extends GameEntity {

  public OldManNPC(GamePanel gamePanel) {
    super(gamePanel);

    direction = DirectionType.DOWN;
    speed = 1;
    type = WorldGameTypes.NPC;
    getPlayerImage();
    setDialogue();
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

  private void setDialogue() {
    dialogues[0] = "Hello, lad";
    dialogues[1] = "So you've come to this island \n to find the treasure?";
    dialogues[2] = "I used to be a great wizard but now... \n I'm a bit too old for taking and adventure";
    dialogues[3] = "Well, good luck on you";
  }

  @Override
  public void speak() {
    super.speak();
    onPath = true;
  }

  @Override
  public void setAction() {
    if (onPath) {
      int goalCol = 12;
      int goalRow = 9;
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
