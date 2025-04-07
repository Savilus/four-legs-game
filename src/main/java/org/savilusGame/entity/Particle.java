package org.savilusGame.entity;

import java.awt.*;

import org.savilusGame.GamePanel;

public class Particle extends GameEntity {

  GameEntity generator;
  Color color;
  int size;
  int xd;
  int yd;

  public Particle(GamePanel gamePanel, GameEntity generator, Color color, int size, int speed, int maxLife, int xd, int yd) {
    super(gamePanel);
    this.generator = generator;
    this.color = color;
    this.size = size;
    this.speed = speed;
    this.maxLife = maxLife;
    this.xd = xd;
    this.yd = yd;

    currentLife = maxLife;
    int offset = (gamePanel.tileSize / 2) - (size / 2);
    worldX = generator.worldX + offset;
    worldY = generator.worldY + offset;
  }

  @Override
  public void update() {
    currentLife--;

    if (currentLife < maxLife / 3) {
      yd++;
    }
    worldX += xd * speed;
    worldY += yd * speed;
    if (currentLife == 0)
      alive = false;

  }

  @Override
  public void draw(Graphics2D graphics2D) {
    int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
    int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;

    graphics2D.setColor(color);
    graphics2D.fillRect(screenX, screenY, size, size);
  }
}
