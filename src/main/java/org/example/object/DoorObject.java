package org.example.object;

import static org.example.config.GameNameFactory.DOOR;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.GamePanel;
import org.example.enums.GameObjectType;

public class DoorObject extends SuperObject {

  public DoorObject(GamePanel gamePanel) {
    super(gamePanel);
    name = GameObjectType.DOOR.toString();
    collision = true;

    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(DOOR)));
      utilityTool.scaleImage(image, gamePanel.tileSize, gamePanel.tileSize);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
