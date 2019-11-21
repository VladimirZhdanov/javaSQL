package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.Config;
import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.CourseDAO;
import com.foxminded.university.dao.layers.GroupDAO;
import com.foxminded.university.dao.layers.StudentDAO;
import com.foxminded.university.domain.Course;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class UniversitySQLTest {
    public static final String PROPERTIES_PATH = "h2.properties";

    private static final String DROP_DB = "dropDB.SQL";
    private static final String CREATE_DB = "dataBaseCreation.SQL";
    private static final String CREATE_TABLES = "tablesCreation.SQL";

    public static final String TABLES_DROP = "DROP TABLE IF EXISTS students, groups, courses, courses_connection;";

    public StudentDAO studentDAO;
    public GroupDAO groupDAO;
    public CourseDAO courseDAO;

    public DataSource dataSourceJunitDB;

    public UniversitySQL universitySQL;

    public Config configH2 = new Config();

    @Mock
    public ExecutorQuery mockedExecutorQuery;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        configH2.loadProperties(PROPERTIES_PATH);

        dataSourceJunitDB = new DataSource(configH2);

        doNothing().doThrow(new RuntimeException()).when(mockedExecutorQuery).execute(dataSourceJunitDB, DROP_DB);
        doNothing().doThrow(new RuntimeException()).when(mockedExecutorQuery).execute(dataSourceJunitDB, CREATE_DB);
        doCallRealMethod().when(mockedExecutorQuery).execute(dataSourceJunitDB, CREATE_TABLES);

        universitySQL = new UniversitySQL(dataSourceJunitDB, dataSourceJunitDB, mockedExecutorQuery);
        studentDAO = new StudentSQL(dataSourceJunitDB);
        courseDAO = new CourseSQL(dataSourceJunitDB);
        groupDAO = new GroupSQL(dataSourceJunitDB);

        try (Connection connection = dataSourceJunitDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(TABLES_DROP)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldReturnTrueWhenGetAllStudents() {
        universitySQL.setDateBase();
        List<Student> students = studentDAO.getAllStudents();

        boolean actual = students.size() != 0;

        assertTrue(actual,
                "Should return true if student tables was created and inserted");
    }

    @Test
    public void shouldReturnTrueWhenGetAllCourses() {
        universitySQL.setDateBase();
        List<Course> courses = courseDAO.getAllCourses();

        boolean actual = courses.size() != 0;

        assertTrue(actual,
                "Should return true if courses tables was created and inserted");
    }

    @Test
    public void shouldReturnTrueWhenGetGroupsByStudentCount() {
        universitySQL.setDateBase();
        List<Group> groups = groupDAO.getGroupsByStudentCount(30);

        boolean actual = groups.size() != 0;

        assertTrue(actual,
                "Should return true if groups tables was created and inserted");
    }

    @Test
    public void shouldReturnTrueWhenCheckExistenceOfStudentsTable() {
        universitySQL.setDateBase();

        boolean actual = false;

        try (Connection conn = dataSourceJunitDB.getConnection()) {
            ResultSet resultSet = conn.getMetaData()
                    .getTables(null, null, "STUDENTS", null);
            if (resultSet.next()) {
                actual = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(actual,
                "Should return true if students table is exist");
    }

    @Test
    public void shouldReturnTrueWhenCheckExistenceOfGroupsTable() {
        universitySQL.setDateBase();
        boolean actual = false;
        try (Connection connection = dataSourceJunitDB.getConnection()) {
            actual = checkTableExistence(connection, "GROUPS");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(actual,
                "Should return true if groups table is exist");
    }

    @Test
    public void shouldReturnTrueWhenCheckExistenceOfCoursesTable() {
        universitySQL.setDateBase();
        boolean actual = false;
        try (Connection connection = dataSourceJunitDB.getConnection()) {
            actual = checkTableExistence(connection, "COURSES");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(actual,
                "Should return true if courses table is exist");
    }

    @Test
    public void shouldReturnTrueWhenCheckExistenceOfCoursesConnectionTable() {
        universitySQL.setDateBase();
        boolean actual = false;
        try (Connection connection = dataSourceJunitDB.getConnection()) {
            actual = checkTableExistence(connection, "COURSES_CONNECTION");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(actual,
                "Should return true if courses_connection table is exist");
    }

    public boolean checkTableExistence(Connection connection, String tableName) throws SQLException {
        boolean result = false;
        ResultSet resultSet = connection.getMetaData()
                .getTables(null, null, tableName, null);
        if (resultSet.next()) {
            result = true;
        }
        return result;
    }
}