package org.example;

import static org.example.config.GameEntityNameFactory.BACKGROUND_SONG;
import static org.example.config.GameEntityNameFactory.COIN;
import static org.example.config.GameEntityNameFactory.CURSOR;
import static org.example.config.GameEntityNameFactory.CUT_TREE;
import static org.example.config.GameEntityNameFactory.FANFARE;
import static org.example.config.GameEntityNameFactory.FIREBALL_SOUND;
import static org.example.config.GameEntityNameFactory.GAME_OVER;
import static org.example.config.GameEntityNameFactory.HIT_MONSTER;
import static org.example.config.GameEntityNameFactory.LEVEL_UP;
import static org.example.config.GameEntityNameFactory.POWER_UP;
import static org.example.config.GameEntityNameFactory.RECEIVE_DAMAGE;
import static org.example.config.GameEntityNameFactory.SWING_WEAPON;
import static org.example.config.GameEntityNameFactory.UNLOCK;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import io.vavr.control.Try;

public class Sound {

  Clip clip;
  Map<String, URL> soundMap = new HashMap<>(20);
  FloatControl floatControl;
  public int volumeScale = 3;
  float volume;

  public Sound() {
    soundMap.put(BACKGROUND_SONG, Objects.requireNonNull(getClass().getResource(BACKGROUND_SONG)));
    soundMap.put(COIN, Objects.requireNonNull(getClass().getResource(COIN)));
    soundMap.put(POWER_UP, Objects.requireNonNull(getClass().getResource(POWER_UP)));
    soundMap.put(UNLOCK, Objects.requireNonNull(getClass().getResource(UNLOCK)));
    soundMap.put(FANFARE, Objects.requireNonNull(getClass().getResource(FANFARE)));
    soundMap.put(HIT_MONSTER, Objects.requireNonNull(getClass().getResource(HIT_MONSTER)));
    soundMap.put(RECEIVE_DAMAGE, Objects.requireNonNull(getClass().getResource(RECEIVE_DAMAGE)));
    soundMap.put(SWING_WEAPON, Objects.requireNonNull(getClass().getResource(SWING_WEAPON)));
    soundMap.put(LEVEL_UP, Objects.requireNonNull(getClass().getResource(LEVEL_UP)));
    soundMap.put(CURSOR, Objects.requireNonNull(getClass().getResource(CURSOR)));
    soundMap.put(FIREBALL_SOUND, Objects.requireNonNull(getClass().getResource(FIREBALL_SOUND)));
    soundMap.put(CUT_TREE, Objects.requireNonNull(getClass().getResource(CUT_TREE)));
    soundMap.put(GAME_OVER, Objects.requireNonNull(getClass().getResource(GAME_OVER)));
  }

  public Void setFile(String sound) {
    return Try.run(() -> {
      AudioInputStream audio = AudioSystem.getAudioInputStream(soundMap.get(sound));
      clip = AudioSystem.getClip();
      clip.open(audio);
      floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      checkVolume();
    }).getOrElseThrow(e -> new IllegalArgumentException("Error during getting file: " + e.getMessage()));

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
