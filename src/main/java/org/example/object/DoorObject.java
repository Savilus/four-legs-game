package org.example.object;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class DoorObject extends SuperObject {

  public DoorObject() {
    name = "Door";
    collision = true;

    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/door.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
