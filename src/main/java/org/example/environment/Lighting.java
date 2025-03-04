package org.example.environment;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.example.GamePanel;

public class Lighting {

  GamePanel gamePanel;
  BufferedImage darknessFilter;

  public Lighting(GamePanel gamePanel, int circleSize) {
    this.gamePanel = gamePanel;
    darknessFilter = new BufferedImage(gamePanel.screenWidth, gamePanel.screenHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics2D = (Graphics2D) darknessFilter.getGraphics();

    Area screenArea = new Area(new Rectangle2D.Double(0, 0, gamePanel.screenWidth, gamePanel.screenHeight));

    // Get the center x and Y of the light circle
    int centerX = gamePanel.player.screenX + (gamePanel.tileSize / 2);
    int centerY = gamePanel.player.screenY + (gamePanel.tileSize / 2);
    // Get the top left x and y of the light circle
    double x = centerX - (circleSize / 2);
    double y = centerY - (circleSize / 2);
    // Create a light circle shape
    Shape circleShape = new Ellipse2D.Double(x, y, circleSize, circleSize);
    // Create a light circle area
    Area lightArea = new Area(circleShape);
    // Subtract the light circle from the screen rectangle
    screenArea.subtract(lightArea);

    // Create a gradation effect within the light circle;
    Color[] color = new Color[12];
    float[] fraction = new float[12];

    color[0] = new Color(0, 0, 0, 0.1F);
    color[1] = new Color(0, 0, 0, 0.42F);
    color[2] = new Color(0, 0, 0, 0.52F);
    color[3] = new Color(0, 0, 0, 0.61F);
    color[4] = new Color(0, 0, 0, 0.69F);
    color[5] = new Color(0, 0, 0, 0.76F);
    color[6] = new Color(0, 0, 0, 0.82F);
    color[7] = new Color(0, 0, 0, 0.87F);
    color[8] = new Color(0, 0, 0, 0.91F);
    color[9] = new Color(0, 0, 0, 0.92F);
    color[10] = new Color(0, 0, 0, 0.93F);
    color[11] = new Color(0, 0, 0, 0.94F);

    fraction[0] = 0f;
    fraction[1] = 0.4f;
    fraction[2] = 0.5f;
    fraction[3] = 0.6f;
    fraction[4] = 0.65f;
    fraction[5] = 0.7f;
    fraction[6] = 0.75f;
    fraction[7] = 0.8f;
    fraction[8] = 0.85f;
    fraction[9] = 0.9f;
    fraction[10] = 0.95f;
    fraction[11] = 1f;

    // Create a gradation paint setting for the light circle
    RadialGradientPaint gradientPaint = new RadialGradientPaint(centerX, centerY, (circleSize / 2), fraction, color);

    // Set the gradient data on graphics2D and draw light circle
    graphics2D.setPaint(gradientPaint);
    graphics2D.fill(lightArea);

    // Draw the screen rectangle without the light circle area
    graphics2D.fill(screenArea);
    graphics2D.dispose();
  }

  public void draw(Graphics2D graphics2D) {
    graphics2D.drawImage(darknessFilter, 0, 0, null);
  }
}
