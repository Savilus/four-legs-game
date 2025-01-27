package org.example;

import java.awt.*;

import javax.swing.*;

import org.example.entity.Player;
import org.example.object.SuperObject;
import org.example.tile.TileManager;

// Game screen
public class GamePanel extends JPanel implements Runnable {

  // SCREEN SETTINGS
  final int originalTitleSize = 16; // 16x16 tile
  final int scale = 3;

  public final int tileSize = originalTitleSize * scale; // 48 x 48 tile
  public final int maxScreenColumn = 16;
  public final int maxScreenRow = 12;
  public final int screenWidth = tileSize * maxScreenColumn; // 768 pixels
  public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

  // WORLD SETTINGS
  public final int maxWorldCol = 50;
  public final int maxWorldRow = 50;
  public final int worldWidth = tileSize * maxWorldCol;
  public final int worldHeight = tileSize * maxWorldRow;

  static final int FPS = 60;

  TileManager tileManager = new TileManager(this);
  KeyHandler keyHandler = new KeyHandler();
  Thread gameThread;
  public CollisionDetector collisionDetector = new CollisionDetector(this);
  public AssetSetter assetSetter = new AssetSetter(this);
  public Player player = new Player(this, keyHandler);
  public SuperObject[] obj = new SuperObject[10];


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
    player.update();
  }

  public void setupGame() {
    assetSetter.setObject();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D graphic2d = (Graphics2D) g;
    tileManager.draw(graphic2d);
    for (SuperObject superObject : obj) {
      if (superObject != null) {
        superObject.draw(graphic2d, this);
      }
    }
    player.draw(graphic2d);
    graphic2d.dispose();
  }

  @Override
  public void run() {
    double drawInterval = (double) 1000000000 / FPS;
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

}
