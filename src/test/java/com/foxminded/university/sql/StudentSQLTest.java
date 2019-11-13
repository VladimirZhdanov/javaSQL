package com.foxminded.university.sql;

import com.foxminded.university.dao.StudentSQL;
import com.foxminded.university.dao.connection.Config;
import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.domain.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class StudentSQLTest {
    public static final String DB_DRIVER = "org.h2.Driver";
    public static final String DB_URL = "jdbc:h2:mem:junitDB;DB_CLOSE_DELAY=-1";
    public static final String DB_USER = "";
    public static final String DB_PASSWORD = "";
    public static final String STUDENT_TABLE_CREATION = "CREATE table if not exists students (student_id serial primary key not null, group_id int, first_name varchar(250), last_name varchar(250));";
    public DataSource dataSource;
    public StudentSQL studentSQL;

    @Mock
    Config mockedConfig;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockedConfig.getDriverName()).thenReturn(DB_DRIVER);
        when(mockedConfig.getUrl()).thenReturn(DB_URL);
        when(mockedConfig.getUser()).thenReturn(DB_USER);
        when(mockedConfig.getPassword()).thenReturn(DB_PASSWORD);
        dataSource = new DataSource(mockedConfig);
        studentSQL = new StudentSQL(dataSource);
    }

    @Test
    public void shouldReturnTrueWhenAddStudent() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(STUDENT_TABLE_CREATION)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        boolean actual = studentSQL.insert(new Student("Lord", "Vladimir", 1));
        assertEquals(true, actual,
                "Should return true if user was added");
    }

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