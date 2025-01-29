package org.example.object;

import static org.example.config.GameNameFactory.KEY;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.GamePanel;
import org.example.enums.GameObjectType;

public class KeyObject extends SuperObject {

  public KeyObject(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.KEY.getValue();
    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(KEY)));
      utilityTool.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
