package org.example.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MonsterObjectType {
  GREEN_SLIME("Green Slime");

  private final String name;

}
