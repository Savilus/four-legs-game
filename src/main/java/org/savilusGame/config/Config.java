package org.savilusGame.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;
import org.savilusGame.GamePanel;

import io.vavr.control.Try;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class Config {

  static String GAME_CONFIG_FILE_NAME = "src/main/gameConfig.txt";
  static String FULL_SCREEN_ON = "On";
  static String FULL_SCREEN_OFF = "Off";

  GamePanel gamePanel;

  public void saveConfig() {
    Try.run(() -> {
      BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_CONFIG_FILE_NAME));
      writer.write(gamePanel.isFullScreenOn() ? FULL_SCREEN_ON : FULL_SCREEN_OFF);
      writer.newLine();
      writer.write(String.valueOf(gamePanel.getMusic().getVolumeScale()));
      writer.newLine();
      writer.write(String.valueOf(gamePanel.getSoundEffect().getVolumeScale()));
      writer.close();
    }).onFailure(e -> log.info("Could not save config: {}", e.getMessage()));
  }

  public void loadConfig() {
    Try.run(() -> {
      BufferedReader reader = new BufferedReader(new FileReader(GAME_CONFIG_FILE_NAME));
      String configLine = reader.readLine();
      gamePanel.setFullScreenOn(StringUtils.equalsIgnoreCase(configLine, FULL_SCREEN_ON));
      configLine = reader.readLine();
      gamePanel.getMusic().setVolumeScale(Integer.parseInt(configLine));
      configLine = reader.readLine();
      gamePanel.getSoundEffect().setVolumeScale(Integer.parseInt(configLine));

    }).onFailure(e -> log.info("Could not load config: {}", e.getMessage()));
  }
}
