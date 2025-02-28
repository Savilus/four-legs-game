package org.example.entity.monster;

import static org.example.config.GameEntityNameFactory.GREEN_SLIME_DOWN1;
import static org.example.config.GameEntityNameFactory.GREEN_SLIME_DOWN2;

import java.util.Random;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.entity.object.BronzeCoinObject;
import org.example.entity.object.HeartObject;
import org.example.entity.object.ManaCrystalObject;
import org.example.entity.object.RockObject;
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
    projectile = new RockObject(gamePanel);

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
  public void update() {
    super.update();

    int xDistance = Math.abs(worldX - gamePanel.player.worldX);
    int yDistance = Math.abs(worldY - gamePanel.player.worldY);
    int tileDistance = (xDistance + yDistance) / gamePanel.tileSize;

    if (onPath && tileDistance < 5) {
      int i = new Random().nextInt(100) + 1;
      if (i > 50) {
        onPath = true;
      }
    } else if (onPath && tileDistance > 15) {
      onPath = false;
    }
  }

  @Override
  public void checkDrop() {
    int percentForDrop = new Random().nextInt(100) + 1;

    if (percentForDrop < 50)
      dropItem(new BronzeCoinObject(gamePanel));
    else if (percentForDrop < 75)
      dropItem(new HeartObject(gamePanel));
    else dropItem(new ManaCrystalObject(gamePanel));
  }

  @Override
  public void damageReaction() {
    actionLockCounter = 0;
    onPath = true;
  }

  @Override
  public void setAction() {
    Random random = new Random();

    if (onPath) {

      int randomNumber = random.nextInt(200) + 1;
      if (randomNumber > 197 && !projectile.alive && shootAvailableCounter == 50) {
        projectile.set(worldX, worldY, direction, true, this);
        gamePanel.projectiles.add(projectile);
        shootAvailableCounter = 0;
      }

      int goalCol = (gamePanel.player.worldX + gamePanel.player.solidArea.x) / gamePanel.tileSize;
      int goalRow = (gamePanel.player.worldY + gamePanel.player.solidArea.y) / gamePanel.tileSize;
      searchPath(goalCol, goalRow);
    } else {
      actionLockCounter++;

      if (actionLockCounter == 120) {
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
