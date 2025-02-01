package org.example.object;

import static org.example.config.GameNameFactory.HEART_BLANK;
import static org.example.config.GameNameFactory.HEART_FULL;
import static org.example.config.GameNameFactory.HEART_HALF;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.GamePanel;
import org.example.enums.GameObjectType;

public class HeartObject extends SuperObject {

  public HeartObject(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.HEART.getValue();
    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(HEART_FULL)));
      image2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(HEART_HALF)));
      image3 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(HEART_BLANK)));
      image = utilityTool.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);
      image2 = utilityTool.scaleImage(image2, gamePanel.tileSize, gamePanel.tileSize);
      image3 = utilityTool.scaleImage(image3, gamePanel.tileSize, gamePanel.tileSize);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
