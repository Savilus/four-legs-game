package aStarPathfindingAlgorithmExample;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class KeyHandler implements KeyListener {
  DemoPanel demoPanel;

  KeyHandler(DemoPanel demoPanel) {
    this.demoPanel = demoPanel;
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    if (code == KeyEvent.VK_ENTER) {
      demoPanel.manualSearch();
// Automatic version of path finder
//      demoPanel.autoSearch();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }
}
