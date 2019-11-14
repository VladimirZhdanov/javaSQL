package com.foxminded.university.dao.layers;

import com.foxminded.university.domain.Course;
import com.foxminded.university.domain.Student;
import java.util.List;
import java.util.Map;

/**
 * Student DAU
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public interface StudentDAO {

    /**
     * Adds student to university.
     *
     * @param student - student
     * @return - added\didn't add - boolean
     */
    boolean insert(Student student);

    /**
     * Inserts passed students.
     *
     * @param students - students
     */
    void insert(List<Student> students);

    /**
     * Inserts relationship: Student - Course.
     *
     * @param studentsWithCourses - students with relationship: Student - Course
     */
    void insertRelationshipStudentsToCourses(List<Student> studentsWithCourses);

    /**
     * Inserts a course to a student.
     *
     * @param studentId - student id
     * @param courseId - student id
     * @return - added\didn't add - boolean
     */
    boolean insertCourseToStudentById(int studentId, int courseId);

    /**
     * Finding the student by id.
     *
     * @param studentId - student id
     * @return - Student instance
     */
    Student getStudent(int studentId);

    /**
     * Gets all students.
     *
     * @return - all students
     */
    List<Student> getAllStudents();

    /**
     * Finds all students related to the passed course name.
     *
     * @param courseName - course name
     * @return - list of student
     */
    List<Student> getStudentsByCourse(String courseName);

    /**
     * Removes a student from university by id.
     *
     * @param studentId - student id.
     * @return deleted/did not - boolean
     */
    boolean removeStudentById(int studentId);
}
