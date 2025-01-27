package org.example.object;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class ChestObject extends SuperObject {

  public ChestObject() {
    name = "Chest";
    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/chest.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
