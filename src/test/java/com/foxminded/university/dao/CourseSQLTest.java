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

import static com.google.inject.internal.util.ImmutableList.of;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class CourseSQLTest {
    public Student studentOne;
    public Student studentTwo;
    public Course courseOne;
    public Course courseTwo;

    public static final String PROPERTIES_PATH = "h2.properties";

    private static final String CREATE_TABLES = "tablesCreation.SQL";
    public static final String TABLES_DROP = "DROP TABLE IF EXISTS students, groups, courses, courses_connection;";

    public DataSource dataSource;
    public StudentDAO studentDAO;
    public CourseDAO courseDAO;

    public GeneratorTestData generatorTestData = new GeneratorTestData();
    public ExecutorQuery executorQuery = new ExecutorQuery();

    public Config configH2 = new Config();

    @BeforeEach
    public void setUp() {
        configH2.loadProperties(PROPERTIES_PATH);

        dataSource = new DataSource(configH2);
        studentDAO = new StudentSQL(dataSource);
        courseDAO = new CourseSQL(dataSource);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(TABLES_DROP)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        studentOne = new Student(1, "Lord", "Vladimir");
        studentTwo = new Student(2, "Pop", "Bom");
        courseOne = new Course("testNameOne", "testDescOne");
        courseTwo = new Course("testNameTwo", "testDescTwo");
    }



    @Test
    public void shouldReturnCorrectedCourseWhenGetCourseById() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        courseDAO.insert(of(courseOne, courseTwo));

        Course courseActual = courseDAO.getCourseById(1);
        assertEquals(courseOne, courseActual,
                "Should return corrected course when get course bu id");
    }

    @Test
    public void shouldReturnTwoCoursesWhenGetAllCourses() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        courseDAO.insert(of(courseOne, courseTwo));

        List<Course> coursesActual = courseDAO.getAllCourses();

        boolean result = false;
        if (coursesActual.size() == 2 && coursesActual.get(0).equals(courseOne)) {
            result = true;
        }
        assertTrue(result,
                "Should return true if get corrected courses");
    }

    @Test
    public void shouldReturnCorrectedCourseWhenGetCoursesByStudentId() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(studentOne);
        studentDAO.insert(studentTwo);
        courseDAO.insert(of(courseOne, courseTwo));
        studentDAO.insertCourseToStudentById(1, 2);
        studentDAO.insertCourseToStudentById(2, 1);

        Course courseActual = courseDAO.getCoursesByStudentId(1).get(0);
        assertEquals(courseTwo, courseActual,
                "Should return corrected course when get courses by student id");
    }

    @Test
    public void shouldReturnTrueWhenRemoveCourseByStudentIdAndCourseId() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        insertTestData();

        studentDAO.insert(studentOne);
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