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
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.GameEntityFactory;
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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GamePanel extends JPanel implements Runnable {

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
  Map<String, GameEntity[]> mapsObjects = new HashMap<>();
  Map<String, GameEntity[]> mapsNpc = new HashMap<>();
  Map<String, GameEntity[]> mapsMonsters = new HashMap<>();
  Map<String, InteractiveTile[]> mapsInteractiveTiles = new HashMap<>();
  Map<String, GameEntity[]> projectiles = new HashMap<>();
  ArrayList<GameEntity> particleList = new ArrayList<>();
  ArrayList<GameEntity> gameObjects = new ArrayList<>();

  // AREA
  AreaType currentArea;
  AreaType nextArea;

  // GAME STATE
  GameStateType gameState;
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
      environmentManager.getLighting().resetDay();
    }
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
      List<GameEntity> allMapObjects = new ArrayList<>(List.of(mapsObjects.get(mapName)));
      if (allMapObjects.isEmpty()) continue;

      allMapObjects.removeIf(object -> Objects.nonNull(object) && object.temporaryObject);
    }
  }

  private void drawToTempScreen() {
    if (gameState == TITLE_STATE) {
      ui.draw(tempGraphic2d);
    } else if (gameState == MAP_STATE) {
      gameMap.drawFullMapScreen(tempGraphic2d);
    } else {
      tileManager.draw(tempGraphic2d);

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

      gameObjects.sort(Comparator.comparingInt(gameEntity -> gameEntity.worldY));

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

      tempGraphic2d.drawString("WorldX -> " + player.worldX, x, y);
      y += lineHeight;
      tempGraphic2d.drawString("WorldY ->" + player.worldY, x, y);
      y += lineHeight;
      tempGraphic2d.drawString("Col ->" + (player.worldX + player.solidArea.x) / TILE_SIZE, x, y);
      y += lineHeight;
      tempGraphic2d.drawString("Row -> " + (player.worldY + player.solidArea.y) / TILE_SIZE, x, y);
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
