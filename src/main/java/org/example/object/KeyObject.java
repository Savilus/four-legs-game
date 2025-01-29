package org.example.object;

import static org.example.config.GameNameFactory.KEY;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.enums.GameObjectType;

public class KeyObject extends SuperObject {

  public KeyObject() {
    name = GameObjectType.KEY.getValue();
    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(KEY)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
