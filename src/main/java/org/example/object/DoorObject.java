package org.example.object;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

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
