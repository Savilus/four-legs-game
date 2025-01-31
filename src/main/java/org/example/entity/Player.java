package org.example.entity;

import static org.example.config.GameNameFactory.BOY_DOWN1;
import static org.example.config.GameNameFactory.BOY_DOWN2;
import static org.example.config.GameNameFactory.BOY_LEFT1;
import static org.example.config.GameNameFactory.BOY_LEFT2;
import static org.example.config.GameNameFactory.BOY_RIGHT1;
import static org.example.config.GameNameFactory.BOY_RIGHT2;
import static org.example.config.GameNameFactory.BOY_UP1;
import static org.example.config.GameNameFactory.BOY_UP2;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.example.GamePanel;
import org.example.enums.DirectionType;
import org.example.utility.KeyHandler;

public class Player extends Entity {

  KeyHandler keyHandler;

  public final int screenX;
  public final int screenY;
  public int standCounter = 0;

  public Player(GamePanel gamePanel, KeyHandler keyHandler) {
    super(gamePanel);
    this.keyHandler = keyHandler;

    screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
    screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

    solidArea = new Rectangle();
    solidArea.x = 8;
    solidArea.y = 16;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    solidArea.width = 32;
    solidArea.height = 32;

    setDefaultValues();
    getPlayerImage();
  }

  public void update() {

    if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
      if (keyHandler.upPressed) {
        direction = DirectionType.UP.getValue();
      } else if (keyHandler.downPressed) {
        direction = DirectionType.DOWN.getValue();
      } else if (keyHandler.leftPressed) {
        direction = DirectionType.LEFT.getValue();
      } else if (keyHandler.rightPressed) {
        direction = DirectionType.RIGHT.getValue();
      }

      // CHECK TILE COLLISION
      collisionOn = false;
      gamePanel.collisionDetector.checkTile(this);

      // CHECK OBJECT COLLISION
      int objectIndex = gamePanel.collisionDetector.checkObject(this, true);
      pickUpObject(objectIndex);

      //CHECK NPC COLLISION
      int npcIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.npc);
      interactNPC(npcIndex);

      // IF COLLISION IS FALSE, PLAYER CAN MOVE
      if (!collisionOn) {
        switch (getDirection()) {
          case UP:
            worldY -= speed;
            break;
          case DOWN:
            worldY += speed;
            break;
          case LEFT:
            worldX -= speed;
            break;
          case RIGHT:
            worldX += speed;
            break;
        }
      }

      spriteCounter++;
      if (spriteCounter > 12) {
        spriteNum = spriteNum == 1 ? 2 : 1;
        spriteCounter = 0;
      }
    } else {
      standCounter++;
      if (standCounter == 20) {
        spriteNum = 1;
        standCounter = 0;
      }
    }

  }


  public void getPlayerImage() {
    up1 = setup(BOY_UP1);
    up2 = setup(BOY_UP2);
    down1 = setup(BOY_DOWN1);
    down2 = setup(BOY_DOWN2);
    left1 = setup(BOY_LEFT1);
    up1 = setup(BOY_UP1);
    left2 = setup(BOY_LEFT2);
    right1 = setup(BOY_RIGHT1);
    right2 = setup(BOY_RIGHT2);
  }

  public void draw(Graphics2D graphic2d) {
    BufferedImage image = switch (getDirection()) {
      case UP -> {
        if (spriteNum == 1)
          yield up1;
        else if (spriteNum == 2)
          yield up2;
        yield null;
      }
      case DOWN -> {
        if (spriteNum == 1)
          yield down1;
        else if (spriteNum == 2)
          yield down2;
        yield null;
      }
      case LEFT -> {
        if (spriteNum == 1)
          yield left1;
        else if (spriteNum == 2)
          yield left2;
        yield null;
      }
      case RIGHT -> {
        if (spriteNum == 1)
          yield right1;
        if (spriteNum == 2)
          yield right2;
        yield null;
      }
    };

    graphic2d.drawImage(image, screenX, screenY, null);

    // COLLISION RECTANGLE
    graphic2d.setColor(Color.RED);
    graphic2d.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
  }

  public void pickUpObject(int objectIndex) {
    if (objectIndex != 999) {
    }
  }


  private void interactNPC(int npcIndex) {
    if (npcIndex != 999) {
      if (gamePanel.keyHandler.enterPressed) {
        gamePanel.gameState = gamePanel.dialogueState;
        gamePanel.npc[npcIndex].speak();
      }
    }
    gamePanel.keyHandler.enterPressed = false;
  }

  private void setDefaultValues() {
    worldX = gamePanel.tileSize * 23;
    worldY = gamePanel.tileSize * 21;
    speed = 4;
    direction = DirectionType.DOWN.getValue();
  }

}
