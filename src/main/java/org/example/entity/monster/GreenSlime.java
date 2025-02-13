package org.example.entity.monster;

import static org.example.config.GameEntityNameFactory.GREEN_SLIME_DOWN1;
import static org.example.config.GameEntityNameFactory.GREEN_SLIME_DOWN2;

import java.util.Random;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.enums.DirectionType;
import org.example.enums.MonsterObjectType;
import org.example.enums.WorldGameTypes;

public class GreenSlime extends GameEntity {

  public GreenSlime(GamePanel gamePanel) {
    super(gamePanel);
    name = MonsterObjectType.GREEN_SLIME.getName();
    speed = 1;
    maxLife = 4;
    currentLife = maxLife;
    type = WorldGameTypes.MONSTER;
    attack = 5;
    defense = 0;
    exp = 2;

    solidArea.x = 3;
    solidArea.y = 18;
    solidArea.width = 42;
    solidArea.height = 30;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    getImage();
  }

  private void getImage() {
    up1 = setup(GREEN_SLIME_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    up2 = setup(GREEN_SLIME_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(GREEN_SLIME_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    down2 = setup(GREEN_SLIME_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    left1 = setup(GREEN_SLIME_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    left2 = setup(GREEN_SLIME_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    right1 = setup(GREEN_SLIME_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    right2 = setup(GREEN_SLIME_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
  }

  @Override
  public void damageReaction() {
    actionLockCounter = 0;
    direction = gamePanel.player.getDirection();
  }

  @Override
  public void setAction() {
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
