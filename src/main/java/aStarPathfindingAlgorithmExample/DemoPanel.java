package aStarPathfindingAlgorithmExample;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

class DemoPanel extends JPanel {

  // Displaying "Nodes" that represent Tiles in game

  //SCREEN SETTINGS
  final int maxCol = 15;
  final int maxRow = 10;
  final int nodeSize = 70;
  final int screenWidth = nodeSize * maxCol;
  final int screenHeight = nodeSize * maxRow;

  // NODE
  Node[][] node = new Node[maxCol][maxRow];
  Node startNode, goalNode, currentNode;
  ArrayList<Node> openList = new ArrayList<>();
  ArrayList<Node> checkedList = new ArrayList<>();

  // OTHERS
  boolean goealReached = false;
  int step = 0;

  DemoPanel() {
    this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    this.setBackground(Color.BLACK);
    this.setLayout(new GridLayout(maxRow, maxCol));
    this.addKeyListener(new KeyHandler(this));
    this.setFocusable(true);

    // PLACE NODES
    int col = 0;
    int row = 0;

    while (col < maxCol && row < maxRow) {
      node[col][row] = new Node(col, row);
      this.add(node[col][row]);

      col++;

      if (col == maxCol) {
        col = 0;
        row++;
      }
    }

    // SET START AND GOAL NODE
    setStartNode(3, 6);
    setGoalNode(11, 3);

    // SET SOLID NODE
    setSolidNode(10, 2);
    setSolidNode(10, 3);
    setSolidNode(10, 4);
    setSolidNode(10, 5);
    setSolidNode(10, 6);
    setSolidNode(10, 7);
    setSolidNode(6, 2);
    setSolidNode(7, 2);
    setSolidNode(8, 2);
    setSolidNode(9, 2);
    setSolidNode(11, 7);
    setSolidNode(12, 7);
    setSolidNode(6, 1);

    // SET COST
    setCostOnNodes();

  }

  private void searchStep() {
    int col = currentNode.col;
    int row = currentNode.row;

    currentNode.setAsChecked();
    checkedList.add(currentNode);
    openList.remove(currentNode);

    // Open neighboring nodes
    if (row - 1 >= 0) openNode(node[col][row - 1]); // UP
    if (col - 1 >= 0) openNode(node[col - 1][row]); // LEFT
    if (row + 1 < maxRow) openNode(node[col][row + 1]); // DOWN
    if (col + 1 < maxCol) openNode(node[col + 1][row]); // RIGHT

    // Find the best node based on fCost (and gCost in case of ties)
    int bestNodeIndex = 0;
    int bestNodeCost = 999;

    for (int i = 0; i < openList.size(); i++) {
      int currentCost = openList.get(i).fCost;
      if (currentCost < bestNodeCost) {
        bestNodeIndex = i;
        bestNodeCost = currentCost;
      } else if (currentCost == bestNodeCost) {
        if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
          bestNodeIndex = i;
        }
      }
    }

    // After finding the best node, set it as the current node
    currentNode = openList.get(bestNodeIndex);

    // Check if we've reached the goal
    if (currentNode == goalNode) {
      goealReached = true;
      trackThePath();
    }
  }

  void autoSearch() {
    while (!goealReached && step < 300) {
      searchStep();
      step++;
    }
  }

  void manualSearch() {
    if (!goealReached) {
      searchStep();
    }
  }

  private void setCostOnNodes() {
    int col = 0;
    int row = 0;

    while (col < maxCol && row < maxRow) {
      getCost(node[col][row]);
      col++;
      if (col == maxCol) {
        col = 0;
        row++;
      }
    }
  }

  private void getCost(Node node) {
    // GET G COST (The distance from the start node)
    int xDistance = Math.abs(node.col - startNode.col);
    int yDistance = Math.abs(node.row - startNode.row);
    node.gCost = xDistance + yDistance;

    // GET H COST (The distance from the goal node)
    xDistance = Math.abs(node.col - goalNode.col);
    yDistance = Math.abs(node.row - goalNode.row);
    node.hCost = xDistance + yDistance;

    // GET F COST (The total cost)
    node.fCost = node.gCost + node.hCost;

    if (node != startNode && node != goalNode) {
      node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "</html>");
    }
  }

  private void openNode(Node node) {
    if (!node.open && !node.checked && !node.solid) {
      // If the node is not opened yet, add it to the open list
      node.setAsOpen();
      node.parent = currentNode;
      openList.add(node);
    }
  }

  private void setStartNode(int col, int row) {
    node[col][row].setAsStart();
    startNode = node[col][row];
    currentNode = startNode;
  }

  private void setGoalNode(int col, int row) {
    node[col][row].setAsGoal();
    goalNode = node[col][row];
  }

  private void setSolidNode(int col, int row) {
    node[col][row].setAsSolid();
  }

  private void trackThePath() {

    // Backtrack and draw the best path
    Node current = goalNode;

    while (current != startNode) {
      current = current.parent;

      if (current != startNode) {
        current.setAsPath();
      }
    }
  }
}
