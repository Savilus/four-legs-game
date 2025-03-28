package org.example;

import static org.example.enums.GameStateType.MAP_STATE;
import static org.example.enums.GameStateType.PAUSE_STATE;
import static org.example.enums.GameStateType.PLAY_STATE;
import static org.example.enums.GameStateType.TITLE_STATE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javax.swing.*;

import org.example.ai.PathFinder;
import org.example.data.SaveLoad;
import org.example.entity.EntityGenerator;
import org.example.entity.GameEntity;
import org.example.entity.Player;
import org.example.entity.interactiveTile.InteractiveTile;
import org.example.enums.GameStateType;
import org.example.environment.EnvironmentManager;
import org.example.tile.GameMap;
import org.example.tile.TileManager;
import org.example.utils.AssetSetter;
import org.example.utils.CollisionDetector;
import org.example.utils.KeyHandler;
import org.example.utils.event.EventHandler;

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

  // SYSTEM
  static final int FPS = 60;
  public TileManager tileManager = TileManager.getInstance(this);
  public KeyHandler keyHandler = new KeyHandler(this);
  public Sound music = new Sound();
  public Sound soundEffect = new Sound();
  public PathFinder pathFinder = new PathFinder(this);
  public CollisionDetector collisionDetector = new CollisionDetector(this);
  public AssetSetter assetSetter = new AssetSetter(this);
  Thread gameThread;
  public SaveLoad saveLoad = new SaveLoad(this);
  Config config = new Config(this);
  public UI ui = new UI(this);
  public EventHandler eventHandler = new EventHandler(this);
  EnvironmentManager environmentManager = new EnvironmentManager(this);
  public GameMap gameMap = new GameMap(this);
  public EntityGenerator entityGenerator = new EntityGenerator(this);

  // ENTITY AND OBJECT
  public Player player = new Player(this, keyHandler);
  public Map<String, GameEntity[]> mapsObjects = new HashMap<>();
  public Map<String, GameEntity[]> mapsNpc = new HashMap<>();
  public Map<String, GameEntity[]> mapsMonsters = new HashMap<>();
  public Map<String, InteractiveTile[]> mapsInteractiveTiles = new HashMap<>();
  public Map<String, GameEntity[]> projectiles = new HashMap<>();
  public ArrayList<GameEntity> particleList = new ArrayList<>();
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
      if (Objects.nonNull(mapsNpc.get(tileManager.currentMap))) {
        for (GameEntity gameEntity : mapsNpc.get(tileManager.currentMap)) {
          if (gameEntity != null) {
            gameEntity.update();
          }
        }
      }

      if (Objects.nonNull(mapsMonsters.get(tileManager.currentMap))) {
        for (int monsterIndex = 0; monsterIndex < mapsMonsters.get(tileManager.currentMap).length; monsterIndex++) {
          if (mapsMonsters.get(tileManager.currentMap)[monsterIndex] != null) {
            if (mapsMonsters.get(tileManager.currentMap)[monsterIndex].alive && !mapsMonsters.get(tileManager.currentMap)[monsterIndex].dying)
              mapsMonsters.get(tileManager.currentMap)[monsterIndex].update();
            if (!mapsMonsters.get(tileManager.currentMap)[monsterIndex].alive) {
              mapsMonsters.get(tileManager.currentMap)[monsterIndex].checkDrop();
              mapsMonsters.get(tileManager.currentMap)[monsterIndex] = null;
            }
          }
        }
      }

      for (int i = 0; i < projectiles.get(tileManager.currentMap).length; i++) {
        GameEntity projectile = projectiles.get(tileManager.currentMap)[i];

        if (Objects.nonNull(projectile) && projectile.alive) {
          projectile.update();
        } else {
          projectiles.get(tileManager.currentMap)[i] = null;
        }
      }

      if (Objects.nonNull(mapsInteractiveTiles.get(tileManager.currentMap))) {
        for (int objIndex = 0; objIndex < mapsInteractiveTiles.get(tileManager.currentMap).length; objIndex++) {
          if (mapsInteractiveTiles.get(tileManager.currentMap)[objIndex] != null) {
            mapsInteractiveTiles.get(tileManager.currentMap)[objIndex].update();
          }
        }
      }

      Iterator<GameEntity> particleIterator = particleList.iterator();
      while (particleIterator.hasNext()) {
        GameEntity particle = particleIterator.next();
        if (Objects.nonNull(particle) && particle.alive) {
          particle.update();
        } else {
          particleIterator.remove();
        }
      }

      environmentManager.update();
    } else if (gameState == PAUSE_STATE) {
      // nothing
    }
  }

  public void setupGame() {
    mapsObjects.put(tileManager.currentMap, new GameEntity[20]);
    assetSetter.setNPC();
    assetSetter.setObject();
    assetSetter.setInteractiveTiles();
    assetSetter.setMonster();
    assetSetter.setProjectile();
    environmentManager.setup();

    tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
    tempGraphic2d = (Graphics2D) tempScreen.getGraphics();

    if (fullScreenOn)
      setFullScreen();
  }

  public void playMusic(String sound) {
    music.setFile(sound);
    music.play();
    music.loop();
  }

  public void resetGame(boolean restart) {
    player.setDefaultPositions();
    player.restorePlayerStatus();
    player.resetCounter();
    assetSetter.setMonster();
    assetSetter.setNPC();

    if (restart) {
      player.setDefaultValues();
      player.setItems();
      assetSetter.setObject();
      assetSetter.setInteractiveTiles();
      environmentManager.lighting.resetDay();
    }
  }

  public void stopMusic() {
    music.stop();
  }

  public void playSoundEffect(String sound) {
    soundEffect.setFile(sound);
    soundEffect.play();
  }

  public void setFullScreen() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();
    Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
    screenWidthFull = (int) width;
    screenHeightFull = (int) height;
  }

  public void drawToScreen() {
    var screenGraphics = getGraphics();
    screenGraphics.drawImage(tempScreen, 0, 0, screenWidthFull, screenHeightFull, null);
    screenGraphics.dispose();
  }

  public void drawToTempScreen() {
    if (gameState == TITLE_STATE) {
      ui.draw(tempGraphic2d);
    } else if (gameState == MAP_STATE) {
      gameMap.drawFullMapScreen(tempGraphic2d);
    } else {
      tileManager.draw(tempGraphic2d);
      // INTERACTIVE TILE
      if (Objects.nonNull(mapsInteractiveTiles.get(tileManager.currentMap))) {
        for (int objIndex = 0; objIndex < mapsInteractiveTiles.get(tileManager.currentMap).length; objIndex++) {
          if (mapsInteractiveTiles.get(tileManager.currentMap)[objIndex] != null) {
            mapsInteractiveTiles.get(tileManager.currentMap)[objIndex].draw(tempGraphic2d);
          }
        }
      }

      gameObjects.add(player);
      // ADD ENTITY TO THE LIST
      if (Objects.nonNull(mapsNpc.get(tileManager.currentMap))) {
        for (GameEntity npc : mapsNpc.get(tileManager.currentMap)) {
          if (npc != null) {
            gameObjects.add(npc);
          }
        }
      }
      if (Objects.nonNull(mapsObjects.get(tileManager.currentMap))) {
        for (GameEntity object : mapsObjects.get(tileManager.currentMap)) {
          if (object != null) {
            gameObjects.add(object);
          }
        }
      }
      if (Objects.nonNull(mapsMonsters.get(tileManager.currentMap))) {
        for (GameEntity monster : mapsMonsters.get(tileManager.currentMap)) {
          if (monster != null) {
            gameObjects.add(monster);
          }
        }
      }

      for (GameEntity projectile : projectiles.get(tileManager.currentMap)) {
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
      environmentManager.draw(tempGraphic2d);
      // Mini map
      gameMap.drawMiniMap(tempGraphic2d);

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
