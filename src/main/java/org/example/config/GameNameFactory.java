package org.example.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GameNameFactory {

  public static final String CONFIG_PATH = "/config.yml";

  // PLAYER IMAGES
  @YamlValue("player.images.boyUp1")
  public static String BOY_UP1;
  @YamlValue("player.images.boyUp2")
  public static String BOY_UP2;
  @YamlValue("player.images.boyDown1")
  public static String BOY_DOWN1;
  @YamlValue("player.images.boyDown2")
  public static String BOY_DOWN2;
  @YamlValue("player.images.boyLeft1")
  public static String BOY_LEFT1;
  @YamlValue("player.images.boyLeft2")
  public static String BOY_LEFT2;
  @YamlValue("player.images.boyRight1")
  public static String BOY_RIGHT1;
  @YamlValue("player.images.boyRight2")
  public static String BOY_RIGHT2;

  // NPC IMAGES
  @YamlValue("npc.images.merchantDown1")
  public static String MERCHANT_DOWN1;
  @YamlValue("npc.images.merchantDown2")
  public static String MERCHANT_DOWN2;
  @YamlValue("npc.images.bigrock")
  public static String BIGROCK;
  @YamlValue("npc.images.oldMan.down1")
  public static String OLD_MAN_DOWN1;
  @YamlValue("npc.images.oldMan.down2")
  public static String OLD_MAN_DOWN2;
  @YamlValue("npc.images.oldMan.up1")
  public static String OLD_MAN_UP1;
  @YamlValue("npc.images.oldMan.up2")
  public static String OLD_MAN_UP2;
  @YamlValue("npc.images.oldMan.left1")
  public static String OLD_MAN_LEFT1;
  @YamlValue("npc.images.oldMan.left2")
  public static String OLD_MAN_LEFT2;
  @YamlValue("npc.images.oldMan.right1")
  public static String OLD_MAN_RIGHT1;
  @YamlValue("npc.images.oldMan.right2")
  public static String OLD_MAN_RIGHT2;

  // ITEMS IMAGES
  @YamlValue("items.images.boots")
  public static String BOOTS;
  @YamlValue("items.images.chest")
  public static String CHEST;
  @YamlValue("items.images.door")
  public static String DOOR;
  @YamlValue("items.images.key")
  public static String KEY;

  // TILES IMAGES
  @YamlValue("world.tile.images.grass.0")
  public static String GRASS0;
  @YamlValue("world.tile.images.grass.1")
  public static String GRASS1;

  @YamlValue("world.tile.images.road.0")
  public static String ROAD0;
  @YamlValue("world.tile.images.road.1")
  public static String ROAD1;
  @YamlValue("world.tile.images.road.2")
  public static String ROAD2;
  @YamlValue("world.tile.images.road.3")
  public static String ROAD3;
  @YamlValue("world.tile.images.road.4")
  public static String ROAD4;
  @YamlValue("world.tile.images.road.5")
  public static String ROAD5;
  @YamlValue("world.tile.images.road.6")
  public static String ROAD6;
  @YamlValue("world.tile.images.road.7")
  public static String ROAD7;
  @YamlValue("world.tile.images.road.8")
  public static String ROAD8;
  @YamlValue("world.tile.images.road.9")
  public static String ROAD9;
  @YamlValue("world.tile.images.road.10")
  public static String ROAD10;
  @YamlValue("world.tile.images.road.11")
  public static String ROAD11;
  @YamlValue("world.tile.images.road.12")
  public static String ROAD12;


  @YamlValue("world.tile.images.water.0")
  public static String WATER0;
  @YamlValue("world.tile.images.water.1")
  public static String WATER1;
  @YamlValue("world.tile.images.water.2")
  public static String WATER2;
  @YamlValue("world.tile.images.water.3")
  public static String WATER3;
  @YamlValue("world.tile.images.water.4")
  public static String WATER4;
  @YamlValue("world.tile.images.water.5")
  public static String WATER5;
  @YamlValue("world.tile.images.water.6")
  public static String WATER6;
  @YamlValue("world.tile.images.water.7")
  public static String WATER7;
  @YamlValue("world.tile.images.water.8")
  public static String WATER8;
  @YamlValue("world.tile.images.water.9")
  public static String WATER9;
  @YamlValue("world.tile.images.water.10")
  public static String WATER10;
  @YamlValue("world.tile.images.water.11")
  public static String WATER11;
  @YamlValue("world.tile.images.water.12")
  public static String WATER12;
  @YamlValue("world.tile.images.water.13")
  public static String WATER13;


  @YamlValue("world.tile.images.wall")
  public static String WALL;

  @YamlValue("world.tile.images.earth")
  public static String EARTH;
  @YamlValue("world.tile.images.tree")
  public static String TREE;
  @YamlValue("world.tile.images.sand")
  public static String SAND;
  @YamlValue("world.tile.images.hut")
  public static String HUT;
  @YamlValue("world.tile.images.floor")
  public static String FLOOR;
  @YamlValue("world.tile.images.table")
  public static String TABLE;

  // WORLD
  @YamlValue("world.mapPath")
  public static String MAP_PATH;

  // SOUND
  @YamlValue("sound.path.backgroundSong")
  public static String BACKGROUND_SONG;
  @YamlValue("sound.path.coin")
  public static String COIN;
  @YamlValue("sound.path.powerUp")
  public static String POWER_UP;
  @YamlValue("sound.path.unlock")
  public static String UNLOCK;
  @YamlValue("sound.path.fanfare")
  public static String FANFARE;

  // FONT
  @YamlValue("font.maruMonica")
  public static String MARU_MONICA_FONT;
  @YamlValue("font.purisaBold")
  public static String PURISA_BOLD_FONT;

  static {
    YamlConfigLoader.loadConfig(GameNameFactory.class, CONFIG_PATH);
  }
}
