package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.CourseDAO;
import com.foxminded.university.dao.layers.GroupDAO;
import com.foxminded.university.dao.layers.StudentDAO;
import com.foxminded.university.domain.Course;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exceptions.DAOException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.ibatis.jdbc.ScriptRunner;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

/**
 * University DAO
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class UniversitySQL {
    private static final String DROP_DB = "dropDB.SQL";
    private static final String CREATE_DB = "dataBaseCreation.SQL";
    private static final String CREATE_TABLES = "tablesCreation.SQL";

    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    private GroupDAO groupDAO;
    /**
     * Generated data.
     */
    private GenerationTestData generationTestData;

    /**
     * Connection pool and connection fabric (postgres).
     */
    private DataSource dataSourcePostgres;

    /**
     * Connection pool and connection fabric (university).
     */
    private DataSource dataSourceUniversity;

    /**
     * Constructor of the class.
     *
     * @param dataSourcePostgres - dataSource for postgres
     * @param dataSourceUniversity - dataSource for admin
     */
    public UniversitySQL(DataSource dataSourcePostgres, DataSource dataSourceUniversity) {
        if (dataSourcePostgres == null && dataSourceUniversity == null) {
            throw new DAOException("Null was passed to the constructor...");
        }
        generationTestData = new GenerationTestData();
        this.dataSourcePostgres = dataSourcePostgres;
        this.dataSourceUniversity = dataSourceUniversity;
        courseDAO = new CourseSQL(dataSourceUniversity);
        groupDAO = new GroupSQL(dataSourceUniversity);
        studentDAO = new StudentSQL(dataSourceUniversity);
    }

    /**
     * Creates DB, user, tables and insert test data into the tables.
     */
    public void setDateBase() {
        executeQuery(dataSourcePostgres, DROP_DB);
        executeQuery(dataSourcePostgres, CREATE_DB);
        executeQuery(dataSourceUniversity, CREATE_TABLES);

        //get test data
        List<Student> students = generationTestData.getStudents();
        Set<Group> groups = generationTestData.getGroups();
        List<Course> courses = generationTestData.getCourses();

        //insert test data
        studentDAO.insertStudents(students);
        groupDAO.insertGroups(groups);
        courseDAO.insertCourses(courses);

        //get students and courses with id and generate relationship between students and courses
        students = studentDAO.getAllStudents();
        courses = courseDAO.getAllCourses();
        List<Student> studentsWithCourses = generationTestData.getCoursesConnectionToStudent(students, courses);

        //insert the relationship between students and courses
        studentDAO.insertStudentsToCourses(studentsWithCourses);
    }

    /**
     * Drops database.
     */
    public void dropDataBase() {
        dataSourceUniversity.closeConnection();
        executeQuery(dataSourcePostgres, DROP_DB);
    }

    /**
     * Executes a query depends on data source.
     *
     * @param dataSource - data source
     * @param fileName - file name of query
     */
    private void executeQuery(DataSource dataSource, String fileName) {
        try {
            ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
            runner.setAutoCommit(true);
            runner.setStopOnError(true);
            runner.runScript(getResourceAsReader(fileName));
            runner.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
