package org.example.object;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class BootsObject extends SuperObject {

  public BootsObject() {
    name = "Boots";
    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/boots.png")));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
