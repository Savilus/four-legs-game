package org.example.environment;

import static java.awt.Color.WHITE;
import static org.example.enums.DayState.DAWN;
import static org.example.enums.DayState.DAY;
import static org.example.enums.DayState.DUSK;
import static org.example.enums.DayState.NIGHT;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.example.GamePanel;
import org.example.enums.DayState;
import org.example.utils.text.TextManager;

public class Lighting {

  private static final String DAY_STATUS_KEY = "dayStatus";
  private static final String DAY_KEY = "day";
  private static final String DUSK_KEY = "dusk";
  private static final String NIGHT_KEY = "night";
  private static final String DAWN_KEY = "dawn";

  GamePanel gamePanel;
  BufferedImage darknessFilter;
  public int dayCounter;
  public float filterAlpha = 0f;
  public DayState dayState = DAY;

  public Lighting(GamePanel gamePanel) {
    this.gamePanel = gamePanel;
    setLightSource();
  }

  public void setLightSource() {
    darknessFilter = new BufferedImage(gamePanel.screenWidth, gamePanel.screenHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics2D = (Graphics2D) darknessFilter.getGraphics();

    if (Objects.isNull(gamePanel.player.currentLightItem)) {
      graphics2D.setColor(new Color(0, 0, 0.1f, 0.95f));
    } else {
      // Get the center x and Y of the light circle
      int centerX = gamePanel.player.screenX + (gamePanel.tileSize / 2);
      int centerY = gamePanel.player.screenY + (gamePanel.tileSize / 2);

      // Create a gradation effect within the light circle;
      Color[] color = new Color[12];
      float[] fraction = new float[12];

      color[0] = new Color(0, 0, 0.1f, 0.1F);
      color[1] = new Color(0, 0, 0.1f, 0.42F);
      color[2] = new Color(0, 0, 0.1f, 0.52F);
      color[3] = new Color(0, 0, 0.1f, 0.61F);
      color[4] = new Color(0, 0, 0.1f, 0.69F);
      color[5] = new Color(0, 0, 0.1f, 0.76F);
      color[6] = new Color(0, 0, 0.1f, 0.82F);
      color[7] = new Color(0, 0, 0.1f, 0.87F);
      color[8] = new Color(0, 0, 0.1f, 0.91F);
      color[9] = new Color(0, 0, 0.1f, 0.92F);
      color[10] = new Color(0, 0, 0.1f, 0.93F);
      color[11] = new Color(0, 0, 0.1f, 0.94F);

      fraction[0] = 0f;
      fraction[1] = 0.4f;
      fraction[2] = 0.5f;
      fraction[3] = 0.6f;
      fraction[4] = 0.65f;
      fraction[5] = 0.7f;
      fraction[6] = 0.75f;
      fraction[7] = 0.8f;
      fraction[8] = 0.85f;
      fraction[9] = 0.9f;
      fraction[10] = 0.95f;
      fraction[11] = 1f;

      // Create a gradation paint setting for the light circle
      RadialGradientPaint gradientPaint = new RadialGradientPaint(centerX, centerY, ((float) gamePanel.player.currentLightItem.lightRadius / 2), fraction, color);

      // Set the gradient data on graphics2D and draw light circle
      graphics2D.setPaint(gradientPaint);
    }

    graphics2D.fillRect(0, 0, gamePanel.screenWidth, gamePanel.screenHeight);
    graphics2D.dispose();
  }

  public void resetDay() {
    dayState = DAY;
    filterAlpha = 0f;
  }

  public void update() {
    if (gamePanel.player.lightUpdated) {
      setLightSource();
      gamePanel.player.lightUpdated = false;
    }

    // Check the state of the day
    switch (dayState) {
      case DAY -> {
        dayCounter++;
        if (dayCounter > 6000) {
          dayState = DUSK;
          dayCounter = 0;
        }
      }
      case DUSK -> {
        filterAlpha += 0.001F;
        if (filterAlpha > 1) {
          filterAlpha = 1F;
          dayState = NIGHT;
        }
      }
      case NIGHT -> {
        dayCounter++;
        if (dayCounter >= 6000) {
          dayState = DAWN;
          dayCounter = 0;
        }
      }
      case DAWN -> {
        filterAlpha -= 0.001F;
        if (filterAlpha < 0) {
          filterAlpha = 0F;
          dayState = DAY;
        }
      }
    }
  }

  public void draw(Graphics2D graphics2D) {
    graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
    graphics2D.drawImage(darknessFilter, 0, 0, null);
    graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));

    // DEBUG
    String dayStateDebug = StringUtils.EMPTY;
    switch (dayState) {
      case DAY -> dayStateDebug = TextManager.getUiText(DAY_STATUS_KEY, DAY_KEY);
      case DUSK -> dayStateDebug = TextManager.getUiText(DAY_STATUS_KEY, DUSK_KEY);
      case NIGHT -> dayStateDebug = TextManager.getUiText(DAY_STATUS_KEY, NIGHT_KEY);
      case DAWN -> dayStateDebug = TextManager.getUiText(DAY_STATUS_KEY, DAWN_KEY);
    }

    graphics2D.setColor(WHITE);
    graphics2D.setFont(graphics2D.getFont().deriveFont(50F));
    graphics2D.drawString(dayStateDebug, 800, 500);
  }
}
