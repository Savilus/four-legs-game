package org.example.ai;

import lombok.Getter;

@Getter
public class Node {

  Node parent;
  int col;
  int row;
  int gCost;
  int hCost;
  int fCost;
  boolean solid;
  boolean open;
  boolean checked;

  public Node(int col, int row) {
    this.col = col;
    this.row = row;
  }
}
