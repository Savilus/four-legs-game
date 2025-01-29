package org.example.object;

import static org.example.config.GameNameFactory.BOOTS;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.enums.GameObjectType;

public class BootsObject extends SuperObject {

  public BootsObject() {
    name = GameObjectType.BOOTS.getValue();
    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(BOOTS)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
