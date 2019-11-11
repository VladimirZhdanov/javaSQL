package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.CourseDAO;
import com.foxminded.university.dao.layers.GroupDAO;
import com.foxminded.university.dao.layers.StudentDAO;
import com.foxminded.university.domain.CoursesConnection;
import com.foxminded.university.exceptions.DAOException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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

    CourseDAO courseDAO;
    StudentDAO studentDAO;
    GroupDAO groupDAO;
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
        studentDAO.insertStudents(generationTestData.getStudents());
        groupDAO.insertGroups(generationTestData.getGroups());
        courseDAO.insertCourses(generationTestData.getCourses());
        insertCourseConnections();
    }

    /**
     * Drops database.
     */
    public void dropDataBase() {
        dataSourceUniversity.closeConnection();
        executeQuery(dataSourcePostgres, DROP_DB);
    }

    /**
     * Inserts relationships between students and courses into the CourseConnections table
     */
    private void insertCourseConnections() {
        List<CoursesConnection> coursesConnections = generationTestData.getRelationshipBetweenStudentsAndCourses();
        try (Connection connection = dataSourceUniversity.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement("insert into courses_connection(student_id, course_id)"
                     + " values (?, ?);", Statement.NO_GENERATED_KEYS)) {
            for (CoursesConnection coursesConnection : coursesConnections) {
                prepStatement.setInt(1, coursesConnection.getStudentId());
                prepStatement.setInt(2, coursesConnection.getCourseId());
                prepStatement.addBatch();
            }
            prepStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
