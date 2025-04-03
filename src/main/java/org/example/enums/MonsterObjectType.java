package org.example.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MonsterObjectType {
  GREEN_SLIME("Green Slime"),
  RED_SLIME("Red Slime"),
  ORC("Orc"),
  BAT("Bat"),
  SKELETON_LORD("Skeleton Lord");

  private final String name;

}
