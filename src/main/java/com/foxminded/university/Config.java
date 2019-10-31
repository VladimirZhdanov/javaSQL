package com.foxminded.university;

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
    private final Properties values;

    /**
     * Constructor of the class.
     */
    public Config() {
        values = new Properties();
    }

    /**
     * Initialisation.
     */
    public void init() {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("app.properties")) {
            assert in != null;
            values.load(in);
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
        return this.values.getProperty(key);
    }
}
