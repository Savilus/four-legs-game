package org.example.environment;

import java.awt.*;

import org.example.GamePanel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnvironmentManager {

  private final GamePanel gamePanel;
  public Lighting lighting;

  public void setup() {
    lighting = new Lighting(gamePanel);
  }

  public void update() {
    lighting.update();
  }

  public void draw(Graphics2D graphics2D) {
    lighting.draw(graphics2D);
  }
}
