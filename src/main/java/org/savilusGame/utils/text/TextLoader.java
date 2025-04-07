package org.savilusGame.utils.text;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import io.vavr.control.Try;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class TextLoader {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public Map<String, Map<String, List<String>>> loadAllDialogues(String folderPath) {
    return loadData(folderPath, "dialogues");
  }

  public Map<String, Map<String, String>> loadItemsInformation(String folderPath) {
    return loadData(folderPath, "items and player information");
  }

  public Map<String, Map<String, String>> loadSettingsAndUITexts(String folderPath) {
    return loadData(folderPath, "settings and ui texts");
  }

  private <T> Map<String, T> loadData(String folderPath, String dataType) {
    Map<String, T> dataMap = new HashMap<>();
    File folder = new File(folderPath);

    if (!folder.exists() || !folder.isDirectory()) {
      log.error("Directory does not exist or is not a directory: {}", folder.getAbsolutePath());
      return Collections.emptyMap();
    }

    Try.run(() -> {
      try (Stream<Path> paths = Files.walk(Paths.get(folderPath))) {
        paths.filter(Files::isRegularFile)
            .forEach(file -> Try.of(() -> objectMapper.readValue(file.toFile(), (Class<T>) Map.class))
                .onSuccess(loadedData -> {
                  dataMap.putAll((Map<String, T>) loadedData);
                })
                .onFailure(e -> log.error("Error loading {} from {}: {}", dataType, file, e.getMessage())));
      }
    }).onFailure(e -> log.error("Error reading {} directory: {}", dataType, e.getMessage()));

    log.info("Total {} loaded: {}", dataMap.size(), dataType);
    return dataMap;
  }
}