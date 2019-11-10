package com.foxminded.university.dao.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * Loads properties from resources.
 * Provides the get method to get a specified property.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class Config {

    private String url;
    private String user;
    private String password;
    private String driverName;

    private static final String PROPERTIES_PATH = "postgres.properties";

    /**
     * Load properties.
     *
     * @throws IOException - constructs an IOException with the specified detail message
     */
    public void loadProperties() throws IOException {
        loadProperties(PROPERTIES_PATH);
    }

    /**
     * Load properties.
     *
     * @param fileName - fileName of a properties file
     * @throws IOException - constructs an IOException with the specified detail message
     */
    public void loadProperties(String fileName) throws IOException {
        if (fileName == null) {
            throw new IllegalArgumentException("Null was passed to the method...");
        }
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        }
        try (FileInputStream fis = new FileInputStream(resource.getFile())) {
            Properties properties = new Properties();
            properties.load(fis);
            loadProperties(properties);
        }
    }

    /**
     * Load properties with given properties.
     *
     * @param properties - properties
     */
    public void loadProperties(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("Null was passed to the method...");
        }
        url = properties.getProperty("url");
        user = properties.getProperty("username");
        password = properties.getProperty("password");
        driverName = properties.getProperty("driver-class-name");
    }

    /**
     * Gets URL to DB.
     *
     * @return URL to DB
     */
    public String getUrl() {
        return url;
    }

    /**
     * Gets username to DB.
     *
     * @return username to DB
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets password to DB.
     *
     * @return password to DB
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets driver name.
     *
     * @return driver name
     */
    public String getDriverName() {
        return driverName;
    }






















    /**
     * Properties.
     private final Properties properties;

     *//**
     * Constructor of the class.
     *//*
    public Config() {
        properties = new Properties();
        init();
    }

    *//**
     * Constructor of the class.
     *//*
    public Config(Properties properties) {
        this.properties = properties;
    }

    *//**
     * Initialisation.
     *//*
    private void init() {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("postgres.properties")) {
            assert in != null;
            properties.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    *//**
     * Gets a specified property.
     *
     * @param key - key of property - String
     * @return - property - String
     *//*
    public String get(String key) {
        return this.properties.getProperty(key);
    }*/
}
