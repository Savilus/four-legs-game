package org.savilusGame.entity.npc;

import static org.savilusGame.config.GameEntityNameFactory.MERCHANT_DOWN1;
import static org.savilusGame.config.GameEntityNameFactory.MERCHANT_DOWN2;
import static org.savilusGame.enums.GameStateType.TRADE_STATE;

import org.savilusGame.GamePanel;
import org.savilusGame.entity.GameEntity;
import org.savilusGame.entity.items.Key;
import org.savilusGame.entity.items.RedPotion;
import org.savilusGame.entity.shield.BlueShield;
import org.savilusGame.entity.shield.WoodShield;
import org.savilusGame.entity.weapon.Axe;
import org.savilusGame.entity.weapon.NormalSword;
import org.savilusGame.enums.DirectionType;
import org.savilusGame.enums.WorldGameTypes;
import org.savilusGame.utils.text.TextManager;

public class MerchantNPC extends GameEntity {
  private static final String MERCHANT_DIALOGUES_KEY = "merchantNpc";

  public MerchantNPC(GamePanel gamePanel) {
    super(gamePanel);

    direction = DirectionType.DOWN;
    speed = 1;
    type = WorldGameTypes.INTERACTIVE;

    getPlayerImage();
    dialogues = TextManager.getAllDialoguesForTarget(MERCHANT_DIALOGUES_KEY);
    setItems();
  }

  public void getPlayerImage() {
    up1 = setup(MERCHANT_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    up2 = setup(MERCHANT_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    down1 = setup(MERCHANT_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    down2 = setup(MERCHANT_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    left1 = setup(MERCHANT_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    left2 = setup(MERCHANT_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
    right1 = setup(MERCHANT_DOWN1, gamePanel.tileSize, gamePanel.tileSize);
    right2 = setup(MERCHANT_DOWN2, gamePanel.tileSize, gamePanel.tileSize);
  }

  public void setItems() {
    inventory.add(new RedPotion(gamePanel));
    inventory.add(new Key(gamePanel));
    inventory.add(new NormalSword(gamePanel));
    inventory.add(new Axe(gamePanel));
    inventory.add(new WoodShield(gamePanel));
    inventory.add(new BlueShield(gamePanel));
  }

  @Override
  public void speak() {
    facePlayer();
    gamePanel.gameState = TRADE_STATE;
    gamePanel.ui.npc = this;
  }
}
