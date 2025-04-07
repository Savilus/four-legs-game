package org.savilusGame.entity.monster;

import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_UP1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_UP2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_UP1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_UP2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_UP1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_UP2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_UP1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_UP2;

import java.util.Random;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.items.BronzeCoin;
import org.savilusGame.entity.items.Heart;
import org.savilusGame.entity.items.ManaCrystal;
import org.savilusGame.enums.MonsterObjectType;
import org.savilusGame.enums.WorldGameTypes;

public class SkeletonLord extends GameEntity {

  public final int bossScale = 5;

  public SkeletonLord(GamePanel gamePanel) {
    super(gamePanel);
    name = MonsterObjectType.SKELETON_LORD.getName();
    boss = true;
    defaultSpeed = 1;
    speed = defaultSpeed;
    maxLife = 10;
    currentLife = maxLife;
    type = WorldGameTypes.MONSTER;
    attack = 10;
    defense = 2;
    exp = 50;
    knockBackPower = 5;

    int size = gamePanel.tileSize * 5;
    solidArea.x = 48;
    solidArea.y = 48;
    solidArea.width = size - 48 * 2;
    solidArea.height = size - 48;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    attackArea.width = 170;
    attackArea.height = 170;
    firstAttackMotionDuration = 25;
    secondAttackMotionDuration = 50;
    getImage();
    getAttackImage();
  }

  private void getImage() {

    if (!inRage) {
      up1 = setup(SKELETON_LORD_UP1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      up2 = setup(SKELETON_LORD_UP2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      down1 = setup(SKELETON_LORD_DOWN1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      down2 = setup(SKELETON_LORD_DOWN2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      left1 = setup(SKELETON_LORD_LEFT1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      left2 = setup(SKELETON_LORD_LEFT2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      right1 = setup(SKELETON_LORD_RIGHT1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      right2 = setup(SKELETON_LORD_RIGHT2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
    } else {
      up1 = setup(SKELETON_LORD_PHASE2_UP1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      up2 = setup(SKELETON_LORD_PHASE2_UP2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      down1 = setup(SKELETON_LORD_PHASE2_DOWN1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      down2 = setup(SKELETON_LORD_PHASE2_DOWN2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      left1 = setup(SKELETON_LORD_PHASE2_LEFT1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      left2 = setup(SKELETON_LORD_PHASE2_LEFT2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      right1 = setup(SKELETON_LORD_PHASE2_RIGHT1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
      right2 = setup(SKELETON_LORD_PHASE2_RIGHT2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale);
    }

  }

  public void getAttackImage() {
    if (!inRage) {
      attackUp1 = setup(SKELETON_LORD_ATTACK_UP1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale * 2);
      attackUp2 = setup(SKELETON_LORD_ATTACK_UP2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale * 2);
      attackDown1 = setup(SKELETON_LORD_ATTACK_DOWN1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale * 2);
      attackDown2 = setup(SKELETON_LORD_ATTACK_DOWN2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale * 2);
      attackLeft1 = setup(SKELETON_LORD_ATTACK_LEFT1, gamePanel.tileSize * bossScale * 2, gamePanel.tileSize * bossScale);
      attackLeft2 = setup(SKELETON_LORD_ATTACK_LEFT2, gamePanel.tileSize * bossScale * 2, gamePanel.tileSize * bossScale);
      attackRight1 = setup(SKELETON_LORD_ATTACK_RIGHT1, gamePanel.tileSize * bossScale * 2, gamePanel.tileSize * bossScale);
      attackRight2 = setup(SKELETON_LORD_ATTACK_RIGHT2, gamePanel.tileSize * bossScale * 2, gamePanel.tileSize * bossScale);
    } else {
      attackUp1 = setup(SKELETON_LORD_ATTACK_PHASE2_UP1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale * 2);
      attackUp2 = setup(SKELETON_LORD_ATTACK_PHASE2_UP2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale * 2);
      attackDown1 = setup(SKELETON_LORD_ATTACK_PHASE2_DOWN1, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale * 2);
      attackDown2 = setup(SKELETON_LORD_ATTACK_PHASE2_DOWN2, gamePanel.tileSize * bossScale, gamePanel.tileSize * bossScale * 2);
      attackLeft1 = setup(SKELETON_LORD_ATTACK_PHASE2_LEFT1, gamePanel.tileSize * bossScale * 2, gamePanel.tileSize * bossScale);
      attackLeft2 = setup(SKELETON_LORD_ATTACK_PHASE2_LEFT2, gamePanel.tileSize * bossScale * 2, gamePanel.tileSize * bossScale);
      attackRight1 = setup(SKELETON_LORD_ATTACK_PHASE2_RIGHT1, gamePanel.tileSize * bossScale * 2, gamePanel.tileSize * bossScale);
      attackRight2 = setup(SKELETON_LORD_ATTACK_PHASE2_RIGHT2, gamePanel.tileSize * bossScale * 2, gamePanel.tileSize * bossScale);
    }
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
  }

  @Override
  public void setAction() {

    if (!inRage && currentLife < maxLife / 2) {
      inRage = true;
      getImage();
      getAttackImage();
      defaultSpeed++;
      speed = defaultSpeed;
      attack *= 2;
    }

    if (getTileDistance(gamePanel.player) < 8) {
      moveTowardPlayer(60);
    } else {
      getRandomDirection(120);
    }

    if (!attacking) {
      checkIfShouldAttack(60, gamePanel.tileSize * 7, gamePanel.tileSize * 5);
    }
  }
}

