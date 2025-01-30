package org.example;

import java.awt.*;

public class UI {

  GamePanel gamePanel;
  Graphics2D graphics2D;
  Font arialFont, arialFontBold;
  public boolean messageOn = false;
  public String message = "";
  int messageCounter = 0;
  public boolean gameFinished = false;
  public String currentDialogue = "";

  public UI(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    arialFont = new Font("Arial", Font.PLAIN, 30);
    arialFontBold = new Font("Arial", Font.BOLD, 80);
  }

  public void showMessage(String text) {
    message = text;
    messageOn = true;
  }

  public void draw(Graphics2D graphics2D) {
    this.graphics2D = graphics2D;
    graphics2D.setFont(arialFont);
    graphics2D.setColor(Color.WHITE);

    if (gamePanel.gameState == gamePanel.playState) {

    } else if (gamePanel.gameState == gamePanel.pauseState) {
      drawPauseScreen();
    } else if (gamePanel.gameState == gamePanel.dialogueState) {
      drawDialogueScreen();
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
