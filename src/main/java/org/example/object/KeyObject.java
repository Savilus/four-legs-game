package org.example.object;

import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

public class KeyObject extends SuperObject {

    public KeyObject() {
        name = "Key";
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/objects/key.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
