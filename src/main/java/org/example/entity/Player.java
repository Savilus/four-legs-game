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
import static org.example.enums.DirectionType.RIGHT;
import static org.example.enums.GameStateType.DIALOG_STATE;
import static org.example.enums.GameStateType.GAME_OVER_STATE;
import static org.example.utils.CollisionDetector.INIT_INDEX;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.example.GamePanel;
import org.example.entity.object.AxeObject;
import org.example.entity.object.FireballObject;
import org.example.entity.object.NormalSwordObject;
import org.example.entity.object.WoodShieldObject;
import org.example.enums.DirectionType;
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
    getPlayerImage();
    getPlayerAttackImage();
    setItems();
  }

  public void getPlayerAttackImage() {
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

  public void update() {

    if (attacking) {
      playerAttacking();
    } else if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed || keyHandler.enterPressed) {
      if (keyHandler.upPressed) {
        direction = DirectionType.UP;
      } else if (keyHandler.downPressed) {
        direction = DirectionType.DOWN;
      } else if (keyHandler.leftPressed) {
        direction = DirectionType.LEFT;
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
      int npcIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsNpc.get(gamePanel.tileManager.currentMap));
      interactNPC(npcIndex);

      // CHECK MONSTER COLLISION
      int monsterIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap));
      detectMonsterContact(monsterIndex);

      // CHECK INTERACTIVE TILE COLLISION
      int interactiveTileIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap));

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
      gamePanel.keyHandler.enterPressed = false;

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

    if (keyHandler.shotKeyPressed && !projectile.alive && shootAvailableCounter == 50 &&
        projectile.haveResource(this)) {
      // SET DEFAULT COORDINATES, DIRECTION AND USER
      projectile.set(worldX, worldY, direction, true, this);

      projectile.substractResource(this);

      gamePanel.projectiles.add(projectile);
      shootAvailableCounter = 0;
      gamePanel.playSoundEffect(FIREBALL_SOUND);
    }

    if (invincible) {
      invincibleCounter++;
      if (invincibleCounter > 60) {
        invincible = false;
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

  private void playerAttacking() {
    spriteCounter++;
    if (spriteCounter <= 5) {
      spriteNum = 1;
    } else if (spriteCounter < 25) {
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
      // check monster collision with updated worldX, worldY and solidArea
      int monsterIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap));
      damageMonster(monsterIndex, attack);

      int interactiveTileIndex = gamePanel.collisionDetector.checkEntity(this, gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap));
      damageInteractiveTile(interactiveTileIndex);

      worldX = currentWorldX;
      worldY = currentWorldY;
      solidArea.width = currentSolidAreaWidth;
      solidArea.height = currentSolidAreaHeight;
    } else if (spriteCounter > 25) {
      spriteNum = 1;
      spriteCounter = 0;
      attacking = false;
    }
  }

  private void damageInteractiveTile(int interactiveTileIndex) {

    if (interactiveTileIndex != INIT_INDEX && gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[interactiveTileIndex].destructible
        && gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[interactiveTileIndex].isCorrectItem(this) && !gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[interactiveTileIndex].invincible) {
      gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[interactiveTileIndex].playSoundEffect();
      gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[interactiveTileIndex].currentLife--;
      gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[interactiveTileIndex].invincible = true;

      generateParticle(gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[interactiveTileIndex], gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[interactiveTileIndex]);

      if (gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[interactiveTileIndex].currentLife <= 0)
        gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[interactiveTileIndex] = gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[interactiveTileIndex].getDestroyedForm();
    }
  }

  public void damageMonster(int monsterIndex, int attack) {
    if (monsterIndex != INIT_INDEX && !gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].invincible) {
      gamePanel.playSoundEffect(HIT_MONSTER);

      int damage = Math.max(0, attack - gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].defense);
      gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].currentLife -= damage;
      gamePanel.ui.addMessage(String.format(DAMAGE_UI_MESSAGE, damage));
      gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].invincible = true;
      gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].damageReaction();

      if (gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].currentLife <= 0) {
        gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].dying = true;
        gamePanel.ui.addMessage(String.format(KILLED_UI_MESSAGE, gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].name));
        gamePanel.ui.addMessage(String.format(EXP_UI_MESSAGE, gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].exp));
        exp += gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].exp;
        checkLevelUp();
      }
    }
  }

  public void selectItem() {
    int itemIndex = gamePanel.ui.getItemIndexFromInventory(gamePanel.ui.playerSlotCol, gamePanel.ui.playerSlotRow);

    if (itemIndex < inventory.size()) {
      GameEntity selectedItem = inventory.get(itemIndex);

      switch (selectedItem.type) {
        case SWORD, AXE -> {
          currentWeapon = selectedItem;
          attack = getAttack();
          getPlayerAttackImage();
        }
        case SHIELD -> {
          currentShield = selectedItem;
          defense = getDefense();
        }
        case CONSUMABLE -> {
          selectedItem.use(this);
          inventory.remove(itemIndex);
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

  public void getPlayerImage() {
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
        gamePanel.mapsNpc.get(gamePanel.tileManager.currentMap)[npcIndex].speak();
      }
    }
  }

  public void setDefaultValues() {
    worldX = gamePanel.tileSize * 23;
    worldY = gamePanel.tileSize * 21;
//    worldX = gamePanel.tileSize * 12;
//    worldY = gamePanel.tileSize * 13;
    speed = 4;
    direction = DirectionType.DOWN;

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
    currentWeapon = new NormalSwordObject(gamePanel);
    currentShield = new WoodShieldObject(gamePanel);
    projectile = new FireballObject(gamePanel);
    attack = getAttack();
    defense = getDefense();

  }

  public void setItems() {
    inventory.clear();
    inventory.add(currentWeapon);
    inventory.add(currentShield);
    inventory.add(new AxeObject(gamePanel));
  }

  private int getDefense() {
    defense = dexterity * currentShield.defenseValue;
    return defense;
  }

  private int getAttack() {
    attackArea = currentWeapon.attackArea;
    attack = strength * currentWeapon.attackValue;
    return attack;
  }

  private void detectMonsterContact(int monsterIndex) {
    if (monsterIndex != INIT_INDEX && !invincible && !gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].dying) {

      int damage = gamePanel.mapsMonsters.get(gamePanel.tileManager.currentMap)[monsterIndex].attack - defense;
      currentLife -= damage;
      gamePanel.playSoundEffect(RECEIVE_DAMAGE);
      invincible = true;
      currentLife -= 1;
    }
  }

  public void setDefaultPositions() {

    worldX = gamePanel.tileSize * 23;
    worldY = gamePanel.tileSize * 21;
    direction = DirectionType.DOWN;
  }

  public void restoreLifeAndMana() {
    currentLife = maxLife;
    mana = maxMana;
    invincible = false;
  }

  @Override
  public void draw(Graphics2D graphic2d) {
    int temporaryScreenX = screenX;
    int temporaryScreenY = screenY;
    BufferedImage image = switch (getDirection()) {
      case UP -> {
        if (!attacking) {
          if (spriteNum == 1)
            yield up1;
          else if (spriteNum == 2)
            yield up2;
        } else {
          temporaryScreenY = screenY - gamePanel.tileSize;
          if (spriteNum == 1)
            yield attackUp1;
          else if (spriteNum == 2)
            yield attackUp2;
        }
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
      case ANY -> null;
    };


    if (invincible) {
      graphic2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
    }
    graphic2d.drawImage(image, temporaryScreenX, temporaryScreenY, null);
    graphic2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    // COLLISION RECTANGLE
    graphic2d.setColor(Color.RED);
    graphic2d.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
  }

  public void pickUpObject(int objectIndex) {

    if (objectIndex != INIT_INDEX) {

      // PICKUP ONLY ITEMS
      if (gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[objectIndex].type == WorldGameTypes.PICK_UP) {
        gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[objectIndex].use(this);
        gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[objectIndex] = null;
      } else {
        // INVENTORY ITEMS
        String text;
        if (inventory.size() != maxInventorySize) {
          inventory.add(gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[objectIndex]);
          gamePanel.playSoundEffect(COIN);
          text = String.format(PICKED_UP, gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[objectIndex].name);
        } else {
          text = INVENTORY_FULL;
        }
        gamePanel.ui.addMessage(text);
        gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[objectIndex] = null;
      }
    }
  }
}
