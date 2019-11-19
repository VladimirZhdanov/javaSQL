package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.Config;
import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.GroupDAO;
import com.foxminded.university.dao.layers.StudentDAO;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exceptions.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Set.of;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class GroupSQLTest {
    public Student studentOne;
    public Student studentTwo;
    public Group groupOne;

    public static final String PROPERTIES_PATH = "h2.properties";
    public static final String CREATE_TABLES = "tablesCreation.SQL";
    public static final String TABLES_DROP = "DROP TABLE IF EXISTS students, groups, courses, courses_connection;";

    public DataSource dataSource;
    public StudentDAO studentDAO;
    public GroupDAO groupDAO;

    public ExecutorQuery executorQuery = new ExecutorQuery();

    public Config configH2 = new Config();

    @BeforeEach
    public void setUp() {
        configH2.loadProperties(PROPERTIES_PATH);

        dataSource = new DataSource(configH2);
        studentDAO = new StudentSQL(dataSource);
        groupDAO = new GroupSQL(dataSource);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(TABLES_DROP)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        studentOne = new Student(1, "Lord", "Vladimir");
        studentTwo = new Student(1, "Pop", "Bom");
        groupOne = new Group("testName");
    }

    @Test
    public void shouldReturnCorrectedGroupWhenGetGroupsByStudentCount() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(studentOne);
        studentDAO.insert(studentTwo);
        groupDAO.insert(of(groupOne));
        Group groupActual = groupDAO.getGroupsByStudentCount(2).get(0);
        assertEquals(groupOne, groupActual,
                "Should return corrected group when GetGroupsByStudentCount()");
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed() {
        Exception exception = assertThrows(DAOException.class, () ->
                groupDAO.insert(null));
        assertEquals("Null was passed", exception.getMessage());
    }
}