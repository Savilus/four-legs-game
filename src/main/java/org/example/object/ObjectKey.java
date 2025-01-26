package org.example.object;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class ObjectKey extends SuperObject {

  public ObjectKey() {
    name = "Key";
    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream()))
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
