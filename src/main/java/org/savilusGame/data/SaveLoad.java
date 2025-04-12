package org.savilusGame.data;

import static org.savilusGame.tile.TileManager.CURRENT_MAP;

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
      for (String mapName : gamePanel.getTileManager().getGameMaps().keySet()) {
        var interactiveTiles = gamePanel.getMapsInteractiveTiles().get(mapName);
        if (gamePanel.getMapsInteractiveTiles().containsKey(mapName) && Objects.nonNull(interactiveTiles)) {
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
      dataStorage.level = gamePanel.getPlayer().level;
      dataStorage.maxLife = gamePanel.getPlayer().maxLife;
      dataStorage.life = gamePanel.getPlayer().currentLife;
      dataStorage.mana = gamePanel.getPlayer().mana;
      dataStorage.maxMana = gamePanel.getPlayer().maxMana;
      dataStorage.strength = gamePanel.getPlayer().strength;
      dataStorage.dexterity = gamePanel.getPlayer().dexterity;
      dataStorage.exp = gamePanel.getPlayer().exp;
      dataStorage.nextLevelExp = gamePanel.getPlayer().nextLevelExp;
      dataStorage.money = gamePanel.getPlayer().money;

      // Player inventory
      for (int i = 0; i < gamePanel.getPlayer().inventory.size(); i++) {
        dataStorage.itemNames.add(gamePanel.getPlayer().inventory.get(i).name);
        dataStorage.itemAmounts.add(gamePanel.getPlayer().inventory.get(i).amount);
      }

      // Player equipment
      dataStorage.currentWeaponSlot = gamePanel.getPlayer().getCurrentWeaponSlot();
      dataStorage.currentShieldSlot = gamePanel.getPlayer().getCurrentShieldSlot();


      List<String> objectNamesList = new ArrayList<>();
      List<Integer> worldXList = new ArrayList<>();
      List<Integer> worldYList = new ArrayList<>();
      List<String> lootNamesList = new ArrayList<>();
      List<Boolean> openedList = new ArrayList<>();

      for (int i = 0; i < gamePanel.getMapsObjects().get(CURRENT_MAP).length; i++) {
        var mapObject = gamePanel.getMapsObjects().get(CURRENT_MAP)[i];
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

      dataStorage.mapObjectNames.put(CURRENT_MAP, objectNamesList);
      dataStorage.mapObjectWorldX.put(CURRENT_MAP, worldXList);
      dataStorage.mapObjectWorldY.put(CURRENT_MAP, worldYList);
      dataStorage.mapObjectLootNames.put(CURRENT_MAP, lootNamesList);
      dataStorage.mapObjectOpened.put(CURRENT_MAP, openedList);

      // Write the DataStorage object
      outputStream.writeObject(dataStorage);

    }).onFailure(error -> log.error("Error saving data", error));
  }

  public void load() {
    Try.run(() -> {
      ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(SAVE_LOAD_PATH_FILE));

      DataStorage dataStorage = (DataStorage) inputStream.readObject();

      // Interactive tiles
      for (String mapName : gamePanel.getTileManager().getGameMaps().keySet()) {
        if (dataStorage.mapInteractiveTilesCurrentLife.containsKey(mapName) && Objects.nonNull(dataStorage.mapInteractiveTilesCurrentLife.get(mapName))) {
          List<Integer> currentLifeList = dataStorage.mapInteractiveTilesCurrentLife.get(mapName);
          List<Boolean> invincibleList = dataStorage.mapInteractiveTilesInvincible.get(mapName);
          var interactiveTiles = gamePanel.getMapsInteractiveTiles().get(mapName);
          if (Objects.nonNull(currentLifeList) && Objects.nonNull(invincibleList) &&
              currentLifeList.size() == invincibleList.size()) {
            for (int index = 0; index < currentLifeList.size(); index++) {
              if (gamePanel.getMapsInteractiveTiles().containsKey(mapName) && interactiveTiles.length > index && Objects.nonNull(interactiveTiles[index])) {
                interactiveTiles[index].currentLife = currentLifeList.get(index);
                interactiveTiles[index].invincible = invincibleList.get(index);
                if (interactiveTiles[index].currentLife <= 0 && Objects.nonNull(interactiveTiles[index].getDestroyedForm())) {
                  gamePanel.getMapsInteractiveTiles().get(mapName)[index] = gamePanel.getMapsInteractiveTiles().get(mapName)[index].getDestroyedForm();
                }
              }
            }
          }
        }
      }

      // Player stats
      gamePanel.getPlayer().setDefaultPositions();
      gamePanel.getPlayer().level = dataStorage.level;
      gamePanel.getPlayer().maxLife = dataStorage.maxLife;
      gamePanel.getPlayer().currentLife = dataStorage.life;
      gamePanel.getPlayer().mana = dataStorage.mana;
      gamePanel.getPlayer().maxMana = dataStorage.maxMana;
      gamePanel.getPlayer().strength = dataStorage.strength;
      gamePanel.getPlayer().dexterity = dataStorage.dexterity;
      gamePanel.getPlayer().exp = dataStorage.exp;
      gamePanel.getPlayer().nextLevelExp = dataStorage.nextLevelExp;
      gamePanel.getPlayer().money = dataStorage.money;

      // Player equipment
      gamePanel.getPlayer().inventory.clear();
      for (int i = 0; i < dataStorage.itemNames.size(); i++) {
        GameEntity item = gamePanel.getGameEntityFactory().getGameEntity(dataStorage.itemNames.get(i));
        item.amount = dataStorage.itemAmounts.get(i);
        gamePanel.getPlayer().inventory.add(item);
      }

      gamePanel.getPlayer().currentWeapon = gamePanel.getPlayer().inventory.get(dataStorage.currentWeaponSlot);
      gamePanel.getPlayer().currentShield = gamePanel.getPlayer().inventory.get(dataStorage.currentShieldSlot);
      gamePanel.getPlayer().getAttack();
      gamePanel.getPlayer().getDefense();
      gamePanel.getPlayer().getAttackImage();

      // OBJECTS ON MAP
      for (String mapName : gamePanel.getTileManager().getGameMaps().keySet()) {
        List<String> objectNames = dataStorage.mapObjectNames.get(mapName);
        List<String> lootNames = dataStorage.mapObjectLootNames.get(mapName);
        List<Integer> worldXList = dataStorage.mapObjectWorldX.get(mapName);
        List<Integer> worldYList = dataStorage.mapObjectWorldY.get(mapName);
        List<Boolean> openedList = dataStorage.mapObjectOpened.get(mapName);

        if (Objects.isNull(objectNames)) continue;

        GameEntity[] objectsOnMap = gamePanel.getMapsObjects().get(mapName);

        for (int i = 0; i < objectNames.size(); i++) {
          if (StringUtils.equals(objectNames.get(i), NA)) {
            objectsOnMap[i] = null;
          } else {
            objectsOnMap[i] = gamePanel.getGameEntityFactory().getGameEntity(objectNames.get(i));
            objectsOnMap[i].worldX = worldXList.get(i);
            objectsOnMap[i].worldY = worldYList.get(i);

            if (Objects.nonNull(lootNames.get(i))) {
              objectsOnMap[i].setLoot(gamePanel.getGameEntityFactory().getGameEntity(lootNames.get(i)));
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
