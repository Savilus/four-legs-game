package org.example;

import java.awt.*;

import javax.swing.*;

// Game screen
public class GamePanel extends JPanel implements Runnable {

  // SCREEN SETTINGS
  final int originalTitleSize = 16; // 16x16 tile
  final int scale = 3;

  final int tileSize = originalTitleSize * scale;
  final int maxScreenColumn = 16;
  final int maxScreenRow = 12;
  final int screenWidth = tileSize * maxScreenColumn;
  final int screenHeight = tileSize * maxScreenRow;

  final int FPS = 60;

  KeyHandler keyHandler = new KeyHandler();
  Thread gameThread;

  // player starting position
  int playerX = 100;
  int playerY = 100;
  int playerSpped = 4;

  public GamePanel() {
    this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    this.setBackground(Color.BLACK);
    this.setDoubleBuffered(true);
    this.addKeyListener(keyHandler);
    this.setFocusable(true);
  }

  public void startGameThread() {
    gameThread = new Thread(this);
    gameThread.start();
  }

  public void update() {
    if (keyHandler.upPressed) {
      playerY -= playerSpped;
    } else if (keyHandler.downPressed) {
      playerY += playerSpped;
    } else if (keyHandler.leftPressed) {
      playerX -= playerSpped;
    } else if (keyHandler.rightPressed) {
      playerX += playerSpped;

    }
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D graphic2d = (Graphics2D) g;
    graphic2d.setColor(Color.WHITE);
    graphic2d.fillRect(playerX, playerY, tileSize, tileSize);
    graphic2d.dispose();
  }

  @Override
  public void run() {
    double drawInterval = 1000000000 / FPS;
    double delta = 0;
    long lastTime = System.nanoTime();
    long currentTime;
    long timer = 0;
    long drawCount = 0;

    while (gameThread != null) {

      currentTime = System.nanoTime();

      delta += (currentTime - lastTime) / drawInterval;
      timer += (currentTime - lastTime);
      lastTime = currentTime;

      if (delta >= 1) {
        update();
        repaint();
        delta--;
        drawCount++;
      }

      if (timer >= 1000000000) {
        System.out.println("FPS: " + drawCount);
        drawCount = 0;
        timer = 0;
      }
    }
  }

//  @Override
//  public void run() {
//
//    double drawInterval = (double) 1000000000 / FPS; // 0.1666 seconds
//    double nextDrawTime = System.nanoTime() + drawInterval;
//
//    while (gameThread != null) {
//      //1 UPDATE: update information such charachter position
//      update();
//      //2 DRAW: draw screen with updated information
//      repaint();
//
//      try {
//        double remainingTime = nextDrawTime - System.nanoTime();
//        remainingTime = remainingTime / 1000000;
//
//        if (remainingTime < 0) {
//          remainingTime = 0;
//        }
//
//        Thread.sleep((long) remainingTime);
//
//        nextDrawTime += drawInterval;
//      } catch (InterruptedException e) {
//        throw new RuntimeException(e);
//      }
//    }
//  }


}
