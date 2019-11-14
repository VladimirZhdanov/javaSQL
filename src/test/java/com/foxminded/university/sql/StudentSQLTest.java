package com.foxminded.university.sql;

import com.foxminded.university.dao.CourseSQL;
import com.foxminded.university.dao.ExecuterQuery;
import com.foxminded.university.dao.GenerationTestData;
import com.foxminded.university.dao.StudentSQL;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    private static final String CREATE_TABLES = "tablesCreation.SQL";
    public static final String TABLES_DROP = "DROP TABLE IF EXISTS students, groups, courses, courses_connection;";

    public DataSource dataSource;
    public StudentDAO studentDAO;
    public CourseDAO courseDAO;

    public GenerationTestData generationTestData = new GenerationTestData();
    public ExecuterQuery executerQuery = new ExecuterQuery();


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
        executerQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(generationTestData.getStudents()); //inserts 200 students
        studentDAO.insert(new Student(1, "Lord", "Vladimir"));
        Student student = studentDAO.getStudent(201);
        String expected = "Lord";
        String actual = student.getFirstName();
        assertEquals(expected, actual,
                "Should return firs name: Lord");
    }

    @Test
    public void shouldReturnTrueWhenRemoveStudent() {
        executerQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(generationTestData.getStudents()); //adds 200 students
        boolean actual = studentDAO.removeStudentById(200);
        assertTrue(actual,
                "Should return true if student was removed");
    }

    @Test
    public void shouldReturnSizeMoreThanZeroWhenGetStudentByCourseName() {
        executerQuery.execute(dataSource, CREATE_TABLES);

        List<Student> students = generationTestData.getStudents();
        List<Course> courses = generationTestData.getCourses();
        studentDAO.insert(students);
        courseDAO.insert(courses);
        students = studentDAO.getAllStudents();
        courses = courseDAO.getAllCourses();
        students = generationTestData.assignCoursesToStudent(students, courses);
        studentDAO.insertRelationshipStudentsToCourses(students);

        students = studentDAO.getStudentsByCourse("History");

        boolean actual = students.size() > 0;
        assertTrue(actual,
                "Should return true if size of returned list was more than 0");
    }

    @Test
    public void shouldReturnSizeZeroWhenGetStudentByUnrealCourseName() {
        executerQuery.execute(dataSource, CREATE_TABLES);

        List<Student> students = generationTestData.getStudents();
        List<Course> courses = generationTestData.getCourses();
        studentDAO.insert(students);
        courseDAO.insert(courses);
        students = studentDAO.getAllStudents();
        courses = courseDAO.getAllCourses();
        students = generationTestData.assignCoursesToStudent(students, courses);
        studentDAO.insertRelationshipStudentsToCourses(students);

        students = studentDAO.getStudentsByCourse("Gistory");

        boolean actual = students.size() == 0;
        assertTrue(actual,
                "Should return true if size of returned list was 0");
    }

    @Test
    public void shouldReturn200StudentWhenGetAllInsertedStudent() {
        executerQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(generationTestData.getStudents()); //adds 200 students
        int expected = 200;
        int actual = studentDAO.getAllStudents().size();
        assertEquals(expected, actual,
                "Should return 200 students");
    }

    @Test
    public void shouldReturnTrueWhenCourseWasAddedToStudent() {
        executerQuery.execute(dataSource, CREATE_TABLES);

        List<Student> students = generationTestData.getStudents();
        List<Course> courses = generationTestData.getCourses();
        studentDAO.insert(students);
        courseDAO.insert(courses);
        students = studentDAO.getAllStudents();
        courses = courseDAO.getAllCourses();
        students = generationTestData.assignCoursesToStudent(students, courses);
        studentDAO.insertRelationshipStudentsToCourses(students);

        studentDAO.insert(new Student(1, "Lord", "Vladimir"));
        studentDAO.insertCourseToStudentById(201, 1);
        String courseName = courseDAO.getCourseById(1).getName();
        courses = courseDAO.getCoursesByStudentId(201);

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
}