package org.savilusGame.entity.npc;

import static org.savilusGame.GamePanel.TILE_SIZE;
import static org.savilusGame.config.GameEntityNameFactory.MERCHANT_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.MERCHANT_DOWN2;
import static org.savilusGame.enums.GameState.TRADE_STATE;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.items.RedPotion;
import org.savilusGame.entity.items.Tent;
import org.savilusGame.entity.shield.BlueShield;
import org.savilusGame.entity.weapon.Axe;
import org.savilusGame.entity.weapon.PickAxe;
import org.savilusGame.enums.Direction;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class MerchantNPC extends GameEntity {
  private static final String MERCHANT_DIALOGUES_KEY = "merchantNpc";

  public MerchantNPC(GamePanel gamePanel) {
    super(gamePanel);
    direction = Direction.DOWN;
    speed = 1;
    type = WorldGameTypes.INTERACTIVE;
    getPlayerImage();
    dialogues = TextManager.getAllDialoguesForTarget(MERCHANT_DIALOGUES_KEY);
    setItems();
  }

  private void getPlayerImage() {
    up1 = setup(MERCHANT_DOWN1, TILE_SIZE, TILE_SIZE);
    up2 = setup(MERCHANT_DOWN2, TILE_SIZE, TILE_SIZE);
    down1 = setup(MERCHANT_DOWN1, TILE_SIZE, TILE_SIZE);
    down2 = setup(MERCHANT_DOWN2, TILE_SIZE, TILE_SIZE);
    left1 = setup(MERCHANT_DOWN1, TILE_SIZE, TILE_SIZE);
    left2 = setup(MERCHANT_DOWN2, TILE_SIZE, TILE_SIZE);
    right1 = setup(MERCHANT_DOWN1, TILE_SIZE, TILE_SIZE);
    right2 = setup(MERCHANT_DOWN2, TILE_SIZE, TILE_SIZE);
  }

  private void setItems() {
    inventory.add(new RedPotion(gamePanel));
    inventory.add(new Tent(gamePanel));
    inventory.add(new Axe(gamePanel));
    inventory.add(new PickAxe(gamePanel));
    inventory.add(new BlueShield(gamePanel));
  }

  @Override
  public void speak() {
    facePlayer();
    gamePanel.setGameState(TRADE_STATE);
    gamePanel.getUi().setDialogueObject(this);
  }
}
