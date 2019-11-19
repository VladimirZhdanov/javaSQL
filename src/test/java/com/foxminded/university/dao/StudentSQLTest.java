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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class StudentSQLTest {
    public Student studentOne;
    public Student studentTwo;
    public Course courseOne;

    public static final String PROPERTIES_PATH = "h2.properties";
    public static final String CREATE_TABLES = "tablesCreation.SQL";
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
        courseOne = new Course("testName", "testDesc");
    }


    @Test
    public void shouldReturnTrueWhenInsertStudents() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(studentOne);
        studentDAO.insert(studentTwo);

        Student studentOneActual = studentDAO.getStudent(1);
        Student studentTwoActual = studentDAO.getStudent(2);

        boolean result = false;
        if (studentOneActual.equals(studentOne) && studentTwoActual.equals(studentTwo)) {
            result = true;
        }
        assertTrue(result,
                "Should return true if students was correctly inserted");
    }

    @Test
    public void shouldReturnNullWhenRemoveStudent() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(studentOne);
        studentDAO.insert(studentTwo);

        if (studentDAO.removeStudentById(2)) {
            studentOne = studentDAO.getStudent(2);
        }
        assertNull(studentOne, "Should return true if student was removed");
    }

    @Test
    public void shouldReturnCorrectedStudentWhenGetStudentsByCourse() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        studentDAO.insert(studentOne);
        studentDAO.insert(studentTwo);
        courseDAO.insert(of(courseOne));
        studentDAO.insertCourseToStudentById(2, 1);

        Student studentActual = studentDAO.getStudentsByCourse(courseOne.getName()).get(0);

        assertEquals(studentTwo, studentActual,
                "Should return corrected student");
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
    public void shouldReturnCorrectedStudentsWhenGetAllInsertedStudents() {
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

    @Test
    public void shouldReturnCorrectedCourseWhenGetCoursesByStudentId() {
        executorQuery.execute(dataSource, CREATE_TABLES);
        courseDAO.insert(of(courseOne));
        studentDAO.insert(studentOne);
        studentDAO.insert(studentTwo);
        studentDAO.insertCourseToStudentById(1, 1);
        Course courseActual = courseDAO.getCoursesByStudentId(1).get(0);

        assertEquals(courseOne, courseActual,
                "Should return corrected course when get course by student id");
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