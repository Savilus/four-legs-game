package org.savilusGame.entity.monster;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.GREEN_SLIME_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.GREEN_SLIME_DOWN2;

import java.util.Random;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.items.BronzeCoin;
import org.savilusGame.entity.items.Heart;
import org.savilusGame.entity.items.ManaCrystal;
import org.savilusGame.entity.projectile.Rock;
import org.savilusGame.enums.MonsterObjectType;
import org.savilusGame.enums.WorldGameTypes;

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
    up1 = setup(GREEN_SLIME_DOWN1, TILE_SIZE, TILE_SIZE);
    up2 = setup(GREEN_SLIME_DOWN2, TILE_SIZE, TILE_SIZE);
    down1 = setup(GREEN_SLIME_DOWN1, TILE_SIZE, TILE_SIZE);
    down2 = setup(GREEN_SLIME_DOWN2, TILE_SIZE, TILE_SIZE);
    left1 = setup(GREEN_SLIME_DOWN1, TILE_SIZE, TILE_SIZE);
    left2 = setup(GREEN_SLIME_DOWN2, TILE_SIZE, TILE_SIZE);
    right1 = setup(GREEN_SLIME_DOWN1, TILE_SIZE, TILE_SIZE);
    right2 = setup(GREEN_SLIME_DOWN2, TILE_SIZE, TILE_SIZE);
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
// Enable if you want green slime to shoot a rock
//      checkIfShouldShoot(100, 50);
    } else {
      getRandomDirection(120);
      checkIfShouldStartChasing(gamePanel.player, 5, 100);
    }
  }
}
