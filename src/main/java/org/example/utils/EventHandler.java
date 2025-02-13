package org.example.utils;

import org.example.GamePanel;
import org.example.enums.DirectionType;
import org.example.enums.GameStateType;

public class EventHandler {

  GamePanel gamePanel;
  EventRect[][] eventRect;

  int previousEventX, previousEventY;
  boolean canTouchEvent = true;

  public EventHandler(GamePanel gamePanel) {
    this.gamePanel = gamePanel;

    eventRect = new EventRect[gamePanel.maxWorldCol][gamePanel.maxWorldRow];
    int col = 0;
    int row = 0;
    while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
      eventRect[col][row] = new EventRect();
      eventRect[col][row].x = 23;
      eventRect[col][row].y = 23;
      eventRect[col][row].width = 2;
      eventRect[col][row].height = 2;
      eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
      eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

      col++;
      if (col == gamePanel.maxWorldCol) {
        col = 0;
        row++;
      }
    }
  }

  public void checkEvent() {
    // Check if the player character is more than 1 tile away from the last event4
    int xDistance = Math.abs(gamePanel.player.worldX - previousEventX);
    int yDistance = Math.abs(gamePanel.player.worldY - previousEventY);
    int distance = Math.max(xDistance, yDistance);

    if (distance > gamePanel.tileSize) {
      canTouchEvent = true;
    }

    if (canTouchEvent) {
      if (hit(27, 16, DirectionType.RIGHT)) damagePit(27, 16, GameStateType.DIALOG_STATE);
      if (hit(23, 19, DirectionType.ANY)) damagePit(23, 19, GameStateType.DIALOG_STATE);
//    if (hit(27, 16, DirectionType.RIGHT.getValue())) teleport(GameStateType.DIALOG_STATE);
      if (hit(23, 12, DirectionType.UP)) healingPool(23, 12, GameStateType.DIALOG_STATE);
    }
  }

  public boolean hit(int col, int row, DirectionType requiredDirection) {
    boolean hit = false;
    gamePanel.player.solidArea.x = gamePanel.player.worldX + gamePanel.player.solidArea.x;
    gamePanel.player.solidArea.y = gamePanel.player.worldY + gamePanel.player.solidArea.y;
    eventRect[col][row].x = col * gamePanel.tileSize + eventRect[col][row].x;
    eventRect[col][row].y = row * gamePanel.tileSize + eventRect[col][row].y;

    if (gamePanel.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].oneTimeEventDone) {
      if (gamePanel.player.direction.equals(requiredDirection) || requiredDirection.equals(DirectionType.ANY))
        hit = true;

      previousEventX = gamePanel.player.worldX;
      previousEventY = gamePanel.player.worldY;
    }

    gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
    gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
    eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
    eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;
    return hit;
  }


  private void damagePit(int col, int row, GameStateType gameState) {
    gamePanel.gameState = gameState;
    gamePanel.ui.currentDialogue = "You fall into a pit!";
    gamePanel.player.currentLife -= 1;
    gamePanel.playSoundEffect(6);
//    eventRect[col][row].eventDone = true;
    canTouchEvent = false;
  }

  private void healingPool(int col, int row, GameStateType gameState) {

    if (gamePanel.keyHandler.enterPressed) {
      gamePanel.gameState = gameState;
      gamePanel.player.attackCancled = true;
      gamePanel.playSoundEffect(2);
      gamePanel.ui.currentDialogue = "You dink the water. \n Your life and mana has been recovered.";
      gamePanel.player.currentLife = gamePanel.player.maxLife;
      gamePanel.player.mana = gamePanel.player.maxMana;
      gamePanel.assetSetter.setMonster();
    }
  }


  private void teleport(GameStateType gameState) {
    gamePanel.gameState = gameState;
    gamePanel.ui.currentDialogue = "You got teleported!";
    gamePanel.player.worldX = gamePanel.tileSize * 37;
    gamePanel.player.worldY = gamePanel.tileSize * 10;

  }
}
