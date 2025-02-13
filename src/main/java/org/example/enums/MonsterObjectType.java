package org.example.enums;

import static org.example.utils.IdEnumManager.getIdForEnum;

import lombok.Getter;

@Getter
public enum MonsterObjectType {
  GREEN_SLIME("Green Slime");

  private final String name;
  private final int monsterTypeId;

  MonsterObjectType(String name) {
    this.name = name;
    this.monsterTypeId = getIdForEnum(this);
  }
}
