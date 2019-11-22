package com.foxminded.university.dao.connection;

import java.sql.Connection;
import java.sql.SQLException;
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
class DataSourceTest {
    public static final String DB_DRIVER = "org.h2.Driver";
    public static final String DB_URL = "jdbc:h2:mem:junitDB;DB_CLOSE_DELAY=-1";
    public static final String DB_USER = "";
    public static final String DB_PASSWORD = "";
    DataSource dataSource;

    @Mock
    public Config mockedConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockedConfig.getDriverName()).thenReturn(DB_DRIVER);
        when(mockedConfig.getUrl()).thenReturn(DB_URL);
        when(mockedConfig.getUser()).thenReturn(DB_USER);
        when(mockedConfig.getPassword()).thenReturn(DB_PASSWORD);
        dataSource = new DataSource(mockedConfig);
    }

    @Test
    public void shouldReturnTrueWhenGetConnection() {

        boolean actual = false;
        try (Connection connection = dataSource.getConnection()) {
            actual = connection.isValid(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        assertTrue(actual,
                "Should return true if connection was available");
    }

    @Test
    public void shouldThrowSQLExceptionWhenTryGetConnectionAfterCloseConnection() {
        dataSource.closeConnection();
        Exception exception = assertThrows(SQLException.class, () ->
                dataSource.getConnection());
        assertEquals("Data source is closed", exception.getMessage());
    }
}