package org.example.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GameMessageFactory {

  public static final String CONFIG_PATH = "/messageConfig.yml";

  // UI MESSAGES
  @YamlValue("ui.gotKey")
  public static String GOT_KEY;
  @YamlValue("ui.needKey")
  public static String NEED_KEY;
  @YamlValue("ui.openDoor")
  public static String OPEN_DOOR;
  @YamlValue("ui.boost")
  public static String BOOST;
  @YamlValue("ui.treasure")
  public static String TREASURE;
  @YamlValue("ui.congrats")
  public static String CONGRATS;

  static {
    YamlConfigLoader.loadConfig(GameMessageFactory.class, CONFIG_PATH);
  }
}
