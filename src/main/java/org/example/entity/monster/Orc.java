package org.example.entity.monster;

import static org.example.config.GameEntityNameFactory.ATTACK_ORC_DOWN1;
import static org.example.config.GameEntityNameFactory.ATTACK_ORC_DOWN2;
import static org.example.config.GameEntityNameFactory.ATTACK_ORC_LEFT1;
import static org.example.config.GameEntityNameFactory.ATTACK_ORC_LEFT2;
import static org.example.config.GameEntityNameFactory.ATTACK_ORC_RIGHT1;
import static org.example.config.GameEntityNameFactory.ATTACK_ORC_RIGHT2;
import static org.example.config.GameEntityNameFactory.ATTACK_ORC_UP1;
import static org.example.config.GameEntityNameFactory.ATTACK_ORC_UP2;
import static org.example.config.GameEntityNameFactory.ORC_DOWN1;
import static org.example.config.GameEntityNameFactory.ORC_DOWN2;
import static org.example.config.GameEntityNameFactory.ORC_LEFT1;
import static org.example.config.GameEntityNameFactory.ORC_LEFT2;
import static org.example.config.GameEntityNameFactory.ORC_RIGHT1;
import static org.example.config.GameEntityNameFactory.ORC_RIGHT2;
import static org.example.config.GameEntityNameFactory.ORC_UP1;
import static org.example.config.GameEntityNameFactory.ORC_UP2;

import java.util.Random;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.entity.object.BronzeCoin;
import org.example.entity.object.Heart;
import org.example.entity.object.ManaCrystal;
import org.example.enums.MonsterObjectType;
import org.example.enums.WorldGameTypes;

public class Orc extends GameEntity {


  public Orc(GamePanel gamePanel) {
    super(gamePanel);
    name = MonsterObjectType.ORC.getName();
    defaultSpeed = 1;
    speed = defaultSpeed;
    maxLife = 7;
    currentLife = maxLife;
    type = WorldGameTypes.MONSTER;
    attack = 5;
    attackValue = 8;
    defense = 2;
    exp = 6;
    knockBackPower = 5;

    solidArea.x = 4;
    solidArea.y = 4;
    solidArea.width = 40;
    solidArea.height = 44;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    attackArea.width = 48;
    attackArea.height = 48;
    firstAttackMotionDuration = 40;
    secondAttackMotionDuration = 85;
    getImage();
    getAttackImage();
  }

  private void getImage() {
    up1 = setup(ORC_UP1, gamePanel.tileSize, gamePanel.tileSize);
    up2 = setup(ORC_UP2, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(ORC_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    down2 = setup(ORC_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    left1 = setup(ORC_LEFT1, gamePanel.tileSize, gamePanel.tileSize);
    left2 = setup(ORC_LEFT2, gamePanel.tileSize, gamePanel.tileSize);
    right1 = setup(ORC_RIGHT1, gamePanel.tileSize, gamePanel.tileSize);
    right2 = setup(ORC_RIGHT2, gamePanel.tileSize, gamePanel.tileSize);
  }

  public void getAttackImage() {
    attackUp1 = setup(ATTACK_ORC_UP1, gamePanel.tileSize, gamePanel.tileSize * 2);
    attackUp2 = setup(ATTACK_ORC_UP2, gamePanel.tileSize, gamePanel.tileSize * 2);
    attackDown1 = setup(ATTACK_ORC_DOWN1, gamePanel.tileSize, gamePanel.tileSize * 2);
    attackDown2 = setup(ATTACK_ORC_DOWN2, gamePanel.tileSize, gamePanel.tileSize * 2);
    attackLeft1 = setup(ATTACK_ORC_LEFT1, gamePanel.tileSize * 2, gamePanel.tileSize);
    attackLeft2 = setup(ATTACK_ORC_LEFT2, gamePanel.tileSize * 2, gamePanel.tileSize);
    attackRight1 = setup(ATTACK_ORC_RIGHT1, gamePanel.tileSize * 2, gamePanel.tileSize);
    attackRight2 = setup(ATTACK_ORC_RIGHT2, gamePanel.tileSize * 2, gamePanel.tileSize);
  }

  @Override
  public void checkDrop() {
    int percentForDrop = new Random().nextInt(100) + 1;

    if (percentForDrop < 50)
      dropItem(new BronzeCoin(gamePanel));
    else if (percentForDrop < 75)
      dropItem(new Heart(gamePanel));
    else dropItem(new ManaCrystal(gamePanel));
  }

  @Override
  public void damageReaction() {
    actionLockCounter = 0;
    onPath = true;
  }

  @Override
  public void setAction() {
    if (onPath) {
      checkIfShouldStopChasing(gamePanel.player, 15, 100);
      searchPath(getGoalCol(gamePanel.player), getGoalRow(gamePanel.player));
    } else {
      getRandomDirection();
      checkIfShouldStartChasing(gamePanel.player, 5, 100);
    }

    if (!attacking) {
      checkIfShouldAttack(30, gamePanel.tileSize * 4, gamePanel.tileSize);
    }
  }
}
