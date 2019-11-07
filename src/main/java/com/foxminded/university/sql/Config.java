package com.foxminded.university.sql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads properties from resources.
 * Provides the get method to get a specified property.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class Config {
    private final Properties properties;

    /**
     * Constructor of the class.
     */
    public Config() {
        properties = new Properties();
        init();
    }

    /**
     * Constructor of the class.
     */
    public Config(Properties properties) {
        this.properties = properties;
    }

    /**
     * Initialisation.
     */
    private void init() {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("app.properties")) {
            assert in != null;
            properties.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Gets a specified property.
     *
     * @param key - key of property - String
     * @return - property - String
     */
    public String get(String key) {
        return this.properties.getProperty(key);
    }
}
