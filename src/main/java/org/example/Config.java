package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Config {

  private static final String GAME_CONFIG_FILE_NAME = "src/main/gameConfig.txt";
  private static final String FULL_SCREEN_ON = "On";
  private static final String FULL_SCREEN_OFF = "Off";

  GamePanel gamePanel;

  public void saveConfig() {
    Try.run(() -> {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_CONFIG_FILE_NAME))) {
        writer.write(gamePanel.fullScreenOn ? FULL_SCREEN_ON : FULL_SCREEN_OFF);
        writer.newLine();
        writer.write(String.valueOf(gamePanel.music.volumeScale));
        writer.newLine();
        writer.write(String.valueOf(gamePanel.soundEffect.volumeScale));
      }
    }).onFailure(e -> System.err.println("Could not save config: " + e.getMessage()));
  }

  public void loadConfig() {
    Try.run(() -> {
      try (BufferedReader reader = new BufferedReader(new FileReader(GAME_CONFIG_FILE_NAME))) {
        String configLine = reader.readLine();
        gamePanel.fullScreenOn = configLine.equals(FULL_SCREEN_ON);
        configLine = reader.readLine();
        gamePanel.music.volumeScale = Integer.parseInt(configLine);
        configLine = reader.readLine();
        gamePanel.soundEffect.volumeScale = Integer.parseInt(configLine);
      }
    }).onFailure(e -> System.err.println("Could not load config: " + e.getMessage()));
  }
}
