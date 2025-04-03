package org.example.entity.monster;

import static org.example.config.GameEntityNameFactory.BAT_DOWN1;
import static org.example.config.GameEntityNameFactory.BAT_DOWN2;

import java.util.Random;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.entity.items.BronzeCoin;
import org.example.entity.items.Heart;
import org.example.entity.items.ManaCrystal;
import org.example.enums.MonsterObjectType;
import org.example.enums.WorldGameTypes;

public class Bat extends GameEntity {

  public Bat(GamePanel gamePanel) {
    super(gamePanel);
    name = MonsterObjectType.BAT.getName();
    defaultSpeed = 4;
    speed = defaultSpeed;
    maxLife = 2;
    currentLife = maxLife;
    type = WorldGameTypes.MONSTER;
    attack = 7;
    defense = 0;
    exp = 7;

    solidArea.x = 3;
    solidArea.y = 15;
    solidArea.width = 42;
    solidArea.height = 21;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    getImage();
  }

  private void getImage() {
    up1 = setup(BAT_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    up2 = setup(BAT_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(BAT_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    down2 = setup(BAT_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    left1 = setup(BAT_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    left2 = setup(BAT_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    right1 = setup(BAT_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    right2 = setup(BAT_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
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
    getRandomDirection(10);
  }
}
