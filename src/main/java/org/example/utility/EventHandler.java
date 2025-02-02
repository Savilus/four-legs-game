package org.example.utility;

import java.awt.*;

import org.example.GamePanel;
import org.example.enums.DirectionType;
import org.example.enums.GameStateType;

public class EventHandler {

  GamePanel gamePanel;
  Rectangle eventRect;
  int eventRectDefaultX, eventRectDefaultY;

  public EventHandler(GamePanel gamePanel) {
    this.gamePanel = gamePanel;

    eventRect = new Rectangle();
    eventRect.x = 23;
    eventRect.y = 23;
    eventRect.width = 2;
    eventRect.height = 2;
    eventRectDefaultX = eventRect.x;
    eventRectDefaultY = eventRect.y;
  }

  public void checkEvent() {

//    if (hit(27, 16, DirectionType.RIGHT.getValue())) damagePit(GameStateType.DIALOG_STATE);
    if (hit(27, 16, DirectionType.RIGHT.getValue())) teleport(GameStateType.DIALOG_STATE);
    if (hit(23, 12, DirectionType.UP.getValue())) healingPool(GameStateType.DIALOG_STATE);


  }

  public boolean hit(int eventCol, int eventRow, String requiredDirection) {
    boolean hit = false;
    gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
    gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;
    eventRect.x = eventCol * gamePanel.tileSize + eventRect.x;
    eventRect.y = eventRow * gamePanel.tileSize + eventRect.y;

    if (gamePanel.player.solidArea.intersects(eventRect)) {
      if (gamePanel.player.direction.contentEquals(requiredDirection) || requiredDirection.contentEquals(DirectionType.ANY.getValue()))
        hit = true;
    }

    gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
    gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
    eventRect.x = eventRectDefaultX;
    eventRect.y = eventRectDefaultY;
    return hit;
  }


  private void damagePit(GameStateType gameState) {
    gamePanel.gameState = gameState;
    gamePanel.ui.currentDialogue = "You fall into a pit!";
    gamePanel.player.currentLife -= 1;
  }

  private void healingPool(GameStateType gameState) {

    if (gamePanel.keyHandler.enterPressed) {
      gamePanel.gameState = gameState;
      gamePanel.ui.currentDialogue = "You dink the water. \n Your life has been recovered.";
      gamePanel.player.currentLife = gamePanel.player.maxLife;
    }
  }


  private void teleport(GameStateType gameState) {
    gamePanel.gameState = gameState;
    gamePanel.ui.currentDialogue = "You got teleported!";
    gamePanel.player.worldX = gamePanel.tileSize * 37;
    gamePanel.player.worldY = gamePanel.tileSize * 10;

  }
}
