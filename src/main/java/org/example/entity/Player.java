package org.example.entity;

import static org.example.config.GameNameFactory.boyDown1Image;
import static org.example.config.GameNameFactory.boyDown2Image;
import static org.example.config.GameNameFactory.boyLeft1Image;
import static org.example.config.GameNameFactory.boyLeft2Image;
import static org.example.config.GameNameFactory.boyRight1Image;
import static org.example.config.GameNameFactory.boyRight2Image;
import static org.example.config.GameNameFactory.boyUp1Image;
import static org.example.config.GameNameFactory.boyUp2Image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.GamePanel;
import org.example.enums.DirectionType;
import org.example.utility.KeyHandler;
import org.example.utility.UtilityTool;

public class Player extends Entity {


  GamePanel gamePanel;
  KeyHandler keyHandler;

  public final int screenX;
  public final int screenY;
  public int standCounter = 0;

  public Player(GamePanel gamePanel, KeyHandler keyHandler) {
    this.gamePanel = gamePanel;
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
      DirectionType directionType = getDirection();

      // IF COLLISION IS FALSE, PLAYER CAN MOVE
      if (!collisionOn) {
        switch (directionType) {
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
    up1 = setup(boyUp1Image);
    up2 = setup(boyUp2Image);
    down1 = setup(boyDown1Image);
    down2 = setup(boyDown2Image);
    left1 = setup(boyLeft1Image);
    up1 = setup(boyUp1Image);
    left2 = setup(boyLeft2Image);
    right1 = setup(boyRight1Image);
    right2 = setup(boyRight2Image);
  }

  public void draw(Graphics2D graphic2d) {
    DirectionType directionType = getDirection();
    BufferedImage image = switch (directionType) {
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

  private void setDefaultValues() {
    worldX = gamePanel.tileSize * 23;
    worldY = gamePanel.tileSize * 21;
    speed = 4;
    direction = DirectionType.DOWN.getValue();
  }

  private BufferedImage setup(String imageName) {
    UtilityTool utilityTool = new UtilityTool();
    BufferedImage scaledImage = null;

    try {
      scaledImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imageName)));
      scaledImage = utilityTool.scaleImage(scaledImage, gamePanel.tileSize, gamePanel.tileSize);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return scaledImage;

  }

}
