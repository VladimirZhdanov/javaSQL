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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class StudentSQLTest {
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
    public void shouldReturnCorrectedNameWhenAddNewStudentWithTheName() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(new Student(2, "Pop", "Bom"));
        studentDAO.insert(new Student(1, STUDENT_FIRST_NAME, STUDENT_LAST_NAME));
        Student student = studentDAO.getStudent(2);
        String actual = student.getFirstName();
        assertEquals(STUDENT_FIRST_NAME, actual,
                format("Should return firs name: %s", STUDENT_FIRST_NAME));
    }

    @Test
    public void shouldReturnNullWhenRemoveStudent() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(new Student(2, "Pop", "Bom"));
        Student student = new Student(1, STUDENT_FIRST_NAME, STUDENT_LAST_NAME);
        studentDAO.insert(student);
        if (studentDAO.removeStudentById(2)) {
            student = studentDAO.getStudent(2);
        }
        assertNull(student, "Should return true if student was removed");
    }

    @Test
    public void shouldReturnCorrectedNameWhenGetStudentsByCourse() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(new Student(2, "Pop", "Bom"));
        studentDAO.insert(new Student(1, STUDENT_FIRST_NAME, STUDENT_LAST_NAME));
        courseDAO.insert(of(new Course("testName", "testDesc")));
        studentDAO.insertCourseToStudentById(2, 1);

        Student student = studentDAO.getStudentsByCourse("testName").get(0);

        String actual = student.getFirstName();
        assertEquals(STUDENT_FIRST_NAME, actual,
                format("Should return firs name: %s", STUDENT_FIRST_NAME));
    }

    @Test
    public void shouldReturnSizeZeroWhenGetStudentByUnrealCourseName() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        insertTestData();
        List<Student> students = studentDAO.getStudentsByCourse("Gistory");
        boolean actual = students.size() == 0;
        assertTrue(actual,
                "Should return true if size of returned list was 0");
    }

    @Test
    public void shouldReturnTwoStudentWhenGetAllInsertedStudent() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(new Student(2, "Pop", "Bom"));
        studentDAO.insert(new Student(1, STUDENT_FIRST_NAME, STUDENT_LAST_NAME));
        int expected = 2;
        int actual = studentDAO.getAllStudents().size();
        assertEquals(expected, actual,
                "Should return two students");
    }

    @Test
    public void shouldReturnTrueWhenCourseWasAddedToStudent() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        insertTestData();
        studentDAO.insert(new Student(1, STUDENT_FIRST_NAME, STUDENT_LAST_NAME));
        studentDAO.insertCourseToStudentById(201, 1);
        String courseName = courseDAO.getCourseById(1).getName();
        List<Course> courses = courseDAO.getCoursesByStudentId(201);
        boolean actual = courses.stream().anyMatch(course -> course.getName().equals(courseName));
        assertTrue(actual,
                "Should return true if course was added to the student");
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed1() {
        Exception exception = assertThrows(DAOException.class, () ->
                studentDAO.insert((Student) null));
        assertEquals("Null was passed", exception.getMessage());
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed2() {
        Exception exception = assertThrows(DAOException.class, () ->
                studentDAO.insert((List<Student>) null));
        assertEquals("Null was passed", exception.getMessage());
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed3() {
        Exception exception = assertThrows(DAOException.class, () ->
                studentDAO.insertRelationshipStudentsToCourses(null));
        assertEquals("Null was passed", exception.getMessage());
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed4() {
        Exception exception = assertThrows(DAOException.class, () ->
                studentDAO.getStudentsByCourse(null));
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