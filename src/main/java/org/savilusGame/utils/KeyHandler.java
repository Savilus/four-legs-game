package org.savilusGame.utils;

import static org.savilusGame.config.GameEntityNameFactory.CURSOR;
import static org.savilusGame.config.GameEntityNameFactory.OUTSIDE_MUSIC;
import static org.savilusGame.enums.GameStateType.CHARACTER_STATE;
import static org.savilusGame.enums.GameStateType.MAP_STATE;
import static org.savilusGame.enums.GameStateType.OPTIONS_STATE;
import static org.savilusGame.enums.GameStateType.PAUSE_STATE;
import static org.savilusGame.enums.GameStateType.PLAY_STATE;
import static org.savilusGame.enums.GameStateType.TITLE_STATE;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.savilusGame.GamePanel;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyHandler implements KeyListener {

  final GamePanel gamePanel;
  boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, spacePressed, shotKeyPressed;
  //DEBUG
  boolean showDebugText;
  boolean godModeOn = false;

  public KeyHandler(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  private void titleState(int code) {
    switch (code) {
      case KeyEvent.VK_W, KeyEvent.VK_UP -> {
        upPressed = true;
        gamePanel.getUi().commandNum--;

        if (gamePanel.getUi().commandNum < 0) {
          gamePanel.getUi().commandNum = 2;
        }
      }
      case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
        downPressed = true;
        gamePanel.getUi().commandNum++;

        if (gamePanel.getUi().commandNum > 2) {
          gamePanel.getUi().commandNum = 0;
        }
      }
      case KeyEvent.VK_ENTER -> {
        if (gamePanel.getUi().commandNum == 0) {
          gamePanel.setGameState(PLAY_STATE);
          gamePanel.playMusic(OUTSIDE_MUSIC);
        }
        if (gamePanel.getUi().commandNum == 1) {
          gamePanel.getSaveLoad().load();
          gamePanel.setGameState(PLAY_STATE);
          gamePanel.playMusic(OUTSIDE_MUSIC);
        }
        if (gamePanel.getUi().commandNum == 2) {
          System.exit(0);
        }
      }
    }
  }

  private void pauseState(int code) {
    if (code == KeyEvent.VK_P) {
      gamePanel.setGameState(PLAY_STATE);
    }
  }

  private void playState(int code) {
    switch (code) {
      case KeyEvent.VK_W -> upPressed = true;
      case KeyEvent.VK_S -> downPressed = true;
      case KeyEvent.VK_A -> leftPressed = true;
      case KeyEvent.VK_D -> rightPressed = true;
      case KeyEvent.VK_SPACE -> spacePressed = true;
      case KeyEvent.VK_P -> gamePanel.setGameState(PAUSE_STATE);
      case KeyEvent.VK_ENTER -> enterPressed = true;
      case KeyEvent.VK_C -> gamePanel.setGameState(CHARACTER_STATE);
      case KeyEvent.VK_F -> shotKeyPressed = true;
      case KeyEvent.VK_ESCAPE -> gamePanel.setGameState(OPTIONS_STATE);
      case KeyEvent.VK_M -> gamePanel.setGameState(MAP_STATE);
      case KeyEvent.VK_X -> gamePanel.getGameMap().setMiniMapOn(!gamePanel.getGameMap().isMiniMapOn());
      case KeyEvent.VK_G -> godModeOn = !godModeOn;

      //DEBUG
      case KeyEvent.VK_T -> showDebugText = !showDebugText;
      //REFRESH MAP
      case KeyEvent.VK_R -> gamePanel.getTileManager().loadMap(CURRENT_MAP);
    }
  }

  private void dialogState(int code) {
    if (code == KeyEvent.VK_ENTER) {
      enterPressed = true;
    }
  }

  private void characterState(int code) {
    switch (code) {
      case KeyEvent.VK_C -> gamePanel.setGameState(PLAY_STATE);
      case KeyEvent.VK_ENTER -> gamePanel.getPlayer().selectItem();
    }
    playerInventory(code);
  }

  private void playerInventory(int code) {
    switch (code) {
      case KeyEvent.VK_W, KeyEvent.VK_UP -> {
        if (gamePanel.getUi().playerSlotRow != 0) {
          gamePanel.getUi().playerSlotRow--;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
        if (gamePanel.getUi().playerSlotCol != 0) {
          gamePanel.getUi().playerSlotCol--;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
        if (gamePanel.getUi().playerSlotRow != 3) {
          gamePanel.getUi().playerSlotRow++;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
        if (gamePanel.getUi().playerSlotCol != 4) {
          gamePanel.getUi().playerSlotCol++;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
    }
  }

  private void npcInventory(int code) {
    switch (code) {
      case KeyEvent.VK_W, KeyEvent.VK_UP -> {
        if (gamePanel.getUi().npcSlotRow != 0) {
          gamePanel.getUi().npcSlotRow--;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
        if (gamePanel.getUi().npcSlotCol != 0) {
          gamePanel.getUi().npcSlotCol--;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
        if (gamePanel.getUi().npcSlotRow != 3) {
          gamePanel.getUi().npcSlotRow++;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
        if (gamePanel.getUi().npcSlotCol != 4) {
          gamePanel.getUi().npcSlotCol++;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
    }
  }

  private void tradeState(int code) {
    if (code == KeyEvent.VK_ENTER) {
      enterPressed = true;
    }

    if (gamePanel.getUi().subState == 0) {
      switch (code) {
        case KeyEvent.VK_W -> {
          gamePanel.getUi().commandNum--;
          if (gamePanel.getUi().commandNum < 0) {
            gamePanel.getUi().commandNum = 2;
          }
          gamePanel.playSoundEffect(CURSOR);
        }
        case KeyEvent.VK_S -> {
          gamePanel.getUi().commandNum++;
          if (gamePanel.getUi().commandNum > 2) {
            gamePanel.getUi().commandNum = 0;
          }
          gamePanel.playSoundEffect(CURSOR);
        }
      }
    } else if (gamePanel.getUi().subState == 1) {
      npcInventory(code);
      if (code == KeyEvent.VK_ESCAPE) {
        gamePanel.getUi().subState = 0;
      }
    } else if (gamePanel.getUi().subState == 2) {
      playerInventory(code);
      if (code == KeyEvent.VK_ESCAPE) {
        gamePanel.getUi().subState = 0;

      }
    }
  }

  private void gameOverState(int code) {
    switch (code) {
      case KeyEvent.VK_W, KeyEvent.VK_UP -> {
        gamePanel.getUi().commandNum--;
        if (gamePanel.getUi().commandNum < 0) {
          gamePanel.getUi().commandNum = 1;
        }
        gamePanel.playSoundEffect(CURSOR);
      }
      case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
        gamePanel.getUi().commandNum++;
        if (gamePanel.getUi().commandNum > 1) {
          gamePanel.getUi().commandNum = 0;
        }
        gamePanel.playSoundEffect(CURSOR);
      }
      case KeyEvent.VK_ENTER -> {
        if (gamePanel.getUi().commandNum == 0) {
          gamePanel.setGameState(PLAY_STATE);
          gamePanel.resetGame(false);
        } else if (gamePanel.getUi().commandNum == 1) {
          gamePanel.setGameState(TITLE_STATE);
          gamePanel.resetGame(true);
        }
      }
    }
  }

  private void optionState(int code) {
    int maxCommandNum = 0;
    switch (gamePanel.getUi().subState) {
      case 0 -> maxCommandNum = 5;
      case 3 -> maxCommandNum = 1;
    }
    switch (code) {
      case KeyEvent.VK_ESCAPE -> gamePanel.setGameState(PLAY_STATE);
      case KeyEvent.VK_ENTER -> enterPressed = true;
      case KeyEvent.VK_W -> {
        gamePanel.getUi().commandNum--;
        gamePanel.playSoundEffect(CURSOR);
        if (gamePanel.getUi().commandNum < 0) {
          gamePanel.getUi().commandNum = maxCommandNum;
        }
      }
      case KeyEvent.VK_S -> {
        gamePanel.getUi().commandNum++;
        gamePanel.playSoundEffect(CURSOR);
        if (gamePanel.getUi().commandNum > maxCommandNum) {
          gamePanel.getUi().commandNum = 0;
        }
      }
      case KeyEvent.VK_A -> {
        if (gamePanel.getUi().subState == 0) {
          if (gamePanel.getUi().commandNum == 1 && gamePanel.getMusic().getVolumeScale() > 0) {
            gamePanel.getMusic().setVolumeScale(gamePanel.getMusic().getVolumeScale() - 1);
            gamePanel.getMusic().checkVolume();
            gamePanel.playSoundEffect(CURSOR);
          }
        }
        if (gamePanel.getUi().subState == 0) {
          if (gamePanel.getUi().commandNum == 2 && gamePanel.getSoundEffect().getVolumeScale() > 0) {
            gamePanel.getSoundEffect().setVolumeScale(gamePanel.getSoundEffect().getVolumeScale() - 1);
            gamePanel.playSoundEffect(CURSOR);
          }
        }
      }
      case KeyEvent.VK_D -> {
        if (gamePanel.getUi().subState == 0) {
          if (gamePanel.getUi().commandNum == 1 && gamePanel.getMusic().getVolumeScale() < 5) {
            gamePanel.getMusic().setVolumeScale(gamePanel.getMusic().getVolumeScale() + 1);
            gamePanel.getMusic().checkVolume();
            gamePanel.playSoundEffect(CURSOR);
          }
        }
        if (gamePanel.getUi().subState == 0) {
          if (gamePanel.getUi().commandNum == 2 && gamePanel.getSoundEffect().getVolumeScale() < 5) {
            gamePanel.getSoundEffect().setVolumeScale(gamePanel.getSoundEffect().getVolumeScale() + 1);
            gamePanel.playSoundEffect(CURSOR);
          }
        }
      }
    }
  }

  private void mapState(int code) {
    if (code == KeyEvent.VK_M) {
      gamePanel.setGameState(PLAY_STATE);
    }
  }

  @Override
  public void keyTyped(KeyEvent keyEvent) {}

  @Override
  public void keyPressed(KeyEvent keyEvent) {
    int code = keyEvent.getKeyCode();

    switch (gamePanel.getGameState()) {
      case PLAY_STATE -> playState(code);
      case PAUSE_STATE -> pauseState(code);
      case DIALOG_STATE,
           CUTSCENE_STATE -> dialogState(code);
      case TITLE_STATE -> titleState(code);
      case CHARACTER_STATE -> characterState(code);
      case OPTIONS_STATE -> optionState(code);
      case GAME_OVER_STATE -> gameOverState(code);
      case TRADE_STATE -> tradeState(code);
      case MAP_STATE -> mapState(code);
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    int code = e.getKeyCode();
    switch (code) {
      case KeyEvent.VK_W -> upPressed = false;
      case KeyEvent.VK_S -> downPressed = false;
      case KeyEvent.VK_A -> leftPressed = false;
      case KeyEvent.VK_D -> rightPressed = false;
      case KeyEvent.VK_F -> shotKeyPressed = false;
      case KeyEvent.VK_SPACE -> spacePressed = false;
      case KeyEvent.VK_ENTER -> enterPressed = false;
    }
  }
}
