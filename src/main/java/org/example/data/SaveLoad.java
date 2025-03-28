package org.example.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.example.GamePanel;
import org.example.entity.GameEntity;

import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class SaveLoad {

  private static final String SAVE_LOAD_PATH_FILE = "save.dat";
  private static final String NA = "NA";

  private GamePanel gamePanel;

  public void save() {
    Try.run(() -> {
      ObjectOutputStream outputStream =
          new ObjectOutputStream(new FileOutputStream(SAVE_LOAD_PATH_FILE));
      DataStorage dataStorage = new DataStorage();

      // Interactive tiles
      for (String mapName : gamePanel.tileManager.gameMaps.keySet()) {
        if (gamePanel.mapsInteractiveTiles.containsKey(mapName) && gamePanel.mapsInteractiveTiles.get(mapName) != null) {
          List<Integer> currentLifeList = new ArrayList<>();
          List<Boolean> invincibleList = new ArrayList<>();
          for (int i = 0; i < gamePanel.mapsInteractiveTiles.get(mapName).length; i++) {
            if (gamePanel.mapsInteractiveTiles.get(mapName)[i] != null) {
              currentLifeList.add(gamePanel.mapsInteractiveTiles.get(mapName)[i].currentLife);
              invincibleList.add(gamePanel.mapsInteractiveTiles.get(mapName)[i].invincible);
            } else {
              currentLifeList.add(0);
              invincibleList.add(false);
            }
          }
          dataStorage.mapInteractiveTilesCurrentLife.put(mapName, currentLifeList);
          dataStorage.mapInteractiveTilesInvincible.put(mapName, invincibleList);
        }
      }

      // Player stats
      dataStorage.level = gamePanel.player.level;
      dataStorage.maxLife = gamePanel.player.maxLife;
      dataStorage.life = gamePanel.player.currentLife;
      dataStorage.mana = gamePanel.player.mana;
      dataStorage.maxMana = gamePanel.player.maxMana;
      dataStorage.strength = gamePanel.player.strength;
      dataStorage.dexterity = gamePanel.player.dexterity;
      dataStorage.exp = gamePanel.player.exp;
      dataStorage.nextLevelExp = gamePanel.player.nextLevelExp;
      dataStorage.money = gamePanel.player.money;

      // Player inventory
      for (int i = 0; i < gamePanel.player.inventory.size(); i++) {
        dataStorage.itemNames.add(gamePanel.player.inventory.get(i).name);
        dataStorage.itemAmounts.add(gamePanel.player.inventory.get(i).amount);
      }

      // Player equipment
      dataStorage.currentWeaponSlot = gamePanel.player.getCurrentWeaponSlot();
      dataStorage.currentShieldSlot = gamePanel.player.getCurrentShieldSlot();

      // OBJECTS ON MAP
      String currentMapName = gamePanel.tileManager.currentMap;

      List<String> objectNamesList = new ArrayList<>();
      List<Integer> worldXList = new ArrayList<>();
      List<Integer> worldYList = new ArrayList<>();
      List<String> lootNamesList = new ArrayList<>();
      List<Boolean> openedList = new ArrayList<>();

      for (int i = 0; i < gamePanel.mapsObjects.get(currentMapName).length; i++) {
        if (gamePanel.mapsObjects.get(currentMapName)[i] == null) {
          objectNamesList.add("NA");
          worldXList.add(0);
          worldYList.add(0);
          lootNamesList.add(null);
          openedList.add(false);
        } else {
          objectNamesList.add(gamePanel.mapsObjects.get(currentMapName)[i].name);
          worldXList.add(gamePanel.mapsObjects.get(currentMapName)[i].worldX);
          worldYList.add(gamePanel.mapsObjects.get(currentMapName)[i].worldY);
          if (gamePanel.mapsObjects.get(currentMapName)[i].loot != null) {
            lootNamesList.add(gamePanel.mapsObjects.get(currentMapName)[i].loot.name);
          } else {
            lootNamesList.add(null);
          }
          openedList.add(gamePanel.mapsObjects.get(currentMapName)[i].opened);
        }
      }

      dataStorage.mapObjectNames.put(currentMapName, objectNamesList);
      dataStorage.mapObjectWorldX.put(currentMapName, worldXList);
      dataStorage.mapObjectWorldY.put(currentMapName, worldYList);
      dataStorage.mapObjectLootNames.put(currentMapName, lootNamesList);
      dataStorage.mapObjectOpened.put(currentMapName, openedList);

      // Write the DataStorage object
      outputStream.writeObject(dataStorage);

    }).onFailure(error -> log.error("Error saving data", error));
  }

  public void load() {
    Try.run(() -> {
      ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(SAVE_LOAD_PATH_FILE));

      DataStorage dataStorage = (DataStorage) inputStream.readObject();

      // Interactive tiles
      for (String mapName : gamePanel.tileManager.gameMaps.keySet()) {
        if (dataStorage.mapInteractiveTilesCurrentLife.containsKey(mapName) && dataStorage.mapInteractiveTilesCurrentLife.get(mapName) != null) {
          List<Integer> currentLifeList = dataStorage.mapInteractiveTilesCurrentLife.get(mapName);
          List<Boolean> invincibleList = dataStorage.mapInteractiveTilesInvincible.get(mapName);

          if (currentLifeList != null && invincibleList != null &&
              currentLifeList.size() == invincibleList.size()) {

            for (int i = 0; i < currentLifeList.size(); i++) {
              if (gamePanel.mapsInteractiveTiles.containsKey(mapName) && gamePanel.mapsInteractiveTiles.get(mapName).length > i && gamePanel.mapsInteractiveTiles.get(mapName)[i] != null) {
                gamePanel.mapsInteractiveTiles.get(mapName)[i].currentLife = currentLifeList.get(i);
                gamePanel.mapsInteractiveTiles.get(mapName)[i].invincible = invincibleList.get(i);
                if (gamePanel.mapsInteractiveTiles.get(mapName)[i].currentLife <= 0 && gamePanel.mapsInteractiveTiles.get(mapName)[i].getDestroyedForm() != null) {
                  gamePanel.mapsInteractiveTiles.get(mapName)[i] = gamePanel.mapsInteractiveTiles.get(mapName)[i].getDestroyedForm();
                }
              }
            }
          }
        }
      }

      // Player stats
      gamePanel.player.setDefaultPositions();
      gamePanel.player.level = dataStorage.level;
      gamePanel.player.maxLife = dataStorage.maxLife;
      gamePanel.player.currentLife = dataStorage.life;
      gamePanel.player.mana = dataStorage.mana;
      gamePanel.player.maxMana = dataStorage.maxMana;
      gamePanel.player.strength = dataStorage.strength;
      gamePanel.player.dexterity = dataStorage.dexterity;
      gamePanel.player.exp = dataStorage.exp;
      gamePanel.player.nextLevelExp = dataStorage.nextLevelExp;
      gamePanel.player.money = dataStorage.money;

      // Player equipment
      gamePanel.player.inventory.clear();
      for (int i = 0; i < dataStorage.itemNames.size(); i++) {
        GameEntity item = gamePanel.entityGenerator.getGameEntity(dataStorage.itemNames.get(i));
        item.amount = dataStorage.itemAmounts.get(i);
        gamePanel.player.inventory.add(item);
      }

      gamePanel.player.currentWeapon = gamePanel.player.inventory.get(dataStorage.currentWeaponSlot);
      gamePanel.player.currentShield = gamePanel.player.inventory.get(dataStorage.currentShieldSlot);
      gamePanel.player.getAttack();
      gamePanel.player.getDefense();
      gamePanel.player.getAttackImage();

      // OBJECTS ON MAP
      for (String mapName : gamePanel.tileManager.gameMaps.keySet()) {
        List<String> objectNames = dataStorage.mapObjectNames.get(mapName);
        List<String> lootNames = dataStorage.mapObjectLootNames.get(mapName);
        List<Integer> worldXList = dataStorage.mapObjectWorldX.get(mapName);
        List<Integer> worldYList = dataStorage.mapObjectWorldY.get(mapName);
        List<Boolean> openedList = dataStorage.mapObjectOpened.get(mapName);

        if (objectNames == null) continue;

        GameEntity[] objectsOnMap = gamePanel.mapsObjects.get(mapName);

        for (int i = 0; i < objectNames.size(); i++) {
          if (objectNames.get(i).equals(NA)) {
            objectsOnMap[i] = null;
          } else {
            objectsOnMap[i] = gamePanel.entityGenerator.getGameEntity(objectNames.get(i));
            objectsOnMap[i].worldX = worldXList.get(i);
            objectsOnMap[i].worldY = worldYList.get(i);

            if (lootNames.get(i) != null) {
              objectsOnMap[i].loot = gamePanel.entityGenerator.getGameEntity(lootNames.get(i));
            }

            objectsOnMap[i].opened = openedList.get(i);
            if (objectsOnMap[i].opened) {
              objectsOnMap[i].image = objectsOnMap[i].image2;
            }
          }
        }
      }
    }).onFailure(error -> log.error("Error loading data", error));
  }
}
