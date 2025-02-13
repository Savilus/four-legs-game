package org.example.enums;

import static org.example.utils.IdEnumManager.getIdForEnum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterType {

  PLAYER("player"),
  NPC("npc");

  private final String name;
  private final int characterTypeId;

  CharacterType(String name) {
    this.name = name;
    this.characterTypeId = getIdForEnum(this);
  }
}
