package org.example.environment;

import java.awt.*;

import org.example.GamePanel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnvironmentManager {

  private final GamePanel gamePanel;
  private Lighting lighting;

  public void setup() {
    lighting = new Lighting(gamePanel, 450);
  }

  public void draw(Graphics2D graphics2D) {
    lighting.draw(graphics2D);
  }
}
