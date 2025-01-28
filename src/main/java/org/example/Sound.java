package org.example;

import java.net.URL;
import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

  Clip clip;
  URL[] soundUrl = new URL[30];

  public Sound() {
    soundUrl[0] = Objects.requireNonNull(getClass().getResource("/sound/BlueBoyAdventure.wav"));
    soundUrl[1] = Objects.requireNonNull(getClass().getResource("/sound/coin.wav"));
    soundUrl[2] = Objects.requireNonNull(getClass().getResource("/sound/powerup.wav"));
    soundUrl[3] = Objects.requireNonNull(getClass().getResource("/sound/unlock.wav"));
    soundUrl[4] = Objects.requireNonNull(getClass().getResource("/sound/fanfare.wav"));
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
