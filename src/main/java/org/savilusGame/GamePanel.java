package org.savilusGame;

import static org.savilusGame.config.GameEntityNameFactory.DUNGEON_SONG;
import static org.savilusGame.config.GameEntityNameFactory.MAIN_MAP;
import static org.savilusGame.config.GameEntityNameFactory.MERCHANT_SONG;
import static org.savilusGame.config.GameEntityNameFactory.OUTSIDE_MUSIC;
import static org.savilusGame.enums.GameState.MAP_STATE;
import static org.savilusGame.enums.GameState.PAUSE_STATE;
import static org.savilusGame.enums.GameState.PLAY_STATE;
import static org.savilusGame.enums.GameState.TITLE_STATE;
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
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.GameEntityFactory;
import org.savilusGame.entity.Player;
import org.savilusGame.entity.interactiveTile.InteractiveTile;
import org.savilusGame.enums.Area;
import org.savilusGame.enums.GameState;
import org.savilusGame.environment.EnvironmentManager;
import org.savilusGame.tile.GameMap;
import org.savilusGame.tile.TileManager;
import org.savilusGame.utils.AssetSetter;
import org.savilusGame.utils.CollisionDetector;
import org.savilusGame.utils.CutsceneManager;
import org.savilusGame.utils.KeyHandler;
import org.savilusGame.utils.event.EventHandler;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GamePanel extends JPanel implements Runnable {

  final static int ALLOWED_ITEMS = 20;

  // SCREEN SETTINGS
  final static int ORIGINAL_TITLE_SIZE = 16; // 16x16 tile
  final static int SCALE = 3;
  public final static int TILE_SIZE = ORIGINAL_TITLE_SIZE * SCALE; // 48 x 48 tile
  final int maxScreenColumn = 20;
  final int maxScreenRow = 12;
  final int screenWidth = TILE_SIZE * maxScreenColumn; // 960 pixels
  final int screenHeight = TILE_SIZE * maxScreenRow; // 576 pixels

  // WORLD SETTINGS
  final int maxWorldCol = 50;
  final int maxWorldRow = 50;
  // FOR FULL SCREEN
  int screenWidthFull = screenWidth;
  int screenHeightFull = screenHeight;
  BufferedImage tempScreen;
  Graphics2D tempGraphic2d;
  boolean fullScreenOn = false;

  // SYSTEM
  static final int FPS = 60;
  final TileManager tileManager = TileManager.getInstance(this);
  final KeyHandler keyHandler = new KeyHandler(this);
  Sound music = new Sound();
  Sound soundEffect = new Sound();
  PathFinder pathFinder = new PathFinder(this);
  CollisionDetector collisionDetector = new CollisionDetector(this);
  AssetSetter assetSetter = new AssetSetter(this);
  Thread gameThread;
  SaveLoad saveLoad = new SaveLoad(this);
  Config config = new Config(this);
  UI ui = new UI(this);
  EventHandler eventHandler = new EventHandler(this);
  final EnvironmentManager environmentManager = new EnvironmentManager(this);
  final GameMap gameMap = new GameMap(this);
  final GameEntityFactory gameEntityFactory = new GameEntityFactory(this);
  final CutsceneManager cutsceneManager = new CutsceneManager(this);

  // ENTITY AND OBJECT
  Player player = new Player(this, keyHandler);
  Map<String, List<GameEntity>> mapsObjects = new HashMap<>();
  Map<String, List<GameEntity>> mapsNpc = new HashMap<>();
  Map<String, List<GameEntity>> mapsMonsters = new HashMap<>();
  Map<String, List<InteractiveTile>> mapsInteractiveTiles = new HashMap<>();
  Map<String, List<GameEntity>> projectiles = new HashMap<>();
  ArrayList<GameEntity> particleList = new ArrayList<>();
  ArrayList<GameEntity> gameObjects = new ArrayList<>();

  // AREA
  Area currentArea;
  Area nextArea;

  // GAME STATE
  GameState gameState;
  boolean bossBattleOn = false;

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

  public void setupGame() {
    mapsObjects.put(CURRENT_MAP, new ArrayList<>());
    assetSetter.setNPC();
    assetSetter.setObject();
    assetSetter.setInteractiveTiles();
    assetSetter.setMonster();
    assetSetter.setProjectile();
    assetSetter.setInteractiveObjects();
    currentArea = Area.OUTSIDE;
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
    currentArea = Area.OUTSIDE;
    assetSetter.setMonster();
    assetSetter.setNPC();
    playMusic(OUTSIDE_MUSIC);

    if (restart) {
      music.stop();
      player.setDefaultValues();
      player.setItems();
      assetSetter.setObject();
      assetSetter.setInteractiveTiles();
      assetSetter.setInteractiveObjects();
      environmentManager.getLighting().resetDay();
    }
  }

  public void changeArea() {
    if (nextArea != currentArea) {
      stopMusic();

      switch (nextArea) {
        case OUTSIDE -> playMusic(OUTSIDE_MUSIC);
        case INDOOR -> playMusic(MERCHANT_SONG);
        case DUNGEON -> playMusic(DUNGEON_SONG);
        default -> throw new IllegalStateException("Unexpected value: " + nextArea);
      }
    }

    currentArea = nextArea;
    assetSetter.setMonster();
  }

  public void stopMusic() {
    music.stop();
  }

  public void playSoundEffect(String sound) {
    soundEffect.setFile(sound);
    soundEffect.play();
  }

  private void setFullScreen() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double width = screenSize.getWidth();
    double height = screenSize.getHeight();
    Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
    screenWidthFull = (int) width;
    screenHeightFull = (int) height;
  }

  private void drawToScreen() {
    var screenGraphics = getGraphics();
    screenGraphics.drawImage(tempScreen, 0, 0, screenWidthFull, screenHeightFull, null);
    screenGraphics.dispose();
  }

  private void removeTemporaryGameEntity() {
    for (String mapName : tileManager.getGameMaps().keySet()) {
      List<GameEntity> allMapObjects = mapsObjects.get(mapName);
      if (allMapObjects == null || allMapObjects.isEmpty()) continue;

      allMapObjects.removeIf(object -> object != null && object.isTemporaryObject());
    }
  }


  private void drawToTempScreen() {
    if (gameState == TITLE_STATE) {
      ui.draw(tempGraphic2d);
    } else if (gameState == MAP_STATE) {
      gameMap.drawFullMapScreen(tempGraphic2d);
    } else {
      tileManager.draw(tempGraphic2d);
      var interactiveTiles = mapsInteractiveTiles.get(CURRENT_MAP);
      if (Objects.nonNull(interactiveTiles)) {
        for (InteractiveTile interactiveTile : interactiveTiles) {
          if (Objects.nonNull(interactiveTile)) {
            interactiveTile.draw(tempGraphic2d);
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

      gameObjects.sort(Comparator.comparingInt(GameEntity::getWorldY));

      for (GameEntity gameObject : gameObjects) {
        gameObject.draw(tempGraphic2d);
      }

      gameObjects.clear();
      environmentManager.draw(tempGraphic2d);
      gameMap.drawMiniMap(tempGraphic2d);
      cutsceneManager.draw(tempGraphic2d);
      ui.draw(tempGraphic2d);
    }

    //DEBUG
    if (keyHandler.isShowDebugText()) {

      tempGraphic2d.setFont(new Font("Arial", Font.PLAIN, 20));
      tempGraphic2d.setColor(Color.WHITE);
      int x = 10;
      int y = 400;
      int lineHeight = 20;

      tempGraphic2d.drawString("WorldX -> " + player.getWorldX(), x, y);
      y += lineHeight;
      tempGraphic2d.drawString("WorldY ->" + player.getWorldY(), x, y);
      y += lineHeight;
      tempGraphic2d.drawString("Col ->" + (player.getWorldX() + player.getSolidArea().x) / TILE_SIZE, x, y);
      y += lineHeight;
      tempGraphic2d.drawString("Row -> " + (player.getWorldY() + player.getSolidArea().y) / TILE_SIZE, x, y);
      y += lineHeight;
      tempGraphic2d.drawString("God Mode: -> " + keyHandler.isGodModeOn(), x, y);
    }
  }

  private void update() {
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
        var monsters = mapsMonsters.get(CURRENT_MAP);
        for (int monsterIndex = 0; monsterIndex < monsters.size(); monsterIndex++) {
          var monster = monsters.get(monsterIndex);
          if (monster.isAlive() && !monster.isDying()) {
            monster.update();
          }
          if (!monster.isAlive()) {
            monster.checkDrop();
            monsters.remove(monsterIndex);
            monsterIndex--;
          }
        }
      }

      var projectilesList = projectiles.get(CURRENT_MAP);
      for (int currentProjectile = 0; currentProjectile < projectilesList.size(); currentProjectile++) {
        var projectile = projectilesList.get(currentProjectile);
        if (Objects.nonNull(projectile) && projectile.isAlive()) {
          projectile.update();
        } else {
          projectilesList.remove(currentProjectile);
          currentProjectile--;
        }
      }

      var interactiveTiles = mapsInteractiveTiles.get(CURRENT_MAP);
      if (Objects.nonNull(interactiveTiles)) {
        for (InteractiveTile interactiveTile : interactiveTiles) {
          if (Objects.nonNull(interactiveTile)) {
            interactiveTile.update();
          }
        }
      }

      Iterator<GameEntity> particleIterator = particleList.iterator();
      while (particleIterator.hasNext()) {
        GameEntity particle = particleIterator.next();
        if (Objects.nonNull(particle) && particle.isAlive()) {
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
