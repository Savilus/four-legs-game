package aStarPathfindingAlgorithmExample;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

class Node extends JButton implements ActionListener {

  Node parent;
  int col;
  int row;
  int gCost;
  int hCost;
  int fCost;
  boolean start;
  boolean goal;
  boolean solid;
  boolean open;
  boolean checked;

  Node(int col, int row) {
    this.col = col;
    this.row = row;

    setBackground(Color.WHITE);
    setForeground(Color.BLACK);
    addActionListener(this);
  }

  void setAsOpen() {
    open = true;
  }

  void setAsChecked() {
    if (!start && !goal) {
      setBackground(Color.ORANGE);
      setForeground(Color.BLACK);
    }
    checked = true;
  }

  void setAsPath() {
    setBackground(Color.GREEN);
    setForeground(Color.BLACK);
  }

  void setAsSolid() {
    setBackground(Color.BLACK);
    setForeground(Color.BLACK);
    solid = true;
  }

  void setAsStart() {
    setBackground(Color.BLUE);
    setForeground(Color.WHITE);
    setText("Start");
    start = true;
  }

  void setAsGoal() {
    setBackground(Color.YELLOW);
    setForeground(Color.BLACK);
    setText("Goal");
    goal = true;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    setBackground(Color.ORANGE);
  }
}
