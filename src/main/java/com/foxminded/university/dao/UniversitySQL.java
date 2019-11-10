package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.Config;
import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.domain.Course;
import com.foxminded.university.domain.CoursesConnection;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exceptions.DAOException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
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
    }

    /**
     * Creates DB, user, tables and insert test data into the tables.
     */
    public void setDateBase() {
        executeQuery(dataSourcePostgres, DROP_DB);
        executeQuery(dataSourcePostgres, CREATE_DB);
        executeQuery(dataSourceUniversity, CREATE_TABLES);
        insertStudents();
        insertGroups();
        insertCourses();
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
     * Inserts students into the student table
     */
    private void insertStudents() {
        List<Student> students = generationTestData.getStudents();
        try (Connection connection = dataSourceUniversity.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement("insert into students(first_name, last_name, group_id)"
                     + " values (?, ?, ?);", Statement.NO_GENERATED_KEYS)) {
            for (Student student : students) {
                prepStatement.setString(1, student.getFirstName());
                prepStatement.setString(2, student.getLastName());
                prepStatement.setInt(3, student.getGroupId());
                prepStatement.addBatch();
            }
            prepStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts groups into the group table
     */
    private void insertGroups() {
        Set<Group> groups = generationTestData.getGroups();
        try (Connection connection = dataSourceUniversity.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement("insert into groups(group_name)"
                     + " values (?);", Statement.NO_GENERATED_KEYS)) {
            for (Group group : groups) {
                prepStatement.setString(1, group.getName());
                prepStatement.addBatch();
            }
            prepStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts courses into the course table
     */
    private void insertCourses() {
        List<Course> courses = generationTestData.getCourses();
        try (Connection connection = dataSourceUniversity.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement("insert into courses(course_name, course_description)"
                     + " values (?, ?);", Statement.NO_GENERATED_KEYS)) {
            for (Course course : courses) {
                prepStatement.setString(1, course.getName());
                prepStatement.setString(2, course.getDescription());
                prepStatement.addBatch();
            }
            prepStatement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
