package org.example;

import static org.example.config.GameEntityNameFactory.MARU_MONICA_FONT;
import static org.example.config.GameEntityNameFactory.PURISA_BOLD_FONT;
import static org.example.enums.DayState.DAY;
import static org.example.enums.GameStateType.DIALOG_STATE;
import static org.example.enums.GameStateType.PLAY_STATE;
import static org.example.enums.GameStateType.TITLE_STATE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import org.example.entity.GameEntity;
import org.example.entity.object.BronzeCoin;
import org.example.entity.object.Heart;
import org.example.entity.object.ManaCrystal;
import org.example.environment.Lighting;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UI {
  private static final String END_GAME_QUESTION = "Are you sure you want to \nend the game?";
  private static final String FULL_SCREEN_NOTIFICATION = "The change will take \neffect after restarting \nthe game.";
  private static final String CURSOR_SELECTOR = ">";
  private static final String PLAYER_LIFE_FORMAT = "%s / %s";
  private static final String PAUSED = "PAUSED";
  private static final String DEFAULT_FONT = "SansSerif";
  private static final String COIN = "Coin";
  private static final String TITLE = "Four Legs";
  private static final String NEW_GAME = "New Game";
  private static final String LOAD_GAME = "Load game";
  private static final String END_GAME = "End Game";
  private static final String WEAPON = "Weapon";
  private static final String LEVEL = "Level";
  private static final String GAME_OVER = "Game Over";
  private static final String SHIELD = "Shield";
  private static final String EXP = "Exp";
  private static final String RETRY = "Retry";
  private static final String OPTIONS = "Options";
  private static final String ATTACK = "Attack";
  private static final String NEXT_LEVEL = "Next Level";
  private static final String DEFENSE = "Defense";
  private static final String FULL_SCREEN = "Full screen";
  private static final String QUIT = "Quit";
  private static final String STRENGTH = "Strength";
  private static final String YES = "Yes";
  private static final String LIFE = "Life";
  private static final String DEXTERITY = "Dexterity";
  private static final String BACK = "Back";
  private static final String MUSIC = "Music";
  private static final String SOUND_EFFECT = "Sound Effect";
  private static final String NO = "No";
  private static final String MOVE = "Move";
  private static final String SHOOT_CAST = "Shoot/Cast";
  private static final String CONFIRM_ATTACK = "Confirm/Attack";
  private static final String CHARACTER_SCREEN = "Character Screen";
  private static final String CONTROL = "Control";
  private static final String PAUSE = "Pause";
  private static final String CHARACTER_CONTROL = "WASD";
  private static final String CONFIRM = "Enter";
  private static final String PROJECTILE_KEY = "F";
  private static final String CHARACTER_KEY = "C";
  private static final String PAUSE_KEY = "P";
  private static final String CLOSE_KEY = "ESC";
  private static final String BUY = "Buy";
  private static final String SELL = "Sell";
  private static final String LEAVE = "Leave";
  private static final String COME_AGAIN = "Come again, hehe";
  private static final String ESCAPE_BACK = "[ESC] Back";
  private static final String YOUR_COIN = "Your Coin: %s";
  private static final String NOT_ENOUGH_MONEY = "You need more coins to buy that!";
  private static final String TOO_MUCH_ITEMS = "You cannot carry more items!";
  private static final String CANNOT_SELL_ITEM = "You cannot sell an equipped item!";


  BufferedImage heartFull, heartHalf, heartBlank, manaCrystalFull, manaCrystalBlank, coin;
  GamePanel gamePanel;
  Graphics2D graphics2D;
  public Font maruMonica, purisaBoldFont;
  ArrayList<String> messages = new ArrayList<>();
  ArrayList<Integer> messageCounter = new ArrayList<>();
  public GameEntity merchantNPC;
  public boolean messageOn = false;
  public boolean gameFinished = false;
  public String currentDialogue = "";
  public int commandNum = 0;
  public int subState = 0;
  public int playerSlotCol = 0;
  public int npcSlotCol = 0;
  public int playerSlotRow = 0;
  public int npcSlotRow = 0;
  private int transitionCounter = 0;

  public UI(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    maruMonica = Try.of(() -> loadFont(MARU_MONICA_FONT))
        .recover(ignored -> getDefaultFont())
        .get();
    purisaBoldFont = Try.of(() -> loadFont(PURISA_BOLD_FONT))
        .recover(ignored -> getDefaultFont())
        .get();

    // CREATE HUD OBJECT
    GameEntity heart = new Heart(gamePanel);
    heartFull = heart.image;
    heartHalf = heart.image2;
    heartBlank = heart.image3;
    GameEntity manaCrysta = new ManaCrystal(gamePanel);
    manaCrystalFull = manaCrysta.image;
    manaCrystalBlank = manaCrysta.image2;
    GameEntity bronzeCoin = new BronzeCoin(gamePanel);
    coin = bronzeCoin.image;
  }

  public void addMessage(String text) {
    messages.add(text);
    messageCounter.add(0);
  }

  public void draw(Graphics2D graphics2D) {
    this.graphics2D = graphics2D;
    graphics2D.setFont(maruMonica);
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics2D.setColor(Color.WHITE);

    switch (gamePanel.gameState) {
      case TITLE_STATE -> drawTitleScreen();
      case PLAY_STATE -> {
        drawPlayerLife();
        drawMessage();
      }
      case PAUSE_STATE -> {
        drawPlayerLife();
        drawPauseScreen();
      }
      case DIALOG_STATE -> {
        drawDialogueScreen();
      }
      case CHARACTER_STATE -> {
        drawCharacterScreen();
        drawInventory(gamePanel.player, true);
      }
      case OPTIONS_STATE -> drawOptionsScreen();
      case GAME_OVER_STATE -> drawGameOverScreen();
      case TRANSITION_STATE -> drawTransition();
      case TRADE_STATE -> drawTradeScreen();
      case SLEEP_STATE -> drawSleepScreen();
    }
  }

  private void drawSleepScreen() {
    transitionCounter++;

    if (transitionCounter < 120) {
      adjustLightingFilterAlpha(0.01F, 1.0F);
    } else {
      adjustLightingFilterAlpha(-0.01F, 0F);

      if (gamePanel.environmentManager.lighting.filterAlpha <= 0F) {
        transitionCounter = 0;
        gamePanel.environmentManager.lighting.dayState = DAY;
        gamePanel.environmentManager.lighting.dayCounter = 0;
        gamePanel.gameState = PLAY_STATE;
        gamePanel.player.getImage();
      }
    }
  }

  private void adjustLightingFilterAlpha(float delta, float limit) {
    Lighting lighting = gamePanel.environmentManager.lighting;
    lighting.filterAlpha = Math.max(0F, Math.min(1.0F, lighting.filterAlpha + delta));
  }

  private void drawGameOverScreen() {

    graphics2D.setColor(new Color(0, 0, 0, 150));
    graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

    // SHADOW
    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 110F));
    graphics2D.setColor(Color.BLACK);
    int x = getXForCenteredText(GAME_OVER);
    int y = gamePanel.tileSize * 4;
    graphics2D.drawString(GAME_OVER, x, y);

    // MAIN
    graphics2D.setColor(Color.WHITE);
    graphics2D.drawString(GAME_OVER, x - 4, y - 4);

    // RETRY
    graphics2D.setFont(graphics2D.getFont().deriveFont(50F));
    x = getXForCenteredText(RETRY);
    y += gamePanel.tileSize * 4;
    graphics2D.drawString(RETRY, x, y);
    if (commandNum == 0)
      graphics2D.drawString(CURSOR_SELECTOR, x - 40, y);

    // BACK TO TITLE SCREEN
    x = getXForCenteredText(QUIT);
    y += 55;
    graphics2D.drawString(QUIT, x, y);
    if (commandNum == 1)
      graphics2D.drawString(CURSOR_SELECTOR, x - 40, y);
  }

  private void drawOptionsScreen() {
    graphics2D.setColor(Color.WHITE);
    graphics2D.setFont(graphics2D.getFont().deriveFont(32F));

    // SUB WINDOW
    int frameX = gamePanel.tileSize * 6;
    int frameY = gamePanel.tileSize;
    int frameWidth = gamePanel.tileSize * 8;
    int frameHeight = gamePanel.tileSize * 10;
    drawSubWindow(frameX, frameY, frameWidth, frameHeight);

    // TODO: switch subState for something else
    switch (subState) {
      case 0 -> optionsMainSettings(frameX, frameY);
      case 1 -> optionsFullScreenNotification(frameX, frameY);
      case 2 -> optionsControl(frameX, frameY);
      case 3 -> optionsEndGameConfirmation(frameX, frameY);
    }

    gamePanel.keyHandler.enterPressed = false;
  }

  private void drawTradeScreen() {
    switch (subState) {
      case 0 -> tradeSelect();
      case 1 -> tradeBuy();
      case 2 -> tradeSell();
    }
    gamePanel.keyHandler.enterPressed = false;
  }

  private void tradeSelect() {
    drawDialogueScreen();

    // DRAW WINDOW

    int x = gamePanel.tileSize * 15;
    int y = gamePanel.tileSize * 4;
    int width = gamePanel.tileSize * 3;
    int height = gamePanel.tileSize * 4;

    drawSubWindow(x, y, width, height);

    // DRAW TEXTS
    x += gamePanel.tileSize;
    y += gamePanel.tileSize;
    graphics2D.drawString(BUY, x, y);
    if (commandNum == 0) {
      graphics2D.drawString(CURSOR_SELECTOR, x - 24, y);
      if (gamePanel.keyHandler.enterPressed)
        subState = 1;
    }
    y += gamePanel.tileSize;
    graphics2D.drawString(SELL, x, y);
    if (commandNum == 1) {
      graphics2D.drawString(CURSOR_SELECTOR, x - 24, y);
      if (gamePanel.keyHandler.enterPressed)
        subState = 2;
    }
    y += gamePanel.tileSize;
    graphics2D.drawString(LEAVE, x, y);
    if (commandNum == 2) {
      graphics2D.drawString(CURSOR_SELECTOR, x - 24, y);
      if (gamePanel.keyHandler.enterPressed) {
        commandNum = 0;
        gamePanel.gameState = DIALOG_STATE;
        currentDialogue = COME_AGAIN;
      }
    }
  }

  private void tradeBuy() {
    drawInventory(gamePanel.player, false);
    drawInventory(merchantNPC, true);

    // DRAW HINT WINDOW
    int x = gamePanel.tileSize * 2;
    int y = gamePanel.tileSize * 9;
    int width = gamePanel.tileSize * 6;
    int height = gamePanel.tileSize * 2;
    drawSubWindow(x, y, width, height);
    graphics2D.drawString(ESCAPE_BACK, x + 24, y + 60);

    // DRAW PLAYER COIN WINDOW
    x = gamePanel.tileSize * 12;
    drawSubWindow(x, y, width, height);
    graphics2D.drawString(String.format(YOUR_COIN, gamePanel.player.money), x + 24, y + 60);

    // DRAW PRICE WINDOW
    int itemIndex = getItemIndexFromInventory(npcSlotCol, npcSlotRow);
    if (itemIndex < merchantNPC.inventory.size()) {
      x = (int) (gamePanel.tileSize * 5.5);
      y = (int) (gamePanel.tileSize * 5.5);
      width = (int) (gamePanel.tileSize * 2.5);
      height = gamePanel.tileSize;
      drawSubWindow(x, y, width, height);
      graphics2D.drawImage(coin, x + 10, y + 8, 32, 32, null);

      int price = merchantNPC.inventory.get(itemIndex).price;
      x = getXForAlignTextToRight(String.valueOf(price), gamePanel.tileSize * 8 - 20);
      graphics2D.drawString(String.valueOf(price), x, y + 34);

      // BUY ITEM
      if (gamePanel.keyHandler.enterPressed) {
        if (merchantNPC.inventory.get(itemIndex).price > gamePanel.player.money) {
          subState = 0;
          gamePanel.gameState = DIALOG_STATE;
          currentDialogue = NOT_ENOUGH_MONEY;
          drawDialogueScreen();
        } else if (!gamePanel.player.canObtainItem(merchantNPC.inventory.get(itemIndex))) {
          subState = 0;
          gamePanel.gameState = DIALOG_STATE;
          currentDialogue = TOO_MUCH_ITEMS;
          drawDialogueScreen();
        } else {
          gamePanel.player.money -= merchantNPC.inventory.get(itemIndex).price;
        }
      }
    }
  }

  private void tradeSell() {
    drawInventory(gamePanel.player, true);

    // DRAW HINT WINDOW
    int x = gamePanel.tileSize * 2;
    int y = gamePanel.tileSize * 9;
    int width = gamePanel.tileSize * 6;
    int height = gamePanel.tileSize * 2;
    drawSubWindow(x, y, width, height);
    graphics2D.drawString(ESCAPE_BACK, x + 24, y + 60);

    // DRAW PLAYER COIN WINDOW
    x = gamePanel.tileSize * 12;
    drawSubWindow(x, y, width, height);
    graphics2D.drawString(String.format(YOUR_COIN, gamePanel.player.money), x + 24, y + 60);

    // DRAW PRICE WINDOW
    int itemIndex = getItemIndexFromInventory(playerSlotCol, playerSlotRow);
    if (itemIndex < gamePanel.player.inventory.size()) {
      x = (int) (gamePanel.tileSize * 15.5);
      y = (int) (gamePanel.tileSize * 5.5);
      width = (int) (gamePanel.tileSize * 2.5);
      height = gamePanel.tileSize;
      drawSubWindow(x, y, width, height);
      graphics2D.drawImage(coin, x + 10, y + 8, 32, 32, null);

      int price = gamePanel.player.inventory.get(itemIndex).price / 2;
      x = getXForAlignTextToRight(String.valueOf(price), gamePanel.tileSize * 18 - 20);
      graphics2D.drawString(String.valueOf(price), x, y + 34);

      // SELL ITEM
      if (gamePanel.keyHandler.enterPressed) {
        if (gamePanel.player.inventory.get(itemIndex) == gamePanel.player.currentWeapon
            || gamePanel.player.inventory.get(itemIndex) == gamePanel.player.currentShield) {
          commandNum = 0;
          subState = 0;
          gamePanel.gameState = DIALOG_STATE;
          currentDialogue = CANNOT_SELL_ITEM;
        } else {
          if (gamePanel.player.inventory.get(itemIndex).amount > 1)
            gamePanel.player.inventory.get(itemIndex).amount--;
          else
            gamePanel.player.inventory.remove(itemIndex);
          gamePanel.player.money += price;
        }
      }
    }
  }

  private void optionsEndGameConfirmation(int frameX, int frameY) {
    int textX = frameX + gamePanel.tileSize;
    int textY = frameY + gamePanel.tileSize * 3;

    for (String line : END_GAME_QUESTION.split("\n")) {
      graphics2D.drawString(line, textX, textY);
      textY += 40;
    }

    // YES OPTION
    textX = getXForCenteredText(YES);
    textY += gamePanel.tileSize * 3;
    graphics2D.drawString(YES, textX, textY);
    if (commandNum == 0) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        gamePanel.stopMusic();
        subState = 0;
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
        gamePanel.gameState = TITLE_STATE;
      }
    }

    // NO OPTION
    textX = getXForCenteredText(NO);
    textY += gamePanel.tileSize;
    graphics2D.drawString(NO, textX, textY);
    if (commandNum == 1) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        subState = 0;
        commandNum = 4;
      }
    }
  }

  private void optionsMainSettings(int frameX, int frameY) {
    int textX;
    int textY;

    // TITLE
    textX = getXForCenteredText(OPTIONS);
    textY = frameY + gamePanel.tileSize;
    graphics2D.drawString(OPTIONS, textX, textY);

    // FULL SCREEN ON/OFF
    textX = frameX + gamePanel.tileSize;
    textY += gamePanel.tileSize * 2;
    graphics2D.drawString(FULL_SCREEN, textX, textY);
    if (commandNum == 0) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        gamePanel.fullScreenOn = !gamePanel.fullScreenOn;
        subState = 1;
      }
    }

    // MUSIC
    textY += gamePanel.tileSize;
    graphics2D.drawString(MUSIC, textX, textY);
    if (commandNum == 1)
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);

    // SE
    textY += gamePanel.tileSize;
    graphics2D.drawString(SOUND_EFFECT, textX, textY);
    if (commandNum == 2)
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
    // CONTROL
    textY += gamePanel.tileSize;
    graphics2D.drawString(CONTROL, textX, textY);
    if (commandNum == 3) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        subState = 2;
        commandNum = 0;
      }
    }

    // END GAME
    textY += gamePanel.tileSize;
    graphics2D.drawString(END_GAME, textX, textY);
    if (commandNum == 4) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        subState = 3;
        commandNum = 0;
      }

    }
    // BACK
    textY += gamePanel.tileSize * 2;
    graphics2D.drawString(BACK, textX, textY);
    if (commandNum == 5) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        gamePanel.gameState = PLAY_STATE;
        commandNum = 0;
      }
    }

    // FULL SCREEN CHECK BOX
    textX = frameX + (int) (gamePanel.tileSize * 4.5);
    textY = frameY + gamePanel.tileSize * 2 + gamePanel.tileSize / 2;
    graphics2D.setStroke(new BasicStroke(2));
    graphics2D.drawRect(textX, textY, gamePanel.tileSize / 2, gamePanel.tileSize / 2);
    if (gamePanel.fullScreenOn) {
      graphics2D.fillRect(textX, textY, gamePanel.tileSize / 2, gamePanel.tileSize / 2);
    }

    // MUSIC VOLUME
    textY += gamePanel.tileSize;
    graphics2D.drawRect(textX, textY, 120, gamePanel.tileSize / 2); // 120/5 = 24 px
    int volumeWidth = 24 * gamePanel.music.volumeScale;
    graphics2D.fillRect(textX, textY, volumeWidth, 24);

    // SE VOLUME
    textY += gamePanel.tileSize;
    graphics2D.drawRect(textX, textY, 120, gamePanel.tileSize / 2);
    volumeWidth = 24 * gamePanel.soundEffect.volumeScale;
    graphics2D.fillRect(textX, textY, volumeWidth, 24);

    gamePanel.config.saveConfig();
  }

  public void optionsControl(int frameX, int frameY) {
    int textX;
    int textY;

    textX = getXForCenteredText(CONTROL);
    textY = frameY + gamePanel.tileSize;
    graphics2D.drawString(CONTROL, textX, textY);

    textX = frameX + gamePanel.tileSize;
    textY += gamePanel.tileSize;

    graphics2D.drawString(MOVE, textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString(CONFIRM_ATTACK, textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString(SHOOT_CAST, textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString(CHARACTER_SCREEN, textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString(PAUSE, textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString(OPTIONS, textX, textY);

    textX = frameX + gamePanel.tileSize * 6;
    textY = frameY + gamePanel.tileSize * 2;

    graphics2D.drawString(CHARACTER_CONTROL, textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString(CONFIRM, textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString(PROJECTILE_KEY, textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString(CHARACTER_KEY, textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString(PAUSE_KEY, textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString(CLOSE_KEY, textX, textY);
    textY += gamePanel.tileSize;

    // BACK
    textX = frameX + gamePanel.tileSize;
    textY = frameY + gamePanel.tileSize * 9;
    graphics2D.drawString(BACK, textX, textY);

    if (commandNum == 0) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        subState = 0;
        commandNum = 3;
      }
    }
  }

  private void drawInventory(GameEntity gameEntity, boolean cursor) {
    int frameX;
    int frameY = gamePanel.tileSize;
    int frameWidth = gamePanel.tileSize * 6;
    int frameHeight = gamePanel.tileSize * 5;
    int slotCol;
    int slotRow;

    if (gameEntity == gamePanel.player) {
      frameX = gamePanel.tileSize * 12;
      slotCol = playerSlotCol;
      slotRow = playerSlotRow;
    } else {
      frameX = gamePanel.tileSize * 2;
      slotCol = npcSlotCol;
      slotRow = npcSlotRow;
    }

    // FRAME
    drawSubWindow(frameX, frameY, frameWidth, frameHeight);

    // SLOT
    final int slotXStart = frameX + 20;
    final int slotYStart = frameY + 20;
    int slotX = slotXStart;
    int slotY = slotYStart;
    int slotSize = gamePanel.tileSize + 3;


    // DRAW PLAYER'S ITEMS
    for (int inventoryItem = 0; inventoryItem < gameEntity.inventory.size(); inventoryItem++) {

      // EQUIP CURSOR
      if (gameEntity.inventory.get(inventoryItem) == gameEntity.currentWeapon ||
          gameEntity.inventory.get(inventoryItem) == gameEntity.currentShield ||
          gameEntity.inventory.get(inventoryItem) == gameEntity.currentLightItem) {
        graphics2D.setColor(new Color(240, 190, 90));
        graphics2D.fillRoundRect(slotX, slotY, gamePanel.tileSize, gamePanel.tileSize, 10, 10);
      }
      graphics2D.drawImage(gameEntity.inventory.get(inventoryItem).down1, slotX, slotY, null);

      // DISPLAY AMOUNT
      if (gameEntity == gamePanel.player && gameEntity.inventory.get(inventoryItem).amount > 1) {
        graphics2D.setFont(graphics2D.getFont().deriveFont(32f));
        int amountX;
        int amountY;
        amountX = getXForAlignTextToRight(String.valueOf(gameEntity.inventory.get(inventoryItem).amount), slotX + 44);
        amountY = slotY + gamePanel.tileSize;

        //SHADOW
        graphics2D.setColor(new Color(60, 60, 60));
        graphics2D.drawString(String.valueOf(gameEntity.inventory.get(inventoryItem).amount), amountX, amountY);
        // NUMBER
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(String.valueOf(gameEntity.inventory.get(inventoryItem).amount), amountX - 3, amountY - 3);
      }
      slotX += slotSize;

      if (inventoryItem == 4 || inventoryItem == 9 || inventoryItem == 14) {
        slotX = slotXStart;
        slotY += slotSize;
      }
    }

    //CURSOR
    if (cursor) {
      // DRAW CURSOR
      int cursorX = slotXStart + (slotSize * slotCol);
      int cursorY = slotYStart + (slotSize * slotRow);
      int cursorWidth = gamePanel.tileSize;
      int cursorHeight = gamePanel.tileSize;

      graphics2D.setColor(Color.WHITE);
      graphics2D.setStroke(new BasicStroke(3));
      graphics2D.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

      // DESCRIPTION FRAME
      int descriptionFrameX = frameX;
      int descriptionFrameY = frameY + frameHeight;
      int descriptionFrameWidth = frameWidth;
      int descriptionFrameHeight = gamePanel.tileSize * 3;

      // DESCRIPTION TEXT
      int textX = descriptionFrameX + 20;
      int textY = descriptionFrameY + gamePanel.tileSize;
      graphics2D.setFont(graphics2D.getFont().deriveFont(28F));

      int itemIndex = getItemIndexFromInventory(slotCol, slotRow);
      if (itemIndex < gameEntity.inventory.size()) {
        drawSubWindow(descriptionFrameX, descriptionFrameY, descriptionFrameWidth, descriptionFrameHeight);
        for (String line : gameEntity.inventory.get(itemIndex).description.split("\n")) {
          graphics2D.drawString(line, textX, textY);
          textY += 32;
        }
      }
    }
  }

  public int getItemIndexFromInventory(int slotCol, int slotRow) {
    return slotCol + (slotRow * 5);
  }

  private void optionsFullScreenNotification(int frameX, int frameY) {
    int textX = frameX + gamePanel.tileSize;
    int textY = frameY + gamePanel.tileSize * 3;

    for (String line : FULL_SCREEN_NOTIFICATION.split("\n")) {
      graphics2D.drawString(line, textX, textY);
      textY += 40;
    }

    // BACK
    textY = frameY + gamePanel.tileSize * 9;
    graphics2D.drawString(BACK, textX, textY);
    if (commandNum == 0) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        subState = 0;
      }
    }
  }

  private void drawMessage() {
    int messageX = gamePanel.tileSize;
    int messageY = gamePanel.tileSize * 4;

    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 20F));

    for (int i = 0; i < messages.size(); i++) {
      if (messages.get(i) != null) {
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(messages.get(i), messageX + 2, messageY + 2);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(messages.get(i), messageX, messageY);

        int counter = messageCounter.get(i) + 1;
        messageCounter.set(i, counter);
        messageY += 50;

        if (messageCounter.get(i) > 180) {
          messages.remove(i);
          messageCounter.remove(i);
        }
      }
    }
  }

  private void drawCharacterScreen() {
    // CREATE A FRAME
    final int frameX = gamePanel.tileSize * 2;
    final int frameY = gamePanel.tileSize;
    final int frameWidth = gamePanel.tileSize * 6;
    final int frameHeight = gamePanel.tileSize * 10;

    // TEXT
    graphics2D.setColor(Color.WHITE);
    graphics2D.setFont(graphics2D.getFont().deriveFont(25F));
    drawSubWindow(frameX, frameY, frameWidth, frameHeight);

    int textX = frameX + 20;
    int textY = frameY + gamePanel.tileSize;
    final int lineHeight = 35;

    // NAMES
    graphics2D.drawString(LEVEL, textX, textY);
    textY += lineHeight;
    graphics2D.drawString(LIFE, textX, textY);
    textY += lineHeight;
    graphics2D.drawString(STRENGTH, textX, textY);
    textY += lineHeight;
    graphics2D.drawString(DEXTERITY, textX, textY);
    textY += lineHeight;
    graphics2D.drawString(ATTACK, textX, textY);
    textY += lineHeight;
    graphics2D.drawString(DEFENSE, textX, textY);
    textY += lineHeight;
    graphics2D.drawString(EXP, textX, textY);
    textY += lineHeight;
    graphics2D.drawString(NEXT_LEVEL, textX, textY);
    textY += lineHeight;
    graphics2D.drawString(COIN, textX, textY);
    textY += lineHeight + 20;
    graphics2D.drawString(WEAPON, textX, textY);
    textY += lineHeight + 15;
    graphics2D.drawString(SHIELD, textX, textY);

    // VALUES
    int tailX = (frameX + frameWidth) - 30;

    textY = frameY + gamePanel.tileSize;
    String value;

    value = String.valueOf(gamePanel.player.level);
    textX = getXForAlignTextToRight(value, tailX);
    graphics2D.drawString(value, textX, textY);
    textY += lineHeight;

    String playerLife = String.format(PLAYER_LIFE_FORMAT, gamePanel.player.currentLife, gamePanel.player.maxLife);
    textX = getXForAlignTextToRight(playerLife, tailX);
    graphics2D.drawString(playerLife, textX, textY);
    textY += lineHeight;

    textX = getXForAlignTextToRight(String.valueOf(gamePanel.player.strength), tailX);
    graphics2D.drawString(String.valueOf(gamePanel.player.strength), textX, textY);
    textY += lineHeight;

    textX = getXForAlignTextToRight(String.valueOf(gamePanel.player.dexterity), tailX);
    graphics2D.drawString(String.valueOf(gamePanel.player.dexterity), textX, textY);
    textY += lineHeight;

    textX = getXForAlignTextToRight(String.valueOf(gamePanel.player.attack), tailX);
    graphics2D.drawString(String.valueOf(gamePanel.player.attack), textX, textY);
    textY += lineHeight;

    textX = getXForAlignTextToRight(String.valueOf(gamePanel.player.defense), tailX);
    graphics2D.drawString(String.valueOf(gamePanel.player.defense), textX, textY);
    textY += lineHeight;

    textX = getXForAlignTextToRight(String.valueOf(gamePanel.player.exp), tailX);
    graphics2D.drawString(String.valueOf(gamePanel.player.exp), textX, textY);
    textY += lineHeight;

    textX = getXForAlignTextToRight(String.valueOf(gamePanel.player.nextLevelExp), tailX);
    graphics2D.drawString(String.valueOf(gamePanel.player.nextLevelExp), textX, textY);
    textY += lineHeight;

    textX = getXForAlignTextToRight(String.valueOf(gamePanel.player.money), tailX);
    graphics2D.drawString(String.valueOf(gamePanel.player.money), textX, textY);
    textY += lineHeight;

    graphics2D.drawImage(gamePanel.player.currentWeapon.down1, tailX - gamePanel.tileSize, textY - 14, null);
    textY += gamePanel.tileSize;

    graphics2D.drawImage(gamePanel.player.currentShield.down1, tailX - gamePanel.tileSize, textY - 14, null);
  }

  private void drawPlayerLife() {
    int x = gamePanel.tileSize / 2;
    int y = gamePanel.tileSize / 2;
    int i = 0;

    // DRAW MAX LIFE
    while (i < gamePanel.player.maxLife / 2) {
      graphics2D.drawImage(heartBlank, x, y, null);
      i++;
      x += gamePanel.tileSize;
    }

    x = gamePanel.tileSize / 2;
    i = 0;

    // DRAW CURRENT LIFE
    while (i < gamePanel.player.currentLife) {
      graphics2D.drawImage(heartHalf, x, y, null);
      i++;
      if (i < gamePanel.player.currentLife) {
        graphics2D.drawImage(heartFull, x, y, null);
      }
      i++;
      x += gamePanel.tileSize;
    }

    // DRAW MAX MANA
    x = gamePanel.tileSize / 2 - 5;
    y = (int) (gamePanel.tileSize * 1.5);
    i = 0;
    while (i < gamePanel.player.maxMana) {
      graphics2D.drawImage(manaCrystalBlank, x, y, null);
      i++;
      x += 35;
    }

    // DRAW MANA
    x = gamePanel.tileSize / 2 - 5;
    i = 0;
    while (i < gamePanel.player.mana) {
      graphics2D.drawImage(manaCrystalFull, x, y, null);
      i++;
      x += 35;
    }
  }

  private void drawTitleScreen() {
    // CLEAR
    graphics2D.setColor(Color.BLACK);
    graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
    // TITLE NAME
    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 96F));
    int x = getXForCenteredText(TITLE);
    int y = gamePanel.tileSize * 3;

    // SHADOW
    graphics2D.setColor(Color.GRAY);
    graphics2D.drawString(TITLE, x + 5, y + 5);
    // MAIN COLOR
    graphics2D.setColor(Color.WHITE);
    graphics2D.drawString(TITLE, x, y);

    // BLUE BOY IMAGE
    x = gamePanel.screenWidth / 2 - (gamePanel.tileSize * 2) / 2;
    y += gamePanel.tileSize * 2;
    graphics2D.drawImage(gamePanel.player.down1, x, y, gamePanel.tileSize * 2, gamePanel.tileSize * 2, null);

    // MENU
    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 48F));

    String[] menuOptions = {NEW_GAME, LOAD_GAME, QUIT};
    y += gamePanel.tileSize * 3;

    for (int i = 0; i < menuOptions.length; i++) {
      x = getXForCenteredText(menuOptions[i]);
      graphics2D.drawString(menuOptions[i], x, y);

      if (commandNum == i) {
        graphics2D.drawString(CURSOR_SELECTOR, x - gamePanel.tileSize, y);
      }

      y += gamePanel.tileSize;
    }
  }


  private void drawDialogueScreen() {
    // WINDOW
    int x = gamePanel.tileSize * 3;
    int y = gamePanel.tileSize / 2;
    int width = gamePanel.screenWidth - (gamePanel.tileSize * 6);
    int height = gamePanel.tileSize * 4;
    drawSubWindow(x, y, width, height);

    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 25F));
    x += gamePanel.tileSize;
    y += gamePanel.tileSize;

    for (String line : currentDialogue.split("\n")) {
      graphics2D.drawString(line, x, y);
      y += 40;
    }
  }

  private void drawSubWindow(int x, int y, int width, int height) {
    graphics2D.setColor(new Color(0, 0, 0, 220));
    graphics2D.fillRoundRect(x, y, width, height, 35, 35);

    graphics2D.setStroke(new BasicStroke(5));
    graphics2D.setColor(new Color(255, 255, 255));
    graphics2D.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

  }


  private void drawTransition() {
    transitionCounter++;
    graphics2D.setColor(new Color(0, 0, 0, transitionCounter * 5));
    graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

    if (transitionCounter == 50) {
      transitionCounter = 0;
      gamePanel.gameState = PLAY_STATE;
      gamePanel.tileManager.currentMap = gamePanel.eventHandler.tempMap;
      gamePanel.gameMap.createWorldMap();
      System.out.println("TM" + gamePanel.tileManager.currentMap);
      gamePanel.player.worldX = gamePanel.tileSize * gamePanel.eventHandler.tempCol;
      gamePanel.player.worldY = gamePanel.tileSize * gamePanel.eventHandler.tempRow;
      gamePanel.eventHandler.previousEventX = gamePanel.player.worldX;
      gamePanel.eventHandler.previousEventY = gamePanel.player.worldY;
    }
  }

  private void drawPauseScreen() {
    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 80F));
    int x = getXForCenteredText(PAUSED);
    int y = gamePanel.screenHeight / 2;

    graphics2D.drawString(PAUSED, x, y);
  }

  private int getXForCenteredText(String text) {
    int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
    return gamePanel.screenWidth / 2 - length / 2;
  }

  private int getXForAlignTextToRight(String text, int tailX) {
    int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
    return tailX - length;
  }

  private Font loadFont(String fontPath) throws Exception {
    try (InputStream fontInputStream = Objects.requireNonNull(getClass().getResourceAsStream(fontPath))) {
      return Font.createFont(Font.TRUETYPE_FONT, fontInputStream);
    }
  }

  private Font getDefaultFont() {
    log.info("Loading default system font due to an error.");
    return new Font(DEFAULT_FONT, Font.PLAIN, 12);
  }
}
