package org.example;

import static org.example.enums.GameStateType.PAUSE_STATE;
import static org.example.enums.GameStateType.PLAY_STATE;
import static org.example.enums.GameStateType.TITLE_STATE;

import java.awt.*;

import javax.swing.*;

import org.example.entity.Entity;
import org.example.entity.Player;
import org.example.enums.GameStateType;
import org.example.object.SuperObject;
import org.example.tile.TileManager;
import org.example.utility.AssetSetter;
import org.example.utility.CollisionDetector;
import org.example.utility.EventHandler;
import org.example.utility.KeyHandler;

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
  public SuperObject[] obj = new SuperObject[10];
  public Entity[] npc = new Entity[10];

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
      for (Entity entity : npc) {
        if (entity != null) {
          entity.update();
        }
      }
    } else if (gameState == PAUSE_STATE) {
      // nothing
    }

  }

  public void setupGame() {
    assetSetter.setObject();
    assetSetter.setNPC();
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

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D graphic2d = (Graphics2D) g;
    if (gameState == TITLE_STATE) {
      ui.draw(graphic2d);
    } else {
      tileManager.draw(graphic2d);
      for (SuperObject superObject : obj) {
        if (superObject != null) {
          superObject.draw(graphic2d, this);
        }
      }
      for (int i = 0; i < npc.length; i++) {
        if (npc[i] != null) {
          npc[i].draw(graphic2d);
        }
      }
      player.draw(graphic2d);
      ui.draw(graphic2d);
      graphic2d.dispose();
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
