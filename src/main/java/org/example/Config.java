package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public final class Config {

  private static final String GAME_CONFIG_FILE_NAME = "src/main/gameConfig.txt";
  private static final String FULL_SCREEN_ON = "On";
  private static final String FULL_SCREEN_OFF = "Off";

  private GamePanel gamePanel;

  void saveConfig() {
    Try.run(() -> {
      BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_CONFIG_FILE_NAME));
      writer.write(gamePanel.fullScreenOn ? FULL_SCREEN_ON : FULL_SCREEN_OFF);
      writer.newLine();
      writer.write(String.valueOf(gamePanel.music.getVolumeScale()));
      writer.newLine();
      writer.write(String.valueOf(gamePanel.soundEffect.getVolumeScale()));
    }).onFailure(e -> log.info("Could not save config: {}", e.getMessage()));
  }

  void loadConfig() {
    Try.run(() -> {
      BufferedReader reader = new BufferedReader(new FileReader(GAME_CONFIG_FILE_NAME));
      String configLine = reader.readLine();
      gamePanel.fullScreenOn = StringUtils.equalsIgnoreCase(configLine, FULL_SCREEN_ON);
      configLine = reader.readLine();
      gamePanel.music.setVolumeScale(Integer.parseInt(configLine));
      configLine = reader.readLine();
      gamePanel.soundEffect.setVolumeScale(Integer.parseInt(configLine));

    }).onFailure(e -> log.info("Could not save config: {}", e.getMessage()));
  }
}
