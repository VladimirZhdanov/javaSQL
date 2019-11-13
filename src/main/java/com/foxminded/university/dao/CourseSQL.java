package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.CourseDAO;
import com.foxminded.university.domain.Course;
import com.foxminded.university.exceptions.DAOException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class CourseSQL implements CourseDAO {

    private Properties properties;

    /**
     * Connection pool and connection fabric.
     */
    private DataSource dataSource;

    /**
     * Constructor of the class
     *
     * @param dataSource - connection pool and connection fabric
     */
    public CourseSQL(DataSource dataSource) {
        this.dataSource = dataSource;
        properties = new Properties();
        init();
    }

    /**
     * Initialisation properties.
     */
    private void init() {
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("queriesDAO.properties")) {
            if (is == null) {
                throw new DAOException("Null was passed.");
            }
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets course by ID.
     *
     * @param courseId - course id
     * @return Course instance
     */
    @Override
    public Course getCourseById(int courseId) {
        Course result = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(properties.getProperty("getCourse"))) {
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

    /**
     * Inserts courses into the course table
     */
    @Override
    public void insert(List<Course> courses) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(properties.getProperty("insertCourses"), Statement.NO_GENERATED_KEYS)) {
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
     * Gets all courses.
     *
     * @return - all courses
     */
    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(properties.getProperty("getAllCourses"))) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    courses.add(extractCourse(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Gets courses related to passed student id.
     *
     * @param studentId - student id
     * @return - courses
     */
    @Override
    public List<Course> getCoursesByStudentId(int studentId) {
        List<Course> courses = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(properties.getProperty("getByStudentId"))) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    courses.add(extractCourse(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    /**
     * Removes course from a student
     *
     * @param studentId - student id
     * @param courseId - course id
     * @return - removed / didn't removeCourseByStudentIdAndCourseId - boolean
     */
    @Override
    public boolean removeCourseByStudentIdAndCourseId(int studentId, int courseId) {
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(properties.getProperty("removeCourse"))) {
            prepStatement.setInt(1, studentId);
            prepStatement.setInt(2, courseId);
            result = prepStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    /**
     * Extracts a course from a ResultSet.
     *
     * @param rs - ResultSet
     * @return - Course
     * @throws SQLException - SQLException
     */
    private Course extractCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("course_id"));
        course.setName(rs.getString("course_name"));
        course.setDescription(rs.getString("course_description"));
        return course;
    }
}
