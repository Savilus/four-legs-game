package org.example.entity;

import static org.example.config.GameEntityNameFactory.BLOCKED;
import static org.example.config.GameEntityNameFactory.PARRY;
import static org.example.config.GameEntityNameFactory.RECEIVE_DAMAGE;
import static org.example.enums.DirectionType.DOWN;
import static org.example.enums.DirectionType.LEFT;
import static org.example.enums.DirectionType.RIGHT;
import static org.example.enums.DirectionType.UP;
import static org.example.enums.GameStateType.DIALOG_STATE;
import static org.example.enums.WorldGameTypes.MONSTER;
import static org.example.utils.CollisionDetector.INIT_INDEX;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.example.GamePanel;
import org.example.entity.projectile.Projectile;
import org.example.enums.DirectionType;
import org.example.enums.WorldGameTypes;
import org.example.utils.UtilityTool;
import org.example.utils.text.TextManager;

import io.vavr.control.Try;
import lombok.Getter;

/*
 * SOLID AREA X - WHEN WE START ON AXIS X COUNTING FROM LEFT
 * SOLID AREA Y - WHEN WE START ON AXIS Y COUNTING FROM TOP
 * WIDTH - WIDTH OF THE SOLID AREA, TILE HAS 48 PIXELS
 * HEIGHT - SAME AS WIDTH
 * */
public abstract class GameEntity {

  private static final String EVENT_MESSAGES = "eventMessages";

  protected GamePanel gamePanel;

  public BufferedImage image, image2, image3;
  public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
  public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
  public BufferedImage guardUp, guardDown, guardLeft, guardRight;
  public Rectangle solidArea = new Rectangle(0, 0, 45, 45);
  public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
  public int solidAreaDefaultX, solidAreaDefaultY;
  public Map<String, List<String>> dialogues;
  public boolean collision = false;
  public GameEntity attacker;
  public GameEntity linkedEntity;

  // STATE
  @Getter
  public DirectionType direction = DirectionType.ANY;
  public int spriteNum = 1;
  public int dialogueIndex = 0;
  public String dialogueSet = StringUtils.EMPTY;
  public boolean invincible = false;
  public boolean collisionOn = false;
  public boolean attacking = false;
  public int worldX, worldY;
  public boolean alive = true;
  public boolean dying = false;
  public boolean hpBarOn = false;
  public boolean onPath = false;
  public boolean knockBack = false;
  public DirectionType knockBackDirection;
  public boolean guarding = false;
  public boolean transparent = false;
  public boolean offBalance = false;
  public GameEntity loot;
  public boolean opened = false;

  // CHARACTER ATTRIBUTES
  public int defaultSpeed;
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
  public int firstAttackMotionDuration;
  public int secondAttackMotionDuration;
  public GameEntity currentLightItem;
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
  public int knockBackCounter = 0;
  public int guardCounter = 0;
  public int offBalanceCounter = 0;

  // ITEM ATTRIBUTES
  public int attackValue;
  public int defenseValue;
  public String description;
  public int value;
  public final int maxInventorySize = 20;
  public ArrayList<GameEntity> inventory = new ArrayList<>();
  public int price;
  public int knockBackPower = 0;
  public boolean stackable = false;
  public int amount = 1;
  public int lightRadius;

  //Type
  public WorldGameTypes type;

  public GameEntity(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    dialogues = TextManager.getAllDialoguesForTarget(EVENT_MESSAGES);
  }

  public void setAction() {
  }

  public void damageReaction() {
  }

  public boolean use(GameEntity gameEntity) {
    return true;
  }

  public void interact() {
  }

  public void setLoot(GameEntity loot) {
  }

  public void speak() {
  }

  public void move(DirectionType direction) {
  }

  public void facePlayer() {
    switch (gamePanel.player.getDirection()) {
      case UP -> direction = DOWN;
      case DOWN -> direction = UP;
      case LEFT -> direction = DirectionType.RIGHT;
      case RIGHT -> direction = DirectionType.LEFT;
    }
  }

  public void startDialogue(GameEntity entity, String selectedDialogue) {
    gamePanel.gameState = DIALOG_STATE;
    gamePanel.ui.npc = entity;
    dialogueSet = selectedDialogue;
  }

  public void checkCollision() {
    collisionOn = false;
    gamePanel.collisionDetector.checkTile(this);
    gamePanel.collisionDetector.checkObject(this, false);
    gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsNpc.get(gamePanel.tileManager.currentMap));
    gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap));
    gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap));
    boolean contactPlayer = gamePanel.collisionDetector.checkPlayer(this);

    if (this.type == MONSTER && contactPlayer && !gamePanel.player.invincible) {
      damagePlayer(attack);
    }
  }

  void attacking() {
    spriteCounter++;
    if (spriteCounter <= firstAttackMotionDuration) {
      spriteNum = 1;
    } else if (spriteCounter <= secondAttackMotionDuration) {
      spriteNum = 2;

      // SAVE THE CURRENT worldX, worldY and solid area
      int currentWorldX = worldX;
      int currentWorldY = worldY;
      int currentSolidAreaWidth = solidArea.width;
      int currentSolidAreaHeight = solidArea.height;

      // adjust player's worldX/Y for the attack area
      switch (getDirection()) {
        case UP -> worldY -= attackArea.height;
        case DOWN -> worldY += attackArea.height;
        case LEFT -> worldX -= attackArea.width;
        case RIGHT -> worldX += attackArea.height;
      }
      // attackArea becomes solid area
      solidArea.width = attackArea.width;
      solidArea.height = attackArea.height;

      if (type == MONSTER) {
        if (gamePanel.collisionDetector.checkPlayer(this)) {
          damagePlayer(attackValue);
        }
      } else {
        // check monster collision with updated worldX, worldY and solidArea
        int monsterIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap));
        gamePanel.player.damageMonster(this, monsterIndex, attack, currentWeapon.knockBackPower);

        int interactiveTileIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap));
        gamePanel.player.damageInteractiveTile(interactiveTileIndex);

        int projectileIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.projectiles.get(gamePanel.tileManager.currentMap));
        gamePanel.player.damageProjectile(projectileIndex);
      }

      worldX = currentWorldX;
      worldY = currentWorldY;
      solidArea.width = currentSolidAreaWidth;
      solidArea.height = currentSolidAreaHeight;
    } else if (spriteCounter > secondAttackMotionDuration) {
      spriteNum = 1;
      spriteCounter = 0;
      attacking = false;
    }
  }

  public void update() {
    checkCollision();
    if (knockBack) {
      if (!collisionOn) {
        switch (knockBackDirection) {
          case UP -> worldY -= speed;
          case DOWN -> worldY += speed;
          case LEFT -> worldX -= speed;
          case RIGHT -> worldX += speed;
        }
      }
      knockBackCounter++;
      if (knockBackCounter == 5 || collisionOn) {
        knockBackCounter = 0;
        knockBack = false;
        speed = defaultSpeed;
      }
    } else if (attacking) {
      attacking();
    } else {
      setAction();

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

    if (offBalance) {
      offBalanceCounter++;
      if (offBalanceCounter > 60) {
        offBalance = false;
        offBalanceCounter = 0;
      }
    }
  }

  public void searchPath(int goalCol, int goalRow) {
    int startCol = (worldX + solidArea.x) / gamePanel.tileSize;
    int startRow = (worldY + solidArea.y) / gamePanel.tileSize;

    gamePanel.pathFinder.setNodes(startCol, startRow, goalCol, goalRow);

    if (gamePanel.pathFinder.search()) {
      // Next wordX & worldY
      int nextX = gamePanel.pathFinder.pathList.getFirst().col * gamePanel.tileSize;
      int nextY = gamePanel.pathFinder.pathList.getFirst().row * gamePanel.tileSize;

      // Entity's solid area position
      int entityLeftX = worldX + solidArea.x;
      int entityRightX = worldX + solidArea.x + solidArea.width;
      int entityTopY = worldY + solidArea.y;
      int entityBottomY = worldY + solidArea.y + solidArea.height;


      if (entityLeftX >= nextX && entityRightX < nextX + gamePanel.tileSize) {
        if (entityTopY > nextY) {
          direction = UP;
        } else if (entityTopY < nextY) {
          direction = DOWN;
        }
      } else if (entityTopY >= nextY && entityBottomY < nextY + gamePanel.tileSize) {
        if (entityLeftX > nextX) {
          direction = LEFT;
        } else if (entityLeftX < nextX) {
          direction = RIGHT;
        }
      } else {
        DirectionType primaryDir = (entityTopY > nextY) ? UP : DOWN;
        DirectionType secondaryDir = (entityLeftX > nextX) ? LEFT : RIGHT;
        direction = primaryDir;
        checkCollision();
        if (collisionOn) {
          direction = secondaryDir;
        }
      }

      // If reach the goal, stop the search. Disable for user
      int nextCol = gamePanel.pathFinder.pathList.getFirst().col;
      int nextRow = gamePanel.pathFinder.pathList.getFirst().row;
      if (nextCol == goalCol && nextRow == goalRow) {
        onPath = false;
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

      int temporaryScreenX = screenX;
      int temporaryScreenY = screenY;

      BufferedImage image = switch (getDirection()) {
        case UP -> {
          if (spriteNum == 1)
            yield up1;
          else if (spriteNum == 2)
            yield up2;
          yield null;
        }
        case DOWN -> {
          if (!attacking) {
            if (spriteNum == 1)
              yield down1;
            else if (spriteNum == 2)
              yield down2;
          } else {
            if (spriteNum == 1)
              yield attackDown1;
            else if (spriteNum == 2)
              yield attackDown2;
          }
          yield null;
        }
        case LEFT -> {
          if (!attacking) {
            if (spriteNum == 1)
              yield left1;
            else if (spriteNum == 2)
              yield left2;
          } else {
            temporaryScreenX = screenX - gamePanel.tileSize;
            if (spriteNum == 1)
              yield attackLeft1;
            else if (spriteNum == 2)
              yield attackLeft2;
          }
          yield null;
        }
        case RIGHT -> {
          if (!attacking) {
            if (spriteNum == 1)
              yield right1;
            else if (spriteNum == 2)
              yield right2;
          } else {
            if (spriteNum == 1)
              yield attackRight1;
            else if (spriteNum == 2)
              yield attackRight2;
          }
          yield null;
        }
        case ANY -> this.image;
      };

      // MONSTER HP BAR
      if (type == MONSTER && hpBarOn) {

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

      graphics2D.drawImage(image, temporaryScreenX, temporaryScreenY, null);

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

  public void generateParticle(GameEntity generator, GameEntity target) {
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
    if (!gamePanel.player.invincible) {
      int damage = attack - gamePanel.player.defense;
      // Get and oposite direction of this attacker
      DirectionType canGuardDirection = direction.getOpposite();
      if (gamePanel.player.guarding && gamePanel.player.direction == canGuardDirection) {
        // Parry
        if (gamePanel.player.guardCounter < 10) {
          damage = 0;
          gamePanel.playSoundEffect(PARRY);
          setKnockBack(this, gamePanel.player, knockBackPower);
          offBalance = true;
          spriteCounter = -60;
        } else {
          // normal guard
          damage /= 3;
          gamePanel.playSoundEffect(BLOCKED);
        }
      } else {
        gamePanel.playSoundEffect(RECEIVE_DAMAGE);
        if (damage < 1)
          damage = 1;
      }
      if (damage != 0) {
        gamePanel.player.transparent = true;
        setKnockBack(gamePanel.player, this, this.knockBackPower);
      }
      gamePanel.player.currentLife -= damage;
      gamePanel.player.invincible = true;
    }
  }

  public int getDetected(GameEntity user, Map<String, GameEntity[]> target, String targetName) {
    int index = INIT_INDEX;
    var gameObjects = target.get(gamePanel.tileManager.currentMap);
    // Check the surround object
    int nextWorldX = user.getLeftX();
    int nextWorldY = user.getTopY();

    switch (user.direction) {
      case UP -> nextWorldY = user.getTopY() - gamePanel.player.speed;
      case DOWN -> nextWorldY = user.getBottomY() + gamePanel.player.speed;
      case LEFT -> nextWorldX = user.getLeftX() - gamePanel.player.speed;
      case RIGHT -> nextWorldX = user.getRightX() + gamePanel.player.speed;
    }

    int col = nextWorldX / gamePanel.tileSize;
    int row = nextWorldY / gamePanel.tileSize;

    for (int objectIndex = 0; objectIndex < gameObjects.length; objectIndex++) {
      if (gameObjects[objectIndex] != null) {
        if (gameObjects[objectIndex].getCol() == col
            && gameObjects[objectIndex].getRow() == row && gameObjects[objectIndex].name.equals(targetName)) {
          index = objectIndex;
        }

      }
    }
    return index;
  }

  protected BufferedImage setup(String imagePath, int width, int height) {
    return Try.of(() -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))))
        .map(image -> UtilityTool.scaleImage(image, width, height))
        .getOrElseThrow(() -> new RuntimeException("Failed to load image: " + imagePath));
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

  public int getLeftX() {
    return worldX + solidArea.x;
  }

  public int getRightX() {
    return worldX + solidArea.x + solidArea.width;
  }

  public int getTopY() {
    return worldY + solidArea.y;
  }

  public int getBottomY() {
    return worldY + solidArea.y + solidArea.height;
  }

  public int getCol() {
    return (worldX + solidArea.x) / gamePanel.tileSize;
  }

  public int getRow() {
    return (worldY + solidArea.y) / gamePanel.tileSize;
  }

  public void checkIfShouldAttack(int rate, int straight, int horizontal) {
    boolean targetInRange = false;
    int xDistance = getXDistance(gamePanel.player);
    int yDistance = getYDistance(gamePanel.player);

    switch (direction) {
      case UP -> {
        if (gamePanel.player.worldY < worldY && yDistance < straight && xDistance < horizontal) {
          targetInRange = true;
        }
      }
      case DOWN -> {
        if (gamePanel.player.worldY > worldY && yDistance < straight && xDistance < horizontal) {
          targetInRange = true;
        }
      }
      case LEFT -> {
        if (gamePanel.player.worldX < worldX && xDistance < straight && yDistance < horizontal) {
          targetInRange = true;
        }
      }
      case RIGHT -> {
        if (gamePanel.player.worldX > worldX && xDistance < straight && yDistance < horizontal) {
          targetInRange = true;
        }
      }
    }
    if (targetInRange) {
      int attackNumber = new Random().nextInt(rate);
      if (attackNumber == 0) {
        attacking = true;
        spriteNum = 1;
        spriteCounter = 0;
        shootAvailableCounter = 0;
      }
    }
  }

  public void checkIfShouldShoot(int rate, int shotInterval) {
    int randomNumber = new Random().nextInt(rate);
    if (randomNumber == 0 && !projectile.alive && shootAvailableCounter == shotInterval) {
      projectile.set(worldX, worldY, direction, true, this);
      IntStream.range(0, gamePanel.projectiles.get(gamePanel.tileManager.currentMap).length)
          .filter(emptyPlace -> Objects.isNull(gamePanel.projectiles.get(gamePanel.tileManager.currentMap)[emptyPlace]))
          .findFirst()
          .ifPresent(emptyPlace -> gamePanel.projectiles.get(gamePanel.tileManager.currentMap)[emptyPlace] = projectile);
      shootAvailableCounter = 0;
    }
  }

  public void checkIfShouldStartChasing(GameEntity target, int distance, int rate) {
    if (getTileDistance(target) < distance) {
      int i = new Random().nextInt(rate);
      if (i == 0) {
        onPath = true;
      }
    }
  }

  public void setKnockBack(GameEntity target, GameEntity attacker, int knockBackPower) {
    this.attacker = attacker;
    target.knockBackDirection = attacker.direction;
    if (knockBackPower == 0) return;
    target.speed += knockBackPower;
    target.knockBack = true;
  }

  public void getRandomDirection() {
    actionLockCounter++;
    if (actionLockCounter == 120) {
      int randomNumber = new Random().nextInt(100) + 1;

      if (randomNumber <= 25) direction = DirectionType.UP;
      else if (randomNumber <= 50) direction = DirectionType.DOWN;
      else if (randomNumber <= 75) direction = DirectionType.LEFT;
      else if (randomNumber <= 100) direction = DirectionType.RIGHT;

      actionLockCounter = 0;
    }
  }

  public void checkIfShouldStopChasing(GameEntity target, int distance, int rate) {
    if (getTileDistance(target) > distance) {
      int i = new Random().nextInt(rate);
      if (i == 0) {
        onPath = false;
      }
    }
  }

  public int getXDistance(GameEntity target) {
    return Math.abs(worldX - target.worldX);
  }

  public int getYDistance(GameEntity target) {
    return Math.abs(worldY - target.worldY);
  }

  public int getTileDistance(GameEntity target) {
    return (getXDistance(target) + getYDistance(target)) / gamePanel.tileSize;
  }

  public int getGoalCol(GameEntity target) {
    return (target.worldX + target.solidArea.x) / gamePanel.tileSize;
  }

  public int getGoalRow(GameEntity target) {
    return (target.worldY + target.solidArea.y) / gamePanel.tileSize;
  }

  public void resetCounter() {
    invincibleCounter = 0;
    actionLockCounter = 0;
    spriteCounter = 0;
    dyingCounter = 0;
    hpBarCounter = 0;
    shootAvailableCounter = 0;
    knockBackCounter = 0;
    guardCounter = 0;
    offBalanceCounter = 0;
  }
}
