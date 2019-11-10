package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.CoursesConnectionDAO;
import com.foxminded.university.domain.Course;
import com.foxminded.university.domain.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO layer for the courses_connection table.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class CoursesConnectionSQL implements CoursesConnectionDAO {
    /**
     * Connection pool and connection fabric.
     */
    private DataSource dataSource;

    /**
     * Constructor of the class
     *
     * @param dataSource - connection pool and connection fabric
     */
    public CoursesConnectionSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Adds a course to a student.
     *
     * @param studentId - student id
     * @param courseId - student id
     * @return - added\didn't add - boolean
     */
    @Override
    public boolean addCourse(int studentId, int courseId) {
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT COUNT(*) FROM courses_connection WHERE student_id = ? AND course_id = ?;");

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

    /**
     * Removes course from a student
     *
     * @param studentId - student id
     * @param courseId - course id
     * @return - removed / didn't remove - boolean
     */
    @Override
    public boolean removeCourse(int studentId, int courseId) {
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement("DELETE FROM courses_connection WHERE student_id = ? and course_id = ?;")) {
            prepStatement.setInt(1, studentId);
            prepStatement.setInt(2, courseId);
            result = prepStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    /**
     * Finding the student by id.
     *
     * @param studentId - student id
     * @return - Student instance
     */
    private Student findStudent(int studentId) {
        Student result = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM students WHERE student_id = ?;")) {
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

    /**
     * Seeking course by ID.
     *
     * @param courseId - course id
     * @return Course instance
     */
    private Course findCourse(int courseId) {
        Course result = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement("SELECT * FROM courses WHERE course_id = ?;")) {
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
}
