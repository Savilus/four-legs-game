package org.example.utils;

import static org.example.config.GameEntityNameFactory.MAP_PATH;
import static org.example.enums.GameStateType.CHARACTER_STATE;
import static org.example.enums.GameStateType.DIALOG_STATE;
import static org.example.enums.GameStateType.PAUSE_STATE;
import static org.example.enums.GameStateType.PLAY_STATE;
import static org.example.enums.GameStateType.TITLE_STATE;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.example.GamePanel;

public class KeyHandler implements KeyListener {

  GamePanel gamePanel;
  public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
  //DEBUG
  public boolean showDebugText;

  public KeyHandler(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  private void titleState(int code) {
    switch (code) {
      case KeyEvent.VK_W, KeyEvent.VK_UP:
        upPressed = true;
        gamePanel.ui.commandNum--;
        if (gamePanel.ui.commandNum < 0) {
          gamePanel.ui.commandNum = 2;
        }
        break;
      case KeyEvent.VK_S, KeyEvent.VK_DOWN:
        downPressed = true;
        gamePanel.ui.commandNum++;
        if (gamePanel.ui.commandNum > 2) {
          gamePanel.ui.commandNum = 0;
        }
        break;
      case KeyEvent.VK_ENTER:
        if (gamePanel.ui.commandNum == 0) {
          gamePanel.gameState = PLAY_STATE;
          gamePanel.playMusic(0);
        }
        if (gamePanel.ui.commandNum == 1) {
          // LATER
        }
        if (gamePanel.ui.commandNum == 2) {
          System.exit(0);
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
      case KeyEvent.VK_W:
        upPressed = true;
        break;
      case KeyEvent.VK_S:
        downPressed = true;
        break;
      case KeyEvent.VK_A:
        leftPressed = true;
        break;
      case KeyEvent.VK_D:
        rightPressed = true;
        break;
      case KeyEvent.VK_P:
        gamePanel.gameState = PAUSE_STATE;
        break;
      case KeyEvent.VK_ENTER:
        enterPressed = true;
        break;
      case KeyEvent.VK_C:
        gamePanel.gameState = CHARACTER_STATE;
        break;
      //DEBUG
      case KeyEvent.VK_T: {
        showDebugText = !showDebugText;
      }
      //REFRESH MAP
      case KeyEvent.VK_R: gamePanel.tileManager.loadMap(MAP_PATH);
    }
  }

  private void dialogState(int code) {
    if (code == KeyEvent.VK_ENTER) {
      gamePanel.gameState = PLAY_STATE;
    }
  }

  public void characterState(int code) {
    switch (code) {
      case KeyEvent.VK_C:
        gamePanel.gameState = PLAY_STATE;
        break;
    }
  }

  @Override
  public void keyTyped(KeyEvent keyEvent) {

  }

  @Override
  public void keyPressed(KeyEvent keyEvent) {
    int code = keyEvent.getKeyCode();

    if (PLAY_STATE == gamePanel.gameState) {
      playState(code);
    } else if (PAUSE_STATE == gamePanel.gameState) {
      pauseState(code);
    } else if (DIALOG_STATE == gamePanel.gameState) {
      dialogState(code);
    } else if (TITLE_STATE == gamePanel.gameState) {
      titleState(code);
    } else if (CHARACTER_STATE == gamePanel.gameState) {
      characterState(code);
    }
  }


  @Override
  public void keyReleased(KeyEvent e) {
    int code = e.getKeyCode();
    switch (code) {
      case KeyEvent.VK_W: {
        upPressed = false;
        break;
      }
      case KeyEvent.VK_S: {
        downPressed = false;
        break;
      }
      case KeyEvent.VK_A: {
        leftPressed = false;
        break;
      }
      case KeyEvent.VK_D: {
        rightPressed = false;
        break;
      }
    }
  }
}
