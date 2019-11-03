package com.foxminded.university;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.jdbc.ScriptRunner;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class UniversitySQL implements AutoCloseable {
    private GenerateTestData generateTestData;
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
                    System.out.println(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    public boolean addStudentToCourse(int studentId, int courseId) {
        int result = 0;
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT COUNT(*) FROM courses_connection WHERE student_id = ? AND course_id = ?;");

             PreparedStatement addStatement = connection.prepareStatement("insert into courses_connection(student_id, course_id) values (?, ?);")) {
            selectStatement.setInt(1, studentId);
            selectStatement.setInt(2, courseId);
            boolean studentExistence = findStudent(studentId) != null;
            boolean courseExistence = findCourse(courseId) != null;
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    if (resultSet.getInt(1) == 0 && studentExistence && courseExistence) {
                        addStatement.setInt(1, studentId);
                        addStatement.setInt(2, courseId);
                        result = addStatement.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    public boolean delete(int studentId) {
        int result = 0;
        try (PreparedStatement prepStatement = connection.prepareStatement("DELETE FROM students WHERE student_id = ?;")) {
            prepStatement.setInt(1, studentId);
            result = prepStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    public boolean removeCourse(int studentId, int courseId) {
        int result = 0;
        try (PreparedStatement prepStatement = connection.prepareStatement("DELETE FROM courses_connection WHERE student_id = ? and course_id = ?;")) {
            prepStatement.setInt(1, studentId);
            prepStatement.setInt(2, courseId);
            result = prepStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    private String findAllCourses(int studentId) {
        String result = String.format("The student with id-%d is not listed in any courses", studentId);
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT course_id FROM courses_connection WHERE student_id = ?;")) {
            selectStatement.setInt(1, studentId);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                StringBuilder stringBuilder = new StringBuilder();
                while (resultSet.next()) {
                    stringBuilder.append(resultSet.getInt(1)).append(" ");
                }
                result = String.format("The student is listed in follow courses with id: %s", stringBuilder.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Student findStudent(int studentId) {
        Student result = null;
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM students WHERE student_id = ?;")) {
            selectStatement.setInt(1, studentId);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    result = new Student(resultSet.getInt("student_id"),
                            resultSet.getInt("group_id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Course findCourse(int courseId) {
        Course result = null;
        try (PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM courses WHERE course_id = ?;")) {
            selectStatement.setInt(1, courseId);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    result = new Course(resultSet.getInt("course_id"),
                            resultSet.getString("course_name"),
                            resultSet.getString("course_description"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void setTables() {
        try {
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setAutoCommit(true);
            runner.setStopOnError(true);
            runner.runScript(getResourceAsReader("tablesCreation.SQL"));
            generateStudents();
            generateGroups();
            generateCourses();
            generateCourseConnections();
            //runner.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void generateGroups() {
        Set<Group> groups = generateTestData.getGroups();
        try (PreparedStatement prepStatement = connection.prepareStatement("insert into groups(group_name)"
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

    private void generateCourses() {
        List<Course> courses = generateTestData.getCourses();
        try (PreparedStatement prepStatement = connection.prepareStatement("insert into courses(course_name, course_description)"
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

    private void generateCourseConnections() {
        List<CoursesConnection> coursesConnections = generateTestData.getRelationshipBetweenStudentsAndCourses();
        try (PreparedStatement prepStatement = connection.prepareStatement("insert into courses_connection(student_id, course_id)"
                + " values (?, ?);", Statement.NO_GENERATED_KEYS)) {
            for (CoursesConnection coursesConnection : coursesConnections) {
                prepStatement.setInt(1, coursesConnection.getIdOfStudent());
                prepStatement.setInt(2, coursesConnection.getIdOfCourse());
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
