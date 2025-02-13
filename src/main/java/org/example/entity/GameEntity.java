package org.example.entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.GamePanel;
import org.example.enums.DirectionType;
import org.example.enums.WorldGameTypes;
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
  public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
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
  public boolean alive = true;
  public boolean dying = false;
  public boolean hpBarOn = false;

  // CHARACTER ATTRIBUTES
  public int speed;
  public String name;
  public int maxLife;
  public int currentLife;
  public int level;
  public int strength;
  public int attack;
  public int dexterity;
  public int exp;
  public int nextLevelExp;
  public int money;
  public int defense;
  public GameEntity currentWeapon;
  public GameEntity currentShield;

  // COUNTER
  public int invincibleCounter = 0;
  public int actionLockCounter = 0;
  public int spriteCounter = 0;
  public int dyingCounter = 0;
  public int hpBarCounter = 0;

  // ITEM ATTRIBUTES
  public int attackValue;
  public int defenseValue;
  public String description;

  //Type
  public WorldGameTypes type;

  protected GameEntity(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void setAction() {
  }

  public void damageReaction() {
  }
  public void use(GameEntity gameEntity){};

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

    if (this.type == WorldGameTypes.MONSTER && contactPlayer && !gamePanel.player.invincible) {
      gamePanel.playSoundEffect(6);
      gamePanel.player.currentLife -= attack - gamePanel.player.defense;
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

      // MONSTER HP BAR
      if (type == WorldGameTypes.MONSTER && hpBarOn) {

        double oneScaleLifeBar = (double) gamePanel.tileSize / maxLife;
        double hpBarValue = oneScaleLifeBar * currentLife;

        graphics2D.setColor(new Color(35, 35, 35));
        graphics2D.fillRect(screenX - 1, screenY - 16, gamePanel.tileSize + 2, 12);

        graphics2D.setColor(new Color(255, 0, 30));
        graphics2D.fillRect(screenX, screenY - 15, (int) hpBarValue, 10);

        hpBarCounter++;

        if (hpBarCounter > 600) {
          hpBarCounter = 0;
          hpBarOn = false;
        }

      }

      if (invincible) {
        hpBarOn = true;
        hpBarCounter = 0;
        changeAlpha(graphics2D, 0.6F);
      }

      if (dying) {
        dyingAnimation(graphics2D);
      }

      graphics2D.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);

      changeAlpha(graphics2D, 1F);
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

  private void dyingAnimation(Graphics2D graphics2D) {
    dyingCounter++;
    int dyingAnimationInterval = 5;

    if (dyingCounter <= dyingAnimationInterval) {
      changeAlpha(graphics2D, 0F);
    } else if (dyingCounter <= dyingAnimationInterval * 2) {
      changeAlpha(graphics2D, 1F);
    } else if (dyingCounter <= dyingAnimationInterval * 3) {
      changeAlpha(graphics2D, 0F);
    } else if (dyingCounter <= dyingAnimationInterval * 4) {
      changeAlpha(graphics2D, 1F);
    } else if (dyingCounter <= dyingAnimationInterval * 5) {
      changeAlpha(graphics2D, 1F);
    } else if (dyingCounter <= dyingAnimationInterval * 6) {
      changeAlpha(graphics2D, 0F);
    } else if (dyingCounter <= dyingAnimationInterval * 7) {
      changeAlpha(graphics2D, 1F);
    } else if (dyingCounter <= dyingAnimationInterval * 8) {
      changeAlpha(graphics2D, 0F);
    } else if (dyingCounter > dyingAnimationInterval * 8) {
      dying = false;
      alive = false;
    }
  }

  private void changeAlpha(Graphics2D graphics2D, float alphaValue) {
    graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
  }
}
