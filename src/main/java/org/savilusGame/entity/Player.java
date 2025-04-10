package org.savilusGame.entity;

import static org.savilusGame.config.GameEntityNameFactory.BOY_ATTACK_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_ATTACK_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_ATTACK_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_ATTACK_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_ATTACK_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_ATTACK_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_ATTACK_UP1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_ATTACK_UP2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_AXE_ATTACK_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_AXE_ATTACK_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_AXE_ATTACK_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_AXE_ATTACK_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_AXE_ATTACK_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_AXE_ATTACK_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_AXE_ATTACK_UP1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_AXE_ATTACK_UP2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_GUARD_DOWN;
import static org.savilusGame.config.GameEntityNameFactory.BOY_GUARD_LEFT;
import static org.savilusGame.config.GameEntityNameFactory.BOY_GUARD_RIGHT;
import static org.savilusGame.config.GameEntityNameFactory.BOY_GUARD_UP;
import static org.savilusGame.config.GameEntityNameFactory.BOY_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.BOY_UP1;
import static org.savilusGame.config.GameEntityNameFactory.BOY_UP2;
import static org.savilusGame.config.GameEntityNameFactory.COIN;
import static org.savilusGame.config.GameEntityNameFactory.FIREBALL_SOUND;
import static org.savilusGame.config.GameEntityNameFactory.GAME_OVER;
import static org.savilusGame.config.GameEntityNameFactory.HIT_MONSTER;
import static org.savilusGame.config.GameEntityNameFactory.LEVEL_UP;
import static org.savilusGame.config.GameEntityNameFactory.PICK_AXE_DOWN_1;
import static org.savilusGame.config.GameEntityNameFactory.PICK_AXE_DOWN_2;
import static org.savilusGame.config.GameEntityNameFactory.PICK_AXE_LEFT_1;
import static org.savilusGame.config.GameEntityNameFactory.PICK_AXE_LEFT_2;
import static org.savilusGame.config.GameEntityNameFactory.PICK_AXE_RIGHT_1;
import static org.savilusGame.config.GameEntityNameFactory.PICK_AXE_RIGHT_2;
import static org.savilusGame.config.GameEntityNameFactory.PICK_AXE_UP_1;
import static org.savilusGame.config.GameEntityNameFactory.PICK_AXE_UP_2;
import static org.savilusGame.config.GameEntityNameFactory.RECEIVE_DAMAGE;
import static org.savilusGame.config.GameEntityNameFactory.SWING_WEAPON;
import static org.savilusGame.enums.DirectionType.DOWN;
import static org.savilusGame.enums.DirectionType.LEFT;
import static org.savilusGame.enums.DirectionType.RIGHT;
import static org.savilusGame.enums.DirectionType.UP;
import static org.savilusGame.enums.GameStateType.DIALOG_STATE;
import static org.savilusGame.enums.GameStateType.GAME_OVER_STATE;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;
import static org.savilusGame.utils.CollisionDetector.INIT_INDEX;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.savilusGame.GamePanel;
import org.savilusGame.entity.items.Key;
import org.savilusGame.entity.items.Lantern;
import org.savilusGame.entity.projectile.Fireball;
import org.savilusGame.entity.shield.WoodShield;
import org.savilusGame.entity.weapon.Axe;
import org.savilusGame.entity.weapon.NormalSword;
import org.savilusGame.entity.weapon.PickAxe;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.tile.TileManager;
import org.savilusGame.utils.KeyHandler;
import org.savilusGame.utils.text.TextManager;

public class Player extends GameEntity {

  private static final String PICKED_UP = "pickedUp";
  private static final String DAMAGE_UI_MESSAGE = "damageUiMessage";
  private static final String KILLED_UI_MESSAGE = "killedUiMessage";
  private static final String UI_MESSAGE = "uiMessages";
  private static final String LVL_UP = "lvlUp";
  private static final String EXP_UI_MESSAGE = "expUiMessage";
  private static final String INVENTORY_FULL = "inventoryFull";
  private static final String EVENT_MESSAGES_KEY = "eventMessages";

  KeyHandler keyHandler;

  public final int screenX;
  public final int screenY;
  public int standCounter = 0;
  public boolean attackCanceled = false;
  public boolean lightUpdated = true;
  public boolean drawing = true;

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
    } else if (currentWeapon.type == WorldGameTypes.PICKAXE) {
      attackUp1 = setup(PICK_AXE_UP_1, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackUp2 = setup(PICK_AXE_UP_2, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackDown1 = setup(PICK_AXE_DOWN_1, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackDown2 = setup(PICK_AXE_DOWN_2, gamePanel.tileSize, gamePanel.tileSize * 2);
      attackLeft1 = setup(PICK_AXE_LEFT_1, gamePanel.tileSize * 2, gamePanel.tileSize);
      attackLeft2 = setup(PICK_AXE_LEFT_2, gamePanel.tileSize * 2, gamePanel.tileSize);
      attackRight1 = setup(PICK_AXE_RIGHT_1, gamePanel.tileSize * 2, gamePanel.tileSize);
      attackRight2 = setup(PICK_AXE_RIGHT_2, gamePanel.tileSize * 2, gamePanel.tileSize);
    }
  }

  @Override
  public void update() {
    if (knockBack) {
      // CHECK TILE COLLISION
      collisionOn = false;
      gamePanel.collisionDetector.checkTile(this);
      gamePanel.collisionDetector.checkObject(this, true);
      gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsNpc.get(CURRENT_MAP));
      gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsMonsters.get(CURRENT_MAP));
      gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsInteractiveTiles.get(CURRENT_MAP));

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
      int npcIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsNpc.get(CURRENT_MAP));
      interactNPC(npcIndex);

      // CHECK MONSTER COLLISION
      int monsterIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsMonsters.get(CURRENT_MAP));
      detectMonsterContact(monsterIndex);

      // CHECK INTERACTIVE TILE COLLISION
      int interactiveTileIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsInteractiveTiles.get(CURRENT_MAP));

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

      IntStream.range(0, gamePanel.projectiles.get(CURRENT_MAP).length)
          .filter(emptyPlace -> Objects.isNull(gamePanel.projectiles.get(CURRENT_MAP)[emptyPlace]))
          .findFirst()
          .ifPresent(emptyPlace -> gamePanel.projectiles.get(CURRENT_MAP)[emptyPlace] = projectile);

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

    if (!keyHandler.godModeOn && currentLife <= 0) {
      gamePanel.gameState = GAME_OVER_STATE;
      gamePanel.ui.commandNum = -1;
      gamePanel.stopMusic();
      gamePanel.playSoundEffect(GAME_OVER);
    }
  }

  void damageProjectile(int projectileIndex) {
    if (projectileIndex != INIT_INDEX) {
      var projectile = gamePanel.projectiles.get(CURRENT_MAP)[projectileIndex];
      projectile.alive = false;
      generateParticle(projectile, projectile);
    }
  }

  void damageInteractiveTile(int interactiveTileIndex) {

    if (interactiveTileIndex != INIT_INDEX && gamePanel.mapsInteractiveTiles.get(CURRENT_MAP)[interactiveTileIndex].isDestructible()
        && gamePanel.mapsInteractiveTiles.get(CURRENT_MAP)[interactiveTileIndex].isCorrectItem(this) && !gamePanel.mapsInteractiveTiles.get(CURRENT_MAP)[interactiveTileIndex].invincible) {
      gamePanel.mapsInteractiveTiles.get(CURRENT_MAP)[interactiveTileIndex].playSoundEffect();
      gamePanel.mapsInteractiveTiles.get(CURRENT_MAP)[interactiveTileIndex].currentLife--;
      gamePanel.mapsInteractiveTiles.get(CURRENT_MAP)[interactiveTileIndex].invincible = true;

      generateParticle(gamePanel.mapsInteractiveTiles.get(CURRENT_MAP)[interactiveTileIndex], gamePanel.mapsInteractiveTiles.get(CURRENT_MAP)[interactiveTileIndex]);

      if (gamePanel.mapsInteractiveTiles.get(CURRENT_MAP)[interactiveTileIndex].currentLife <= 0)
        gamePanel.mapsInteractiveTiles.get(CURRENT_MAP)[interactiveTileIndex] = gamePanel.mapsInteractiveTiles.get(CURRENT_MAP)[interactiveTileIndex].getDestroyedForm();
    }
  }

  public void damageMonster(GameEntity attacker, int monsterIndex, int attack, int knockBackPower) {
    if (monsterIndex != INIT_INDEX && !gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].invincible) {
      gamePanel.playSoundEffect(HIT_MONSTER);
      if (knockBackPower > 0) {
        setKnockBack(gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex], attacker, knockBackPower);
      }

      if (gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].offBalance) {
        attack *= 3;
      }

      int damage = Math.max(0, attack - gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].defense);
      gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].currentLife -= damage;
      gamePanel.ui.addMessage(String.format(TextManager.getUiText(UI_MESSAGE, DAMAGE_UI_MESSAGE), damage));
      gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].invincible = true;
      gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].damageReaction();

      if (gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].currentLife <= 0) {
        gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].dying = true;
        gamePanel.ui.addMessage(String.format(TextManager.getUiText(UI_MESSAGE, KILLED_UI_MESSAGE), gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].name));
        gamePanel.ui.addMessage(String.format(TextManager.getUiText(UI_MESSAGE, EXP_UI_MESSAGE), gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].exp));
        exp += gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].exp;
        checkLevelUp();
      }
    }
  }

  public int getCurrentWeaponSlot() {
    int currentWeaponSlot = 0;
    for (int inventoryIndex = 0; inventory.size() > inventoryIndex; inventoryIndex++) {
      if (inventory.get(inventoryIndex) == currentWeapon) {
        currentWeaponSlot = inventoryIndex;
      }
    }
    return currentWeaponSlot;
  }

  public int getCurrentShieldSlot() {
    int currentShieldSlot = 0;
    for (int inventoryIndex = 0; inventory.size() > inventoryIndex; inventoryIndex++) {
      if (inventory.get(inventoryIndex) == currentShield) {
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
        case SWORD, AXE, PICKAXE -> {
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

      startDialogue(this, LVL_UP);
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
    if (npcIndex != INIT_INDEX) {

      if (gamePanel.keyHandler.enterPressed) {
        attackCanceled = true;
        gamePanel.mapsNpc.get(CURRENT_MAP)[npcIndex].speak();
      }
      gamePanel.mapsNpc.get(CURRENT_MAP)[npcIndex].move(direction);
    }
  }

  public void setDefaultValues() {
    // DUNGEON 1
    worldX = gamePanel.tileSize * 20;
    worldY = gamePanel.tileSize * 27;
    // dungeon 1 góra
//    worldX = gamePanel.tileSize * 9;
//    worldY = gamePanel.tileSize * 10;
    // main map
//    worldX = gamePanel.tileSize * 23;
//    worldY = gamePanel.tileSize * 21;
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
    strength = 5;
    dexterity = 1;
    exp = 0;
    nextLevelExp = 5;
    money = 100;
    setItems();
    attack = getAttack();
    defense = getDefense();
    dialogues = TextManager.getAllDialoguesForTarget(EVENT_MESSAGES_KEY);
    getImage();
    getAttackImage();
    getGuardImage();
  }

  public void setItems() {
    currentWeapon = new NormalSword(gamePanel);
    currentShield = new WoodShield(gamePanel);
    projectile = new Fireball(gamePanel);
    currentLightItem = new Lantern(gamePanel);
    inventory.clear();
    inventory.add(currentWeapon);
    inventory.add(currentShield);
    inventory.add(new Axe(gamePanel));
    inventory.add(new Key(gamePanel));
    inventory.add(new PickAxe(gamePanel));
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
    if (monsterIndex != INIT_INDEX && !invincible && !gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].dying) {

      int damage = gamePanel.mapsMonsters.get(CURRENT_MAP)[monsterIndex].attack - defense;
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

  public void restorePlayerStatus() {
    currentLife = maxLife;
    mana = maxMana;
    invincible = false;
    transparent = false;
    attacking = false;
    lightUpdated = true;
    knockBack = false;
    speed = defaultSpeed;
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
    if (drawing) {
      graphic2d.drawImage(image, temporaryScreenX, temporaryScreenY, null);
    }
    graphic2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    // COLLISION RECTANGLE
//    graphic2d.setColor(Color.RED);
//    graphic2d.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
  }

  // Use also to check if player has special event item
  // TODO: implement in different places
  public int searchItemInInventory(String itemName) {
    return IntStream.range(0, inventory.size())
        .filter(itemIndex -> StringUtils.equals(inventory.get(itemIndex).name, itemName))
        .findFirst()
        .orElse(INIT_INDEX);
  }

  public boolean canObtainItem(GameEntity item) {
    GameEntity newItem = gamePanel.entityGenerator.getGameEntity(item.name);
    if (newItem.stackable) {
      int itemIndex = searchItemInInventory(item.name);

      if (itemIndex != INIT_INDEX) {
        inventory.get(itemIndex).amount++;
        return true;
      }

    }
    if (doesInventoryHaveSpace()) {
      inventory.add(newItem);
      return true;
    }
    return false;
  }

  public void pickUpObject(int objectIndex) {
    if (objectIndex != INIT_INDEX) {
      var interactedObject = gamePanel.mapsObjects.get(CURRENT_MAP)[objectIndex];

      switch (interactedObject.type) {
        case PICK_UP -> {
          interactedObject.use(this);
          gamePanel.mapsObjects.get(CURRENT_MAP)[objectIndex] = null;
        }
        case OBSTACLE -> {
          if (keyHandler.enterPressed) {
            attackCanceled = true;
            gamePanel.mapsObjects.get(CURRENT_MAP)[objectIndex].interact();
          }
        }
        default -> {
          // INVENTORY ITEMS
          String text;
          if (canObtainItem(interactedObject)) {
            gamePanel.playSoundEffect(COIN);
            text = String.format(TextManager.getUiText(UI_MESSAGE, PICKED_UP), interactedObject.name);
          } else {
            text = TextManager.getUiText(UI_MESSAGE, INVENTORY_FULL);
          }
          gamePanel.ui.addMessage(text);
          gamePanel.mapsObjects.get(CURRENT_MAP)[objectIndex] = null;
        }
      }
    }
  }

  private boolean doesInventoryHaveSpace() {
    return inventory.size() < maxInventorySize;
  }
  
}
