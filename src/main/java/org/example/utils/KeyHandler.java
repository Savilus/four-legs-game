package org.example.utils;

import static org.example.config.GameEntityNameFactory.BACKGROUND_SONG;
import static org.example.config.GameEntityNameFactory.CURSOR;
import static org.example.enums.GameStateType.CHARACTER_STATE;
import static org.example.enums.GameStateType.MAP_STATE;
import static org.example.enums.GameStateType.OPTIONS_STATE;
import static org.example.enums.GameStateType.PAUSE_STATE;
import static org.example.enums.GameStateType.PLAY_STATE;
import static org.example.enums.GameStateType.TITLE_STATE;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.example.GamePanel;

public class KeyHandler implements KeyListener {

  GamePanel gamePanel;
  public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, spacePressed, shotKeyPressed;
  //DEBUG
  public boolean showDebugText;

  public KeyHandler(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  private void titleState(int code) {
    switch (code) {
      case KeyEvent.VK_W, KeyEvent.VK_UP -> {
        upPressed = true;
        gamePanel.ui.commandNum--;

        if (gamePanel.ui.commandNum < 0) {
          gamePanel.ui.commandNum = 2;
        }
      }
      case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
        downPressed = true;
        gamePanel.ui.commandNum++;

        if (gamePanel.ui.commandNum > 2) {
          gamePanel.ui.commandNum = 0;
        }
      }
      case KeyEvent.VK_ENTER -> {
        if (gamePanel.ui.commandNum == 0) {
          gamePanel.gameState = PLAY_STATE;
          gamePanel.playMusic(BACKGROUND_SONG);
        }
        if (gamePanel.ui.commandNum == 1) {
          // LATER
        }
        if (gamePanel.ui.commandNum == 2) {
          System.exit(0);
        }
      }
    }
  }

  private void pauseState(int code) {
    if (code == KeyEvent.VK_P) {
      gamePanel.gameState = PLAY_STATE;
    }
  }

  private void playState(int code) {
    switch (code) {
      case KeyEvent.VK_W -> upPressed = true;
      case KeyEvent.VK_S -> downPressed = true;
      case KeyEvent.VK_A -> leftPressed = true;
      case KeyEvent.VK_D -> rightPressed = true;
      case KeyEvent.VK_SPACE -> spacePressed = true;
      case KeyEvent.VK_P -> gamePanel.gameState = PAUSE_STATE;
      case KeyEvent.VK_ENTER -> enterPressed = true;
      case KeyEvent.VK_C -> gamePanel.gameState = CHARACTER_STATE;
      case KeyEvent.VK_F -> shotKeyPressed = true;
      case KeyEvent.VK_ESCAPE -> gamePanel.gameState = OPTIONS_STATE;
      case KeyEvent.VK_M -> gamePanel.gameState = MAP_STATE;
      case KeyEvent.VK_X -> gamePanel.gameMap.miniMapOn = !gamePanel.gameMap.miniMapOn;

      //DEBUG
      case KeyEvent.VK_T -> showDebugText = !showDebugText;
      //REFRESH MAP
      case KeyEvent.VK_R -> gamePanel.tileManager.loadMap(gamePanel.tileManager.currentMap);
    }
  }

  private void dialogState(int code) {
    if (code == KeyEvent.VK_ENTER) {
      gamePanel.gameState = PLAY_STATE;
    }
  }

  public void characterState(int code) {
    switch (code) {
      case KeyEvent.VK_C -> gamePanel.gameState = PLAY_STATE;
      case KeyEvent.VK_ENTER -> gamePanel.player.selectItem();
    }
    playerInventory(code);
  }

  public void playerInventory(int code) {
    switch (code) {
      case KeyEvent.VK_W, KeyEvent.VK_UP -> {
        if (gamePanel.ui.playerSlotRow != 0) {
          gamePanel.ui.playerSlotRow--;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
        if (gamePanel.ui.playerSlotCol != 0) {
          gamePanel.ui.playerSlotCol--;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
        if (gamePanel.ui.playerSlotRow != 3) {
          gamePanel.ui.playerSlotRow++;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
        if (gamePanel.ui.playerSlotCol != 4) {
          gamePanel.ui.playerSlotCol++;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
    }
  }

  public void npcInventory(int code) {
    switch (code) {
      case KeyEvent.VK_W, KeyEvent.VK_UP -> {
        if (gamePanel.ui.npcSlotRow != 0) {
          gamePanel.ui.npcSlotRow--;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_A, KeyEvent.VK_LEFT -> {
        if (gamePanel.ui.npcSlotCol != 0) {
          gamePanel.ui.npcSlotCol--;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
        if (gamePanel.ui.npcSlotRow != 3) {
          gamePanel.ui.npcSlotRow++;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
      case KeyEvent.VK_D, KeyEvent.VK_RIGHT -> {
        if (gamePanel.ui.npcSlotCol != 4) {
          gamePanel.ui.npcSlotCol++;
          gamePanel.playSoundEffect(CURSOR);
        }
      }
    }
  }

  private void tradeState(int code) {
    if (code == KeyEvent.VK_ENTER) {
      enterPressed = true;
    }

    if (gamePanel.ui.subState == 0) {
      switch (code) {
        case KeyEvent.VK_W -> {
          gamePanel.ui.commandNum--;
          if (gamePanel.ui.commandNum < 0) {
            gamePanel.ui.commandNum = 2;
          }
          gamePanel.playSoundEffect(CURSOR);
        }
        case KeyEvent.VK_S -> {
          gamePanel.ui.commandNum++;
          if (gamePanel.ui.commandNum > 2) {
            gamePanel.ui.commandNum = 0;
          }
          gamePanel.playSoundEffect(CURSOR);
        }
      }
    } else if (gamePanel.ui.subState == 1) {
      npcInventory(code);
      if (code == KeyEvent.VK_ESCAPE) {
        gamePanel.ui.subState = 0;
      }
    } else if (gamePanel.ui.subState == 2) {
      playerInventory(code);
      if (code == KeyEvent.VK_ESCAPE) {
        gamePanel.ui.subState = 0;

      }
    }
  }

  private void gameOverState(int code) {
    switch (code) {
      case KeyEvent.VK_W, KeyEvent.VK_UP -> {
        gamePanel.ui.commandNum--;
        if (gamePanel.ui.commandNum < 0) {
          gamePanel.ui.commandNum = 1;
        }
        gamePanel.playSoundEffect(CURSOR);
      }
      case KeyEvent.VK_S, KeyEvent.VK_DOWN -> {
        gamePanel.ui.commandNum++;
        if (gamePanel.ui.commandNum > 1) {
          gamePanel.ui.commandNum = 0;
        }
        gamePanel.playSoundEffect(CURSOR);
      }
      case KeyEvent.VK_ENTER -> {
        if (gamePanel.ui.commandNum == 0) {
          gamePanel.gameState = PLAY_STATE;
          gamePanel.resetGame(false);
        } else if (gamePanel.ui.commandNum == 1) {
          gamePanel.gameState = TITLE_STATE;
          gamePanel.resetGame(true);
        }
      }
    }
  }

  private void optionState(int code) {
    int maxCommandNum = 0;
    switch (gamePanel.ui.subState) {
      case 0 -> maxCommandNum = 5;
      case 3 -> maxCommandNum = 1;
    }
    switch (code) {
      case KeyEvent.VK_ESCAPE -> gamePanel.gameState = PLAY_STATE;
      case KeyEvent.VK_ENTER -> enterPressed = true;
      case KeyEvent.VK_W -> {
        gamePanel.ui.commandNum--;
        gamePanel.playSoundEffect(CURSOR);
        if (gamePanel.ui.commandNum < 0) {
          gamePanel.ui.commandNum = maxCommandNum;
        }
      }
      case KeyEvent.VK_S -> {
        gamePanel.ui.commandNum++;
        gamePanel.playSoundEffect(CURSOR);
        if (gamePanel.ui.commandNum > maxCommandNum) {
          gamePanel.ui.commandNum = 0;
        }
      }
      case KeyEvent.VK_A -> {
        if (gamePanel.ui.subState == 0) {
          if (gamePanel.ui.commandNum == 1 && gamePanel.music.volumeScale > 0) {
            gamePanel.music.volumeScale--;
            gamePanel.music.checkVolume();
            gamePanel.playSoundEffect(CURSOR);
          }
        }
        if (gamePanel.ui.subState == 0) {
          if (gamePanel.ui.commandNum == 2 && gamePanel.soundEffect.volumeScale > 0) {
            gamePanel.soundEffect.volumeScale--;
            gamePanel.playSoundEffect(CURSOR);
          }
        }
      }
      case KeyEvent.VK_D -> {
        if (gamePanel.ui.subState == 0) {
          if (gamePanel.ui.commandNum == 1 && gamePanel.music.volumeScale < 5) {
            gamePanel.music.volumeScale++;
            gamePanel.music.checkVolume();
            gamePanel.playSoundEffect(CURSOR);
          }
        }
        if (gamePanel.ui.subState == 0) {
          if (gamePanel.ui.commandNum == 2 && gamePanel.soundEffect.volumeScale < 5) {
            gamePanel.soundEffect.volumeScale++;
            gamePanel.playSoundEffect(CURSOR);
          }
        }
      }
    }
  }

  private void mapState(int code) {
    if (code == KeyEvent.VK_M) {
      gamePanel.gameState = PLAY_STATE;
    }
  }

  @Override
  public void keyTyped(KeyEvent keyEvent) {

  }

  @Override
  public void keyPressed(KeyEvent keyEvent) {
    int code = keyEvent.getKeyCode();

    switch (gamePanel.gameState) {
      case PLAY_STATE -> playState(code);
      case PAUSE_STATE -> pauseState(code);
      case DIALOG_STATE -> dialogState(code);
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
