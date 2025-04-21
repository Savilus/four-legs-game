package org.savilusGame.entity;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.BLOCKED;
import static org.savilusGame.config.GameEntityNameFactory.PARRY;
import static org.savilusGame.config.GameEntityNameFactory.RECEIVE_DAMAGE;
import static org.savilusGame.enums.Direction.DOWN;
import static org.savilusGame.enums.Direction.LEFT;
import static org.savilusGame.enums.Direction.RIGHT;
import static org.savilusGame.enums.Direction.UP;
import static org.savilusGame.enums.GameState.CUTSCENE_STATE;
import static org.savilusGame.enums.GameState.DIALOG_STATE;
import static org.savilusGame.enums.WorldGameTypes.MONSTER;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;
import static org.savilusGame.utils.CollisionDetector.INIT_INDEX;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.savilusGame.GamePanel;
import org.savilusGame.entity.items.Chest;
import org.savilusGame.entity.projectile.Projectile;
import org.savilusGame.enums.Direction;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.UtilityTool;
import org.savilusGame.utils.text.TextManager;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/*
 * SOLID AREA X - WHEN WE START ON AXIS X COUNTING FROM LEFT
 * SOLID AREA Y - WHEN WE START ON AXIS Y COUNTING FROM TOP
 * WIDTH - WIDTH OF THE SOLID AREA, TILE HAS 48 PIXELS
 * HEIGHT - SAME AS WIDTH
 * */
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class GameEntity {

  private static final String EVENT_MESSAGES = "eventMessages";
  private static final int PARRY_WINDOW = 10;
  private static final int DELAY_SPRITE_CHANGE = -60;
  private static final int INVINCIBLE_TIME = 40;
  private static final int SHOOT_INTERVAL = 50;
  private static final int OFF_BALANCE_TIME = 60;
  private static final int KNOCK_BACK_TIME = 5;
  private static final int SPRITE_CHANGE_TIME = 24;

  final GamePanel gamePanel;

  BufferedImage mainImage, mainImage2, mainImage3;
  BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
  BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
  BufferedImage guardUp, guardDown, guardLeft, guardRight;
  Rectangle solidArea = new Rectangle(0, 0, 45, 45);
  Rectangle attackArea = new Rectangle(0, 0, 0, 0);
  int solidAreaDefaultX, solidAreaDefaultY;
  Map<String, List<String>> dialogues;
  boolean collision = false;
  GameEntity attacker;
  GameEntity linkedEntity;

  // STATE
  private String dialogueSet = StringUtils.EMPTY;
  private int dialogueIndex = 0;
  Direction direction = Direction.ANY;
  int spriteNum = 1;
  boolean invincible = false;
  boolean collisionOn = false;
  boolean attacking = false;
  int worldX, worldY;
  boolean alive = true;
  boolean dying = false;
  private boolean hpBarOn = false;
  boolean onPath = false;
  boolean knockBack = false;
  Direction knockBackDirection;
  boolean guarding = false;
  boolean transparent = false;
  boolean offBalance = false;
  GameEntity loot;
  boolean opened = false;
  boolean inRage = false;
  boolean sleep = false;
  private boolean temporaryObject = false;

  // CHARACTER ATTRIBUTES
  int defaultSpeed;
  int speed;
  String name;
  int maxLife;
  int currentLife;
  int level;
  int strength;
  int attack;
  int dexterity;
  int ammo;
  int exp;
  int nextLevelExp;
  int money;
  int defense;
  int maxMana;
  int mana;
  int firstAttackMotionDuration;
  int secondAttackMotionDuration;
  boolean boss;
  GameEntity currentLightItem;
  GameEntity currentWeapon;
  GameEntity currentShield;
  Projectile projectile;

  // COUNTER
  int invincibleCounter = 0;
  int actionLockCounter = 0;
  int spriteCounter = 0;
  int dyingCounter = 0;
  int hpBarCounter = 0;
  int shootAvailableCounter = 0;
  int knockBackCounter = 0;
  int guardCounter = 0;
  int offBalanceCounter = 0;

  // ITEM ATTRIBUTES
  int attackValue;
  int defenseValue;
  String description;
  int value;
  final int maxInventorySize = 20;
  ArrayList<GameEntity> inventory = new ArrayList<>();
  int price;
  int knockBackPower = 0;
  boolean stackable = false;
  int amount = 1;
  int lightRadius;

  //Type
  WorldGameTypes type;

  public GameEntity(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    dialogues = TextManager.getAllDialoguesForTarget(EVENT_MESSAGES);
  }

  public void startDialogue(GameEntity entity, String selectedDialogue) {
    if (gamePanel.getGameState() != CUTSCENE_STATE) {
      gamePanel.setGameState(DIALOG_STATE);
    }
    gamePanel.getUi().setDialogueObject(entity);
    dialogueSet = selectedDialogue;
  }

  public void update() {
    checkCollision();
    if (!sleep) {
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
        if (knockBackCounter == KNOCK_BACK_TIME || collisionOn) {
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
        if (spriteCounter > SPRITE_CHANGE_TIME) {
          spriteNum = spriteNum == 1 ? 2 : 1;
          spriteCounter = 0;
        }
      }

      if (invincible) {
        invincibleCounter++;
        if (invincibleCounter > INVINCIBLE_TIME) {
          invincible = false;
          invincibleCounter = 0;
        }
      }

      if (shootAvailableCounter < SHOOT_INTERVAL) {
        shootAvailableCounter++;
      }

      if (offBalance) {
        offBalanceCounter++;
        if (offBalanceCounter > OFF_BALANCE_TIME) {
          offBalance = false;
          offBalanceCounter = 0;
        }
      }
    }
  }

  public boolean inCamera() {
    return worldX + TILE_SIZE * 5 > getPlayer().worldX - getPlayer().getScreenX() &&
        worldX - TILE_SIZE < getPlayer().worldX + getPlayer().getScreenX() &&
        worldY + TILE_SIZE * 5 > getPlayer().worldY - getPlayer().getScreenY() &&
        worldY - TILE_SIZE < getPlayer().worldY + getPlayer().getScreenY();
  }

  public void draw(Graphics2D graphics2D) {
    if (inCamera()) {
      int temporaryScreenX = getScreenX();
      int temporaryScreenY = getScreenY();

      BufferedImage image = switch (getDirection()) {
        case UP -> {
          if (!attacking) {
            yield (spriteNum == 1) ? up1 : up2;
          } else {
            temporaryScreenY = getScreenY() - up1.getHeight();
            yield (spriteNum == 1) ? attackUp1 : attackUp2;
          }
        }
        case DOWN -> {
          if (!attacking) {
            yield (spriteNum == 1) ? down1 : down2;
          } else {
            yield (spriteNum == 1) ? attackDown1 : attackDown2;
          }
        }
        case LEFT -> {
          if (!attacking) {
            yield (spriteNum == 1) ? left1 : left2;
          } else {
            temporaryScreenX = getScreenX() - left1.getWidth();
            yield (spriteNum == 1) ? attackLeft1 : attackLeft2;
          }
        }
        case RIGHT -> {
          if (!attacking) {
            yield (spriteNum == 1) ? right1 : right2;
          } else {
            yield (spriteNum == 1) ? attackRight1 : attackRight2;
          }
        }
        case ANY -> this.mainImage;
      };

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

  public void dropItem(GameEntity droppedItem) {
    droppedItem.worldX = worldX;
    droppedItem.worldY = worldY;
    gamePanel.getMapsObjects()
        .computeIfAbsent(CURRENT_MAP, key -> new ArrayList<>())
        .add(droppedItem);
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

  public int getDetected(GameEntity user, Map<String, List<GameEntity>> target, String targetName) {
    int index = INIT_INDEX;
    var gameObjects = target.get(CURRENT_MAP);
    // Check the surround object
    int nextWorldX = user.getLeftX();
    int nextWorldY = user.getTopY();
    int playerSpeed = getPlayer().speed;
    switch (user.direction) {
      case UP -> nextWorldY = user.getTopY() - playerSpeed;
      case DOWN -> nextWorldY = user.getBottomY() + playerSpeed;
      case LEFT -> nextWorldX = user.getLeftX() - playerSpeed;
      case RIGHT -> nextWorldX = user.getRightX() + playerSpeed;
    }

    int col = nextWorldX / TILE_SIZE;
    int row = nextWorldY / TILE_SIZE;

    for (int objectIndex = 0; objectIndex < gameObjects.size(); objectIndex++) {
      var gameObject = gameObjects.get(objectIndex);
      if (Objects.nonNull(gameObject)) {
        if (gameObject.getCol() == col
            && gameObject.getRow() == row && gameObject.name.equals(targetName)) {
          index = objectIndex;
        }
      }
    }

    return index;
  }

  public void getRandomDirection(int interval) {
    actionLockCounter++;
    if (actionLockCounter > interval) {
      int randomNumber = new Random().nextInt(100) + 1;

      if (randomNumber <= 25) direction = Direction.UP;
      else if (randomNumber <= 50) direction = Direction.DOWN;
      else if (randomNumber <= 75) direction = Direction.LEFT;
      else if (randomNumber <= 100) direction = Direction.RIGHT;

      actionLockCounter = 0;
    }
  }

  public GameEntity setWorldPosition(int worldX, int worldY) {
    this.worldX = worldX;
    this.worldY = worldY;
    return this;
  }

  public int getScreenX() {
    return worldX - getPlayer().worldX + getPlayer().getScreenX();
  }

  public int getScreenY() {
    return worldY - getPlayer().worldY + getPlayer().getScreenY();
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

  public boolean use(GameEntity gameEntity) {
    return true;
  }

  public Chest setLoot(GameEntity loot) {
    return null;
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
        if (gamePanel.getCollisionDetector().checkPlayer(this)) {
          damagePlayer(attackValue);
        }
      } else {
        // check monster collision with updated worldX, worldY and solidArea
        int monsterIndex = gamePanel.getCollisionDetector().checkEntity(this, gamePanel.getMapsMonsters().get(CURRENT_MAP));
        getPlayer().damageMonster(this, monsterIndex, attack, currentWeapon.knockBackPower);

        int interactiveTileIndex = gamePanel.getCollisionDetector().checkEntity(
            this,
            Optional.ofNullable(gamePanel.getMapsInteractiveTiles().get(CURRENT_MAP))
                .orElse(Collections.emptyList())
                .stream()
                .map(tile -> (GameEntity) tile)
                .collect(Collectors.toList())
        );

        getPlayer().damageInteractiveTile(interactiveTileIndex);

        int projectileIndex = gamePanel.getCollisionDetector().checkEntity(this, gamePanel.getProjectiles().get(CURRENT_MAP));
        getPlayer().damageProjectile(projectileIndex);
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

  int getCenterX() {
    return worldX + up1.getHeight() / 2;
  }

  int getCenterY() {
    return worldY + up1.getWidth() / 2;
  }

  protected void searchPath(int goalCol, int goalRow) {
    int startCol = (worldX + solidArea.x) / TILE_SIZE;
    int startRow = (worldY + solidArea.y) / TILE_SIZE;

    gamePanel.getPathFinder().setNodes(startCol, startRow, goalCol, goalRow);

    if (gamePanel.getPathFinder().search()) {
      var nextNode = gamePanel.getPathFinder().getPathList().getFirst();
      int nextX = nextNode.getCol() * TILE_SIZE;
      int nextY = nextNode.getRow() * TILE_SIZE;

      int entityLeftX = worldX + solidArea.x;
      int entityRightX = entityLeftX + solidArea.width;
      int entityTopY = worldY + solidArea.y;
      int entityBottomY = entityTopY + solidArea.height;

      if (entityLeftX >= nextX && entityRightX < nextX + TILE_SIZE) {
        direction = (entityTopY > nextY) ? UP : (entityTopY < nextY ? DOWN : direction);
      } else if (entityTopY >= nextY && entityBottomY < nextY + TILE_SIZE) {
        direction = (entityLeftX > nextX) ? LEFT : (entityLeftX < nextX ? RIGHT : direction);
      } else {
        // Prefer vertical movement, fallback to horizontal if blocked.
        Direction primaryDir = (entityTopY > nextY) ? UP : DOWN;
        Direction secondaryDir = (entityLeftX > nextX) ? LEFT : RIGHT;

        direction = primaryDir;
        checkCollision();

        if (collisionOn) {
          direction = secondaryDir;
        }
      }

      if (nextNode.getCol() == goalCol && nextNode.getRow() == goalRow) {
        onPath = false;
      }
    }
  }

  protected void moveTowardPlayer(int interval) {
    actionLockCounter++;

    if (actionLockCounter > interval) {
      Player player = gamePanel.getPlayer();

      int xDistance = getXDistance(player);
      int yDistance = getYDistance(player);

      if (xDistance > yDistance) {
        direction = (player.getCenterX() < getCenterX()) ? LEFT : RIGHT;
      } else {
        direction = (player.getCenterY() < getCenterY()) ? UP : DOWN;
      }

      actionLockCounter = 0;
    }
  }

  protected void checkCollision() {
    collisionOn = false;
    gamePanel.getCollisionDetector().checkTile(this);
    gamePanel.getCollisionDetector().checkObject(this, false);
    gamePanel.getCollisionDetector().checkEntity(this, gamePanel.getMapsNpc().get(CURRENT_MAP));
    gamePanel.getCollisionDetector().checkEntity(this, gamePanel.getMapsMonsters().get(CURRENT_MAP));
    gamePanel.getCollisionDetector().checkEntity(this, new ArrayList<>(
        Optional.ofNullable(gamePanel.getMapsInteractiveTiles().get(CURRENT_MAP))
            .orElse(Collections.emptyList())
    ));
    boolean contactPlayer = gamePanel.getCollisionDetector().checkPlayer(this);

    if (this.type == MONSTER && contactPlayer && !gamePanel.getPlayer().invincible) {
      damagePlayer(attack);
    }
  }

  protected void generateParticle(GameEntity generator, GameEntity target) {
    Color color = generator.getParticleColor();
    int size = generator.getParticleSize();
    int speed = generator.getParticleSpeed();
    int maxLife = generator.getParticleMaxLife();

    int[][] directions = {
        {-2, -1}, {2, -1}, {-2, 1}, {2, 1}
    };

    for (int[] direction : directions) {
      var particle = new Particle(gamePanel, target, color, size, speed, maxLife, direction[0], direction[1]);
      gamePanel.getParticleList().add(particle);
    }
  }

  protected void facePlayer() {
    if (Objects.isNull(gamePanel.getPlayer())) return;

    switch (gamePanel.getPlayer().getDirection()) {
      case UP -> direction = DOWN;
      case DOWN -> direction = UP;
      case LEFT -> direction = Direction.RIGHT;
      case RIGHT -> direction = Direction.LEFT;
    }
  }

  protected void damagePlayer(int attack) {

    if (getPlayer() == null || getPlayer().invincible) return;

    int damage = attack - getPlayer().defense;
    Direction canGuardDirection = direction.getOpposite();
    if (getPlayer().guarding && getPlayer().direction == canGuardDirection) {
      if (getPlayer().guardCounter < PARRY_WINDOW) {
        damage = 0;
        gamePanel.playSoundEffect(PARRY);
        setKnockBack(this, getPlayer(), knockBackPower);
        offBalance = true;
        spriteCounter = DELAY_SPRITE_CHANGE;
      } else {
        damage = Math.max(damage / 3, 1);
        gamePanel.playSoundEffect(BLOCKED);
      }
    } else {
      gamePanel.playSoundEffect(RECEIVE_DAMAGE);
      damage = Math.max(damage, 1);
    }

    if (damage > 0) {
      getPlayer().transparent = true;
      setKnockBack(getPlayer(), this, knockBackPower);
    }

    getPlayer().currentLife -= damage;
    getPlayer().invincible = true;
  }


  protected BufferedImage setup(String imagePath, int width, int height) {
    return Try.of(() -> ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath))))
        .map(image -> UtilityTool.scaleImage(image, width, height))
        .getOrElseThrow(() -> new RuntimeException("Failed to load image: " + imagePath));
  }

  protected int getTileDistance(GameEntity target) {
    return (getXDistance(target) + getYDistance(target)) / TILE_SIZE;
  }

  protected int getGoalCol(GameEntity target) {
    return (target.worldX + target.solidArea.x) / TILE_SIZE;
  }

  protected int getGoalRow(GameEntity target) {
    return (target.worldY + target.solidArea.y) / TILE_SIZE;
  }

  protected void checkIfShouldAttack(int rate, int straight, int horizontal) {
    boolean targetInRange = false;
    int xDistance = getXDistance(getPlayer());
    int yDistance = getYDistance(getPlayer());

    switch (direction) {
      case UP -> {
        if (getPlayer().getCenterY() < getCenterY() && yDistance < straight && xDistance < horizontal) {
          targetInRange = true;
        }
      }
      case DOWN -> {
        if (getPlayer().getCenterY() > getCenterY() && yDistance < straight && xDistance < horizontal) {
          targetInRange = true;
        }
      }
      case LEFT -> {
        if (getPlayer().getCenterX() < getCenterX() && xDistance < straight && yDistance < horizontal) {
          targetInRange = true;
        }
      }
      case RIGHT -> {
        if (getPlayer().getCenterX() > getCenterX() && xDistance < straight && yDistance < horizontal) {
          targetInRange = true;
        }
      }
    }
    if (targetInRange) {
      int randomChance = new Random().nextInt(rate);
      if (randomChance == 0) {
        attacking = true;
        spriteNum = 1;
        spriteCounter = 0;
        shootAvailableCounter = 0;
      }
    }
  }

  protected void checkIfShouldShoot(int rate, int shotInterval) {
    int randomNumber = new Random().nextInt(rate);
    if (randomNumber == 0 && !projectile.isAlive() && shootAvailableCounter == shotInterval) {
      projectile.set(worldX, worldY, direction, true, this);
      List<GameEntity> projectilesList = gamePanel.getProjectiles().get(CURRENT_MAP);
      projectilesList.add(projectile);
      shootAvailableCounter = 0;
    }
  }

  protected void checkIfShouldStopChasing(GameEntity target, int distance, int rate) {
    if (getTileDistance(target) > distance) {
      int randomChance = new Random().nextInt(rate);
      if (randomChance == 0) {
        onPath = false;
      }
    }
  }

  protected void checkIfShouldStartChasing(GameEntity target, int distance, int rate) {
    if (getTileDistance(target) < distance) {
      int randomChance = new Random().nextInt(rate);
      if (randomChance == 0) {
        onPath = true;
      }
    }
  }

  protected void setKnockBack(GameEntity target, GameEntity attacker, int knockBackPower) {
    this.attacker = attacker;
    target.knockBackDirection = attacker.direction;
    if (knockBackPower == 0) return;
    target.speed += knockBackPower;
    target.knockBack = true;
  }

  private void dyingAnimation(Graphics2D graphics2D) {
    dyingCounter++;
    int dyingAnimationInterval = 5;
    int totalPhases = 8;

    if (dyingCounter <= dyingAnimationInterval * totalPhases) {
      boolean visible = ((dyingCounter / dyingAnimationInterval) % 2) == 1;
      changeAlpha(graphics2D, visible ? 1F : 0F);
    } else {
      alive = false;
    }
  }

  private void changeAlpha(Graphics2D graphics2D, float alphaValue) {
    graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
  }

  private int getLeftX() {
    return worldX + solidArea.x;
  }

  private int getRightX() {
    return worldX + solidArea.x + solidArea.width;
  }

  private int getTopY() {
    return worldY + solidArea.y;
  }

  private int getBottomY() {
    return worldY + solidArea.y + solidArea.height;
  }

  private int getCol() {
    return (worldX + solidArea.x) / TILE_SIZE;
  }

  private int getRow() {
    return (worldY + solidArea.y) / TILE_SIZE;
  }

  private int getXDistance(GameEntity target) {
    return Math.abs(getCenterX() - target.getCenterX());
  }

  private int getYDistance(GameEntity target) {
    return Math.abs(getCenterY() - target.getCenterY());
  }

  private Player getPlayer() {
    return gamePanel.getPlayer();
  }

  public void setAction() {
  }

  public void damageReaction() {
  }

  public void speak() {
  }

  public void interact() {
  }

  public void checkDrop() {
  }

  public void move(Direction direction) {
  }

}
