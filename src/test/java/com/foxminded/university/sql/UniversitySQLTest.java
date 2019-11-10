package com.foxminded.university.sql;

import com.foxminded.university.dao.connection.Config;
import com.foxminded.university.dao.UniversitySQL;
import com.foxminded.university.domain.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class UniversitySQLTest {
    public static final String DB_DRIVER = "org.h2.Driver";
    public static final String DRIVER = "driver-class-name";
    public static final String DB_CONNECTION = "jdbc:h2:mem:junitDB;DB_CLOSE_DELAY=-1";
    public static final String URL = "url";
    public static final String DB_USER = "";
    public static final String USERNAME = "username";
    public static final String DB_PASSWORD = "";
    public static final String PASSWORD = "password";
    public String createStudentsTableQuery = "CREATE table if not exists students (student_id serial primary key not null, group_id int, first_name varchar(250), last_name varchar(250));";
    public Config config;
    UniversitySQL universitySQL;

    @Mock
    Properties mockedProperties;

    /*@BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        config = new Config(mockedProperties);
        universitySQL = new UniversitySQL(config);
        when(mockedProperties.getProperty(URL)).thenReturn(DB_CONNECTION);
        when(mockedProperties.getProperty(DRIVER)).thenReturn(DB_DRIVER);
        when(mockedProperties.getProperty(USERNAME)).thenReturn(DB_USER);
        when(mockedProperties.getProperty(PASSWORD)).thenReturn(DB_PASSWORD);
        universitySQL.init();
    }*/

   /* @Test
    public void shouldReturnEndUrlWhenLoadMockedProperties() {
        Connection connection = universitySQL.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(createStudentsTableQuery)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean actual = universitySQL.add(new Student("Lord", "Vladimir", 1));

              assertEquals(true, actual,
                "Should return true if user was added");
    }*/

  /*  @Test
    public void shouldThrowIllegalArgumentExceptionWhenNullWasPassed() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new UniversitySQL(null));
        assertEquals("Null was passed to the method...", exception.getMessage());
    }*/

    /*@Test
    public void shouldReturnTrueWhenInitialiseConnection() {
        try (UniversitySQL sql = new UniversitySQL(config)) {
            assertTrue(sql.init(), "Should return true");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}