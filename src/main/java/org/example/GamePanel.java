package org.example;

import static org.example.Main.window;
import static org.example.enums.GameStateType.PAUSE_STATE;
import static org.example.enums.GameStateType.PLAY_STATE;
import static org.example.enums.GameStateType.TITLE_STATE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.*;

import org.example.entity.GameEntity;
import org.example.entity.Player;
import org.example.entity.interactiveTile.InteractiveTile;
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
  public final int maxScreenColumn = 20;
  public final int maxScreenRow = 12;
  public final int screenWidth = tileSize * maxScreenColumn; // 960 pixels
  public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

  // WORLD SETTINGS
  public final int maxWorldCol = 50;
  public final int maxWorldRow = 50;
  // FOR FULL SCREEN
  int screenWidthFull = screenWidth;
  int screenHeightFull = screenHeight;
  BufferedImage tempScreen;
  Graphics2D tempGraphic2d;
  public boolean fullScreenOn = false;

  static final int FPS = 60;
  // SYSTEM
  public TileManager tileManager = new TileManager(this);
  public KeyHandler keyHandler = new KeyHandler(this);
  public Sound music = new Sound();
  public Sound soundEffect = new Sound();
  public CollisionDetector collisionDetector = new CollisionDetector(this);
  public AssetSetter assetSetter = new AssetSetter(this);
  Thread gameThread;
  Config config = new Config(this);
  public UI ui = new UI(this);
  public EventHandler eventHandler = new EventHandler(this);

  // ENTITY AND OBJECT
  public Player player = new Player(this, keyHandler);
  public GameEntity[] obj = new GameEntity[20];
  public GameEntity[] npc = new GameEntity[10];
  public GameEntity[] monsters = new GameEntity[20];
  public InteractiveTile[] interactiveTiles = new InteractiveTile[50];
  public ArrayList<GameEntity> particleList = new ArrayList<>();
  ArrayList<GameEntity> gameObjects = new ArrayList<>();
  public ArrayList<GameEntity> projectiles = new ArrayList<>();

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

    for (int monsterIndex = 0; monsterIndex < monsters.length; monsterIndex++) {
      if (monsters[monsterIndex] != null) {
        if (monsters[monsterIndex].alive && !monsters[monsterIndex].dying)
          monsters[monsterIndex].update();
        if (!monsters[monsterIndex].alive) {
          monsters[monsterIndex].checkDrop();
          monsters[monsterIndex] = null;
        }
      }
    }

    for (int projectileIndex = 0; projectileIndex < projectiles.size(); projectileIndex++) {
      if (projectiles.get(projectileIndex) != null) {
        if (projectiles.get(projectileIndex).alive)
          projectiles.get(projectileIndex).update();
        else
          projectiles.remove(projectileIndex);
      }
    }

    for (int objIndex = 0; objIndex < interactiveTiles.length; objIndex++) {
      if (interactiveTiles[objIndex] != null) {
        interactiveTiles[objIndex].update();
      }
    }

    for (int particleIndex = 0; particleIndex < particleList.size(); particleIndex++) {
      if (particleList.get(particleIndex) != null) {
        if (particleList.get(particleIndex).alive)
          particleList.get(particleIndex).update();
        else
          particleList.remove(particleIndex);
      }
    }
  }

  public void setupGame() {
    assetSetter.setNPC();
    assetSetter.setObject();
    assetSetter.setInteractiveTiles();
    assetSetter.setMonster();

    tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
    tempGraphic2d = (Graphics2D) tempScreen.getGraphics();

    if (fullScreenOn)
      setFullScreen();
  }

  public void playMusic(int soundIndex) {
    music.setFile(soundIndex);
    music.play();
    music.loop();
  }


  public void stopMusic() {
    music.stop();
  }

  public void playSoundEffect(int soundIndex) {
    soundEffect.setFile(soundIndex);
    soundEffect.play();
  }

  public void setFullScreen() {
    // GET LOCAL SCREEN DEVICE
    var graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    var graphicsDevice = graphicsEnvironment.getDefaultScreenDevice();
    graphicsDevice.setFullScreenWindow(window);

    // GET FULL SCREEN WIDTH AND HEIGHT
    screenWidthFull = window.getWidth();
    screenHeightFull = window.getHeight();
  }

  public void drawToScreen() {
    var screenGraphics = getGraphics();
    screenGraphics.drawImage(tempScreen, 0, 0, screenWidthFull, screenHeightFull, null);
    screenGraphics.dispose();
  }

  public void drawToTempScreen() {
    if (gameState == TITLE_STATE) {
      ui.draw(tempGraphic2d);
    } else {
      tileManager.draw(tempGraphic2d);
      // INTERACTIVE TILE
      for (int objIndex = 0; objIndex < interactiveTiles.length; objIndex++) {
        if (interactiveTiles[objIndex] != null) {
          interactiveTiles[objIndex].draw(tempGraphic2d);
        }
      }

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

      for (GameEntity projectile : projectiles) {
        if (projectile != null) {
          gameObjects.add(projectile);
        }
      }

      for (GameEntity particle : particleList) {
        if (particle != null) {
          gameObjects.add(particle);
        }
      }

      // SORT
      gameObjects.sort(Comparator.comparingInt(gameEntity -> gameEntity.worldY));

      // DRAW ENTITIES
      for (GameEntity gameObject : gameObjects) {
        gameObject.draw(tempGraphic2d);
      }

      // EMPLTY ENTITY LIST
      gameObjects.clear();

      ui.draw(tempGraphic2d);
    }

    //DEBUG
    if (keyHandler.showDebugText) {

      tempGraphic2d.setFont(new Font("Arial", Font.PLAIN, 20));
      tempGraphic2d.setColor(Color.WHITE);
      int x = 10;
      int y = 400;
      int lineHeight = 20;

      tempGraphic2d.drawString("WorldX -> " + player.worldX, x, y);
      y += lineHeight;
      tempGraphic2d.drawString("WorldY ->" + player.worldY, x, y);
      y += lineHeight;
      tempGraphic2d.drawString("Col ->" + (player.worldX + player.solidArea.x) / tileSize, x, y);
      y += lineHeight;
      tempGraphic2d.drawString("Row -> " + (player.worldY + player.solidArea.y) / tileSize, x, y);
    }
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
        drawToTempScreen(); // draw everything to the buffered image
        drawToScreen(); // draw the buffered image to the screen
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
