package org.savilusGame.entity.monster;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.ATTACK_ORC_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.ATTACK_ORC_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.ATTACK_ORC_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.ATTACK_ORC_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.ATTACK_ORC_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.ATTACK_ORC_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.ATTACK_ORC_UP1;
import static org.savilusGame.config.GameEntityNameFactory.ATTACK_ORC_UP2;
import static org.savilusGame.config.GameEntityNameFactory.ORC_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.ORC_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.ORC_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.ORC_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.ORC_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.ORC_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.ORC_UP1;
import static org.savilusGame.config.GameEntityNameFactory.ORC_UP2;

import java.util.Random;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.items.BronzeCoin;
import org.savilusGame.entity.items.Heart;
import org.savilusGame.entity.items.ManaCrystal;
import org.savilusGame.enums.MonsterObjectType;
import org.savilusGame.enums.WorldGameTypes;

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
    up1 = setup(ORC_UP1, TILE_SIZE, TILE_SIZE);
    up2 = setup(ORC_UP2, TILE_SIZE, TILE_SIZE);
    down1 = setup(ORC_DOWN1, TILE_SIZE, TILE_SIZE);
    down2 = setup(ORC_DOWN2, TILE_SIZE, TILE_SIZE);
    left1 = setup(ORC_LEFT1, TILE_SIZE, TILE_SIZE);
    left2 = setup(ORC_LEFT2, TILE_SIZE, TILE_SIZE);
    right1 = setup(ORC_RIGHT1, TILE_SIZE, TILE_SIZE);
    right2 = setup(ORC_RIGHT2, TILE_SIZE, TILE_SIZE);
  }

  public void getAttackImage() {
    attackUp1 = setup(ATTACK_ORC_UP1, TILE_SIZE, TILE_SIZE * 2);
    attackUp2 = setup(ATTACK_ORC_UP2, TILE_SIZE, TILE_SIZE * 2);
    attackDown1 = setup(ATTACK_ORC_DOWN1, TILE_SIZE, TILE_SIZE * 2);
    attackDown2 = setup(ATTACK_ORC_DOWN2, TILE_SIZE, TILE_SIZE * 2);
    attackLeft1 = setup(ATTACK_ORC_LEFT1, TILE_SIZE * 2, TILE_SIZE);
    attackLeft2 = setup(ATTACK_ORC_LEFT2, TILE_SIZE * 2, TILE_SIZE);
    attackRight1 = setup(ATTACK_ORC_RIGHT1, TILE_SIZE * 2, TILE_SIZE);
    attackRight2 = setup(ATTACK_ORC_RIGHT2, TILE_SIZE * 2, TILE_SIZE);
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
      checkIfShouldStopChasing(gamePanel.getPlayer(), 15, 100);
      searchPath(getGoalCol(gamePanel.getPlayer()), getGoalRow(gamePanel.getPlayer()));
    } else {
      getRandomDirection(120);
      checkIfShouldStartChasing(gamePanel.getPlayer(), 5, 100);
    }

    if (!attacking) {
      checkIfShouldAttack(30, TILE_SIZE * 4, TILE_SIZE);
    }
  }
}
