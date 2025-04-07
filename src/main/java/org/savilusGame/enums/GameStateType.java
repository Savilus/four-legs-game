package org.savilusGame.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameStateType {
  TITLE_STATE,
  PLAY_STATE,
  PAUSE_STATE,
  DIALOG_STATE,
  CHARACTER_STATE,
  OPTIONS_STATE,
  GAME_OVER_STATE,
  TRANSITION_STATE,
  TRADE_STATE,
  SLEEP_STATE,
  MAP_STATE

}
