package org.example;

import java.awt.*;
import java.text.DecimalFormat;

public class UI {

  GamePanel gamePanel;
  Graphics2D graphics2D;
  Font arialFont, arialFontBold;
  public boolean messageOn = false;
  public String message = "";
  int messageCounter = 0;
  public boolean gameFinished = false;
  double playTime;
  DecimalFormat decimalFormat = new DecimalFormat("#.00");

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
    }
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
