package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.Config;
import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.CourseDAO;
import com.foxminded.university.dao.layers.StudentDAO;
import com.foxminded.university.domain.Course;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exceptions.DAOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.lang.String.format;

import static com.google.inject.internal.util.ImmutableList.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class CourseSQLTest {
    public static final String COURSE_NAME_ONE = "testNameOne";
    public static final String COURSE_DESC_ONE = "testDescOne";
    public static final String COURSE_NAME_TWO = "testNameTwo";
    public static final String COURSE_DESC_TWO = "testDescTwo";
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
    public CourseDAO courseDAO;

    public GeneratorTestData generatorTestData = new GeneratorTestData();
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
        courseDAO = new CourseSQL(dataSource);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(TABLES_DROP)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void shouldReturnCorrectedNameWhenGetCourseById() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        courseDAO.insert(of(new Course(COURSE_NAME_ONE, COURSE_DESC_ONE),
                new Course(COURSE_NAME_TWO, COURSE_DESC_TWO)));

        String actual = courseDAO.getCourseById(1).getName();
        assertEquals(COURSE_NAME_ONE, actual,
                format("Should return group name: %s", COURSE_NAME_ONE));
    }

    @Test
    public void shouldReturnTwoCoursesWhenGetAllCourses() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        courseDAO.insert(of(new Course(COURSE_NAME_ONE, COURSE_DESC_ONE),
                new Course(COURSE_NAME_TWO, COURSE_DESC_TWO)));

        int actual = courseDAO.getAllCourses().size();
        int expected = 2;
        assertEquals(expected, actual,
                "Should return 2 courses");
    }

    @Test
    public void shouldReturnCorrectedCourseNameWhenGetCoursesByStudentId1() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(new Student(2, "Pop", "Bom"));
        studentDAO.insert(new Student(1, STUDENT_FIRST_NAME, STUDENT_LAST_NAME));
        courseDAO.insert(of(new Course(COURSE_NAME_ONE, COURSE_DESC_ONE),
                new Course(COURSE_NAME_TWO, COURSE_DESC_TWO)));
        studentDAO.insertCourseToStudentById(1, 1);
        studentDAO.insertCourseToStudentById(2, 2);

        Course course = courseDAO.getCoursesByStudentId(1).get(0);

        String actual = course.getName();
        String expected = COURSE_NAME_ONE;
        assertEquals(expected, actual,
                "Should return corrected course name when get courses by student id");
    }

    @Test
    public void shouldReturnCorrectedCourseNameWhenGetCoursesByStudentId2() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        courseDAO.insert(generatorTestData.getCourses());
        studentDAO.insert(new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME));
        studentDAO.insertCourseToStudentById(1, 1);

        String actual = courseDAO.getCoursesByStudentId(1).get(0).getName();
        String expected = courseDAO.getCourseById(1).getName();

        assertEquals(expected, actual,
                "Should return group name: Architecture");
    }

    @Test
    public void shouldReturnTrueWhenRemoveCourseByStudentIdAndCourseId() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        insertTestData();

        studentDAO.insert(new Student(STUDENT_FIRST_NAME, STUDENT_LAST_NAME));
        studentDAO.insertCourseToStudentById(201, 1);
        studentDAO.insertCourseToStudentById(201, 2);
        studentDAO.insertCourseToStudentById(201, 3);

        boolean actual = false;
        if (courseDAO.getCoursesByStudentId(201).size() == 3) {
            courseDAO.removeCourseByStudentIdAndCourseId(201, 1);
            if (courseDAO.getCoursesByStudentId(201).size() == 2) {
                actual = true;
            }
        }

        assertTrue(actual,
                "Should return true if course was returned");
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed() {
        Exception exception = assertThrows(DAOException.class, () ->
                courseDAO.insert(null));
        assertEquals("Null was passed", exception.getMessage());
    }

    private void insertTestData() {
        List<Student> students = generatorTestData.getStudents();
        List<Course> courses = generatorTestData.getCourses();
        studentDAO.insert(students);
        courseDAO.insert(courses);
        students = studentDAO.getAllStudents();
        courses = courseDAO.getAllCourses();
        students = generatorTestData.assignCoursesToStudent(students, courses);
        studentDAO.insertRelationshipStudentsToCourses(students);
    }
}