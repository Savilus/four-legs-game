package org.example.entity;

import static org.example.config.GameEntityNameFactory.BOY_ATTACK_DOWN1;
import static org.example.config.GameEntityNameFactory.BOY_ATTACK_DOWN2;
import static org.example.config.GameEntityNameFactory.BOY_ATTACK_LEFT1;
import static org.example.config.GameEntityNameFactory.BOY_ATTACK_LEFT2;
import static org.example.config.GameEntityNameFactory.BOY_ATTACK_RIGHT1;
import static org.example.config.GameEntityNameFactory.BOY_ATTACK_RIGHT2;
import static org.example.config.GameEntityNameFactory.BOY_ATTACK_UP1;
import static org.example.config.GameEntityNameFactory.BOY_ATTACK_UP2;
import static org.example.config.GameEntityNameFactory.BOY_AXE_ATTACK_DOWN1;
import static org.example.config.GameEntityNameFactory.BOY_AXE_ATTACK_DOWN2;
import static org.example.config.GameEntityNameFactory.BOY_AXE_ATTACK_LEFT1;
import static org.example.config.GameEntityNameFactory.BOY_AXE_ATTACK_LEFT2;
import static org.example.config.GameEntityNameFactory.BOY_AXE_ATTACK_RIGHT1;
import static org.example.config.GameEntityNameFactory.BOY_AXE_ATTACK_RIGHT2;
import static org.example.config.GameEntityNameFactory.BOY_AXE_ATTACK_UP1;
import static org.example.config.GameEntityNameFactory.BOY_AXE_ATTACK_UP2;
import static org.example.config.GameEntityNameFactory.BOY_DOWN1;
import static org.example.config.GameEntityNameFactory.BOY_DOWN2;
import static org.example.config.GameEntityNameFactory.BOY_GUARD_DOWN;
import static org.example.config.GameEntityNameFactory.BOY_GUARD_LEFT;
import static org.example.config.GameEntityNameFactory.BOY_GUARD_RIGHT;
import static org.example.config.GameEntityNameFactory.BOY_GUARD_UP;
import static org.example.config.GameEntityNameFactory.BOY_LEFT1;
import static org.example.config.GameEntityNameFactory.BOY_LEFT2;
import static org.example.config.GameEntityNameFactory.BOY_RIGHT1;
import static org.example.config.GameEntityNameFactory.BOY_RIGHT2;
import static org.example.config.GameEntityNameFactory.BOY_UP1;
import static org.example.config.GameEntityNameFactory.BOY_UP2;
import static org.example.config.GameEntityNameFactory.COIN;
import static org.example.config.GameEntityNameFactory.FIREBALL_SOUND;
import static org.example.config.GameEntityNameFactory.GAME_OVER;
import static org.example.config.GameEntityNameFactory.HIT_MONSTER;
import static org.example.config.GameEntityNameFactory.LEVEL_UP;
import static org.example.config.GameEntityNameFactory.RECEIVE_DAMAGE;
import static org.example.config.GameEntityNameFactory.SWING_WEAPON;
import static org.example.enums.DirectionType.DOWN;
import static org.example.enums.DirectionType.LEFT;
import static org.example.enums.DirectionType.RIGHT;
import static org.example.enums.DirectionType.UP;
import static org.example.enums.GameStateType.DIALOG_STATE;
import static org.example.enums.GameStateType.GAME_OVER_STATE;
import static org.example.utils.CollisionDetector.INIT_INDEX;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.stream.IntStream;

import org.example.GamePanel;
import org.example.entity.weapon.Axe;
import org.example.entity.projectile.Fireball;
import org.example.entity.items.Key;
import org.example.entity.weapon.NormalSword;
import org.example.entity.shield.WoodShield;
import org.example.enums.WorldGameTypes;
import org.example.utils.KeyHandler;

public class Player extends GameEntity {

  private static final String PICKED_UP = "Picked up %s !";
  private static final String DAMAGE_UI_MESSAGE = "%s damage";
  private static final String KILLED_UI_MESSAGE = "Killed the %s !";
  private static final String EXP_UI_MESSAGE = "%s + Exp!";
  private static final String INVENTORY_FULL = "Inventory is full!";

  KeyHandler keyHandler;

  public final int screenX;
  public final int screenY;
  public int standCounter = 0;
  public boolean attackCanceled = false;
  public boolean lightUpdated = true;


  public Player(GamePanel gamePanel, KeyHandler keyHandler) {
    super(gamePanel);
    this.keyHandler = keyHandler;
    type = WorldGameTypes.PLAYER;
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
    getImage();
    getAttackImage();
    getGuardImage();
    setItems();
  }

  public void getSleepingImage(BufferedImage image) {
    up1 = image;
    up2 = image;
    down1 = image;
    down2 = image;
    left1 = image;
    left2 = image;
    right1 = image;
    right2 = image;
  }

  public void getAttackImage() {
    if (currentWeapon.type == WorldGameTypes.SWORD) {
      attackUp1 = setup(BOY_ATTACK_UP1, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackUp2 = setup(BOY_ATTACK_UP2, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackDown1 = setup(BOY_ATTACK_DOWN1, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackDown2 = setup(BOY_ATTACK_DOWN2, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackLeft1 = setup(BOY_ATTACK_LEFT1, gamePanel.tileSize * 2, gamePanel.tileSize);
      attackLeft2 = setup(BOY_ATTACK_LEFT2, gamePanel.tileSize * 2, gamePanel.tileSize);
      attackRight1 = setup(BOY_ATTACK_RIGHT1, gamePanel.tileSize * 2, gamePanel.tileSize);
      attackRight2 = setup(BOY_ATTACK_RIGHT2, gamePanel.tileSize * 2, gamePanel.tileSize);
    } else if (currentWeapon.type == WorldGameTypes.AXE) {
      attackUp1 = setup(BOY_AXE_ATTACK_UP1, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackUp2 = setup(BOY_AXE_ATTACK_UP2, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackDown1 = setup(BOY_AXE_ATTACK_DOWN1, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackDown2 = setup(BOY_AXE_ATTACK_DOWN2, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackLeft1 = setup(BOY_AXE_ATTACK_LEFT1, gamePanel.tileSize * 2, gamePanel.tileSize);
      attackLeft2 = setup(BOY_AXE_ATTACK_LEFT2, gamePanel.tileSize * 2, gamePanel.tileSize);
      attackRight1 = setup(BOY_AXE_ATTACK_RIGHT1, gamePanel.tileSize * 2, gamePanel.tileSize);
      attackRight2 = setup(BOY_AXE_ATTACK_RIGHT2, gamePanel.tileSize * 2, gamePanel.tileSize);
    }
  }

  @Override
  public void update() {
    if (knockBack) {
      // CHECK TILE COLLISION
      collisionOn = false;
      gamePanel.collisionDetector.checkTile(this);
      gamePanel.collisionDetector.checkObject(this, true);
      gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsNpc.get(getCurrentMap()));
      gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsMonsters.get(getCurrentMap()));
      gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsInteractiveTiles.get(getCurrentMap()));

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
    } else if (keyHandler.spacePressed) {
      guarding = true;
      guardCounter++;
    } else if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed || keyHandler.enterPressed) {
      if (keyHandler.upPressed) {
        direction = UP;
      } else if (keyHandler.downPressed) {
        direction = DOWN;
      } else if (keyHandler.leftPressed) {
        direction = LEFT;
      } else if (keyHandler.rightPressed) {
        direction = RIGHT;
      }

      // CHECK TILE COLLISION
      collisionOn = false;
      gamePanel.collisionDetector.checkTile(this);

      // CHECK OBJECT COLLISION
      int objectIndex = gamePanel.collisionDetector.checkObject(this, true);
      pickUpObject(objectIndex);

      //CHECK NPC COLLISION
      int npcIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsNpc.get(getCurrentMap()));
      interactNPC(npcIndex);

      // CHECK MONSTER COLLISION
      int monsterIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsMonsters.get(getCurrentMap()));
      detectMonsterContact(monsterIndex);

      // CHECK INTERACTIVE TILE COLLISION
      int interactiveTileIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsInteractiveTiles.get(getCurrentMap()));

      // CHECK EVENT
      gamePanel.eventHandler.checkEvent();

      // IF COLLISION IS FALSE, PLAYER CAN MOVE
      if (!collisionOn && !keyHandler.enterPressed) {
        switch (getDirection()) {
          case UP -> worldY -= speed;
          case DOWN -> worldY += speed;
          case LEFT -> worldX -= speed;
          case RIGHT -> worldX += speed;
        }
      }

      if (keyHandler.enterPressed && !attackCanceled) {
        gamePanel.playSoundEffect(SWING_WEAPON);
        attacking = true;
        spriteCounter = 0;
      }

      attackCanceled = false;
      guarding = false;
      gamePanel.keyHandler.enterPressed = false;
      guardCounter = 0;

      spriteCounter++;
      if (spriteCounter > 12) {
        spriteNum = spriteNum == 1 ? 2 : 1;
        spriteCounter = 0;
      }
    } else {
      standCounter++;
      guarding = false;
      guardCounter = 0;
      if (standCounter == 20) {
        spriteNum = 1;
        standCounter = 0;
      }
    }

    if (keyHandler.shotKeyPressed && !projectile.alive && shootAvailableCounter == 50 &&
        projectile.haveResource(this)) {
      // SET DEFAULT COORDINATES, DIRECTION AND USER
      projectile.set(worldX, worldY, direction, true, this);

      projectile.substractResource(this);

      IntStream.range(0, gamePanel.projectiles.get(getCurrentMap()).length)
          .filter(emptyPlace -> Objects.isNull(gamePanel.projectiles.get(getCurrentMap())[emptyPlace]))
          .findFirst()
          .ifPresent(emptyPlace -> gamePanel.projectiles.get(getCurrentMap())[emptyPlace] = projectile);

      shootAvailableCounter = 0;
      gamePanel.playSoundEffect(FIREBALL_SOUND);
    }

    if (invincible) {
      invincibleCounter++;
      if (invincibleCounter > 60) {
        invincible = false;
        transparent = false;
        invincibleCounter = 0;
      }
    }

    if (shootAvailableCounter < 50) {
      shootAvailableCounter++;
    }

    if (currentLife > maxLife) {
      currentLife = maxLife;
    }

    if (mana > maxMana) {
      mana = maxMana;
    }

    if (currentLife <= 0) {
      gamePanel.gameState = GAME_OVER_STATE;
      gamePanel.ui.commandNum = -1;
      gamePanel.stopMusic();
      gamePanel.playSoundEffect(GAME_OVER);
    }
  }

  void damageProjectile(int projectileIndex) {
    if (projectileIndex != INIT_INDEX) {
      var projectile = gamePanel.projectiles.get(getCurrentMap())[projectileIndex];
      projectile.alive = false;
      generateParticle(projectile, projectile);
    }
  }

  void damageInteractiveTile(int interactiveTileIndex) {

    if (interactiveTileIndex != INIT_INDEX && gamePanel.mapsInteractiveTiles.get(getCurrentMap())[interactiveTileIndex].destructible
        && gamePanel.mapsInteractiveTiles.get(getCurrentMap())[interactiveTileIndex].isCorrectItem(this) && !gamePanel.mapsInteractiveTiles.get(getCurrentMap())[interactiveTileIndex].invincible) {
      gamePanel.mapsInteractiveTiles.get(getCurrentMap())[interactiveTileIndex].playSoundEffect();
      gamePanel.mapsInteractiveTiles.get(getCurrentMap())[interactiveTileIndex].currentLife--;
      gamePanel.mapsInteractiveTiles.get(getCurrentMap())[interactiveTileIndex].invincible = true;

      generateParticle(gamePanel.mapsInteractiveTiles.get(getCurrentMap())[interactiveTileIndex], gamePanel.mapsInteractiveTiles.get(getCurrentMap())[interactiveTileIndex]);

      if (gamePanel.mapsInteractiveTiles.get(getCurrentMap())[interactiveTileIndex].currentLife <= 0)
        gamePanel.mapsInteractiveTiles.get(getCurrentMap())[interactiveTileIndex] = gamePanel.mapsInteractiveTiles.get(getCurrentMap())[interactiveTileIndex].getDestroyedForm();
    }
  }

  public void damageMonster(GameEntity attacker, int monsterIndex, int attack, int knockBackPower) {
    if (monsterIndex != INIT_INDEX && !gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].invincible) {
      gamePanel.playSoundEffect(HIT_MONSTER);
      if (knockBackPower > 0) {
        setKnockBack(gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex], attacker, knockBackPower);
      }

      if (gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].offBalance) {
        attack *= 3;
      }

      int damage = Math.max(0, attack - gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].defense);
      gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].currentLife -= damage;
      gamePanel.ui.addMessage(String.format(DAMAGE_UI_MESSAGE, damage));
      gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].invincible = true;
      gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].damageReaction();

      if (gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].currentLife <= 0) {
        gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].dying = true;
        gamePanel.ui.addMessage(String.format(KILLED_UI_MESSAGE, gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].name));
        gamePanel.ui.addMessage(String.format(EXP_UI_MESSAGE, gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].exp));
        exp += gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].exp;
        checkLevelUp();
      }
    }
  }

  public int getCurrentWeaponSlot() {
    int currentWeaponSlot = 0;
    for (int inventoryIndex = 0; inventory.size() > inventoryIndex; inventoryIndex++) {
      if(inventory.get(inventoryIndex) == currentWeapon){
        currentWeaponSlot = inventoryIndex;
      }
    }
    return currentWeaponSlot;
  }

  public int getCurrentShieldSlot() {
    int currentShieldSlot = 0;
    for (int inventoryIndex = 0; inventory.size() > inventoryIndex; inventoryIndex++) {
      if(inventory.get(inventoryIndex) == currentShield){
        currentShieldSlot = inventoryIndex;
      }
    }
    return currentShieldSlot;
  }

  public void selectItem() {
    int itemIndex = gamePanel.ui.getItemIndexFromInventory(gamePanel.ui.playerSlotCol, gamePanel.ui.playerSlotRow);

    if (itemIndex < inventory.size()) {
      GameEntity selectedItem = inventory.get(itemIndex);

      switch (selectedItem.type) {
        case SWORD, AXE -> {
          currentWeapon = selectedItem;
          attack = getAttack();
          getAttackImage();
        }
        case SHIELD -> {
          currentShield = selectedItem;
          defense = getDefense();
        }
        case CONSUMABLE -> {
          if (selectedItem.use(this))
            if (selectedItem.amount > 1) {
              selectedItem.amount--;
            } else {
              inventory.remove(itemIndex);
            }
        }
        case LIGHTING -> {
          if (currentLightItem == selectedItem) {
            currentLightItem = null;
          } else {
            currentLightItem = selectedItem;
          }
          lightUpdated = true;
        }
      }
    }
  }

  private void checkLevelUp() {
    if (exp >= nextLevelExp) {
      level++;
      nextLevelExp = nextLevelExp * 2;
      maxLife += 2;
      strength++;
      dexterity++;
      attack = getAttack();
      defense = getDefense();
      gamePanel.playSoundEffect(LEVEL_UP);
      gamePanel.gameState = DIALOG_STATE;
      gamePanel.ui.currentDialogue = "You are level " + level + " now!";
    }
  }

  public void getImage() {
    up1 = setup(BOY_UP1, gamePanel.tileSize, gamePanel.tileSize);
    up2 = setup(BOY_UP2, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(BOY_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    down2 = setup(BOY_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    left1 = setup(BOY_LEFT1, gamePanel.tileSize, gamePanel.tileSize);
    up1 = setup(BOY_UP1, gamePanel.tileSize, gamePanel.tileSize);
    left2 = setup(BOY_LEFT2, gamePanel.tileSize, gamePanel.tileSize);
    right1 = setup(BOY_RIGHT1, gamePanel.tileSize, gamePanel.tileSize);
    right2 = setup(BOY_RIGHT2, gamePanel.tileSize, gamePanel.tileSize);
  }

  private void interactNPC(int npcIndex) {

    if (gamePanel.keyHandler.enterPressed) {
      if (npcIndex != INIT_INDEX) {
        attackCanceled = true;
        gamePanel.gameState = DIALOG_STATE;
        gamePanel.mapsNpc.get(getCurrentMap())[npcIndex].speak();
      }
    }
  }

  public void setDefaultValues() {
    worldX = gamePanel.tileSize * 23;
    worldY = gamePanel.tileSize * 21;
//    worldX = gamePanel.tileSize * 12;
//    worldY = gamePanel.tileSize * 13;
    defaultSpeed = 4;
    speed = defaultSpeed;
    direction = DOWN;

    // PLAYER STATUS
    maxLife = 6;
    currentLife = maxLife;
    maxMana = 4;
    mana = maxMana;
    ammo = 10;
    level = 1;
    strength = 1;
    dexterity = 1;
    exp = 0;
    nextLevelExp = 5;
    money = 100;
    currentWeapon = new NormalSword(gamePanel);
    currentShield = new WoodShield(gamePanel);
    projectile = new Fireball(gamePanel);
    attack = getAttack();
    defense = getDefense();

  }

  public void setItems() {
    inventory.clear();
    inventory.add(currentWeapon);
    inventory.add(currentShield);
    inventory.add(new Axe(gamePanel));
    inventory.add(new Key(gamePanel));
  }

  public int getDefense() {
    defense = dexterity * currentShield.defenseValue;
    return defense;
  }

  public int getAttack() {
    attackArea = currentWeapon.attackArea;
    firstAttackMotionDuration = currentWeapon.firstAttackMotionDuration;
    secondAttackMotionDuration = currentWeapon.secondAttackMotionDuration;
    attack = strength * currentWeapon.attackValue;
    return attack;
  }

  private void detectMonsterContact(int monsterIndex) {
    if (monsterIndex != INIT_INDEX && !invincible && !gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].dying) {

      int damage = gamePanel.mapsMonsters.get(getCurrentMap())[monsterIndex].attack - defense;
      if (damage < 1)
        damage = 1;
      currentLife -= damage;
      gamePanel.playSoundEffect(RECEIVE_DAMAGE);
      invincible = true;
      transparent = true;
      currentLife -= 1;
    }
  }

  public void getGuardImage() {
    guardUp = setup(BOY_GUARD_UP, gamePanel.tileSize, gamePanel.tileSize);
    guardDown = setup(BOY_GUARD_DOWN, gamePanel.tileSize, gamePanel.tileSize);
    guardLeft = setup(BOY_GUARD_LEFT, gamePanel.tileSize, gamePanel.tileSize);
    guardRight = setup(BOY_GUARD_RIGHT, gamePanel.tileSize, gamePanel.tileSize);
  }

  public void setDefaultPositions() {

    worldX = gamePanel.tileSize * 23;
    worldY = gamePanel.tileSize * 21;
    direction = DOWN;
  }

  public void restoreLifeAndMana() {
    currentLife = maxLife;
    mana = maxMana;
    invincible = false;
    transparent = false;
  }

  @Override
  public void draw(Graphics2D graphic2d) {
    int temporaryScreenX = screenX;
    int temporaryScreenY = screenY;
    BufferedImage image = switch (getDirection()) {
      case UP -> {
        if (attacking) {
          temporaryScreenY = screenY - gamePanel.tileSize;
          if (spriteNum == 1) yield attackUp1;
          else yield attackUp2;
        } else {
          if (guarding) {
            yield guardUp;
          }
          if (spriteNum == 1) yield up1;
          else yield up2;
        }
      }
      case DOWN -> {
        if (attacking) {
          if (spriteNum == 1) yield attackDown1;
          else yield attackDown2;
        } else {
          if (guarding) {
            yield guardDown;
          }
          if (spriteNum == 1) yield down1;
          else yield down2;
        }
      }
      case LEFT -> {
        if (attacking) {
          temporaryScreenX = screenX - gamePanel.tileSize;
          if (spriteNum == 1) yield attackLeft1;
          else yield attackLeft2;
        } else {
          if (guarding) {
            yield guardLeft;
          }
          if (spriteNum == 1) yield left1;
          else yield left2;
        }
      }
      case RIGHT -> {
        if (attacking) {
          if (spriteNum == 1) yield attackRight1;
          else yield attackRight2;
        } else {
          if (guarding) {
            yield guardRight;
          }
          if (spriteNum == 1) yield right1;
          else yield right2;
        }
      }
      case ANY -> null;
    };


    if (transparent) {
      graphic2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
    }
    graphic2d.drawImage(image, temporaryScreenX, temporaryScreenY, null);
    graphic2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    // COLLISION RECTANGLE
//    graphic2d.setColor(Color.RED);
//    graphic2d.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
  }

  // Use also to check if player has special event item
  // TODO: implement in different places
  public int searchItemInInventory(String itemName) {
    return IntStream.range(0, inventory.size())
        .filter(itemIndex -> inventory.get(itemIndex).name.equals(itemName))
        .findFirst()
        .orElse(INIT_INDEX);
  }

  public boolean canObtainItem(GameEntity item) {
    if (item.stackable) {
      int itemIndex = searchItemInInventory(item.name);

      if (itemIndex != INIT_INDEX) {
        inventory.get(itemIndex).amount++;
        return true;
      }

    }
    if (doesInventoryHaveSpace()) {
      inventory.add(item);
      return true;
    }
    return false;
  }

  public void pickUpObject(int objectIndex) {
    if (objectIndex != INIT_INDEX) {
      var interactedObject = gamePanel.mapsObjects.get(getCurrentMap())[objectIndex];

      switch (interactedObject.type) {
        case PICK_UP -> {
          interactedObject.use(this);
          gamePanel.mapsObjects.get(getCurrentMap())[objectIndex] = null;
        }
        case OBSTACLE -> {
          if (keyHandler.enterPressed) {
            attackCanceled = true;
            gamePanel.mapsObjects.get(getCurrentMap())[objectIndex].interact();
          }
        }
        default -> {
          // INVENTORY ITEMS
          String text;
          if (canObtainItem(interactedObject)) {
            gamePanel.playSoundEffect(COIN);
            text = String.format(PICKED_UP, interactedObject.name);
          } else {
            text = INVENTORY_FULL;
          }
          gamePanel.ui.addMessage(text);
          gamePanel.mapsObjects.get(getCurrentMap())[objectIndex] = null;
        }
      }
    }
  }

  private boolean doesInventoryHaveSpace() {
    return inventory.size() < maxInventorySize;
  }

  private String getCurrentMap() {
    return gamePanel.tileManager.currentMap;
  }
}
