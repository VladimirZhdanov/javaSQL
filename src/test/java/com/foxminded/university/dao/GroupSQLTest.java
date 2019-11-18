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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Set.of;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class GroupSQLTest {
    public static final String GROUP_NAME = "testName";
    public static final String STUDENT_FIRST_NAME = "Lord";
    public static final String STUDENT_LAST_NAME = "Vladimir";

    public static final String DB_DRIVER = "org.h2.Driver";
    public static final String DB_URL = "jdbc:h2:mem:junitDB;DB_CLOSE_DELAY=-1";
    public static final String DB_USER = "";
    public static final String DB_PASSWORD = "";

    public static final String CREATE_TABLES = "tablesCreation.SQL";
    public static final String TABLES_DROP = "DROP TABLE IF EXISTS students, groups, courses, courses_connection;";

    public DataSource dataSource;
    public StudentDAO studentDAO;
    public GroupDAO groupDAO;

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
        groupDAO = new GroupSQL(dataSource);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(TABLES_DROP)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldReturnCorrectedGroupNameWhenGetGroupsByStudentCount() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(new Student(1, "Pop", "Bom"));
        studentDAO.insert(new Student(1, STUDENT_FIRST_NAME, STUDENT_LAST_NAME));
        groupDAO.insert(of(new Group(GROUP_NAME)));
        Group group = groupDAO.getGroupsByStudentCount(2).get(0);
        String actual = group.getName();
        assertEquals(GROUP_NAME, actual,
                "Should return corrected group name when GetGroupsByStudentCount()");
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed() {
        Exception exception = assertThrows(DAOException.class, () ->
                groupDAO.insert(null));
        assertEquals("Null was passed", exception.getMessage());
    }
}