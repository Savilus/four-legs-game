package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Config {

  private static final String GAME_CONFIG_FILE_NAME = "src/main/gameConfig.txt";
  private static final String FULL_SCREEN_ON = "On";
  private static final String FULL_SCREEN_OFF = "Off";

  GamePanel gamePanel;

  public void saveConfig() {
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_CONFIG_FILE_NAME));

      if (gamePanel.fullScreenOn) {
        writer.write(FULL_SCREEN_ON);
      } else {
        writer.write(FULL_SCREEN_OFF);
      }

      writer.newLine();
      writer.write(String.valueOf(gamePanel.music.volumeScale));
      writer.newLine();
      writer.write(String.valueOf(gamePanel.soundEffect.volumeScale));
      writer.newLine();
      writer.close();

    } catch (IOException e) {
      throw new RuntimeException("Could not save config", e);
    }
  }

  public void loadConfig() {
    try {

      BufferedReader reader = new BufferedReader(new FileReader(GAME_CONFIG_FILE_NAME));
      String configLine = reader.readLine();
      gamePanel.fullScreenOn = configLine.equals(FULL_SCREEN_ON);
      configLine = reader.readLine();
      gamePanel.music.volumeScale = Integer.parseInt(configLine);
      configLine = reader.readLine();
      gamePanel.soundEffect.volumeScale = Integer.parseInt(configLine);

      reader.close();

    } catch (FileNotFoundException e) {
      throw new RuntimeException("Could not load config", e);
    } catch (IOException e) {
      throw new RuntimeException("Error while loading config", e);
    }
  }
}
