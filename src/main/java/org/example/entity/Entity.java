package org.example.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.GamePanel;
import org.example.enums.DirectionType;
import org.example.utility.UtilityTool;

public abstract class Entity {

  GamePanel gamePanel;
  public int worldX, worldY;
  public int speed;

  public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
  public String direction;

  public int spriteCounter = 0;
  public int spriteNum = 1;

  public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
  public int solidAreaDefaultX, solidAreaDefaultY;
  public boolean collisionOn = false;
  public int actionLockCounter = 0;
  String[] dialogues = new String[20];
  int dialogueIndex = 0;

  // CHARACTER STATUS
  public int maxLife;
  public int currentLife;

  public Entity(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public DirectionType getDirection() {
    return DirectionType.fromString(direction);
  }

  public void setAction() {}
  public void speak() {
    if (dialogues[dialogueIndex] == null) {
      dialogueIndex = 0;
    }
    gamePanel.ui.currentDialogue = dialogues[dialogueIndex];
    dialogueIndex++;

    switch (gamePanel.player.getDirection()) {
      case UP:
        direction = DirectionType.DOWN.getValue();
        break;
      case DOWN:
        direction = DirectionType.UP.getValue();
        break;
      case LEFT:
        direction = DirectionType.RIGHT.getValue();
        break;
      case RIGHT:
        direction = DirectionType.LEFT.getValue();
        break;
    }
  }

  public void update() {
    setAction();
    collisionOn = false;
    gamePanel.collisionDetector.checkTile(this);
    gamePanel.collisionDetector.checkObject(this, false);
    gamePanel.collisionDetector.checkPlayer(this);
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
  }

  public void draw(Graphics2D graphics2D) {
    int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
    int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

    if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
        worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
        worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
        worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {

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
        case ANY -> null;
      };
      graphics2D.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
    }
  }

  protected BufferedImage setup(String imagePath) {
    UtilityTool utilityTool = new UtilityTool();
    BufferedImage scaledImage = null;

    try {
      scaledImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
      scaledImage = utilityTool.scaleImage(scaledImage, gamePanel.tileSize, gamePanel.tileSize);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return scaledImage;

  }
}
