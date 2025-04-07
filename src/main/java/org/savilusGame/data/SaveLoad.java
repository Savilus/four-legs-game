package org.savilusGame.data;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.interactiveTile.InteractiveTile;

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
        var interactiveTiles = gamePanel.mapsInteractiveTiles.get(mapName);
        if (gamePanel.mapsInteractiveTiles.containsKey(mapName) && Objects.nonNull(interactiveTiles)) {
          List<Integer> currentLifeList = new ArrayList<>();
          List<Boolean> invincibleList = new ArrayList<>();
          for (InteractiveTile interactiveTile : interactiveTiles) {
            if (Objects.nonNull(interactiveTile)) {
              currentLifeList.add(interactiveTile.currentLife);
              invincibleList.add(interactiveTile.invincible);
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
        var mapObject = gamePanel.mapsObjects.get(currentMapName)[i];
        if (Objects.isNull(mapObject)) {
          objectNamesList.add("NA");
          worldXList.add(0);
          worldYList.add(0);
          lootNamesList.add(null);
          openedList.add(false);
        } else {
          objectNamesList.add(mapObject.name);
          worldXList.add(mapObject.worldX);
          worldYList.add(mapObject.worldY);
          if (Objects.nonNull(mapObject.loot)) {
            lootNamesList.add(mapObject.loot.name);
          } else {
            lootNamesList.add(null);
          }
          openedList.add(mapObject.opened);
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
        if (dataStorage.mapInteractiveTilesCurrentLife.containsKey(mapName) && Objects.nonNull(dataStorage.mapInteractiveTilesCurrentLife.get(mapName))) {
          List<Integer> currentLifeList = dataStorage.mapInteractiveTilesCurrentLife.get(mapName);
          List<Boolean> invincibleList = dataStorage.mapInteractiveTilesInvincible.get(mapName);
          var interactiveTiles = gamePanel.mapsInteractiveTiles.get(mapName);
          if (Objects.nonNull(currentLifeList) && Objects.nonNull(invincibleList) &&
              currentLifeList.size() == invincibleList.size()) {
            for (int index = 0; index < currentLifeList.size(); index++) {
              if (gamePanel.mapsInteractiveTiles.containsKey(mapName) && interactiveTiles.length > index && Objects.nonNull(interactiveTiles[index])) {
                interactiveTiles[index].currentLife = currentLifeList.get(index);
                interactiveTiles[index].invincible = invincibleList.get(index);
                if (interactiveTiles[index].currentLife <= 0 && Objects.nonNull(interactiveTiles[index].getDestroyedForm())) {
                  gamePanel.mapsInteractiveTiles.get(mapName)[index] = gamePanel.mapsInteractiveTiles.get(mapName)[index].getDestroyedForm();
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

        if (Objects.isNull(objectNames)) continue;

        GameEntity[] objectsOnMap = gamePanel.mapsObjects.get(mapName);

        for (int i = 0; i < objectNames.size(); i++) {
          if (StringUtils.equals(objectNames.get(i), NA)) {
            objectsOnMap[i] = null;
          } else {
            objectsOnMap[i] = gamePanel.entityGenerator.getGameEntity(objectNames.get(i));
            objectsOnMap[i].worldX = worldXList.get(i);
            objectsOnMap[i].worldY = worldYList.get(i);

            if (Objects.nonNull(lootNames.get(i))) {
              objectsOnMap[i].setLoot(gamePanel.entityGenerator.getGameEntity(lootNames.get(i)));
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
