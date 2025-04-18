package org.savilusGame.entity.monster;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.DOOR_OPEN;
import static org.savilusGame.config.GameEntityNameFactory.DUNGEON_SONG;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_UP1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_PHASE2_UP2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_UP1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_ATTACK_UP2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_DOWN2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_LEFT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_LEFT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_UP1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_PHASE2_UP2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_RIGHT1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_RIGHT2;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_UP1;
import static org.savilusGame.config.GameEntityNameFactory.SKELETON_LORD_UP2;
import static org.savilusGame.enums.MonsterType.SKELETON_LORD;
import static org.savilusGame.tile.TileManager.CURRENT_MAP;

import java.util.stream.IntStream;

import org.savilusGame.GamePanel;
import org.savilusGame.data.Progress;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.items.IronDoor;
import org.savilusGame.enums.Direction;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkeletonLord extends GameEntity {

  static int SHOOT_INTERVAL = 60;
  static int MOVE_TOWARDS_PLAYER_INTERVAL = 60;
  static int RANDOM_DIRECTION_INTERVAL = 120;

  int bossScale = 5;
  int widthHeight = TILE_SIZE * bossScale;

  public SkeletonLord(GamePanel gamePanel) {
    super(gamePanel);
    name = SKELETON_LORD.getName();
    direction = Direction.DOWN;
    boss = true;
    defaultSpeed = 1;
    speed = defaultSpeed;
    maxLife = 10;
    currentLife = maxLife;
    type = WorldGameTypes.MONSTER;
    attack = 10;
    defense = 2;
    exp = 50;
    knockBackPower = 5;
    sleep = true;

    int size = TILE_SIZE * 5;
    solidArea.x = 48;
    solidArea.y = 48;
    solidArea.width = size - 48 * 2;
    solidArea.height = size - 48;
    solidAreaDefaultX = solidArea.x;
    solidAreaDefaultY = solidArea.y;
    attackArea.width = 170;
    attackArea.height = 170;
    firstAttackMotionDuration = 25;
    secondAttackMotionDuration = 50;
    getImage();
    getAttackImage();
    dialogues = TextManager.getAllDialoguesForTarget(SKELETON_LORD.getDialogueKey());
  }

  private void getImage() {
    if (!inRage) {
      loadImages(SKELETON_LORD_UP1, SKELETON_LORD_UP2, SKELETON_LORD_DOWN1, SKELETON_LORD_DOWN2,
          SKELETON_LORD_LEFT1, SKELETON_LORD_LEFT2, SKELETON_LORD_RIGHT1, SKELETON_LORD_RIGHT2);
    } else {
      loadImages(SKELETON_LORD_PHASE2_UP1, SKELETON_LORD_PHASE2_UP2, SKELETON_LORD_PHASE2_DOWN1, SKELETON_LORD_PHASE2_DOWN2,
          SKELETON_LORD_PHASE2_LEFT1, SKELETON_LORD_PHASE2_LEFT2, SKELETON_LORD_PHASE2_RIGHT1, SKELETON_LORD_PHASE2_RIGHT2);
    }
  }

  private void getAttackImage() {
    if (!inRage) {
      loadAttackImages(SKELETON_LORD_ATTACK_UP1, SKELETON_LORD_ATTACK_UP2, SKELETON_LORD_ATTACK_DOWN1, SKELETON_LORD_ATTACK_DOWN2,
          SKELETON_LORD_ATTACK_LEFT1, SKELETON_LORD_ATTACK_LEFT2, SKELETON_LORD_ATTACK_RIGHT1, SKELETON_LORD_ATTACK_RIGHT2);
    } else {
      loadAttackImages(SKELETON_LORD_ATTACK_PHASE2_UP1, SKELETON_LORD_ATTACK_PHASE2_UP2, SKELETON_LORD_ATTACK_PHASE2_DOWN1,
          SKELETON_LORD_ATTACK_PHASE2_DOWN2, SKELETON_LORD_ATTACK_PHASE2_LEFT1, SKELETON_LORD_ATTACK_PHASE2_LEFT2, SKELETON_LORD_ATTACK_PHASE2_RIGHT1, SKELETON_LORD_ATTACK_PHASE2_RIGHT2);
    }
  }

  private void loadImages(String skeletonLordUp1, String skeletonLordUp2, String skeletonLordDown1, String skeletonLordDown2, String skeletonLordLeft1, String skeletonLordLeft2, String skeletonLordRight1, String skeletonLordRight2) {
    up1 = setup(skeletonLordUp1, widthHeight, widthHeight);
    up2 = setup(skeletonLordUp2, widthHeight, widthHeight);
    down1 = setup(skeletonLordDown1, widthHeight, widthHeight);
    down2 = setup(skeletonLordDown2, widthHeight, widthHeight);
    left1 = setup(skeletonLordLeft1, widthHeight, widthHeight);
    left2 = setup(skeletonLordLeft2, widthHeight, widthHeight);
    right1 = setup(skeletonLordRight1, widthHeight, widthHeight);
    right2 = setup(skeletonLordRight2, widthHeight, widthHeight);
  }

  private void loadAttackImages(String skeletonLordAttackUp1, String skeletonLordAttackUp2, String skeletonLordAttackDown1, String skeletonLordAttackDown2, String skeletonLordAttackLeft1, String skeletonLordAttackLeft2, String skeletonLordAttackRight1, String skeletonLordAttackRight2) {
    attackUp1 = setup(skeletonLordAttackUp1, widthHeight, widthHeight * 2);
    attackUp2 = setup(skeletonLordAttackUp2, widthHeight, widthHeight * 2);
    attackDown1 = setup(skeletonLordAttackDown1, widthHeight, widthHeight * 2);
    attackDown2 = setup(skeletonLordAttackDown2, widthHeight, widthHeight * 2);
    attackLeft1 = setup(skeletonLordAttackLeft1, widthHeight * 2, widthHeight);
    attackLeft2 = setup(skeletonLordAttackLeft2, widthHeight * 2, widthHeight);
    attackRight1 = setup(skeletonLordAttackRight1, widthHeight * 2, widthHeight);
    attackRight2 = setup(skeletonLordAttackRight2, widthHeight * 2, widthHeight);
  }

  @Override
  public void checkDrop() {
    gamePanel.setBossBattleOn(false);
    Progress.skeletonLordDefeated = true;

    gamePanel.stopMusic();
    gamePanel.playMusic(DUNGEON_SONG);

    var objects = gamePanel.getMapsObjects().get(CURRENT_MAP);
    if (objects == null) return;

    objects.removeIf(object -> object instanceof IronDoor);
    gamePanel.playSoundEffect(DOOR_OPEN);
  }

  @Override
  public void damageReaction() {
    actionLockCounter = 0;
  }

  @Override
  public void setAction() {
    if (!inRage && currentLife < maxLife / 2) {
      inRage = true;
      getImage();
      getAttackImage();
      defaultSpeed++;
      speed = defaultSpeed;
      attack *= 2;
    }

    if (getTileDistance(gamePanel.getPlayer()) < 8) {
      moveTowardPlayer(MOVE_TOWARDS_PLAYER_INTERVAL);
    } else {
      getRandomDirection(RANDOM_DIRECTION_INTERVAL);
    }

    if (!attacking) {
      checkIfShouldAttack(SHOOT_INTERVAL, TILE_SIZE * 7, TILE_SIZE * 5);
    }
  }
}

