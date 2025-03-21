package org.example.entity.monster;

import static org.example.config.GameEntityNameFactory.GREEN_SLIME_DOWN1;
import static org.example.config.GameEntityNameFactory.GREEN_SLIME_DOWN2;

import java.util.Random;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.entity.items.BronzeCoin;
import org.example.entity.items.Heart;
import org.example.entity.items.ManaCrystal;
import org.example.entity.projectile.Rock;
import org.example.enums.MonsterObjectType;
import org.example.enums.WorldGameTypes;

public class GreenSlime extends GameEntity {

  public GreenSlime(GamePanel gamePanel) {
    super(gamePanel);
    name = MonsterObjectType.GREEN_SLIME.getName();
    defaultSpeed = 1;
    speed = defaultSpeed;
    maxLife = 4;
    currentLife = maxLife;
    type = WorldGameTypes.MONSTER;
    attack = 5;
    defense = 0;
    exp = 2;
    projectile = new Rock(gamePanel);

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
      checkIfShouldShoot(100, 50);
    } else {
      getRandomDirection();
      checkIfShouldStartChasing(gamePanel.player, 5, 100);
    }
  }
}
