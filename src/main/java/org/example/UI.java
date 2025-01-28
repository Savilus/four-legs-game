package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.example.object.KeyObject;

public class UI {

  GamePanel gamePanel;
  Font arialFont;
  BufferedImage keyImage;
  public boolean messageOn = false;
  public String message = "";

  public UI(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    arialFont = new Font("Arial", Font.PLAIN, 20);
    // TODO: CHANGE TO key configuration
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
    graphics2D.drawImage(keyImage, gamePanel.tileSize / 2, gamePanel.tileSize / 2, gamePanel.tileSize, gamePanel.tileSize, null);
    graphics2D.drawString("x " + gamePanel.player.playerKeys, 75, 50);

    // MESSAGE
    if (messageOn) {
      graphics2D.setFont(graphics2D.getFont().deriveFont(30F));
      graphics2D.drawString(message, gamePanel.tileSize / 2, gamePanel.tileSize * 5);
    }
  }
}
