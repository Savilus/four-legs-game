package org.example;

import static org.example.config.GameEntityNameFactory.MARU_MONICA_FONT;
import static org.example.config.GameEntityNameFactory.PURISA_BOLD_FONT;
import static org.example.enums.GameStateType.PLAY_STATE;
import static org.example.enums.GameStateType.TITLE_STATE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

import org.example.entity.GameEntity;
import org.example.entity.object.HeartObject;
import org.example.entity.object.ManaCrystalObject;
import org.example.enums.GameStateType;

public class UI {
  private static final String TITLE = "Four Legs";
  private static final String NEW_GAME = "New Game";
  private static final String LOAD_GAME = "Load game";
  private static final String QUIT = "Quit";

  BufferedImage heartFull, heartHalf, heartBlank, manaCrystalFull, manaCrystalBlank;
  GamePanel gamePanel;
  Graphics2D graphics2D;
  Font maruMonica, purisaBoldFont;
  ArrayList<String> messages = new ArrayList<>();
  ArrayList<Integer> messageCounter = new ArrayList<>();
  public boolean messageOn = false;
  public boolean gameFinished = false;
  public String currentDialogue = "";
  public int commandNum = 0;
  public int subState = 0;
  public int slotCol = 0;
  public int slotRow = 0;

  public UI(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    try {
      var fontInputStream = Objects.requireNonNull(getClass().getResourceAsStream(MARU_MONICA_FONT));
      maruMonica = Font.createFont(Font.TRUETYPE_FONT, fontInputStream);
      fontInputStream = Objects.requireNonNull(getClass().getResourceAsStream(PURISA_BOLD_FONT));
      purisaBoldFont = Font.createFont(Font.TRUETYPE_FONT, fontInputStream);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    // CREATE HUD OBJECT
    GameEntity heart = new HeartObject(gamePanel);
    heartFull = heart.image;
    heartHalf = heart.image2;
    heartBlank = heart.image3;
    GameEntity manaCrysta = new ManaCrystalObject(gamePanel);
    manaCrystalFull = manaCrysta.image;
    manaCrystalBlank = manaCrysta.image2;
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
        drawPlayerLife();
        drawDialogueScreen();
      }
      case CHARACTER_STATE -> {
        drawCharacterScreen();
        drawInventory();
      }
      case OPTIONS_STATE -> drawOptionsScreen();
    }
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
      case 0 -> optionsTop(frameX, frameY);
      case 1 -> optionsFullScreenNotification(frameX, frameY);
      case 2 -> optionsControl(frameX, frameY);
      case 3 -> optionsEndGameConfirmation(frameX, frameY);
    }

    gamePanel.keyHandler.enterPressed = false;
  }

  private void optionsEndGameConfirmation(int frameX, int frameY) {
    int textX = frameX + gamePanel.tileSize;
    int textY = frameY + gamePanel.tileSize * 3;

    currentDialogue = "Are you sure you want to \nend the game?";

    for (String line : currentDialogue.split("\n")) {
      graphics2D.drawString(line, textX, textY);
      textY += 40;
    }

    // YES OPTION
    String text = "Yes";
    textX = getXForCenteredText(text);
    textY += gamePanel.tileSize * 3;
    graphics2D.drawString(text, textX, textY);
    if (commandNum == 0) {
      graphics2D.drawString(">", textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        subState = 0;
        gamePanel.gameState = TITLE_STATE;
      }
    }

    // NO OPTION
    text = "No";
    textX = getXForCenteredText(text);
    textY += gamePanel.tileSize;
    graphics2D.drawString(text, textX, textY);
    if (commandNum == 1) {
      graphics2D.drawString(">", textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        subState = 0;
        commandNum = 4;
      }
    }
  }

  private void optionsTop(int frameX, int frameY) {
    int textX;
    int textY;

    // TITLE
    String title = "Options";
    textX = getXForCenteredText(title);
    textY = frameY + gamePanel.tileSize;
    graphics2D.drawString(title, textX, textY);

    // FULL SCREEN ON/OFF
    textX = frameX + gamePanel.tileSize;
    textY += gamePanel.tileSize * 2;
    graphics2D.drawString("Full screen", textX, textY);
    if (commandNum == 0) {
      graphics2D.drawString(">", textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        gamePanel.fullScreenOn = !gamePanel.fullScreenOn;
        subState = 1;
      }
    }


    // MUSIC
    textY += gamePanel.tileSize;
    graphics2D.drawString("Music", textX, textY);
    if (commandNum == 1)
      graphics2D.drawString(">", textX - 25, textY);

    // SE
    textY += gamePanel.tileSize;
    graphics2D.drawString("Sound Effect", textX, textY);
    if (commandNum == 2)
      graphics2D.drawString(">", textX - 25, textY);
    // CONTROL
    textY += gamePanel.tileSize;
    graphics2D.drawString("Control", textX, textY);
    if (commandNum == 3) {
      graphics2D.drawString(">", textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        subState = 2;
        commandNum = 0;
      }
    }

    // END GAME
    textY += gamePanel.tileSize;
    graphics2D.drawString("End Game", textX, textY);
    if (commandNum == 4) {
      graphics2D.drawString(">", textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        subState = 3;
        commandNum = 0;
      }

    }
    // BACK
    textY += gamePanel.tileSize * 2;
    graphics2D.drawString("Back", textX, textY);
    if (commandNum == 5) {
      graphics2D.drawString(">", textX - 25, textY);
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
  }

  public void optionsControl(int frameX, int frameY) {

    int textX;
    int textY;

    String text = "Control";
    textX = getXForCenteredText(text);
    textY = frameY + gamePanel.tileSize;
    graphics2D.drawString(text, textX, textY);

    textX = frameX + gamePanel.tileSize;
    textY += gamePanel.tileSize;

    graphics2D.drawString("Move", textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString("Confirm/Attack", textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString("Shoot/Cast", textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString("Character Screen", textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString("Pause", textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString("Options", textX, textY);

    textX = frameX + gamePanel.tileSize * 6;
    textY = frameY + gamePanel.tileSize * 2;

    graphics2D.drawString("WASD", textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString("ENTER", textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString("F", textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString("C", textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString("P", textX, textY);
    textY += gamePanel.tileSize;
    graphics2D.drawString("ESC", textX, textY);
    textY += gamePanel.tileSize;

    // BACK
    textX = frameX + gamePanel.tileSize;
    textY = frameY + gamePanel.tileSize * 9;
    graphics2D.drawString("Back", textX, textY);

    if (commandNum == 0) {
      graphics2D.drawString(">", textX - 25, textY);
      if (gamePanel.keyHandler.enterPressed) {
        subState = 0;
        commandNum = 3;
      }
    }

  }


  private void drawInventory() {
    // FRAME
    int frameX = gamePanel.tileSize * 12;
    int frameY = gamePanel.tileSize;
    int frameWidth = gamePanel.tileSize * 6;
    int frameHeight = gamePanel.tileSize * 5;
    drawSubWindow(frameX, frameY, frameWidth, frameHeight);

    // SLOT
    final int slotXStart = frameX + 20;
    final int slotYStart = frameY + 20;
    int slotX = slotXStart;
    int slotY = slotYStart;
    int slotSize = gamePanel.tileSize + 3;

    //CURSOR
    int cursorX = slotXStart + (slotSize * slotCol);
    int cursorY = slotYStart + (slotSize * slotRow);
    int cursorWidth = gamePanel.tileSize;
    int cursorHeight = gamePanel.tileSize;

    // DRAW PLAYER'S ITEMS

    for (int inventoryItem = 0; inventoryItem < gamePanel.player.inventory.size(); inventoryItem++) {

      // EQUIP CURSOR
      if (gamePanel.player.inventory.get(inventoryItem) == gamePanel.player.currentWeapon ||
          gamePanel.player.inventory.get(inventoryItem) == gamePanel.player.currentShield) {
        graphics2D.setColor(new Color(240, 190, 90));
        graphics2D.fillRoundRect(slotX, slotY, gamePanel.tileSize, gamePanel.tileSize, 10, 10);
      }

      graphics2D.drawImage(gamePanel.player.inventory.get(inventoryItem).down1, slotX, slotY, null);
      slotX += slotSize;

      if (inventoryItem == 4 || inventoryItem == 9 || inventoryItem == 14) {
        slotX = slotXStart;
        slotY += slotSize;
      }
    }

    // DRAW CURSOR
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

    int itemIndex = getItemIndexFromInventory();
    if (itemIndex < gamePanel.player.inventory.size()) {
      drawSubWindow(descriptionFrameX, descriptionFrameY, descriptionFrameWidth, descriptionFrameHeight);
      for (String line : gamePanel.player.inventory.get(itemIndex).description.split("\n")) {
        graphics2D.drawString(line, textX, textY);
        textY += 32;
      }
    }
  }

  public int getItemIndexFromInventory() {
    return slotCol + (slotRow * 5);
  }

  private void optionsFullScreenNotification(int frameX, int frameY) {
    int textX = frameX + gamePanel.tileSize;
    int textY = frameY + gamePanel.tileSize * 3;

    currentDialogue = "The change will take \neffect after restarting \nthe game.";

    for (String line : currentDialogue.split("\n")) {
      graphics2D.drawString(line, textX, textY);
      textY += 40;
    }

    // BACK
    textY = frameY + gamePanel.tileSize * 9;
    graphics2D.drawString("Back", textX, textY);
    if (commandNum == 0) {
      graphics2D.drawString(">", textX - 25, textY);
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
    graphics2D.drawString("Level", textX, textY);
    textY += lineHeight;
    graphics2D.drawString("Life", textX, textY);
    textY += lineHeight;
    graphics2D.drawString("Strength", textX, textY);
    textY += lineHeight;
    graphics2D.drawString("Dexterity", textX, textY);
    textY += lineHeight;
    graphics2D.drawString("Attack", textX, textY);
    textY += lineHeight;
    graphics2D.drawString("Defense", textX, textY);
    textY += lineHeight;
    graphics2D.drawString("Exp", textX, textY);
    textY += lineHeight;
    graphics2D.drawString("Next Level", textX, textY);
    textY += lineHeight;
    graphics2D.drawString("Coin", textX, textY);
    textY += lineHeight + 20;
    graphics2D.drawString("Weapon", textX, textY);
    textY += lineHeight + 15;
    graphics2D.drawString("Shield", textX, textY);

    // VALUES
    int tailX = (frameX + frameWidth) - 30;

    textY = frameY + gamePanel.tileSize;
    String value;

    value = String.valueOf(gamePanel.player.level);
    textX = getXForAlignTextToRight(value, tailX);
    graphics2D.drawString(value, textX, textY);
    textY += lineHeight;

    // TODO: use String.format
    value = gamePanel.player.currentLife + "/" + gamePanel.player.maxLife;
    textX = getXForAlignTextToRight(value, tailX);
    graphics2D.drawString(value, textX, textY);
    textY += lineHeight;

    value = String.valueOf(gamePanel.player.strength);
    textX = getXForAlignTextToRight(value, tailX);
    graphics2D.drawString(value, textX, textY);
    textY += lineHeight;

    value = String.valueOf(gamePanel.player.dexterity);
    textX = getXForAlignTextToRight(value, tailX);
    graphics2D.drawString(value, textX, textY);
    textY += lineHeight;

    value = String.valueOf(gamePanel.player.attack);
    textX = getXForAlignTextToRight(value, tailX);
    graphics2D.drawString(value, textX, textY);
    textY += lineHeight;

    value = String.valueOf(gamePanel.player.defense);
    textX = getXForAlignTextToRight(value, tailX);
    graphics2D.drawString(value, textX, textY);
    textY += lineHeight;

    value = String.valueOf(gamePanel.player.exp);
    textX = getXForAlignTextToRight(value, tailX);
    graphics2D.drawString(value, textX, textY);
    textY += lineHeight;

    value = String.valueOf(gamePanel.player.nextLevelExp);
    textX = getXForAlignTextToRight(value, tailX);
    graphics2D.drawString(value, textX, textY);
    textY += lineHeight;

    value = String.valueOf(gamePanel.player.money);
    textX = getXForAlignTextToRight(value, tailX);
    graphics2D.drawString(value, textX, textY);
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

    x = getXForCenteredText(NEW_GAME);
    y += gamePanel.tileSize * 3;
    graphics2D.drawString(NEW_GAME, x, y);
    if (commandNum == 0) {
      graphics2D.drawString(">", x - gamePanel.tileSize, y);
    }

    x = getXForCenteredText(LOAD_GAME);
    y += gamePanel.tileSize;
    graphics2D.drawString(LOAD_GAME, x, y);
    if (commandNum == 1) {
      graphics2D.drawString(">", x - gamePanel.tileSize, y);
    }

    x = getXForCenteredText(QUIT);
    y += gamePanel.tileSize;
    graphics2D.drawString(QUIT, x, y);
    if (commandNum == 2) {
      graphics2D.drawString(">", x - gamePanel.tileSize, y);
    }
  }

  private void drawDialogueScreen() {
    // WINDOW
    int x = gamePanel.tileSize * 2;
    int y = gamePanel.tileSize / 2;
    int width = gamePanel.screenWidth - (gamePanel.tileSize * 4);
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

  private void drawPauseScreen() {
    String text = "PAUSED";
    graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, 80F));
    int x = getXForCenteredText(text);
    int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
    x = gamePanel.screenWidth / 2 - length / 2;
    int y = gamePanel.screenHeight / 2;

    graphics2D.drawString(text, x, y);
  }

  private int getXForCenteredText(String text) {
    int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
    return gamePanel.screenWidth / 2 - length / 2;
  }

  private int getXForAlignTextToRight(String text, int tailX) {
    int length = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
    return tailX - length;
  }
}
