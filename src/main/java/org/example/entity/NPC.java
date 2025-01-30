package org.example.entity;

import static org.example.config.GameNameFactory.OLD_MAN_DOWN1;
import static org.example.config.GameNameFactory.OLD_MAN_DOWN2;
import static org.example.config.GameNameFactory.OLD_MAN_LEFT1;
import static org.example.config.GameNameFactory.OLD_MAN_LEFT2;
import static org.example.config.GameNameFactory.OLD_MAN_RIGHT1;
import static org.example.config.GameNameFactory.OLD_MAN_RIGHT2;
import static org.example.config.GameNameFactory.OLD_MAN_UP1;
import static org.example.config.GameNameFactory.OLD_MAN_UP2;

import java.util.Random;

import org.example.GamePanel;
import org.example.enums.DirectionType;

public class NPC extends Entity {

  public NPC(GamePanel gamePanel) {
    super(gamePanel);

    direction = DirectionType.DOWN.getValue();
    speed = 1;
    getPlayerImage();
  }

  public void getPlayerImage() {
    up1 = setup(OLD_MAN_UP1);
    up2 = setup(OLD_MAN_UP2);
    down1 = setup(OLD_MAN_DOWN1);
    down2 = setup(OLD_MAN_DOWN2);
    left1 = setup(OLD_MAN_LEFT1);
    left2 = setup(OLD_MAN_LEFT2);
    right1 = setup(OLD_MAN_RIGHT1);
    right2 = setup(OLD_MAN_RIGHT2);
  }

  @Override
  public void setAction() {
    actionLockCounter++;

    if (actionLockCounter == 120) {
      Random random = new Random();
      int randomNumber = random.nextInt(100) + 1;

      if (randomNumber <= 25) {
        direction = DirectionType.UP.getValue();
      } else if (randomNumber <= 50) {
        direction = DirectionType.DOWN.getValue();
      } else if (randomNumber <= 75) {
        direction = DirectionType.LEFT.getValue();
      } else if (randomNumber <= 100) {
        direction = DirectionType.RIGHT.getValue();
      }
      actionLockCounter = 0;
    }

  }


}
