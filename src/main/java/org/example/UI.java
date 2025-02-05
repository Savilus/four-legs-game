package org.example;

import static org.example.config.GameEntityNameFactory.MARU_MONICA_FONT;
import static org.example.config.GameEntityNameFactory.PURISA_BOLD_FONT;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

import org.example.entity.GameEntity;
import org.example.entity.object.HeartObject;

public class UI {
  private static final String TITLE = "Four Legs";
  private static final String NEW_GAME = "New Game";
  private static final String LOAD_GAME = "Load game";
  private static final String QUIT = "Quit";

  BufferedImage heartFull, heartHalf, heartBlank;
  GamePanel gamePanel;
  Graphics2D graphics2D;
  Font maruMonica, purisaBoldFont;
  public boolean messageOn = false;
  public String message = "";
  int messageCounter = 0;
  public boolean gameFinished = false;
  public String currentDialogue = "";
  public int commandNum = 0;

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

    GameEntity heart = new HeartObject(gamePanel);
    heartFull = heart.image;
    heartHalf = heart.image2;
    heartBlank = heart.image3;
  }

  public void showMessage(String text) {
    message = text;
    messageOn = true;
  }

  public void draw(Graphics2D graphics2D) {
    this.graphics2D = graphics2D;
    graphics2D.setFont(purisaBoldFont);
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics2D.setColor(Color.WHITE);

    switch (gamePanel.gameState) {
      case TITLE_STATE -> drawTitleScreen();
      case PLAY_STATE -> drawPlayerLife();
      case PAUSE_STATE -> {
        drawPlayerLife();
        drawPauseScreen();
      }
      case DIALOG_STATE -> {
        drawPlayerLife();
        drawDialogueScreen();
      }
    }

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
}
