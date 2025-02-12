package org.example;

import static org.example.config.GameEntityNameFactory.BACKGROUND_SONG;
import static org.example.config.GameEntityNameFactory.COIN;
import static org.example.config.GameEntityNameFactory.FANFARE;
import static org.example.config.GameEntityNameFactory.HIT_MONSTER;
import static org.example.config.GameEntityNameFactory.LEVEL_UP;
import static org.example.config.GameEntityNameFactory.POWER_UP;
import static org.example.config.GameEntityNameFactory.RECEIVE_DAMAGE;
import static org.example.config.GameEntityNameFactory.SWING_WEAPON;
import static org.example.config.GameEntityNameFactory.UNLOCK;

import java.net.URL;
import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

  Clip clip;
  URL[] soundUrl = new URL[30];

  public Sound() {
    soundUrl[0] = Objects.requireNonNull(getClass().getResource(BACKGROUND_SONG));
    soundUrl[1] = Objects.requireNonNull(getClass().getResource(COIN));
    soundUrl[2] = Objects.requireNonNull(getClass().getResource(POWER_UP));
    soundUrl[3] = Objects.requireNonNull(getClass().getResource(UNLOCK));
    soundUrl[4] = Objects.requireNonNull(getClass().getResource(FANFARE));
    soundUrl[5] = Objects.requireNonNull(getClass().getResource(HIT_MONSTER));
    soundUrl[6] = Objects.requireNonNull(getClass().getResource(RECEIVE_DAMAGE));
    soundUrl[7] = Objects.requireNonNull(getClass().getResource(SWING_WEAPON));
    soundUrl[8] = Objects.requireNonNull(getClass().getResource(LEVEL_UP));
  }

  public void setFile(int i) {
    try {
      AudioInputStream audio = AudioSystem.getAudioInputStream(soundUrl[i]);
      clip = AudioSystem.getClip();
      clip.open(audio);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void play() {
    clip.start();
  }

  public void loop() {
    clip.loop(Clip.LOOP_CONTINUOUSLY);
  }

  public void stop() {
    clip.stop();
  }
}
