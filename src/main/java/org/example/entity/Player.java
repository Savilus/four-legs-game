package org.example.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.example.GamePanel;
import org.example.KeyHandler;

public class Player extends Entity {

  GamePanel gamePanel;
  KeyHandler keyHandler;

  public Player(GamePanel gamePanel, KeyHandler keyHandler) {
    this.gamePanel = gamePanel;
    this.keyHandler = keyHandler;
    setDefaultValues();
    getPlayerImage();
  }

  public void update() {

    if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
      if (keyHandler.upPressed) {
        direction = "up";
        y -= speed;
      } else if (keyHandler.downPressed) {
        direction = "down";
        y += speed;
      } else if (keyHandler.leftPressed) {
        direction = "left";
        x -= speed;
      } else if (keyHandler.rightPressed) {
        direction = "right";
        x += speed;
      }
      spriteCounter++;
      if (spriteCounter > 12) {
        spriteNum = spriteNum == 1 ? 2 : 1;
        spriteCounter = 0;
      }
    }

  }

  public void getPlayerImage() {
    try {
      up1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_1.png"));
      up2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_up_2.png"));
      down1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_1.png"));
      down2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_down_2.png"));
      left1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_1.png"));
      left2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_left_2.png"));
      right1 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_1.png"));
      right2 = ImageIO.read(getClass().getResourceAsStream("/player/boy_right_2.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void draw(Graphics2D graphic2d) {
    BufferedImage image = switch (direction) {
      case "up" -> {
        if (spriteNum == 1)
          yield up1;
        else if (spriteNum == 2)
          yield up2;
        yield null;
      }
      case "down" -> {
        if (spriteNum == 1)
          yield down1;
        else if (spriteNum == 2)
          yield down2;
        yield null;
      }
      case "left" -> {
        if (spriteNum == 1)
          yield left1;
        else if (spriteNum == 2)
          yield left2;
        yield null;
      }
      case "right" -> {
        if (spriteNum == 1)
          yield right1;
        if (spriteNum == 2)
          yield right2;
        yield null;
      }
      default -> throw new IllegalStateException("Unexpected direction: " + direction);
    };

    graphic2d.drawImage(image, x, y, gamePanel.tileSize, gamePanel.tileSize, null);
  }

  private void setDefaultValues() {
    x = 100;
    y = 100;
    speed = 4;
    direction = "down";
  }
}
