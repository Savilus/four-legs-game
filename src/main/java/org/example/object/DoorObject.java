package org.example.object;

import static org.example.config.GameNameFactory.DOOR;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.enums.GameObjectType;

public class DoorObject extends SuperObject {

  public DoorObject() {
    name = GameObjectType.DOOR.toString();
    collision = true;

    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(DOOR)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
