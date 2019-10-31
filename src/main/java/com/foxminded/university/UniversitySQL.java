package com.foxminded.university;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import org.apache.ibatis.jdbc.ScriptRunner;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class UniversitySQL implements AutoCloseable {
    GenerateTestData generateTestData;
    private final Config config;
    private Connection connection;

    public UniversitySQL(Config config) {
        generateTestData = new GenerateTestData();
        this.config = config;
        this.config.init();
    }

    public boolean init() {
        try {
            Class.forName(config.get("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.get("url"),
                    config.get("username"),
                    config.get("password"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        setTables();
        return connection != null;
    }

    private void setTables() {
        try {
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setAutoCommit(true);
            runner.setStopOnError(true);
            runner.runScript(getResourceAsReader("tablesCreation.SQL"));
            generateStudents();
            //runner.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean add(Student student) {
        int result = 0;
        try (PreparedStatement prepStatement = connection.prepareStatement("insert into students(first_name, last_name, group_id)"
                + " values (?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
            prepStatement.setString(1, student.getFirstName());
            prepStatement.setString(2, student.getLastName());
            prepStatement.setInt(3, student.getGroupId());
            result = prepStatement.executeUpdate();
            try (ResultSet generatedKeys = prepStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    //student.setId(generatedKeys.getInt(1));
                    System.out.println(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    public boolean delete(int id) {
        int result = 0;
        try (PreparedStatement prepStatement = connection.prepareStatement("DELETE FROM students WHERE student_id = ?;")) {
            prepStatement.setInt(1, id);
            result = prepStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    private void generateStudents() {
        List<Student> students = generateTestData.getStudents();
        try (PreparedStatement prepStatement = connection.prepareStatement("insert into students(first_name, last_name, group_id)"
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

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}
