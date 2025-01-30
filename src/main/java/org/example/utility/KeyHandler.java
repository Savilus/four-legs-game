package org.example.utility;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.example.GamePanel;

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

    if (gamePanel.gameState == gamePanel.playState) {
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
          gamePanel.gameState = gamePanel.pauseState;
          break;
        case KeyEvent.VK_ENTER:
          enterPressed = true;
      }
    }

    // PAUSE STATE
    else if (gamePanel.gameState == gamePanel.pauseState) {
      if (code == KeyEvent.VK_P) {
        gamePanel.gameState = gamePanel.playState;
      }
    }

    // DIALOGUE STATE
    else if (gamePanel.gameState == gamePanel.dialogueState) {
      if (code == KeyEvent.VK_ENTER) {
        gamePanel.gameState = gamePanel.playState;
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
