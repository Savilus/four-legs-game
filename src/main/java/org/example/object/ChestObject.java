package org.example.object;

import static org.example.config.GameNameFactory.CHEST;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.example.enums.GameObjectType;

public class ChestObject extends SuperObject {

  public ChestObject() {
    name = GameObjectType.CHEST.getValue();
    try {
      image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(CHEST)));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
