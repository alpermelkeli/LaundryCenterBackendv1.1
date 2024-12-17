package org.alpermelkeli.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    public static String getProperty(String key) {
        Properties properties = new Properties();
        try (InputStream inputStream = ConfigUtil.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (inputStream == null) {
                throw new RuntimeException("Config file not found in resources!");
            }

            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot read configuration file!");
        }
    }
}
