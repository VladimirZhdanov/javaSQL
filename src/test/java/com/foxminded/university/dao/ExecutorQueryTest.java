package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.Config;
import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.StudentDAO;
import com.foxminded.university.domain.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
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
class ExecutorQueryTest {
    public static final String STUDENT_FIRST_NAME = "Lord";
    public static final String STUDENT_LAST_NAME = "Vladimir";

    public static final String DB_DRIVER = "org.h2.Driver";
    public static final String DB_URL = "jdbc:h2:mem:junitDB;DB_CLOSE_DELAY=-1";
    public static final String DB_USER = "";
    public static final String DB_PASSWORD = "";

    private static final String CREATE_TABLES = "tablesCreation.SQL";
    public static final String TABLES_DROP = "DROP TABLE IF EXISTS students, groups, courses, courses_connection;";

    public DataSource dataSource;
    public StudentDAO studentDAO;

    public ExecutorQuery executorQuery = new ExecutorQuery();


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
        studentDAO = new StudentSQL(dataSource);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(TABLES_DROP)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldReturnTrueWhenStudentsWasInsertedInExistentTable() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(new Student(2, STUDENT_FIRST_NAME, STUDENT_LAST_NAME));
        studentDAO.insert(new Student(1, "Pop", "Bom"));
        List<Student> students = studentDAO.getAllStudents();

        boolean actual = false;

        if (students.size() > 0 && students.get(0).getId() == 1 && students.get(0).getFirstName().equals(STUDENT_FIRST_NAME)) {
            actual = true;
        }

        assertTrue(actual,
                "Should return true if students added in existent table");
    }
}