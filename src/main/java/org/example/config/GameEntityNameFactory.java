package org.example.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public class GameEntityNameFactory {

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

  // PROJECTILE IMAGES
  @YamlValue("projectile.fireball.attackImages.attackUp1")
  public static String FIREBALL_UP1;
  @YamlValue("projectile.fireball.attackImages.attackUp2")
  public static String FIREBALL_UP2;
  @YamlValue("projectile.fireball.attackImages.attackDown1")
  public static String FIREBALL_DOWN1;
  @YamlValue("projectile.fireball.attackImages.attackDown2")
  public static String FIREBALL_DOWN2;
  @YamlValue("projectile.fireball.attackImages.attackRight1")
  public static String FIREBALL_RIGHT1;
  @YamlValue("projectile.fireball.attackImages.attackRight2")
  public static String FIREBALL_RIGHT2;
  @YamlValue("projectile.fireball.attackImages.attackLeft1")
  public static String FIREBALL_LEFT1;
  @YamlValue("projectile.fireball.attackImages.attackLeft2")
  public static String FIREBALL_LEFT2;
  @YamlValue("projectile.rock.attackImage")
  public static String ROCK_ATTACK;

  // PLAYER ATTACK IMAGES
  @YamlValue("player.swordAttackImages.attackUp1")
  public static String BOY_ATTACK_UP1;
  @YamlValue("player.swordAttackImages.attackUp2")
  public static String BOY_ATTACK_UP2;
  @YamlValue("player.swordAttackImages.attackDown1")
  public static String BOY_ATTACK_DOWN1;
  @YamlValue("player.swordAttackImages.attackDown2")
  public static String BOY_ATTACK_DOWN2;
  @YamlValue("player.swordAttackImages.attackRight1")
  public static String BOY_ATTACK_RIGHT1;
  @YamlValue("player.swordAttackImages.attackRight2")
  public static String BOY_ATTACK_RIGHT2;
  @YamlValue("player.swordAttackImages.attackLeft1")
  public static String BOY_ATTACK_LEFT1;
  @YamlValue("player.swordAttackImages.attackLeft2")
  public static String BOY_ATTACK_LEFT2;
  @YamlValue("player.axeAttackImages.attackUp1")
  public static String BOY_AXE_ATTACK_UP1;
  @YamlValue("player.axeAttackImages.attackUp2")
  public static String BOY_AXE_ATTACK_UP2;
  @YamlValue("player.axeAttackImages.attackDown1")
  public static String BOY_AXE_ATTACK_DOWN1;
  @YamlValue("player.axeAttackImages.attackDown2")
  public static String BOY_AXE_ATTACK_DOWN2;
  @YamlValue("player.axeAttackImages.rightAttack1")
  public static String BOY_AXE_ATTACK_RIGHT1;
  @YamlValue("player.axeAttackImages.rightAttack2")
  public static String BOY_AXE_ATTACK_RIGHT2;
  @YamlValue("player.axeAttackImages.leftAttack1")
  public static String BOY_AXE_ATTACK_LEFT1;
  @YamlValue("player.axeAttackImages.leftAttack2")
  public static String BOY_AXE_ATTACK_LEFT2;


  // MONSTER IMAGES
  @YamlValue("monster.images.greenSlime.down1")
  public static String GREEN_SLIME_DOWN1;
  @YamlValue("monster.images.greenSlime.down2")
  public static String GREEN_SLIME_DOWN2;

  // UI IMAGES
  @YamlValue("ui.images.heartFull")
  public static String HEART_FULL;
  @YamlValue("ui.images.heartHalf")
  public static String HEART_HALF;
  @YamlValue("ui.images.heartBlank")
  public static String HEART_BLANK;
  @YamlValue("ui.images.manaCrystalFull")
  public static String MANA_CRYSTAL_FULL;
  @YamlValue("ui.images.manaCrystalBlank")
  public static String MANA_CRYSTAL_BLANK;


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
  @YamlValue("items.images.normalSword")
  public static String NORMAL_SWORD;
  @YamlValue("items.images.woodenShield")
  public static String WOODEN_SHIELD;
  @YamlValue("items.images.blueShield")
  public static String BLUE_SHIELD;
  @YamlValue("items.images.axe")
  public static String AXE;
  @YamlValue("items.images.redPotion")
  public static String RED_POTION;
  @YamlValue("items.images.bronzeCoin")
  public static String BRONZE_COIN;

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
  @YamlValue("sound.path.hitMonster")
  public static String HIT_MONSTER;
  @YamlValue("sound.path.receiveDamage")
  public static String RECEIVE_DAMAGE;
  @YamlValue("sound.path.swingWeapon")
  public static String SWING_WEAPON;
  @YamlValue("sound.path.levelUp")
  public static String LEVEL_UP;
  @YamlValue("sound.path.cursor")
  public static String CURSOR;
  @YamlValue("sound.path.fireballShoot")
  public static String FIREBALL_SOUND;

  // FONT
  @YamlValue("font.maruMonica")
  public static String MARU_MONICA_FONT;
  @YamlValue("font.purisaBold")
  public static String PURISA_BOLD_FONT;

  static {
    YamlConfigLoader.loadConfig(GameEntityNameFactory.class, CONFIG_PATH);
  }
}
