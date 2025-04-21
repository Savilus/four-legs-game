package org.savilusGame.entity.items;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.BLUE_HEART;
import static org.savilusGame.enums.GameState.CUTSCENE_STATE;
import static org.savilusGame.enums.WorldGameTypes.PICK_UP;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.enums.GameObject;
import org.savilusGame.utils.text.TextManager;

public class BlueHeart extends GameEntity {

  public BlueHeart(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObject.BLUE_HEART.getName();
    type = PICK_UP;
    down1 = setup(BLUE_HEART, TILE_SIZE, TILE_SIZE);
    mainImage = setup(BLUE_HEART, TILE_SIZE, TILE_SIZE);
    dialogues = TextManager.getAllDialoguesForTarget(GameObject.BLUE_HEART.getTextKey());
  }

  @Override
  public boolean use(GameEntity entity) {
    gamePanel.setGameState(CUTSCENE_STATE);
    gamePanel.getCutsceneManager().setSceneNum(gamePanel.getCutsceneManager().getEnding());
    return true;
  }
}
