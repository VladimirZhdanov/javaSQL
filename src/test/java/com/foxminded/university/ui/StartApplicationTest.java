package com.foxminded.university.ui;

import com.foxminded.university.dao.ExecutorQuery;
import com.foxminded.university.dao.StudentSQL;
import com.foxminded.university.dao.connection.Config;
import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.StudentDAO;
import com.foxminded.university.domain.Student;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.lang.String.format;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class StartApplicationTest {
    public static final String LINE_SEPARATOR = System.lineSeparator();

    public static final String PROPERTIES_PATH = "h2.properties";

    public static final String DROP_DB = "dropDB.SQL";
    public static final String CREATE_DB = "dataBaseCreation.SQL";
    public static final String CREATE_TABLES = "tablesCreation.SQL";

    public static final String TABLES_DROP = "DROP TABLE IF EXISTS students, groups, courses, courses_connection;";

    public DataSource dataSourceJunitDB;
    public DataSource dataSourceSecond;
    public StudentDAO studentDAO;

    public final ByteArrayOutputStream out = new ByteArrayOutputStream();
    public final Consumer<String> output = new Consumer<>() {
        public final PrintStream stdout = new PrintStream(out);

        @Override
        public void accept(String s) {
            stdout.println(s);
        }

        @Override
        public String toString() {
            return out.toString();
        }
    };

    public Config configH2 = new Config();

    @Mock
    public ExecutorQuery mockedExecutorQuery;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        configH2.loadProperties(PROPERTIES_PATH);

        dataSourceJunitDB = new DataSource(configH2);
        dataSourceSecond = new DataSource(configH2);

        doNothing().doNothing().doThrow(new RuntimeException()).when(mockedExecutorQuery).execute(dataSourceJunitDB, DROP_DB);
        doNothing().doThrow(new RuntimeException()).when(mockedExecutorQuery).execute(dataSourceJunitDB, CREATE_DB);
        doCallRealMethod().when(mockedExecutorQuery).execute(dataSourceJunitDB, CREATE_TABLES);

        studentDAO = new StudentSQL(dataSourceSecond);

        try (Connection connection = dataSourceSecond.getConnection();
             PreparedStatement statement = connection.prepareStatement(TABLES_DROP)) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void shouldReturnCorrectedNameWhenAddNewStudentWithTheName() {
        final String FIRST_NAME = "Lord";
        Input input = new SimulateInput(Arrays.asList("2", FIRST_NAME, "Vladimir", "1", "6"));
        StartApplication startApplication = new StartApplication(input, output, dataSourceJunitDB, dataSourceJunitDB, mockedExecutorQuery);
        startApplication.init();

        int lastStudentId = studentDAO.getAllStudents().size();
        String actualFirstName = studentDAO.getStudent(lastStudentId).getFirstName();

        assertEquals(FIRST_NAME, actualFirstName,
                "Should return firs name: Lord");
    }

    @Test
    public void shouldReturnCorrectedStudentsWhenRemoveOneStudent() {
        Input input = new SimulateInput(Arrays.asList("3", "200", "6"));
        StartApplication startApplication = new StartApplication(input, output, dataSourceJunitDB, dataSourceJunitDB, mockedExecutorQuery);
        startApplication.init();

        int actualSize = studentDAO.getAllStudents().size();

        assertEquals(199, actualSize,
                "Should return 199 students because 1 was removed from 200");
    }

    @Test
    public void shouldReturnTrueWhenInsertCourseToStudent() {
        final String FIRST_NAME = "Lord";
        final String COURSE_NAME = "Architecture";
        Input input = new SimulateInput(Arrays.asList("2", FIRST_NAME, "Vladimir", "1", "4", "201", "1", "6"));
        StartApplication startApplication = new StartApplication(input, output, dataSourceJunitDB, dataSourceJunitDB, mockedExecutorQuery);
        startApplication.init();

        boolean actual = false;
        Optional<Student> optionalStudent = studentDAO.getStudentsByCourse(COURSE_NAME)
                .stream().filter(student -> student.getId() == 201).findFirst();
        if (optionalStudent.isPresent()) {
            actual = true;
        }
        assertTrue(actual,
                "Should return true if course was inserted");
    }

    @Test
    public void shouldReturnTrueWhenRemoveCourseFromStudent() {
        final String COURSE_NAME = "Architecture";
        Input input = new SimulateInput(Arrays.asList("2", "Lord", "Vladimir", "1", "4", "201", "1", "5", "201", "1", "6"));
        StartApplication startApplication = new StartApplication(input, output, dataSourceJunitDB, dataSourceJunitDB, mockedExecutorQuery);
        startApplication.init();

        boolean actual = true;
        Optional<Student> optionalStudent = studentDAO.getStudentsByCourse(COURSE_NAME)
                .stream().filter(student -> student.getId() == 201).findFirst();
        if (optionalStudent.isPresent()) {
            actual = false;
        }
        assertTrue(actual,
                "Should return true if course was removed from student");
    }

    @Test
    public void shouldReturnWarningWhenTryFindGroups() {
        final String GROUPS_HAVE_NOT_FOUND = format("Groups have not found.%s", LINE_SEPARATOR);
        Input input = new SimulateInput(Arrays.asList("0", "1", "6"));
        StartApplication startApplication = new StartApplication(input, output, dataSourceJunitDB, dataSourceJunitDB, mockedExecutorQuery);
        startApplication.init();

        String actual = output.toString();
        assertEquals(GROUPS_HAVE_NOT_FOUND, actual,
                "Should return a warning that groups have not found");
    }

    @Test
    public void shouldReturnSomethingWhenTryFindGroups() {
        Input input = new SimulateInput(Arrays.asList("0", "30", "6"));
        StartApplication startApplication = new StartApplication(input, output, dataSourceJunitDB, dataSourceJunitDB, mockedExecutorQuery);
        startApplication.init();

        boolean actual = !output.toString().isEmpty();
        assertTrue(actual,
                "Should return true if groups was found");
    }

    @Test
    public void shouldReturnWarningWhenTryFindStudents() {
        final String STUDENTS_HAVE_NOT_FOUND = format("Students have not found.%s", LINE_SEPARATOR);
        Input input = new SimulateInput(Arrays.asList("1", "blabla", "6"));
        StartApplication startApplication = new StartApplication(input, output, dataSourceJunitDB, dataSourceJunitDB, mockedExecutorQuery);
        startApplication.init();

        String actual = output.toString();
        assertEquals(STUDENTS_HAVE_NOT_FOUND, actual,
                "Should return a warning that students have not found");
    }

    @Test
    public void shouldReturnSomethingWhenTryFindStudents() {
        Input input = new SimulateInput(Arrays.asList("1", "History", "6"));
        StartApplication startApplication = new StartApplication(input, output, dataSourceJunitDB, dataSourceJunitDB, mockedExecutorQuery);
        startApplication.init();

        boolean actual = !output.toString().isEmpty();
        assertTrue(actual,
                "Should return true if students was found");
    }

}