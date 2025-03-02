package org.example.config;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

import org.example.Main;
import org.yaml.snakeyaml.Yaml;

import io.vavr.control.Try;
import lombok.experimental.UtilityClass;

@UtilityClass
public class YamlConfigLoader {

  public static final String ERROR_MESSAGE = "Error during loading value for path: %s. Failed field: %s";

  public static void loadConfig(Class<?> clazz, String configPath) {
    InputStream yamlFile = Main.class.getResourceAsStream(configPath);
    if (yamlFile == null) {
      throw new RuntimeException("Config file not found: " + configPath);
    }

    Yaml yaml = new Yaml();
    Map<String, Object> data = yaml.load(yamlFile);

    for (Field field : clazz.getDeclaredFields()) {
      YamlValue annotation = field.getAnnotation(YamlValue.class);
      if (annotation != null) {
        setFieldValue(field, data, annotation.value());
      }
    }
  }

  private static void setFieldValue(Field field, Map<String, Object> data, String path) {
    Try.run(() -> {
          String[] keys = path.split("\\.");
          Object value = data;

          for (String key : keys) {
            value = ((Map<?, ?>) value).get(key);
            if (value == null) {
              throw new IllegalArgumentException("Key not found in YAML: " + path);
            }
          }

          field.setAccessible(true);
          field.set(null, value);
        })
        .getOrElseThrow(e -> new RuntimeException(String.format(ERROR_MESSAGE, path, field.getName()), e));
  }
}
