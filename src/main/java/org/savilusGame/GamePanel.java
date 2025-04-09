package org.savilusGame;

import static org.savilusGame.config.GameEntityNameFactory.DUNGEON_SONG;
import static org.savilusGame.config.GameEntityNameFactory.MAIN_MAP;
import static org.savilusGame.config.GameEntityNameFactory.MERCHANT_SONG;
import static org.savilusGame.config.GameEntityNameFactory.OUTSIDE_MUSIC;
import static org.savilusGame.enums.GameStateType.MAP_STATE;
import static org.savilusGame.enums.GameStateType.PAUSE_STATE;
import static org.savilusGame.enums.GameStateType.PLAY_STATE;
import static org.savilusGame.enums.GameStateType.TITLE_STATE;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.*;

import org.savilusGame.ai.PathFinder;
import org.savilusGame.data.SaveLoad;
import org.savilusGame.entity.EntityGenerator;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.Player;
import org.savilusGame.entity.interactiveTile.InteractiveTile;
import org.savilusGame.enums.AreaType;
import org.savilusGame.enums.GameStateType;
import org.savilusGame.environment.EnvironmentManager;
import org.savilusGame.tile.GameMap;
import org.savilusGame.tile.TileManager;
import org.savilusGame.utils.AssetSetter;
import org.savilusGame.utils.CollisionDetector;
import org.savilusGame.utils.CutsceneManager;
import org.savilusGame.utils.KeyHandler;
import org.savilusGame.utils.event.EventHandler;

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
  public CutsceneManager cutsceneManager = new CutsceneManager(this);

  // ENTITY AND OBJECT
  public Player player = new Player(this, keyHandler);
  public Map<String, GameEntity[]> mapsObjects = new HashMap<>();
  public Map<String, GameEntity[]> mapsNpc = new HashMap<>();
  public Map<String, GameEntity[]> mapsMonsters = new HashMap<>();
  public Map<String, InteractiveTile[]> mapsInteractiveTiles = new HashMap<>();
  public Map<String, GameEntity[]> projectiles = new HashMap<>();
  public ArrayList<GameEntity> particleList = new ArrayList<>();
  ArrayList<GameEntity> gameObjects = new ArrayList<>();

  // AREA
  public AreaType currentArea;
  public AreaType nextArea;

  // GAME STATE
  public GameStateType gameState;
  public boolean bossBattleOn = false;


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
      if (Objects.nonNull(mapsNpc.get(CURRENT_MAP))) {
        for (GameEntity gameEntity : mapsNpc.get(CURRENT_MAP)) {
          if (Objects.nonNull(gameEntity)) {
            gameEntity.update();
          }
        }
      }

      if (Objects.nonNull(mapsMonsters.get(CURRENT_MAP))) {
        for (int monsterIndex = 0; monsterIndex < mapsMonsters.get(CURRENT_MAP).length; monsterIndex++) {
          var monster = mapsMonsters.get(CURRENT_MAP)[monsterIndex];
          if (Objects.nonNull(monster)) {
            if (monster.alive && !monster.dying)
              monster.update();
            if (!monster.alive) {
              monster.checkDrop();
              mapsMonsters.get(CURRENT_MAP)[monsterIndex] = null;
            }
          }
        }
      }

      for (int i = 0; i < projectiles.get(CURRENT_MAP).length; i++) {
        GameEntity projectile = projectiles.get(CURRENT_MAP)[i];

        if (Objects.nonNull(projectile) && projectile.alive) {
          projectile.update();
        } else {
          projectiles.get(CURRENT_MAP)[i] = null;
        }
      }

      if (Objects.nonNull(mapsInteractiveTiles.get(CURRENT_MAP))) {
        for (int objIndex = 0; objIndex < mapsInteractiveTiles.get(CURRENT_MAP).length; objIndex++) {
          if (Objects.nonNull(mapsInteractiveTiles.get(CURRENT_MAP)[objIndex])) {
            mapsInteractiveTiles.get(CURRENT_MAP)[objIndex].update();
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
    mapsObjects.put(CURRENT_MAP, new GameEntity[20]);
    assetSetter.setNPC();
    assetSetter.setObject();
    assetSetter.setInteractiveTiles();
    assetSetter.setMonster();
    assetSetter.setProjectile();
    assetSetter.setInteractiveObjects();
    currentArea = AreaType.OUTSIDE;
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
    removeTemporaryGameEntity();
    bossBattleOn = false;
    CURRENT_MAP = MAIN_MAP;
    currentArea = AreaType.OUTSIDE;
    assetSetter.setMonster();
    assetSetter.setNPC();

    if (restart) {
      player.setDefaultValues();
      player.setItems();
      assetSetter.setObject();
      assetSetter.setInteractiveTiles();
      assetSetter.setInteractiveObjects();
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

  public void changeArea() {
    if (nextArea != currentArea) {
      stopMusic();

      if (nextArea == AreaType.OUTSIDE) {
        playMusic(OUTSIDE_MUSIC);
      } else if (nextArea == AreaType.INDOOR) {
        playMusic(MERCHANT_SONG);
      } else if (nextArea == AreaType.DUNGEON) {
        playMusic(DUNGEON_SONG);
      }
    }
    currentArea = nextArea;
    assetSetter.setMonster();
  }

  public void removeTemporaryGameEntity() {
    for (String mapName : tileManager.gameMaps.keySet()) {
      List<GameEntity> allMapObjects = new ArrayList<>(List.of(mapsObjects.get(mapName)));
      if (allMapObjects.isEmpty()) continue;

      allMapObjects.removeIf(object -> Objects.nonNull(object) && object.temporaryObject);
    }
  }


  public void drawToTempScreen() {
    if (gameState == TITLE_STATE) {
      ui.draw(tempGraphic2d);
    } else if (gameState == MAP_STATE) {
      gameMap.drawFullMapScreen(tempGraphic2d);
    } else {
      tileManager.draw(tempGraphic2d);
      // INTERACTIVE TILE
      if (Objects.nonNull(mapsInteractiveTiles.get(CURRENT_MAP))) {
        for (int objIndex = 0; objIndex < mapsInteractiveTiles.get(CURRENT_MAP).length; objIndex++) {
          if (Objects.nonNull(mapsInteractiveTiles.get(CURRENT_MAP)[objIndex])) {
            mapsInteractiveTiles.get(CURRENT_MAP)[objIndex].draw(tempGraphic2d);
          }
        }
      }

      gameObjects.add(player);
      // ADD ENTITY TO THE LIST
      if (Objects.nonNull(mapsNpc.get(CURRENT_MAP))) {
        for (GameEntity npc : mapsNpc.get(CURRENT_MAP)) {
          if (Objects.nonNull(npc)) {
            gameObjects.add(npc);
          }
        }
      }
      if (Objects.nonNull(mapsObjects.get(CURRENT_MAP))) {
        for (GameEntity object : mapsObjects.get(CURRENT_MAP)) {
          if (Objects.nonNull(object)) {
            gameObjects.add(object);
          }
        }
      }
      if (Objects.nonNull(mapsMonsters.get(CURRENT_MAP))) {
        for (GameEntity monster : mapsMonsters.get(CURRENT_MAP)) {
          if (Objects.nonNull(monster)) {
            gameObjects.add(monster);
          }
        }
      }

      for (GameEntity projectile : projectiles.get(CURRENT_MAP)) {
        if (Objects.nonNull(projectile)) {
          gameObjects.add(projectile);
        }
      }

      for (GameEntity particle : particleList) {
        if (Objects.nonNull(particle)) {
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

      cutsceneManager.draw(tempGraphic2d);

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
      y += lineHeight;
      tempGraphic2d.drawString("God Mode: -> " + keyHandler.godModeOn, x, y);
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

    while (Objects.nonNull(gameThread)) {

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
