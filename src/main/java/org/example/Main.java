package org.example;

import java.util.List;
import java.util.Map;

import javax.swing.*;

import org.example.utils.text.TextManager;

public class Main {

  private static final String GAME_TITLE = "Four legs";

  public static JFrame window;

  public static void main(String[] args) {
    Map<String, Map<String, List<String>>> dialogues = TextManager.getDialogues();
    System.out.println(dialogues);
    window = new JFrame();
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.setTitle(GAME_TITLE);

    GamePanel gamePanel = new GamePanel();
    window.add(gamePanel);
    gamePanel.config.loadConfig();

    if (gamePanel.fullScreenOn) {
      window.setUndecorated(true);
    }

    window.pack();

    window.setLocationRelativeTo(null);
    window.setVisible(true);
    gamePanel.setupGame();
    gamePanel.startGameThread();
  }
}