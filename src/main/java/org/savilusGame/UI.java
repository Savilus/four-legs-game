package org.savilusGame;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.MARU_MONICA_FONT;
import static org.savilusGame.config.GameEntityNameFactory.PURISA_BOLD_FONT;
import static org.savilusGame.config.GameEntityNameFactory.SPEAK;
import static org.savilusGame.enums.DayState.DAY;
import static org.savilusGame.enums.GameStateType.CUTSCENE_STATE;
import static org.savilusGame.enums.GameStateType.DIALOG_STATE;
import static org.savilusGame.enums.GameStateType.PLAY_STATE;
import static org.savilusGame.enums.GameStateType.TITLE_STATE;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.items.BronzeCoin;
import org.savilusGame.entity.items.Heart;
import org.savilusGame.entity.items.ManaCrystal;
import org.savilusGame.environment.Lighting;
import org.savilusGame.utils.text.TextManager;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UI {
  private static final String MERCHANT_COME_AGAIN_DIALOGUE_KEY = "merchantNpcComeAgain";
  private static final String MERCHANT_NO_MONEY_DIALOGUE_KEY = "merchantNpcNotEnoughMoney";
  private static final String MERCHANT_TO_MUCH_ITEMS_DIALOGUE_KEY = "merchantNpcTooMuchItems";
  private static final String MERCHANT_TRADE_DIALOGUE_KEY = "merchantNpcTradeQuestion";
  private static final String MERCHANT_CANNOT_SELL_KEY = "merchantNpcCannotSellItem";
  private static final String CURSOR_SELECTOR = ">";
  private static final String PLAYER_LIFE_FORMAT = "%s / %s";
  private static final String CHARACTER_CONTROL = "WASD";
  private static final String CONFIRM = "Enter";
  private static final String PROJECTILE_KEY = "F";
  private static final String CHARACTER_KEY = "C";
  private static final String PAUSE_KEY = "P";
  private static final String CLOSE_KEY = "ESC";
  private static final String SPACE_KEY = "SPACE";
  private static final String DEFAULT_FONT = "SansSerif";
  private static final String MENU_OPTIONS = "menuOptions";
  private static final String DIALOGUES = "dialogues";
  private static final String SETTINGS = "settings";
  private static final String UI_MESSAGES = "uiMessages";
  private static final String GAME_STATS = "gameStats";
  private static final String NEW_GAME = "newGame";
  private static final String LOAD_GAME = "loadGame";
  private static final String QUIT = "quit";
  private static final String END_GAME_QUESTION = "endGameQuestion";
  private static final String FULL_SCREEN_NOTIFICATION = "fullScreenNotification";
  private static final String PAUSED = "paused";
  private static final String COIN = "coin";
  private static final String TITLE = "title";
  private static final String END_GAME = "endGame";
  private static final String LIFE = "life";
  private static final String DEXTERITY = "dexterity";
  private static final String WEAPON = "weapon";
  private static final String LEVEL = "level";
  private static final String SHIELD = "shield";
  private static final String EXP = "exp";
  private static final String STRENGTH = "strength";
  private static final String GAME_OVER = "gameOver";
  private static final String RETRY = "retry";
  private static final String OPTIONS = "options";
  private static final String ATTACK = "attack";
  private static final String NEXT_LEVEL = "nextLevel";
  private static final String DEFENSE = "defense";
  private static final String FULL_SCREEN = "fullScreen";
  private static final String YES = "yes";
  private static final String BACK = "back";
  private static final String MUSIC = "music";
  private static final String SOUND_EFFECT = "soundEffect";
  private static final String NO = "no";
  private static final String MOVE = "move";
  private static final String SHOOT_CAST = "shootCast";
  private static final String PARRY = "parry";
  private static final String CONFIRM_ATTACK = "confirmAttack";
  private static final String CHARACTER_SCREEN = "characterScreen";
  private static final String CONTROL = "control";
  private static final String CONTROLS = "controls";
  private static final String PAUSE = "pause";
  private static final String BUY = "buy";
  private static final String SHOP = "shop";
  private static final String SELL = "sell";
  private static final String LEAVE = "leave";
  private static final String ESCAPE_BACK = "escapeBack";
  private static final String YOUR_COIN = "yourCoin";

  BufferedImage heartFull, heartHalf, heartBlank, manaCrystalFull, manaCrystalBlank, coin;
  GamePanel gamePanel;
  Graphics2D graphics2D;
  public Font maruMonica, purisaBoldFont;
  ArrayList<String> messages = new ArrayList<>();
  ArrayList<Integer> messageCounter = new ArrayList<>();
  public GameEntity npc;

  public int commandNum = 0;
  public int subState = 0;
  public int playerSlotCol = 0;
  public int npcSlotCol = 0;
  public int playerSlotRow = 0;
  public int npcSlotRow = 0;
  private int transitionCounter = 0;
  int characterIndex = 0;
  public String currentDialogue = StringUtils.EMPTY;
  StringBuilder combinedText = new StringBuilder();

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
        drawMonsterLife();
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

      if (gamePanel.environmentManager.getLighting().getFilterAlpha() <= 0F) {
        transitionCounter = 0;
        gamePanel.environmentManager.getLighting().setDayState(DAY);
        gamePanel.environmentManager.getLighting().setDayCounter(0);
        gamePanel.gameState = PLAY_STATE;
        gamePanel.player.getImage();
      }
    }
  }

  private void adjustLightingFilterAlpha(float delta, float limit) {
    Lighting lighting = gamePanel.environmentManager.getLighting();
    lighting.setFilterAlpha(Math.max(0F, Math.min(1.0F, lighting.getFilterAlpha() + delta)));
  }

  private void drawGameOverScreen() {

    graphics2D.setColor(new Color(0, 0, 0, 150));
    graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);

    // SHADOW
    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 110F));
    graphics2D.setColor(Color.BLACK);
    int x = getXForCenteredText(TextManager.getUiText(UI_MESSAGES, GAME_OVER));
    int y = TILE_SIZE * 4;
    graphics2D.drawString(TextManager.getUiText(UI_MESSAGES, GAME_OVER), x, y);

    // MAIN
    graphics2D.setColor(Color.WHITE);
    graphics2D.drawString(TextManager.getUiText(UI_MESSAGES, GAME_OVER), x - 4, y - 4);

    // RETRY
    graphics2D.setFont(graphics2D.getFont().deriveFont(50F));
    x = getXForCenteredText(TextManager.getSettingText(MENU_OPTIONS, RETRY));
    y += TILE_SIZE * 4;
    graphics2D.drawString(TextManager.getSettingText(MENU_OPTIONS, RETRY), x, y);
    if (commandNum == 0)
      graphics2D.drawString(CURSOR_SELECTOR, x - 40, y);

    // BACK TO TITLE SCREEN
    x = getXForCenteredText(TextManager.getSettingText(MENU_OPTIONS, QUIT));
    y += 55;
    graphics2D.drawString(TextManager.getSettingText(MENU_OPTIONS, QUIT), x, y);
    if (commandNum == 1)
      graphics2D.drawString(CURSOR_SELECTOR, x - 40, y);
  }

  private void drawOptionsScreen() {
    graphics2D.setColor(Color.WHITE);
    graphics2D.setFont(graphics2D.getFont().deriveFont(32F));

    // SUB WINDOW
    int frameX = TILE_SIZE * 6;
    int frameY = TILE_SIZE;
    int frameWidth = TILE_SIZE * 8;
    int frameHeight = TILE_SIZE * 10;
    drawSubWindow(frameX, frameY, frameWidth, frameHeight);

    // TODO: switch subState for something else
    switch (subState) {
      case 0 -> optionsMainSettings(frameX, frameY);
      case 1 -> optionsFullScreenNotification(frameX, frameY);
      case 2 -> optionsControl(frameX, frameY);
      case 3 -> optionsEndGameConfirmation(frameX, frameY);
    }

    gamePanel.keyHandler.setEnterPressed(false);
  }

  private void drawTradeScreen() {
    switch (subState) {
      case 0 -> tradeSelect();
      case 1 -> tradeBuy();
      case 2 -> tradeSell();
    }
    gamePanel.keyHandler.setEnterPressed(false);
  }

  private void tradeSelect() {
    npc.dialogueSet = MERCHANT_TRADE_DIALOGUE_KEY;
    drawDialogueScreen();
    // DRAW WINDOW

    int x = TILE_SIZE * 15;
    int y = TILE_SIZE * 4;
    int width = TILE_SIZE * 3;
    int height = TILE_SIZE * 4;

    drawSubWindow(x, y, width, height);

    // DRAW TEXTS
    x += TILE_SIZE;
    y += TILE_SIZE;
    graphics2D.drawString(TextManager.getUiText(SHOP, BUY), x, y);
    if (commandNum == 0) {
      graphics2D.drawString(CURSOR_SELECTOR, x - 24, y);
      if (gamePanel.keyHandler.isEnterPressed())
        subState = 1;
    }
    y += TILE_SIZE;
    graphics2D.drawString(TextManager.getUiText(SHOP, SELL), x, y);
    if (commandNum == 1) {
      graphics2D.drawString(CURSOR_SELECTOR, x - 24, y);
      if (gamePanel.keyHandler.isEnterPressed())
        subState = 2;
    }
    y += TILE_SIZE;
    graphics2D.drawString(TextManager.getUiText(SHOP, LEAVE), x, y);
    if (commandNum == 2) {
      graphics2D.drawString(CURSOR_SELECTOR, x - 24, y);
      if (gamePanel.keyHandler.isEnterPressed()) {
        commandNum = 0;
        npc.startDialogue(npc, MERCHANT_COME_AGAIN_DIALOGUE_KEY);
      }
    }
  }

  private void tradeBuy() {
    drawInventory(gamePanel.player, false);
    drawInventory(npc, true);

    // DRAW HINT WINDOW
    int x = TILE_SIZE * 2;
    int y = TILE_SIZE * 9;
    int width = TILE_SIZE * 6;
    int height = TILE_SIZE * 2;
    drawSubWindow(x, y, width, height);
    graphics2D.drawString(TextManager.getSettingText(CONTROLS, ESCAPE_BACK), x + 24, y + 60);

    // DRAW PLAYER COIN WINDOW
    x = TILE_SIZE * 12;
    drawSubWindow(x, y, width, height);
    graphics2D.drawString(String.format(TextManager.getUiText(UI_MESSAGES, YOUR_COIN), gamePanel.player.money), x + 24, y + 60);

    // DRAW PRICE WINDOW
    int itemIndex = getItemIndexFromInventory(npcSlotCol, npcSlotRow);
    if (itemIndex < npc.inventory.size()) {
      x = (int) (TILE_SIZE * 5.5);
      y = (int) (TILE_SIZE * 5.5);
      width = (int) (TILE_SIZE * 2.5);
      height = TILE_SIZE;
      drawSubWindow(x, y, width, height);
      graphics2D.drawImage(coin, x + 10, y + 8, 32, 32, null);

      int price = npc.inventory.get(itemIndex).price;
      x = getXForAlignTextToRight(String.valueOf(price), TILE_SIZE * 8 - 20);
      graphics2D.drawString(String.valueOf(price), x, y + 34);

      // BUY ITEM
      if (gamePanel.keyHandler.isEnterPressed()) {
        if (npc.inventory.get(itemIndex).price > gamePanel.player.money) {
          subState = 0;
          gamePanel.gameState = DIALOG_STATE;
          npc.startDialogue(npc, MERCHANT_NO_MONEY_DIALOGUE_KEY);
        } else if (!gamePanel.player.canObtainItem(npc.inventory.get(itemIndex))) {
          subState = 0;
          npc.startDialogue(npc, MERCHANT_TO_MUCH_ITEMS_DIALOGUE_KEY);
        } else {
          gamePanel.player.money -= npc.inventory.get(itemIndex).price;
        }
      }
    }
  }

  private void tradeSell() {
    drawInventory(gamePanel.player, true);

    // DRAW HINT WINDOW
    int x = TILE_SIZE * 2;
    int y = TILE_SIZE * 9;
    int width = TILE_SIZE * 6;
    int height = TILE_SIZE * 2;
    drawSubWindow(x, y, width, height);
    graphics2D.drawString(TextManager.getSettingText(CONTROLS, ESCAPE_BACK), x + 24, y + 60);

    // DRAW PLAYER COIN WINDOW
    x = TILE_SIZE * 12;
    drawSubWindow(x, y, width, height);
    graphics2D.drawString(String.format(TextManager.getUiText(UI_MESSAGES, YOUR_COIN), gamePanel.player.money), x + 24, y + 60);

    // DRAW PRICE WINDOW
    int itemIndex = getItemIndexFromInventory(playerSlotCol, playerSlotRow);
    if (itemIndex < gamePanel.player.inventory.size()) {
      x = (int) (TILE_SIZE * 15.5);
      y = (int) (TILE_SIZE * 5.5);
      width = (int) (TILE_SIZE * 2.5);
      height = TILE_SIZE;
      drawSubWindow(x, y, width, height);
      graphics2D.drawImage(coin, x + 10, y + 8, 32, 32, null);

      int price = gamePanel.player.inventory.get(itemIndex).price / 2;
      x = getXForAlignTextToRight(String.valueOf(price), TILE_SIZE * 18 - 20);
      graphics2D.drawString(String.valueOf(price), x, y + 34);

      // SELL ITEM
      if (gamePanel.keyHandler.isEnterPressed()) {
        if (gamePanel.player.inventory.get(itemIndex) == gamePanel.player.currentWeapon
            || gamePanel.player.inventory.get(itemIndex) == gamePanel.player.currentShield) {
          commandNum = 0;
          subState = 0;
          npc.startDialogue(npc, MERCHANT_CANNOT_SELL_KEY);
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
    int textX = frameX + TILE_SIZE;
    int textY = frameY + TILE_SIZE * 3;

    for (String line : TextManager.getUiText(UI_MESSAGES, END_GAME_QUESTION).split("\n")) {
      graphics2D.drawString(line, textX, textY);
      textY += 40;
    }

    // YES OPTION
    textX = getXForCenteredText(TextManager.getUiText(DIALOGUES, YES));
    textY += TILE_SIZE * 3;
    graphics2D.drawString(TextManager.getUiText(DIALOGUES, YES), textX, textY);
    if (commandNum == 0) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.isEnterPressed()) {
        gamePanel.stopMusic();
        subState = 0;
        graphics2D.setColor(Color.BLACK);
        graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
        gamePanel.gameState = TITLE_STATE;
        gamePanel.resetGame(true);
      }
    }

    // NO OPTION
    textX = getXForCenteredText(TextManager.getUiText(DIALOGUES, NO));
    textY += TILE_SIZE;
    graphics2D.drawString(TextManager.getUiText(DIALOGUES, NO), textX, textY);
    if (commandNum == 1) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.isEnterPressed()) {
        subState = 0;
        commandNum = 4;
      }
    }
  }

  private void optionsMainSettings(int frameX, int frameY) {
    int textX;
    int textY;

    // TITLE
    textX = getXForCenteredText(TextManager.getSettingText(MENU_OPTIONS, OPTIONS));
    textY = frameY + TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(MENU_OPTIONS, OPTIONS), textX, textY);

    // FULL SCREEN ON/OFF
    textX = frameX + TILE_SIZE;
    textY += TILE_SIZE * 2;
    graphics2D.drawString(TextManager.getSettingText(SETTINGS, FULL_SCREEN), textX, textY);
    if (commandNum == 0) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.isEnterPressed()) {
        gamePanel.fullScreenOn = !gamePanel.fullScreenOn;
        subState = 1;
      }
    }

    // MUSIC
    textY += TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(SETTINGS, MUSIC), textX, textY);
    if (commandNum == 1)
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);

    // SE
    textY += TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(SETTINGS, SOUND_EFFECT), textX, textY);
    if (commandNum == 2)
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
    // CONTROL
    textY += TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(CONTROLS, CONTROL), textX, textY);
    if (commandNum == 3) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.isEnterPressed()) {
        subState = 2;
        commandNum = 0;
      }
    }

    // END GAME
    textY += TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(MENU_OPTIONS, END_GAME), textX, textY);
    if (commandNum == 4) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.isEnterPressed()) {
        subState = 3;
        commandNum = 0;
      }

    }
    // BACK
    textY += TILE_SIZE * 2;
    graphics2D.drawString(TextManager.getSettingText(MENU_OPTIONS, BACK), textX, textY);
    if (commandNum == 5) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.isEnterPressed()) {
        gamePanel.gameState = PLAY_STATE;
        commandNum = 0;
      }
    }

    // FULL SCREEN CHECK BOX
    textX = frameX + (int) (TILE_SIZE * 4.5);
    textY = frameY + TILE_SIZE * 2 + TILE_SIZE / 2;
    graphics2D.setStroke(new BasicStroke(2));
    graphics2D.drawRect(textX, textY, TILE_SIZE / 2, TILE_SIZE / 2);
    if (gamePanel.fullScreenOn) {
      graphics2D.fillRect(textX, textY, TILE_SIZE / 2, TILE_SIZE / 2);
    }

    // MUSIC VOLUME
    textY += TILE_SIZE;
    graphics2D.drawRect(textX, textY, 120, TILE_SIZE / 2); // 120/5 = 24 px
    int volumeWidth = 24 * gamePanel.music.getVolumeScale();
    graphics2D.fillRect(textX, textY, volumeWidth, 24);

    // SE VOLUME
    textY += TILE_SIZE;
    graphics2D.drawRect(textX, textY, 120, TILE_SIZE / 2);
    volumeWidth = 24 * gamePanel.soundEffect.getVolumeScale();
    graphics2D.fillRect(textX, textY, volumeWidth, 24);

    gamePanel.config.saveConfig();
  }

  public void optionsControl(int frameX, int frameY) {
    int textX;
    int textY;

    textX = getXForCenteredText(TextManager.getSettingText(CONTROLS, CONTROL));
    textY = frameY + TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(CONTROLS, CONTROL), textX, textY);

    textX = frameX + TILE_SIZE;
    textY += TILE_SIZE;

    graphics2D.drawString(TextManager.getSettingText(CONTROLS, MOVE), textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(CONTROLS, CONFIRM_ATTACK), textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(CONTROLS, SHOOT_CAST), textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(CONTROLS, PARRY), textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(CONTROLS, CHARACTER_SCREEN), textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(CONTROLS, PAUSE), textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(TextManager.getSettingText(MENU_OPTIONS, OPTIONS), textX, textY);

    textX = frameX + TILE_SIZE * 6;
    textY = frameY + TILE_SIZE * 2;

    graphics2D.drawString(CHARACTER_CONTROL, textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(CONFIRM, textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(PROJECTILE_KEY, textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(SPACE_KEY, textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(CHARACTER_KEY, textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(PAUSE_KEY, textX, textY);
    textY += TILE_SIZE;
    graphics2D.drawString(CLOSE_KEY, textX, textY);
    textY += TILE_SIZE;

    // BACK
    textX = frameX + TILE_SIZE;
    textY = frameY + TILE_SIZE * 9;
    graphics2D.drawString(TextManager.getSettingText(MENU_OPTIONS, BACK), textX, textY);

    if (commandNum == 0) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.isEnterPressed()) {
        subState = 0;
        commandNum = 3;
      }
    }
  }

  private void drawInventory(GameEntity gameEntity, boolean cursor) {
    int frameX;
    int frameY = TILE_SIZE;
    int frameWidth = TILE_SIZE * 6;
    int frameHeight = TILE_SIZE * 5;
    int slotCol;
    int slotRow;

    if (gameEntity == gamePanel.player) {
      frameX = TILE_SIZE * 12;
      slotCol = playerSlotCol;
      slotRow = playerSlotRow;
    } else {
      frameX = TILE_SIZE * 2;
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
    int slotSize = TILE_SIZE + 3;


    // DRAW PLAYER'S ITEMS
    for (int inventoryItem = 0; inventoryItem < gameEntity.inventory.size(); inventoryItem++) {

      // EQUIP CURSOR
      if (gameEntity.inventory.get(inventoryItem) == gameEntity.currentWeapon ||
          gameEntity.inventory.get(inventoryItem) == gameEntity.currentShield ||
          gameEntity.inventory.get(inventoryItem) == gameEntity.currentLightItem) {
        graphics2D.setColor(new Color(240, 190, 90));
        graphics2D.fillRoundRect(slotX, slotY, TILE_SIZE, TILE_SIZE, 10, 10);
      }
      graphics2D.drawImage(gameEntity.inventory.get(inventoryItem).down1, slotX, slotY, null);

      // DISPLAY AMOUNT
      if (gameEntity == gamePanel.player && gameEntity.inventory.get(inventoryItem).amount > 1) {
        graphics2D.setFont(graphics2D.getFont().deriveFont(32f));
        int amountX;
        int amountY;
        amountX = getXForAlignTextToRight(String.valueOf(gameEntity.inventory.get(inventoryItem).amount), slotX + 44);
        amountY = slotY + TILE_SIZE;

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

      graphics2D.setColor(Color.WHITE);
      graphics2D.setStroke(new BasicStroke(3));
      graphics2D.drawRoundRect(cursorX, cursorY, TILE_SIZE, TILE_SIZE, 10, 10);

      // DESCRIPTION FRAME
      int descriptionFrameY = frameY + frameHeight;
      int descriptionFrameHeight = TILE_SIZE * 3;

      // DESCRIPTION TEXT
      int textX = frameX + 20;
      int textY = descriptionFrameY + TILE_SIZE;
      graphics2D.setFont(graphics2D.getFont().deriveFont(28F));

      int itemIndex = getItemIndexFromInventory(slotCol, slotRow);
      if (itemIndex < gameEntity.inventory.size()) {
        drawSubWindow(frameX, descriptionFrameY, frameWidth, descriptionFrameHeight);
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
    int textX = frameX + TILE_SIZE;
    int textY = frameY + TILE_SIZE * 3;

    for (String line : TextManager.getUiText(UI_MESSAGES, FULL_SCREEN_NOTIFICATION).split("\n")) {
      graphics2D.drawString(line, textX, textY);
      textY += 40;
    }

    // BACK
    textY = frameY + TILE_SIZE * 9;
    graphics2D.drawString(TextManager.getSettingText(MENU_OPTIONS, BACK), textX, textY);
    if (commandNum == 0) {
      graphics2D.drawString(CURSOR_SELECTOR, textX - 25, textY);
      if (gamePanel.keyHandler.isEnterPressed()) {
        subState = 0;
      }
    }
  }

  private void drawMessage() {
    int messageX = TILE_SIZE;
    int messageY = TILE_SIZE * 4;

    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 20F));

    for (int i = 0; i < messages.size(); i++) {
      if (Objects.nonNull(messages.get(i))) {
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
    final int frameX = TILE_SIZE * 2;
    final int frameY = TILE_SIZE;
    final int frameWidth = TILE_SIZE * 6;
    final int frameHeight = TILE_SIZE * 10;

    // TEXT
    graphics2D.setColor(Color.WHITE);
    graphics2D.setFont(graphics2D.getFont().deriveFont(25F));
    drawSubWindow(frameX, frameY, frameWidth, frameHeight);

    int textX = frameX + 20;
    int textY = frameY + TILE_SIZE;
    final int lineHeight = 35;

    // NAMES
    graphics2D.drawString(TextManager.getUiText(GAME_STATS, LEVEL), textX, textY);
    textY += lineHeight;
    graphics2D.drawString(TextManager.getUiText(GAME_STATS, LIFE), textX, textY);
    textY += lineHeight;
    graphics2D.drawString(TextManager.getUiText(GAME_STATS, STRENGTH), textX, textY);
    textY += lineHeight;
    graphics2D.drawString(TextManager.getUiText(GAME_STATS, DEXTERITY), textX, textY);
    textY += lineHeight;
    graphics2D.drawString(TextManager.getUiText(GAME_STATS, ATTACK), textX, textY);
    textY += lineHeight;
    graphics2D.drawString(TextManager.getUiText(GAME_STATS, DEFENSE), textX, textY);
    textY += lineHeight;
    graphics2D.drawString(TextManager.getUiText(GAME_STATS, EXP), textX, textY);
    textY += lineHeight;
    graphics2D.drawString(TextManager.getUiText(GAME_STATS, NEXT_LEVEL), textX, textY);
    textY += lineHeight;
    graphics2D.drawString(TextManager.getUiText(GAME_STATS, COIN), textX, textY);
    textY += lineHeight + 20;
    graphics2D.drawString(TextManager.getUiText(GAME_STATS, WEAPON), textX, textY);
    textY += lineHeight + 15;
    graphics2D.drawString(TextManager.getUiText(GAME_STATS, SHIELD), textX, textY);

    // VALUES
    int tailX = (frameX + frameWidth) - 30;

    textY = frameY + TILE_SIZE;
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

    graphics2D.drawImage(gamePanel.player.currentWeapon.down1, tailX - TILE_SIZE, textY - 14, null);
    textY += TILE_SIZE;

    graphics2D.drawImage(gamePanel.player.currentShield.down1, tailX - TILE_SIZE, textY - 14, null);
  }

  private void drawPlayerLife() {
    int x = TILE_SIZE - 15;
    int y = TILE_SIZE / 2;
    int iconSize = 32;
    int i = 0;

    // DRAW MAX LIFE
    while (i < gamePanel.player.maxLife / 2) {
      graphics2D.drawImage(heartBlank, x, y, iconSize, iconSize, null);
      i++;
      x += TILE_SIZE - 15;
    }

    x = TILE_SIZE - 15;
    i = 0;

    // DRAW CURRENT LIFE
    while (i < gamePanel.player.currentLife) {
      graphics2D.drawImage(heartHalf, x, y, iconSize, iconSize, null);
      i++;
      if (i < gamePanel.player.currentLife) {
        graphics2D.drawImage(heartFull, x, y, iconSize, iconSize, null);
      }
      i++;
      x += TILE_SIZE - 15;
    }

    // DRAW MAX MANA
    x = TILE_SIZE - 20;
    y = TILE_SIZE + 10;
    i = 0;
    while (i < gamePanel.player.maxMana) {
      graphics2D.drawImage(manaCrystalBlank, x, y, iconSize, iconSize, null);
      i++;
      x += 15;
    }

    // DRAW MANA
    x = TILE_SIZE - 20;
    i = 0;
    while (i < gamePanel.player.mana) {
      graphics2D.drawImage(manaCrystalFull, x, y, iconSize, iconSize, null);
      i++;
      x += 15;
    }
  }

  public void drawMonsterLife() {
    for (GameEntity monster : gamePanel.mapsMonsters.get(CURRENT_MAP)) {
      if (Objects.nonNull(monster) && monster.inCamera()) {
        boolean isBoss = monster.boss;
        double oneScaleLifeBar = (double) (TILE_SIZE * (isBoss ? 8 : 1)) / monster.maxLife;
        double hpBarValue = oneScaleLifeBar * monster.currentLife;
        int barWidth = TILE_SIZE * (isBoss ? 8 : 1);
        int barHeight = isBoss ? 20 : 10;
        int x = isBoss ? (gamePanel.screenWidth / 2 - TILE_SIZE * 4) : monster.getScreenX();
        int y = isBoss ? (TILE_SIZE * 10) : (monster.getScreenY() - 15);

        graphics2D.setColor(new Color(35, 35, 35));
        graphics2D.fillRect(x - 1, y - 1, barWidth + 2, barHeight + 2);

        graphics2D.setColor(new Color(255, 0, 30));
        graphics2D.fillRect(x, y, (int) hpBarValue, barHeight);

        if (isBoss) {
          graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 24F));
          graphics2D.setColor(Color.WHITE);
          graphics2D.drawString(monster.name, x + 4, y - 10);
        } else {
          if (++monster.hpBarCounter > 600) {
            monster.hpBarCounter = 0;
            monster.hpBarOn = false;
          }
        }
      }
    }
  }

  private void drawTitleScreen() {
    // CLEAR
    graphics2D.setColor(Color.BLACK);
    graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
    // TITLE NAME
    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 96F));
    int x = getXForCenteredText(TextManager.getSettingText(MENU_OPTIONS, TITLE));
    int y = TILE_SIZE * 3;

    // SHADOW
    graphics2D.setColor(Color.GRAY);
    graphics2D.drawString(TextManager.getSettingText(MENU_OPTIONS, TITLE), x + 5, y + 5);
    // MAIN COLOR
    graphics2D.setColor(Color.WHITE);
    graphics2D.drawString(TextManager.getSettingText(MENU_OPTIONS, TITLE), x, y);

    // BLUE BOY IMAGE
    x = gamePanel.screenWidth / 2 - (TILE_SIZE * 2) / 2;
    y += TILE_SIZE * 2;
    graphics2D.drawImage(gamePanel.player.down1, x, y, TILE_SIZE * 2, TILE_SIZE * 2, null);

    // MENU
    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.BOLD, 48F));

    String[] menuOptions = {
        TextManager.getSettingText(MENU_OPTIONS, NEW_GAME),
        TextManager.getSettingText(MENU_OPTIONS, LOAD_GAME),
        TextManager.getSettingText(MENU_OPTIONS, QUIT)
    };

    y += TILE_SIZE * 3;

    for (int i = 0; i < menuOptions.length; i++) {
      x = getXForCenteredText(menuOptions[i]);
      graphics2D.drawString(menuOptions[i], x, y);

      if (commandNum == i) {
        graphics2D.drawString(CURSOR_SELECTOR, x - TILE_SIZE, y);
      }

      y += TILE_SIZE;
    }
  }

  public void drawDialogueScreen() {
    // WINDOW
    int x = TILE_SIZE * 3;
    int y = TILE_SIZE / 2;
    int width = gamePanel.screenWidth - (TILE_SIZE * 6);
    int height = TILE_SIZE * 4;
    drawSubWindow(x, y, width, height);

    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 25F));
    x += TILE_SIZE;
    y += TILE_SIZE;

    List<String> dialoguesForSet = npc.dialogues.get(npc.dialogueSet);

    if (Objects.nonNull(dialoguesForSet) && !dialoguesForSet.isEmpty()) {
      if (npc.dialogueIndex >= dialoguesForSet.size()) {
        npc.dialogueIndex = 0;
      }

      String fullDialogue = dialoguesForSet.get(npc.dialogueIndex);
      currentDialogue = fullDialogue;

      if (characterIndex < fullDialogue.length()) {
        combinedText.append(fullDialogue.charAt(characterIndex));
        currentDialogue = combinedText.toString();
        characterIndex++;
        gamePanel.playSoundEffect(SPEAK);
      }

      if (gamePanel.keyHandler.isEnterPressed() && (gamePanel.gameState == DIALOG_STATE
          || gamePanel.gameState == CUTSCENE_STATE)) {

        characterIndex = 0;
        combinedText.setLength(0);
        npc.dialogueIndex++;
        gamePanel.keyHandler.setEnterPressed(false);


        if (npc.dialogueIndex >= dialoguesForSet.size()) {
          npc.dialogueIndex = 0;
          if (gamePanel.gameState == CUTSCENE_STATE) {
            gamePanel.cutsceneManager.setScenePhase(gamePanel.cutsceneManager.getScenePhase() + 1);
          } else {
            gamePanel.gameState = PLAY_STATE;
          }
        }
      }
    } else {
      npc.dialogueIndex = 0;
      if (gamePanel.gameState == DIALOG_STATE) {
        gamePanel.gameState = PLAY_STATE;
      }
    }

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
      CURRENT_MAP = gamePanel.eventHandler.getTempMap();
      gamePanel.gameMap.createWorldMap();
      gamePanel.player.worldX = TILE_SIZE * gamePanel.eventHandler.getTempCol();
      gamePanel.player.worldY = TILE_SIZE * gamePanel.eventHandler.getTempRow();
      gamePanel.eventHandler.setPreviousEventX(gamePanel.player.worldX);
      gamePanel.eventHandler.setPreviousEventY(gamePanel.player.worldY);
      gamePanel.changeArea();
    }
  }

  private void drawPauseScreen() {
    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 80F));
    int x = getXForCenteredText(TextManager.getUiText(UI_MESSAGES, PAUSED));
    int y = gamePanel.screenHeight / 2;

    graphics2D.drawString(TextManager.getUiText(UI_MESSAGES, PAUSED), x, y);
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
