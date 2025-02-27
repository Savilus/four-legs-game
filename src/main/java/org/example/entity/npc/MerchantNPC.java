package org.example.entity.npc;

import static org.example.config.GameEntityNameFactory.MERCHANT_DOWN1;
import static org.example.config.GameEntityNameFactory.MERCHANT_DOWN2;
import static org.example.enums.GameStateType.TRADE_STATE;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.entity.object.AxeObject;
import org.example.entity.object.BlueShieldObject;
import org.example.entity.object.KeyObject;
import org.example.entity.object.NormalSwordObject;
import org.example.entity.object.RedPotionObject;
import org.example.entity.object.WoodShieldObject;
import org.example.enums.DirectionType;
import org.example.enums.WorldGameTypes;

public class MerchantNPC extends GameEntity {

  public MerchantNPC(GamePanel gamePanel) {
    super(gamePanel);

    direction = DirectionType.DOWN;
    speed = 1;
    type = WorldGameTypes.NPC;

    getPlayerImage();
    setDialogue();
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

  private void setDialogue() {
    dialogues[0] = "He, he, he, so you found me. \n I have some good stuff. \n Do you want to trade?";
  }

  public void setItems() {
    inventory.add(new RedPotionObject(gamePanel));
    inventory.add(new KeyObject(gamePanel));
    inventory.add(new NormalSwordObject(gamePanel));
    inventory.add(new AxeObject(gamePanel));
    inventory.add(new WoodShieldObject(gamePanel));
    inventory.add(new BlueShieldObject(gamePanel));
  }

  @Override
  public void speak() {
    super.speak();
    gamePanel.gameState = TRADE_STATE;
    gamePanel.ui.merchantNPC = this;
  }
}
