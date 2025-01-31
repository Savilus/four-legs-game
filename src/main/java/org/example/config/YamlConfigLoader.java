package org.example.config;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

import org.example.Main;
import org.yaml.snakeyaml.Yaml;

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
        setFieldValue(clazz, field, data, annotation.value());
      }
    }
  }

  private static void setFieldValue(Class<?> clazz, Field field, Map<String, Object> data, String path) {
    try {
      String[] keys = path.split("\\.");
      Object value = data;

      for (String key : keys) {
        value = ((Map<?, ?>) value).get(key);
        if (value == null) {
          throw new RuntimeException("Key not found in YAML: " + path);
        }
      }

      field.setAccessible(true);
      field.set(null, value);
    } catch (Exception e) {
      throw new RuntimeException(String.format(ERROR_MESSAGE, path, field.getName()), e);
    }
  }
}
