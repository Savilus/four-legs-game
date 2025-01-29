package org.example;

import static org.example.config.GameMessageFactory.CONGRATS;
import static org.example.config.GameMessageFactory.TREASURE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import org.example.object.KeyObject;

public class UI {

  GamePanel gamePanel;
  Font arialFont, arialFontBold;
  BufferedImage keyImage;
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
    KeyObject key = new KeyObject();
    keyImage = key.image;
  }

  public void showMessage(String text) {
    message = text;
    messageOn = true;
  }

  public void draw(Graphics2D graphics2D) {
    graphics2D.setFont(arialFont);
    graphics2D.setColor(Color.WHITE);
    if (gameFinished) {

      String text;
      int textLength;
      int x;
      int y;

      textLength = (int) graphics2D.getFontMetrics().getStringBounds(TREASURE, graphics2D).getWidth();
      x = gamePanel.screenWidth / 2 - textLength / 2;
      y = gamePanel.screenHeight / 2 - (gamePanel.tileSize * 3);
      graphics2D.drawString(TREASURE, x, y);

      text = "Your Time is: " + decimalFormat.format(playTime) + "!";
      textLength = (int) graphics2D.getFontMetrics().getStringBounds(text, graphics2D).getWidth();
      x = gamePanel.screenWidth / 2 - textLength / 2;
      y = gamePanel.screenHeight / 2 + (gamePanel.tileSize * 4);
      graphics2D.drawString(text, x, y);

      graphics2D.setFont(arialFontBold);
      graphics2D.setColor(Color.YELLOW);

      textLength = (int) graphics2D.getFontMetrics().getStringBounds(CONGRATS, graphics2D).getWidth();
      x = gamePanel.screenWidth / 2 - textLength / 2;
      y = gamePanel.screenHeight / 2 + (gamePanel.tileSize * 2);
      graphics2D.drawString(CONGRATS, x, y);

      gamePanel.gameThread = null;
    } else {
      graphics2D.drawImage(keyImage, gamePanel.tileSize / 2, gamePanel.tileSize / 2, gamePanel.tileSize, gamePanel.tileSize, null);
      graphics2D.drawString("x " + gamePanel.player.playerKeys, 75, 50);
      playTime += (double) 1 / 60;

      graphics2D.drawString("Time: " + decimalFormat.format(playTime), gamePanel.tileSize * 12, 65);
      if (messageOn) {
        graphics2D.setFont(graphics2D.getFont().deriveFont(30F));
        graphics2D.drawString(message, gamePanel.tileSize / 2, gamePanel.tileSize * 5);

        messageCounter++;

        if (messageCounter == 120) {
          messageCounter = 0;
          messageOn = false;
        }
      }
    }
  }
}
