package aStarPathfindingAlgorithmExample;

import javax.swing.*;

/*
A* algorithm use three numbers -> G cost, H cost, F cost
G cost: The distance between the current node and the start node
H cost: The distance from the current node to the goal node
F cost: The total cost(G + H) of the node (Most important)

How A* algorithm calculates:
 Algorithm evaluates F values -> starting from the lowest, near the starting point
 then checks for G numbers and search from the lowest. If there is no more G number
 Algorithm starts looking for F++;
 If there is for example solid area without F or G value.
 Algorithm expands the search for all nodes that are touching evaluated nodes at this point.
 If there is a node with lower F or G it's selecting that node
*/

public class Main {

  public static void main(String[] args) {

    JFrame window = new JFrame();
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setResizable(false);
    window.add(new DemoPanel());

    window.pack();
    window.setLocationRelativeTo(null);
    window.setVisible(true);
  }
}
