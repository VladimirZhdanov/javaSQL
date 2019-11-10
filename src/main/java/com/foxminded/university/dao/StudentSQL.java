package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.StudentDAO;
import com.foxminded.university.domain.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO layer for the students table.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class StudentSQL implements StudentDAO {
    /**
     * Connection pool and connection fabric.
     */
    private DataSource dataSource;

    /**
     * Constructor of the class
     *
     * @param dataSource - connection pool and connection fabric
     */
    public StudentSQL(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Adds student to university.
     *
     * @param student - student
     * @return - added\didn't add - boolean
     */
    @Override
    public boolean insertStudent(Student student) {
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement("insert into students(first_name, last_name, group_id)"
                             + " values (?, ?, ?);",
                     Statement.RETURN_GENERATED_KEYS)) {
            prepStatement.setString(1, student.getFirstName());
            prepStatement.setString(2, student.getLastName());
            prepStatement.setInt(3, student.getGroupId());
            result = prepStatement.executeUpdate();
            try (ResultSet generatedKeys = prepStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    student.setId(id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public boolean deleteStudent(int studentId) {
        int result = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement prepStatement = connection.prepareStatement("DELETE FROM students WHERE student_id = ?;")) {
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
    public List<Student> findStudents(String courseName) {
        List<Student> students = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM students "
                     + "WHERE student_id IN ("
                     + "SELECT student_id "
                     + "FROM courses_connection as cc INNER JOIN courses as c ON c.course_id = cc.course_id "
                     + "WHERE course_name = ?);")) {
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
