package org.example.entity;

import static org.example.config.GameEntityNameFactory.RECEIVE_DAMAGE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
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
  public int ammo;
  public int exp;
  public int nextLevelExp;
  public int money;
  public int defense;
  public int maxMana;
  public int mana;
  public GameEntity currentWeapon;
  public GameEntity currentShield;
  public Projectile projectile;

  // COUNTER
  public int invincibleCounter = 0;
  public int actionLockCounter = 0;
  public int spriteCounter = 0;
  public int dyingCounter = 0;
  public int hpBarCounter = 0;
  public int shootAvailableCounter = 0;

  // ITEM ATTRIBUTES
  public int attackValue;
  public int defenseValue;
  public String description;
  public int value;
  public final int maxInventorySize = 20;
  public ArrayList<GameEntity> inventory = new ArrayList<>();
  public int price;

  //Type
  public WorldGameTypes type;

  protected GameEntity(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  public void setAction() {
  }

  public void damageReaction() {
  }

  public void use(GameEntity gameEntity) {
  }

  public void speak() {
    if (dialogues[dialogueIndex] == null) {
      dialogueIndex = 0;
    }
    gamePanel.ui.currentDialogue = dialogues[dialogueIndex];
    dialogueIndex++;

    switch (gamePanel.player.getDirection()) {
      case UP -> direction = DirectionType.DOWN;
      case DOWN -> direction = DirectionType.UP;
      case LEFT -> direction = DirectionType.RIGHT;
      case RIGHT -> direction = DirectionType.LEFT;
    }
  }

  public void update() {
    setAction();
    collisionOn = false;
    gamePanel.collisionDetector.checkTile(this);
    gamePanel.collisionDetector.checkObject(this, false);
    gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsNpc.get(gamePanel.tileManager.currentMap));
    gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap));
    gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap));
    boolean contactPlayer = gamePanel.collisionDetector.checkPlayer(this);

    if (this.type == WorldGameTypes.MONSTER && contactPlayer && !gamePanel.player.invincible) {
      damagePlayer(attack);
    }
    // IF COLLISION IS FALSE, PLAYER CAN MOVE
    if (!collisionOn) {
      switch (getDirection()) {
        case UP -> worldY -= speed;
        case DOWN -> worldY += speed;
        case LEFT -> worldX -= speed;
        case RIGHT -> worldX += speed;
      }
    }

    spriteCounter++;
    if (spriteCounter > 24) {
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

    if (shootAvailableCounter < 50) {
      shootAvailableCounter++;
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

      graphics2D.drawImage(image, screenX, screenY, null);

      changeAlpha(graphics2D, 1F);
    }
  }

  public void checkDrop() {
  }

  public void dropItem(GameEntity droppedItem) {
    for (int i = 0; i < gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap).length; i++) {
      if (gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i] == null) {
        gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i] = droppedItem;
        gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].worldX = worldX;
        gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].worldY = worldY;
        break;
      }
    }
  }

  public Color getParticleColor() {
    return Color.WHITE;
  }

  public int getParticleSize() {
    return 0;
  }

  public int getParticleSpeed() {
    return 0;
  }

  public int getParticleMaxLife() {
    return 0;
  }

  public void generateParticle(GameEntity generator, GameEntity target){
    Color color = generator.getParticleColor();
    int size = generator.getParticleSize();
    int speed = generator.getParticleSpeed();
    int maxLife = generator.getParticleMaxLife();

    Particle particle1 = new Particle(gamePanel, target, color, size, speed, maxLife, -2, -1);
    Particle particle2 = new Particle(gamePanel, target, color, size, speed, maxLife, 2, -1);
    Particle particle3 = new Particle(gamePanel, target, color, size, speed, maxLife, -2, 1);
    Particle particle4 = new Particle(gamePanel, target, color, size, speed, maxLife, 2, 1);
    gamePanel.particleList.add(particle1);
    gamePanel.particleList.add(particle2);
    gamePanel.particleList.add(particle3);
    gamePanel.particleList.add(particle4);
  }

  public void damagePlayer(int attack) {
    gamePanel.playSoundEffect(RECEIVE_DAMAGE);
    gamePanel.player.currentLife -= attack - gamePanel.player.defense;
    gamePanel.player.invincible = true;
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
      alive = false;
    }
  }

  private void changeAlpha(Graphics2D graphics2D, float alphaValue) {
    graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
  }
}
