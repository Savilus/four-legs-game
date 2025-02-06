package org.example.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.GamePanel;
import org.example.enums.DirectionType;
import org.example.utils.UtilityTool;

import lombok.Getter;

/*
 * SOLID AREA X - WHEN WE START ON AXIS X COUNTING FROM LEFT
 * SOLID AREA Y - WHEN WE START ON AXIS Y COUNTING FROM TOP
 * WIDTH - WIDTH OF THE SOLID AREA, TILE HAS 48 PIXELS
 * HEIGHT - SAME AS WIDTH
 * */
public abstract class GameEntity {

  protected GamePanel gamePanel;

  public BufferedImage image, image2, image3;
  public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
  public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
  public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
  public Rectangle attackingArea = new Rectangle(0,0,0,0);
  public int solidAreaDefaultX, solidAreaDefaultY;
  String[] dialogues = new String[20];
  public boolean collision = false;

  // STATE
  @Getter
  public DirectionType direction = DirectionType.ANY;
  public int spriteNum = 1;
  int dialogueIndex = 0;
  public boolean invincible = false;
  public boolean collisionOn = false;
  public boolean attacking = false;
  public int worldX, worldY;

  // CHARACTER ATTRIBUTES
  public int speed;
  public String name;
  public int maxLife;
  public int currentLife;
  public int type;

  // COUNTER
  public int invincibleCounter = 0;
  public int actionLockCounter = 0;
  public int spriteCounter = 0;

  protected GameEntity(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void setAction() {
  }

  public void speak() {
    if (dialogues[dialogueIndex] == null) {
      dialogueIndex = 0;
    }
    gamePanel.ui.currentDialogue = dialogues[dialogueIndex];
    dialogueIndex++;

    switch (gamePanel.player.getDirection()) {
      case UP:
        direction = DirectionType.DOWN;
        break;
      case DOWN:
        direction = DirectionType.UP;
        break;
      case LEFT:
        direction = DirectionType.RIGHT;
        break;
      case RIGHT:
        direction = DirectionType.LEFT;
        break;
    }
  }

  public void update() {
    setAction();
    collisionOn = false;
    gamePanel.collisionDetector.checkTile(this);
    gamePanel.collisionDetector.checkObject(this, false);
    gamePanel.collisionDetector.checkEntity(this, gamePanel.npc);
    gamePanel.collisionDetector.checkEntity(this, gamePanel.monsters);
    boolean contactPlayer = gamePanel.collisionDetector.checkPlayer(this);

    if(this.type == 2 && contactPlayer && !gamePanel.player.invincible) {
      gamePanel.player.currentLife -= 1;
      gamePanel.player.invincible = true;
    }
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

    if (invincible) {
      invincibleCounter++;
      if (invincibleCounter > 40) {
        invincible = false;
        invincibleCounter = 0;
      }
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
        case ANY -> this.image;
      };

      if (invincible) {
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
      }

      graphics2D.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);

      graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
  }

  protected BufferedImage setup(String imagePath, int width, int height) {
    UtilityTool utilityTool = new UtilityTool();
    BufferedImage scaledImage = null;

    try {
      scaledImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
      scaledImage = utilityTool.scaleImage(scaledImage, width, height);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
    return scaledImage;

  }
}
