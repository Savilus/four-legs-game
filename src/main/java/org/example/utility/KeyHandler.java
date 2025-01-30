package org.example.utility;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.example.GamePanel;

public class KeyHandler implements KeyListener {

  GamePanel gamePanel;
  public boolean upPressed, downPressed, leftPressed, rightPressed;

  public KeyHandler(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();

    switch (code) {
      case KeyEvent.VK_W: {
        upPressed = true;
        break;
      }
      case KeyEvent.VK_S: {
        downPressed = true;
        break;
      }
      case KeyEvent.VK_A: {
        leftPressed = true;
        break;
      }
      case KeyEvent.VK_D: {
        rightPressed = true;
        break;
      }
      case KeyEvent.VK_P: {
        if (gamePanel.gameState == gamePanel.playState) {
          gamePanel.gameState = gamePanel.pauseState;
        } else if (gamePanel.gameState == gamePanel.pauseState) {
          gamePanel.gameState = gamePanel.playState;
        }
        break;
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
