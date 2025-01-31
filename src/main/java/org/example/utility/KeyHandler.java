package org.example.utility;

import static org.example.enums.GameStateType.DIALOG_STATE;
import static org.example.enums.GameStateType.PAUSE_STATE;
import static org.example.enums.GameStateType.PLAY_STATE;
import static org.example.enums.GameStateType.TITLE_STATE;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.example.GamePanel;
import org.example.enums.GameStateType;

public class KeyHandler implements KeyListener {

  GamePanel gamePanel;
  public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;

  public KeyHandler(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();

    if (gamePanel.gameState == PLAY_STATE) {
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
      }
    }

    // PAUSE STATE
    else if (gamePanel.gameState == PAUSE_STATE) {
      if (code == KeyEvent.VK_P) {
        gamePanel.gameState = PLAY_STATE;
      }
    }

    // DIALOGUE STATE
    else if (gamePanel.gameState == DIALOG_STATE) {
      if (code == KeyEvent.VK_ENTER) {
        gamePanel.gameState = PLAY_STATE;
      }
    }

    // TITLE STATE
    else if (gamePanel.gameState == TITLE_STATE) {
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
          if(gamePanel.ui.commandNum == 0) {
            gamePanel.gameState = PLAY_STATE;
            gamePanel.playMusic(0);
          }
          if(gamePanel.ui.commandNum == 1) {
            // LATER
          }
          if(gamePanel.ui.commandNum == 2) {
            System.exit(0);
          }

      }
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
