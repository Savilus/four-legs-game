package org.example.entity.npc;

import static org.example.config.GameEntityNameFactory.MERCHANT_DOWN1;
import static org.example.config.GameEntityNameFactory.MERCHANT_DOWN2;
import static org.example.enums.GameStateType.TRADE_STATE;

import org.example.GamePanel;
import org.example.entity.GameEntity;
import org.example.entity.object.Axe;
import org.example.entity.object.BlueShield;
import org.example.entity.object.Key;
import org.example.entity.object.NormalSword;
import org.example.entity.object.RedPotion;
import org.example.entity.object.WoodShield;
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
    inventory.add(new RedPotion(gamePanel));
    inventory.add(new Key(gamePanel));
    inventory.add(new NormalSword(gamePanel));
    inventory.add(new Axe(gamePanel));
    inventory.add(new WoodShield(gamePanel));
    inventory.add(new BlueShield(gamePanel));
  }

  @Override
  public void speak() {
    super.speak();
    gamePanel.gameState = TRADE_STATE;
    gamePanel.ui.merchantNPC = this;
  }
}
