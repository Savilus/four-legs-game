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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SaveLoad {

  static String SAVE_LOAD_PATH_FILE = "save.dat";
  static String NA = "NA";

  GamePanel gamePanel;

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
              currentLifeList.add(interactiveTile.getCurrentLife());
              invincibleList.add(interactiveTile.isInvincible());
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
      dataStorage.setLevel(gamePanel.getPlayer().getLevel());
      dataStorage.setMaxLife(gamePanel.getPlayer().getMaxLife());
      dataStorage.setLife(gamePanel.getPlayer().getCurrentLife());
      dataStorage.setMana(gamePanel.getPlayer().getMana());
      dataStorage.setMaxMana(gamePanel.getPlayer().getMaxMana());
      dataStorage.setStrength(gamePanel.getPlayer().getStrength());
      dataStorage.setDexterity(gamePanel.getPlayer().getDexterity());
      dataStorage.setExp(gamePanel.getPlayer().getExp());
      dataStorage.setNextLevelExp(gamePanel.getPlayer().getNextLevelExp());
      dataStorage.setMoney(gamePanel.getPlayer().getMoney());

      // Player inventory
      for (int i = 0; i < gamePanel.getPlayer().getInventory().size(); i++) {
        dataStorage.getItemNames().add(gamePanel.getPlayer().getInventory().get(i).getName());
        dataStorage.getItemAmounts().add(gamePanel.getPlayer().getInventory().get(i).getAmount());
      }

      // Player equipment
      dataStorage.setCurrentWeaponSlot(gamePanel.getPlayer().getCurrentWeaponSlot());
      dataStorage.setCurrentShieldSlot(gamePanel.getPlayer().getCurrentShieldSlot());


      List<String> objectNamesList = new ArrayList<>();
      List<Integer> worldXList = new ArrayList<>();
      List<Integer> worldYList = new ArrayList<>();
      List<String> lootNamesList = new ArrayList<>();
      List<Boolean> openedList = new ArrayList<>();

      for (int i = 0; i < gamePanel.getMapsObjects().get(CURRENT_MAP).length; i++) {
        var mapObject = gamePanel.getMapsObjects().get(CURRENT_MAP)[i];
        if (Objects.isNull(mapObject)) {
          objectNamesList.add(NA);
          worldXList.add(0);
          worldYList.add(0);
          lootNamesList.add(null);
          openedList.add(false);
        } else {
          objectNamesList.add(mapObject.getName());
          worldXList.add(mapObject.getWorldX());
          worldYList.add(mapObject.getWorldY());
          if (Objects.nonNull(mapObject.getLoot())) {
            lootNamesList.add(mapObject.getLoot().getName());
          } else {
            lootNamesList.add(null);
          }
          openedList.add(mapObject.isOpened());
        }
      }

      dataStorage.getMapObjectNames().put(CURRENT_MAP, objectNamesList);
      dataStorage.getMapObjectWorldX().put(CURRENT_MAP, worldXList);
      dataStorage.getMapObjectWorldY().put(CURRENT_MAP, worldYList);
      dataStorage.getMapObjectLootNames().put(CURRENT_MAP, lootNamesList);
      dataStorage.getMapObjectOpened().put(CURRENT_MAP, openedList);

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
                interactiveTiles[index].setCurrentLife(currentLifeList.get(index));
                interactiveTiles[index].setInvincible(invincibleList.get(index));
                if (interactiveTiles[index].getCurrentLife() <= 0 && Objects.nonNull(interactiveTiles[index].getDestroyedForm())) {
                  gamePanel.getMapsInteractiveTiles().get(mapName)[index] = gamePanel.getMapsInteractiveTiles().get(mapName)[index].getDestroyedForm();
                }
              }
            }
          }
        }
      }

      // Player stats
      gamePanel.getPlayer().setDefaultPositions();
      gamePanel.getPlayer().setLevel(dataStorage.getLevel());
      gamePanel.getPlayer().setMaxLife(dataStorage.getMaxLife());
      gamePanel.getPlayer().setCurrentLife(dataStorage.getLife());
      gamePanel.getPlayer().setMana(dataStorage.getMana());
      gamePanel.getPlayer().setMaxMana(dataStorage.getMaxMana());
      gamePanel.getPlayer().setStrength(dataStorage.getStrength());
      gamePanel.getPlayer().setDexterity(dataStorage.getDexterity());
      gamePanel.getPlayer().setExp(dataStorage.getExp());
      gamePanel.getPlayer().setNextLevelExp(dataStorage.getNextLevelExp());
      gamePanel.getPlayer().setMoney(dataStorage.getMoney());

      // Player equipment
      gamePanel.getPlayer().getInventory().clear();
      for (int i = 0; i < dataStorage.getItemNames().size(); i++) {
        GameEntity item = gamePanel.getGameEntityFactory().getGameEntity(dataStorage.getItemNames().get(i));
        item.setAmount(dataStorage.getItemAmounts().get(i));
        gamePanel.getPlayer().getInventory().add(item);
      }

      gamePanel.getPlayer().setCurrentWeapon(gamePanel.getPlayer().getInventory().get(dataStorage.getCurrentWeaponSlot()));
      gamePanel.getPlayer().setCurrentShield(gamePanel.getPlayer().getInventory().get(dataStorage.getCurrentShieldSlot()));
      gamePanel.getPlayer().getAttack();
      gamePanel.getPlayer().getDefense();
      gamePanel.getPlayer().getAttackImage();

      // OBJECTS ON MAP
      for (String mapName : gamePanel.getTileManager().getGameMaps().keySet()) {
        List<String> objectNames = dataStorage.getMapObjectNames().get(mapName);
        List<String> lootNames = dataStorage.getMapObjectLootNames().get(mapName);
        List<Integer> worldXList = dataStorage.getMapObjectWorldX().get(mapName);
        List<Integer> worldYList = dataStorage.getMapObjectWorldY().get(mapName);
        List<Boolean> openedList = dataStorage.getMapObjectOpened().get(mapName);

        if (Objects.isNull(objectNames)) continue;

        GameEntity[] objectsOnMap = gamePanel.getMapsObjects().get(mapName);

        for (int i = 0; i < objectNames.size(); i++) {
          if (StringUtils.equals(objectNames.get(i), NA)) {
            objectsOnMap[i] = null;
          } else {
            objectsOnMap[i] = gamePanel.getGameEntityFactory().getGameEntity(objectNames.get(i));
            objectsOnMap[i].setWorldX(worldXList.get(i));
            objectsOnMap[i].setWorldY(worldYList.get(i));

            if (Objects.nonNull(lootNames.get(i))) {
              objectsOnMap[i].setLoot(gamePanel.getGameEntityFactory().getGameEntity(lootNames.get(i)));
            }

            objectsOnMap[i].setOpened(openedList.get(i));
            if (objectsOnMap[i].isOpened()) {
              objectsOnMap[i].setMainImage(objectsOnMap[i].getMainImage2());
            }
          }
        }
      }
    }).onFailure(error -> log.error("Error loading data", error));
  }
}
