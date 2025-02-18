package org.example;

import static org.example.config.GameEntityNameFactory.BACKGROUND_SONG;
import static org.example.config.GameEntityNameFactory.COIN;
import static org.example.config.GameEntityNameFactory.CURSOR;
import static org.example.config.GameEntityNameFactory.CUT_TREE;
import static org.example.config.GameEntityNameFactory.FANFARE;
import static org.example.config.GameEntityNameFactory.FIREBALL_SOUND;
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
import javax.sound.sampled.FloatControl;

public class Sound {

  Clip clip;
  URL[] soundUrl = new URL[30];
  FloatControl floatControl;
  public int volumeScale = 3;
  float volume;

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
    soundUrl[9] = Objects.requireNonNull(getClass().getResource(CURSOR));
    soundUrl[10] = Objects.requireNonNull(getClass().getResource(FIREBALL_SOUND));
    soundUrl[11] = Objects.requireNonNull(getClass().getResource(CUT_TREE));
  }

  public void setFile(int i) {
    try {
      AudioInputStream audio = AudioSystem.getAudioInputStream(soundUrl[i]);
      clip = AudioSystem.getClip();
      clip.open(audio);
      floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      checkVolume();
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

  public void checkVolume() {
    switch (volumeScale) {
      case 0 -> volume = -80f;
      case 1 -> volume = -20f;
      case 2 -> volume = -12f;
      case 3 -> volume = -5f;
      case 4 -> volume = 1f;
      case 5 -> volume = 6f;
    }

    floatControl.setValue(volume);
  }
}
