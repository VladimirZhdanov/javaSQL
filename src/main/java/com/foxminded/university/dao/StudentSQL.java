package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.StudentDAO;
import com.foxminded.university.domain.Course;
import com.foxminded.university.domain.Student;
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

import static java.sql.Statement.NO_GENERATED_KEYS;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * DAO layer for the students table.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class StudentSQL implements StudentDAO {

    private static final String NULL_WAS_PASSED = "Null was passed";

    private Properties properties;

    /**
     * Connection pool and connection fabric.
     */
    private DataSource dataSource;

    private CourseSQL courseSQL;

    /**
     * Constructor of the class
     *
     * @param dataSource - connection pool and connection fabric
     */
    public StudentSQL(DataSource dataSource) {
        this.dataSource = dataSource;
        courseSQL = new CourseSQL(dataSource);
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
     * Adds student to university.
     *
     * @param student - student
     * @return - added\didn't add - boolean
     */
    @Override
    public boolean insert(Student student) {
        boolean result = false;

        if (student == null) {
            throw new DAOException(NULL_WAS_PASSED);
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(properties.getProperty("insertStudent"),
                     RETURN_GENERATED_KEYS)) {
            result = insertStudentToDB(statement, student);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Inserts passed students.
     *
     * @param students - students
     */
    @Override
    public boolean insert(List<Student> students) {
        boolean result = false;

        if (students == null) {
            throw new DAOException(NULL_WAS_PASSED);
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(properties.getProperty("insertStudents"),
                     RETURN_GENERATED_KEYS)) {
            for (Student student : students) {
                result = insertStudentToDB(statement, student);
            }
            statement.executeBatch();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean insertStudentToDB(PreparedStatement statement, Student student) throws SQLException {
        int result = 0;

        statement.setString(1, student.getFirstName());
        statement.setString(2, student.getLastName());
        statement.setInt(3, student.getGroupId());
        result = statement.executeUpdate();
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                student.setId(id);
            }
        }
        return result == 1;
    }

    /**
     * Deletes a student from university by id.
     *
     * @param studentId - student id.
     * @return deleted/did not - boolean
     */
    @Override
    public boolean removeStudentById(int studentId) {
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement(properties.getProperty("deleteStudent"))) {
            prepStatement.setInt(1, studentId);
            result = prepStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    /**
     * Finds all students related to the passed course name.
     *
     * @param courseName - course name
     * @return - list of student
     */
    @Override
    public List<Student> getStudentsByCourse(String courseName) {
        if (courseName == null) {
            throw new DAOException(NULL_WAS_PASSED);
        }
        List<Student> students = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(properties.getProperty("findStudents"))) {
            statement.setString(1, courseName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(extractStudent(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Gets all students.
     *
     * @return - all students
     */
    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(properties.getProperty("getAllStudents"))) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(extractStudent(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Adds a course to a student.
     *
     * @param studentId - student id
     * @param courseId - student id
     * @return - added\didn't add - boolean
     */
    @Override
    public boolean insertCourseToStudentById(int studentId, int courseId) {
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(properties.getProperty("countStudentToCourses"));

             PreparedStatement addStatement = connection.prepareStatement(properties.getProperty("addCourse"))) {
            selectStatement.setInt(1, studentId);
            selectStatement.setInt(2, courseId);
            boolean studentExistence = getStudent(studentId) != null;
            boolean courseExistence = courseSQL.getCourseById(courseId) != null;
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
     * Gets the student by id.
     *
     * @param studentId - student id
     * @return - Student instance
     */
    @Override
    public Student getStudent(int studentId) {
        Student result = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(properties.getProperty("getStudent"))) {
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
     * Inserts relationship: Student - Course.
     *
     * @param students - students with relationship: Student - Course
     */
    @Override
    public void insertRelationshipStudentsToCourses(List<Student> students) {
        if (students == null) {
            throw new DAOException(NULL_WAS_PASSED);
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(properties.getProperty("insertStudentsToCourses"), NO_GENERATED_KEYS)) {

            students.forEach(student -> {
                List<Course> courses = student.getCourses();
                courses.forEach(course -> {
                    try {
                        statement.setInt(1, student.getId());
                        statement.setInt(2, course.getId());
                        statement.addBatch();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            });
            statement.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts a student from a ResultSet.
     *
     * @param rs - ResultSet
     * @return - Student
     * @throws SQLException - SQLException
     */
    private Student extractStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("student_id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setGroupId(rs.getInt("group_id"));
        return student;
    }
}
