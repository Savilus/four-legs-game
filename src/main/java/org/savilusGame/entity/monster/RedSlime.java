package org.savilusGame.entity.monster;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.RED_SLIME_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.RED_SLIME_DOWN2;

import java.util.Random;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.items.BronzeCoin;
import org.savilusGame.entity.items.Heart;
import org.savilusGame.entity.items.ManaCrystal;
import org.savilusGame.entity.projectile.Rock;
import org.savilusGame.enums.MonsterType;
import org.savilusGame.enums.WorldGameTypes;

public class RedSlime extends GameEntity {

  public RedSlime(GamePanel gamePanel) {
    super(gamePanel);
    name = MonsterType.RED_SLIME.getName();
    defaultSpeed = 2;
    speed = defaultSpeed;
    maxLife = 8;
    currentLife = maxLife;
    type = WorldGameTypes.MONSTER;
    attack = 7;
    defense = 0;
    exp = 5;
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
    up1 = setup(RED_SLIME_DOWN1, TILE_SIZE, TILE_SIZE);
    up2 = setup(RED_SLIME_DOWN2, TILE_SIZE, TILE_SIZE);
    down1 = setup(RED_SLIME_DOWN1, TILE_SIZE, TILE_SIZE);
    down2 = setup(RED_SLIME_DOWN2, TILE_SIZE, TILE_SIZE);
    left1 = setup(RED_SLIME_DOWN1, TILE_SIZE, TILE_SIZE);
    left2 = setup(RED_SLIME_DOWN2, TILE_SIZE, TILE_SIZE);
    right1 = setup(RED_SLIME_DOWN1, TILE_SIZE, TILE_SIZE);
    right2 = setup(RED_SLIME_DOWN2, TILE_SIZE, TILE_SIZE);
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
      checkIfShouldShoot(100, 50);
    } else {
      getRandomDirection(80);
      checkIfShouldStartChasing(gamePanel.getPlayer(), 5, 100);
    }
  }
}
