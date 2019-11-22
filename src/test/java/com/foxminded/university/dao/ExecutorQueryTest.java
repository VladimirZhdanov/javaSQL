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

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class ExecutorQueryTest {
    public Student studentOne;
    public Student studentTwo;

    public static final String PROPERTIES_PATH = "h2.properties";

    private static final String CREATE_TABLES = "tablesCreation.SQL";
    public static final String TABLES_DROP = "DROP TABLE IF EXISTS students, groups, courses, courses_connection;";

    public DataSource dataSource;
    public StudentDAO studentDAO;

    public ExecutorQuery executorQuery = new ExecutorQuery();

    public Config configH2 = new Config();

    @BeforeEach
    public void setUp() {
        configH2.loadProperties(PROPERTIES_PATH);

        dataSource = new DataSource(configH2);
        studentDAO = new StudentSQL(dataSource);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(TABLES_DROP)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        studentOne = new Student(1, "Lord", "Vladimir");
        studentTwo = new Student(2, "Pop", "Bom");
    }

    @Test
    public void shouldReturnTrueWhenStudentsWasInsertedInExistentTable() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(studentOne);
        studentDAO.insert(studentTwo);
        List<Student> studentsActual = studentDAO.getAllStudents();

        boolean result = false;
        if (studentsActual.size() == 2 && studentsActual.get(0).equals(studentOne)) {
            result = true;
        }
        assertTrue(result,
                "Should return true if get corrected students");
    }
}