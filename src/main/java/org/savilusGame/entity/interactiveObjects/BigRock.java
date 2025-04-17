package org.savilusGame.entity.interactiveObjects;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.BIGROCK;
import static org.savilusGame.config.GameEntityNameFactory.DOOR_OPEN;
import static org.savilusGame.config.GameEntityNameFactory.UNLOCK;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.interactiveTile.InteractiveTile;
import org.savilusGame.enums.Direction;
import org.savilusGame.enums.GameObject;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class BigRock extends GameEntity {

  private static final String BIG_ROCK_DIALOGUES_KEY = "bigRock";
  private static final String USE = "use";
  private static final int LINKED_OBJECTS_DISTANCE = 12;

  public BigRock(GamePanel gamePanel) {
    super(gamePanel);

    name = GameObject.BIG_ROCK.getName();
    direction = Direction.DOWN;
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

  private void detectPlate() {
    var currentMap = gamePanel.getMapsInteractiveTiles().get(CURRENT_MAP);

    List<InteractiveTile> plateList = Arrays.stream(currentMap)
        .filter(Objects::nonNull)
        .filter(tile -> StringUtils.equals(GameObject.METAL_PLATE.getName(), tile.getName()))
        .toList();

    List<GameEntity> rockList = Arrays.stream(gamePanel.getMapsNpc().get(CURRENT_MAP))
        .filter(Objects::nonNull)
        .filter(npc -> StringUtils.equals(GameObject.BIG_ROCK.getName(), npc.getName()))
        .toList();

    for (InteractiveTile plate : plateList) {
      int distance = Math.max(Math.abs(worldX - plate.getWorldX()), Math.abs(worldY - plate.getWorldY()));

      if (distance < LINKED_OBJECTS_DISTANCE) {
        if (Objects.isNull(linkedEntity)) {
          linkedEntity = plate;
          gamePanel.playSoundEffect(UNLOCK);
        }
      } else if (linkedEntity == plate) {
        linkedEntity = null;
      }
    }

    long linkedRocks = rockList.stream()
        .filter(rock -> Objects.nonNull(rock.getLinkedEntity()))
        .count();

    if (linkedRocks == rockList.size()) {
      var objects = gamePanel.getMapsObjects().get(CURRENT_MAP);
      for (int doorQuantity = 0; doorQuantity < objects.length; doorQuantity++) {
        if (Objects.nonNull(objects[doorQuantity]) &&
            GameObject.IRON_DOOR.getName().equals(objects[doorQuantity].getName())) {

          objects[doorQuantity] = null;
          gamePanel.playSoundEffect(DOOR_OPEN);
        }
      }
    }
  }

  private void getImage() {
    up1 = setup(BIGROCK, TILE_SIZE, TILE_SIZE);
    up2 = setup(BIGROCK, TILE_SIZE, TILE_SIZE);
    down1 = setup(BIGROCK, TILE_SIZE, TILE_SIZE);
    down2 = setup(BIGROCK, TILE_SIZE, TILE_SIZE);
    left1 = setup(BIGROCK, TILE_SIZE, TILE_SIZE);
    left2 = setup(BIGROCK, TILE_SIZE, TILE_SIZE);
    right1 = setup(BIGROCK, TILE_SIZE, TILE_SIZE);
    right2 = setup(BIGROCK, TILE_SIZE, TILE_SIZE);
  }

  @Override
  public void move(Direction direction) {
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

