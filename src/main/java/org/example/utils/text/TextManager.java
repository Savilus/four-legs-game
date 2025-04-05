package org.example.utils.text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.example.enums.GameObjectType;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class TextManager {
  private static final String DIALOGUES_PATH = "src/main/resources/texts/dialogues";
  private static final String PLAYER_AND_ITEMS_INFO_PATH = "src/main/resources/texts/itemsInformation";
  private static final String SETTINGS_AND_UI_TEXTS_PATH = "src/main/resources/texts/settingsAndUI";
  private static final String DESCRIPTION = "description";
  private static final String USE = "use";
  private static final String OPEN_DOOR = "openDoor";
  private static final String COLLECT_ITEM = "collectItem";
  private static final String LVL_UP = "lvlUp";
  private static final String NAME = "name";
  private static final String EVENT_MESSAGES_KEY = "eventMessages";

  @Getter
  private final Map<String, Map<String, List<String>>> dialogues = TextLoader.loadAllDialogues(DIALOGUES_PATH);
  @Getter
  private final Map<String, Map<String, String>> items = TextLoader.loadItemsInformation(PLAYER_AND_ITEMS_INFO_PATH);
  @Getter
  private final Map<String, Map<String, String>> settingsAndUI = TextLoader.loadSettingsAndUITexts(SETTINGS_AND_UI_TEXTS_PATH);

  private static final Map<String, String> formatKeys = new HashMap<>();

  static {
    formatKeys.put(GameObjectType.CHEST.getTextKey(), COLLECT_ITEM);
    formatKeys.put(GameObjectType.RED_POTION.getTextKey(), USE);
    formatKeys.put(GameObjectType.KEY.getTextKey(), OPEN_DOOR);
    formatKeys.put(EVENT_MESSAGES_KEY, LVL_UP);
  }

  public String getSettingText(String settingField, String settingName) {
    return settingsAndUI.get(settingField).get(settingName);
  }

  public String getUiText(String uiField, String uiTextKey) {
    return settingsAndUI.get(uiField).get(uiTextKey);
  }

  public String getItemName(String itemTextKey) {
    return items.get(itemTextKey).get(NAME);
  }

  public Map<String, List<String>> getAllDialoguesForTarget(String target) {
    return dialogues.get(target);
  }

  public List<String> getDialoguesKeysForTarget(String target) {
    return dialogues.get(target).keySet().stream().toList();
  }

  public Map<String, List<String>> getItemTexts(String itemTextKey, String... replacements) {
    Map<String, String> itemTexts = items.get(itemTextKey);

    if (Objects.isNull(itemTexts) || itemTexts.isEmpty()) {
      return new HashMap<>();
    }

    if (formatKeys.containsKey(itemTextKey)) {
      String formatKey = formatKeys.get(itemTextKey);
      if (itemTexts.containsKey(formatKey)) {
        if (Objects.nonNull(replacements) && replacements.length > 0) {
          itemTexts.put(formatKey, String.format(itemTexts.get(formatKey), replacements));
        }
      }
    }

    return itemTexts.entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> List.of(entry.getValue())));
  }

  public String getItemDescription(String itemTextKey) {
    return items.get(itemTextKey).get(DESCRIPTION);
  }

}
