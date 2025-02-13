package org.example;

import static org.example.enums.GameStateType.PAUSE_STATE;
import static org.example.enums.GameStateType.PLAY_STATE;
import static org.example.enums.GameStateType.TITLE_STATE;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.*;

import org.example.entity.GameEntity;
import org.example.entity.Player;
import org.example.enums.GameStateType;
import org.example.tile.TileManager;
import org.example.utils.AssetSetter;
import org.example.utils.CollisionDetector;
import org.example.utils.EventHandler;
import org.example.utils.KeyHandler;

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

  static final int FPS = 60;
  // SYSTEM
  public TileManager tileManager = new TileManager(this);
  public KeyHandler keyHandler = new KeyHandler(this);
  Sound music = new Sound();
  Sound soundEffect = new Sound();
  public CollisionDetector collisionDetector = new CollisionDetector(this);
  public AssetSetter assetSetter = new AssetSetter(this);
  Thread gameThread;
  public UI ui = new UI(this);
  public EventHandler eventHandler = new EventHandler(this);

  // ENTITY AND OBJECT
  public Player player = new Player(this, keyHandler);
  public GameEntity[] obj = new GameEntity[10];
  public GameEntity[] npc = new GameEntity[10];
  public GameEntity[] monsters = new GameEntity[20];
  ArrayList<GameEntity> gameObjects = new ArrayList<>();

  // GAME STATE
  public GameStateType gameState;

  public GamePanel() {
    this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    this.setBackground(Color.BLACK);
    this.setDoubleBuffered(true);
    this.addKeyListener(keyHandler);
    this.setFocusable(true);
    gameState = TITLE_STATE;
  }

  public void startGameThread() {
    gameThread = new Thread(this);
    gameThread.start();
  }

  public void update() {
    if (gameState == PLAY_STATE) {
      player.update();
      for (GameEntity gameEntity : npc) {
        if (gameEntity != null) {
          gameEntity.update();
        }
      }
    } else if (gameState == PAUSE_STATE) {
      // nothing
    }

    for (int i = 0; i < monsters.length; i++) {
      if (monsters[i] != null) {
        if (monsters[i].alive && !monsters[i].dying)
          monsters[i].update();
        if (!monsters[i].alive)
          monsters[i] = null;
      }
    }
  }

  public void setupGame() {
    assetSetter.setNPC();
    assetSetter.setObject();
    assetSetter.setMonster();
  }

  public void playMusic(int soundIndex) {
    music.setFile(soundIndex);
//    music.play();
//    music.loop();
  }

  public void stopMusic() {
    music.stop();
  }

  public void playSoundEffect(int soundIndex) {
    soundEffect.setFile(soundIndex);
    soundEffect.play();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D graphic2d = (Graphics2D) g;

    if (gameState == TITLE_STATE) {
      ui.draw(graphic2d);
    } else {
      tileManager.draw(graphic2d);
      gameObjects.add(player);
      // ADD ENTITY TO THE LIST
      for (GameEntity npc : npc) {
        if (npc != null) {
          gameObjects.add(npc);
        }
      }

      for (GameEntity object : obj) {
        if (object != null) {
          gameObjects.add(object);
        }
      }

      for (GameEntity monster : monsters) {
        if (monster != null) {
          gameObjects.add(monster);
        }
      }
      // SORT
      gameObjects.sort(Comparator.comparingInt(gameEntity -> gameEntity.worldY));

      // DRAW ENTITIES
      for (GameEntity gameObject : gameObjects) {
        gameObject.draw(graphic2d);
      }

      // EMPLTY ENTITY LIST
      gameObjects.clear();

      ui.draw(graphic2d);
    }

    //DEBUG
    if (keyHandler.showDebugText) {

      graphic2d.setFont(new Font("Arial", Font.PLAIN, 20));
      graphic2d.setColor(Color.WHITE);
      int x = 10;
      int y = 400;
      int lineHeight = 20;

      graphic2d.drawString("WorldX -> " + player.worldX, x, y);
      y += lineHeight;
      graphic2d.drawString("WorldY ->" + player.worldY, x, y);
      y += lineHeight;
      graphic2d.drawString("Col ->" + (player.worldX + player.solidArea.x) / tileSize, x, y);
      y += lineHeight;
      graphic2d.drawString("Row -> " + (player.worldY + player.solidArea.y) / tileSize, x, y);
    }

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
