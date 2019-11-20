package com.foxminded.university.dao.connection;

import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class ConfigTest {
    public static final String URL = "url";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DRIVER = "driver-class-name";

    public static final String DB_DRIVER = "org.h2.Driver";
    public static final String DB_URL = "jdbc:h2:mem:junitDB;DB_CLOSE_DELAY=-1";
    public static final String DB_USER = "";
    public static final String DB_PASSWORD = "";

    public static final String PROPERTIES_PATH = "postgres.properties";
    public static final String POSTGRES_URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    public static final String POSTGRES_USER = "postgres";
    public static final String POSTGRES_PASSWORD = "Qqqqqqqq1";
    public static final String POSTGRES_DRIVER = "org.postgresql.Driver";

    public Config config = new Config();

    @Mock
    Properties mockedProperties;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockedProperties.getProperty(URL)).thenReturn(DB_URL);
        when(mockedProperties.getProperty(USERNAME)).thenReturn(DB_USER);
        when(mockedProperties.getProperty(PASSWORD)).thenReturn(DB_PASSWORD);
        when(mockedProperties.getProperty(DRIVER)).thenReturn(DB_DRIVER);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhenNullWasPassedToLoadProperties() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                config.loadProperties((String) null));
        assertEquals("Null was passed to the method...", exception.getMessage());
    }

    @Test
    public void shouldReturnUrlWhenGetURL() {
        config.loadProperties(mockedProperties);
        String actualURL = config.getUrl();
        assertEquals(DB_URL, actualURL,
                "Should return the corrected url for DB");
    }

    @Test
    public void shouldReturnUserWhenGetUser() {
        config.loadProperties(mockedProperties);
        String actualUser = config.getUser();
        assertEquals(DB_USER, actualUser,
                "Should return the corrected user for DB");
    }

    @Test
    public void shouldReturnPasswordWhenGetPassword() {
        config.loadProperties(mockedProperties);
        String actualPassword = config.getPassword();
        assertEquals(DB_PASSWORD, actualPassword,
                "Should return the corrected password for DB");
    }

    @Test
    public void shouldReturnDriverWhenGetDriver() {
        config.loadProperties(mockedProperties);
        String actualDriver = config.getDriverName();
        assertEquals(DB_DRIVER, actualDriver,
                "Should return the corrected driver for DB");
    }

    @Test
    public void shouldReturnUrlWhenGetURL2() {
        config.loadProperties(PROPERTIES_PATH);
        String actualURL = config.getUrl();
        assertEquals(POSTGRES_URL, actualURL,
                "Should return the corrected url for DB");
    }

    @Test
    public void shouldReturnUserWhenGetUserSituationTwo() {
        config.loadProperties(PROPERTIES_PATH);
        String actualUser = config.getUser();
        assertEquals(POSTGRES_USER, actualUser,
                "Should return the corrected user for DB");
    }

    @Test
    public void shouldReturnPasswordWhenGetPasswordSituationTwo() {
        config.loadProperties(PROPERTIES_PATH);
        String actualPassword = config.getPassword();
        assertEquals(POSTGRES_PASSWORD, actualPassword,
                "Should return the corrected password for DB");
    }

    @Test
    public void shouldReturnDriverWhenGetDriverSituationTwo() {
        config.loadProperties(PROPERTIES_PATH);
        String actualDriver = config.getDriverName();
        assertEquals(POSTGRES_DRIVER, actualDriver,
                "Should return the corrected driver for DB");
    }
}