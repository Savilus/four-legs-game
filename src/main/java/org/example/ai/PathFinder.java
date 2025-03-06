package org.example.ai;

import java.util.ArrayList;
import java.util.Objects;

import org.example.GamePanel;

public class PathFinder {

  GamePanel gamePanel;
  Node[][] node;
  ArrayList<Node> openList = new ArrayList<>();
  public ArrayList<Node> pathList = new ArrayList<>();
  Node startNode, goalNode, currentNode;
  boolean goalReached = false;
  int step = 0;

  public PathFinder(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    instantiateNodes();
  }

  public void instantiateNodes() {
    node = new Node[gamePanel.maxWorldCol][gamePanel.maxWorldRow];

    int col = 0;
    int row = 0;

    while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
      node[col][row] = new Node(col, row);
      col++;
      if (col == gamePanel.maxWorldCol) {
        col = 0;
        row++;
      }
    }
  }

  public void resetNodes() {
    int col = 0;
    int row = 0;

    while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
      // Reset open, checked and solid state
      node[col][row].open = false;
      node[col][row].checked = false;
      node[col][row].solid = false;

      col++;
      if (col == gamePanel.maxWorldCol) {
        col = 0;
        row++;
      }
    }

    // Reset other settings
    openList.clear();
    pathList.clear();
    goalReached = false;
    step = 0;
  }

  public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
    resetNodes();

    // Set start and goal node
    startNode = node[startCol][startRow];
    currentNode = startNode;
    goalNode = node[goalCol][goalRow];
    openList.add(currentNode);

    int col = 0;
    int row = 0;

    while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
      //SET SOLID NODE
      // CHECK TILES
      int tileNum = gamePanel.tileManager.gameMaps.get(gamePanel.tileManager.currentMap)[col][row];
      if (gamePanel.tileManager.tile[tileNum].collision()) {
        node[col][row].solid = true;
      }
      // CHECK INTERACTIVE TILES
      for (int i = 0; i < gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap).length; i++) {
        if (Objects.nonNull(gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[i]) &&
            gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[i].destructible) {
          int itCol = gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[i].worldX / gamePanel.tileSize;
          int itRow = gamePanel.mapsInteractiveTiles.get(gamePanel.tileManager.currentMap)[i].worldY / gamePanel.tileSize;
          node[itCol][itRow].solid = true;
        }
      }

      // SET COST
      getCost(node[col][row]);

      col++;
      if (col == gamePanel.maxWorldCol) {
        col = 0;
        row++;
      }
    }
  }

  public boolean search() {
    while (!goalReached && step < 500) {
      int col = currentNode.col;
      int row = currentNode.row;

      // Check the current node
      currentNode.checked = true;
      openList.remove(currentNode);

      // OPEN THE UP NODE
      if (row - 1 >= 0)
        openNode(node[col][row - 1]);
      // OPEN THE LEFT NODE
      if (col - 1 >= 0)
        openNode(node[col - 1][row]);
      // OPEN THE DOWN NODE
      if (row + 1 < gamePanel.maxWorldRow)
        openNode(node[col][row + 1]);
      // OPEN THE RIGHT NODE
      if (col + 1 < gamePanel.maxWorldCol)
        openNode(node[col + 1][row]);

      // Find the best node
      int bestNodeIndex = 0;
      int bestNodeCost = 999;

      for (int nodeIndex = 0; nodeIndex < openList.size(); nodeIndex++) {
        // Check if this node's F cost is better
        if (openList.get(nodeIndex).fCost < bestNodeCost) {
          bestNodeIndex = nodeIndex;
          bestNodeCost = openList.get(nodeIndex).fCost;
        }
        // If F cost is equal, check the G cost
        else if (openList.get(nodeIndex).fCost == bestNodeCost) {
          if (openList.get(nodeIndex).gCost < openList.get(bestNodeIndex).gCost) {
            bestNodeIndex = nodeIndex;
          }
        }
      }

      if (openList.isEmpty()) {
        break;
      }

      currentNode = openList.get(bestNodeIndex);

      if (currentNode == goalNode) {
        goalReached = true;
        trackThePath();
      }
      step++;
    }

    return goalReached;
  }

  private void trackThePath() {
    Node current = goalNode;

    while (current != startNode) {
      pathList.addFirst(current);
      current = current.parent;
    }
  }

  private void openNode(Node node) {
    if (!node.open && !node.checked && !node.solid) {
      node.open = true;
      node.parent = currentNode;
      openList.add(node);
    }
  }

  private void getCost(Node node) {
    // G cost
    int xDistance = Math.abs(node.col - startNode.col);
    int yDistance = Math.abs(node.row - startNode.row);
    node.gCost = xDistance + yDistance;
    // H cost
    xDistance = Math.abs(node.col - goalNode.col);
    yDistance = Math.abs(node.row - goalNode.row);
    node.hCost = xDistance + yDistance;
    // F cost
    node.fCost = node.gCost + node.hCost;
  }
}
