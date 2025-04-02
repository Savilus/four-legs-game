package org.example.entity.interactiveObjects;

import static org.example.config.GameEntityNameFactory.BIGROCK;
import static org.example.config.GameEntityNameFactory.DOOR_OPEN;
import static org.example.config.GameEntityNameFactory.UNLOCK;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.entity.interactiveTile.InteractiveTile;
import org.example.enums.DirectionType;
import org.example.enums.GameObjectType;
import org.example.enums.WorldGameTypes;
import org.example.utils.text.TextManager;

public class BigRock extends GameEntity {

  private static final String BIG_ROCK_DIALOGUES_KEY = "bigRock";
  private static final String USE = "use";

  public BigRock(GamePanel gamePanel) {
    super(gamePanel);

    name = GameObjectType.BIG_ROCK.getName();
    direction = DirectionType.DOWN;
    speed = 4;
    type = WorldGameTypes.OBSTACLE;
    getImage();
    dialogues = TextManager.getItemTexts(BIG_ROCK_DIALOGUES_KEY);

    solidArea = new Rectangle();
    solidArea.x = 2;
    solidArea.y = 6;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    solidArea.width = 44;
    solidArea.height = 40;
  }

  public void getImage() {
    up1 = setup(BIGROCK, gamePanel.tileSize, gamePanel.tileSize);
    up2 = setup(BIGROCK, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(BIGROCK, gamePanel.tileSize, gamePanel.tileSize);
    down2 = setup(BIGROCK, gamePanel.tileSize, gamePanel.tileSize);
    left1 = setup(BIGROCK, gamePanel.tileSize, gamePanel.tileSize);
    left2 = setup(BIGROCK, gamePanel.tileSize, gamePanel.tileSize);
    right1 = setup(BIGROCK, gamePanel.tileSize, gamePanel.tileSize);
    right2 = setup(BIGROCK, gamePanel.tileSize, gamePanel.tileSize);
  }

  public void detectPlate() {
    ArrayList<InteractiveTile> plateList = new ArrayList<>();
    ArrayList<GameEntity> rockList = new ArrayList<>();

    for (int i = 0; i < gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap).length; i++) {

      if (Objects.nonNull(gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[i]) &&
          StringUtils.equals(gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[i].name, GameObjectType.METAL_PLATE.getName())) {
        plateList.add(gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[i]);
      }

    }

    for (int i = 0; i < gamePanel.mapsNpc.get(gamePanel.tileManager.currentMap).length; i++) {
      if (Objects.nonNull(gamePanel.mapsNpc.get(gamePanel.tileManager.currentMap)[i]) &&
          StringUtils.equals(gamePanel.mapsNpc.get(gamePanel.tileManager.currentMap)[i].name, GameObjectType.BIG_ROCK.getName())) {
        rockList.add(gamePanel.mapsNpc.get(gamePanel.tileManager.currentMap)[i]);
      }
    }

    int countRock = 0;

    for (InteractiveTile interactiveTile : plateList) {
      int xDistance = Math.abs(worldX - interactiveTile.worldX);
      int yDistance = Math.abs(worldY - interactiveTile.worldY);
      int distance = Math.max(xDistance, yDistance);

      if (distance < 12) {
        if (linkedEntity == null) {
          linkedEntity = interactiveTile;
          gamePanel.playSoundEffect(UNLOCK);
        }
      } else {
        if (linkedEntity == interactiveTile) {
          linkedEntity = null;
        }
      }
    }

    for (GameEntity gameEntity : rockList) {
      if (Objects.nonNull(gameEntity.linkedEntity)) {
        countRock++;
      }
    }

    if (countRock == rockList.size()) {
      for (int i = 0; i < gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap).length; i++) {

        if (Objects.nonNull(gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i]) &&
            StringUtils.equals(gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i].name, GameObjectType.IRON_DOOR.getName())) {

          gamePanel.mapsObjects.get(gamePanel.tileManager.currentMap)[i] = null;
          gamePanel.playSoundEffect(DOOR_OPEN);

        }
      }
    }
  }

  @Override
  public void move(DirectionType direction) {
    this.direction = direction;
    checkCollision();

    if (!collisionOn) {
      switch (direction) {
        case UP:
          worldY -= speed;
          break;
        case DOWN:
          worldY += speed;
          break;
        case LEFT:
          worldX -= speed;
          break;
        case RIGHT:
          worldX += speed;
          break;
      }
    }
    detectPlate();
  }

  @Override
  public void speak() {
    startDialogue(this, USE);
  }

  @Override
  public void update() {
  }

}

