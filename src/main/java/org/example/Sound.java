package org.example;

import static org.example.config.GameEntityNameFactory.BLOCKED;
import static org.example.config.GameEntityNameFactory.CHIP_WALL;
import static org.example.config.GameEntityNameFactory.COIN;
import static org.example.config.GameEntityNameFactory.CURSOR;
import static org.example.config.GameEntityNameFactory.DESTROY_TREE;
import static org.example.config.GameEntityNameFactory.DOOR_OPEN;
import static org.example.config.GameEntityNameFactory.DUNGEON_SONG;
import static org.example.config.GameEntityNameFactory.FANFARE;
import static org.example.config.GameEntityNameFactory.FIREBALL_SOUND;
import static org.example.config.GameEntityNameFactory.GAME_OVER;
import static org.example.config.GameEntityNameFactory.HIT_MONSTER;
import static org.example.config.GameEntityNameFactory.LEVEL_UP;
import static org.example.config.GameEntityNameFactory.MERCHANT_SONG;
import static org.example.config.GameEntityNameFactory.OUTSIDE_MUSIC;
import static org.example.config.GameEntityNameFactory.PARRY;
import static org.example.config.GameEntityNameFactory.POWER_UP;
import static org.example.config.GameEntityNameFactory.RECEIVE_DAMAGE;
import static org.example.config.GameEntityNameFactory.SLEEP;
import static org.example.config.GameEntityNameFactory.SPEAK;
import static org.example.config.GameEntityNameFactory.STAIRS;
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
import lombok.Getter;
import lombok.Setter;

public class Sound {

  private Clip clip;
  private final Map<String, URL> soundMap = new HashMap<>(20);
  private FloatControl floatControl;
  @Getter
  @Setter
  private int volumeScale = 3;
  private float volume;

  public Sound() {
    soundMap.put(OUTSIDE_MUSIC, Objects.requireNonNull(getClass().getResource(OUTSIDE_MUSIC)));
    soundMap.put(MERCHANT_SONG, Objects.requireNonNull(getClass().getResource(MERCHANT_SONG)));
    soundMap.put(DUNGEON_SONG, Objects.requireNonNull(getClass().getResource(DUNGEON_SONG)));
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
    soundMap.put(DESTROY_TREE, Objects.requireNonNull(getClass().getResource(DESTROY_TREE)));
    soundMap.put(GAME_OVER, Objects.requireNonNull(getClass().getResource(GAME_OVER)));
    soundMap.put(STAIRS, Objects.requireNonNull(getClass().getResource(STAIRS)));
    soundMap.put(SLEEP, Objects.requireNonNull(getClass().getResource(SLEEP)));
    soundMap.put(BLOCKED, Objects.requireNonNull(getClass().getResource(BLOCKED)));
    soundMap.put(PARRY, Objects.requireNonNull(getClass().getResource(PARRY)));
    soundMap.put(SPEAK, Objects.requireNonNull(getClass().getResource(SPEAK)));
    soundMap.put(CHIP_WALL, Objects.requireNonNull(getClass().getResource(CHIP_WALL)));
    soundMap.put(DOOR_OPEN, Objects.requireNonNull(getClass().getResource(DOOR_OPEN)));
  }

  protected void setFile(String sound) {
    Try.run(() -> {
      AudioInputStream audio = AudioSystem.getAudioInputStream(soundMap.get(sound));
      clip = AudioSystem.getClip();
      clip.open(audio);
      floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
      checkVolume();
    }).getOrElseThrow(e -> new IllegalArgumentException("Error during getting file: " + e.getMessage()));

  }

  protected void play() {
    clip.start();
  }

  protected void loop() {
    clip.loop(Clip.LOOP_CONTINUOUSLY);
  }

  protected void stop() {
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
