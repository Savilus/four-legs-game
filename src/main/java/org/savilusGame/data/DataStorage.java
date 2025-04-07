package org.savilusGame.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStorage implements Serializable {

  // PLAYER STATS
  int level;
  int maxLife;
  int life;
  int mana;
  int maxMana;
  int strength;
  int dexterity;
  int exp;
  int nextLevelExp;
  int money;

  // Player inventory
  ArrayList<String> itemNames = new ArrayList<>();
  ArrayList<Integer> itemAmounts = new ArrayList<>();
  int currentWeaponSlot;
  int currentShieldSlot;

  // Objects on map
  Map<String, List<String>> mapObjectNames = new HashMap<>();
  Map<String, List<String>> mapObjectLootNames = new HashMap<>();
  Map<String, List<Integer>> mapObjectWorldX = new HashMap<>();
  Map<String, List<Integer>> mapObjectWorldY = new HashMap<>();
  Map<String, List<Boolean>> mapObjectOpened = new HashMap<>();
  public Map<String, List<Integer>> mapInteractiveTilesCurrentLife = new HashMap<>();
  public Map<String, List<Boolean>> mapInteractiveTilesInvincible = new HashMap<>();
}
