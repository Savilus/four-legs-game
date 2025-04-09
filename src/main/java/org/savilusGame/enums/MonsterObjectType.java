package org.savilusGame.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public enum MonsterObjectType {
  GREEN_SLIME("Green Slime"),
  RED_SLIME("Red Slime"),
  ORC("Orc"),
  BAT("Bat"),
  SKELETON_LORD("Skeleton Lord", "skeletonLord");

  private final String name;
  private String dialogueKey;

}
