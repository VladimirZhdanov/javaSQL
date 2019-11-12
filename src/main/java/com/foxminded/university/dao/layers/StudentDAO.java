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
    boolean insertStudent(Student student);

    /**
     * Deletes a student from university by id.
     *
     * @param studentId - student id.
     * @return deleted/did not - boolean
     */
    boolean deleteStudent(int studentId);

    /**
     * Finds all students related to the passed course name.
     *
     * @param courseName - course name
     * @return - list of student
     */
    List<Student> findStudents(String courseName);

    /**
     * Gets all students.
     *
     * @return - all students
     */
    List<Student> getAllStudents();

    /**
     * Inserts passed students.
     *
     * @param students - students
     */
    void insertStudents(List<Student> students);

    /**
     * Inserts relationship: Student - Course.
     *
     * @param studentToCourses - relationship: Student - Course
     */
    void insertStudentsToCourses(Map<Student, List<Course>> studentToCourses);

    /**
     * Adds a course to a student.
     *
     * @param studentId - student id
     * @param courseId - student id
     * @return - added\didn't add - boolean
     */
    boolean addCourse(int studentId, int courseId);

    /**
     * Removes course from a student
     *
     * @param studentId - student id
     * @param courseId - course id
     * @return - removed / didn't remove - boolean
     */
    boolean removeCourse(int studentId, int courseId);

    /**
     * Finding the student by id.
     *
     * @param studentId - student id
     * @return - Student instance
     */
    Student getStudent(int studentId);
}
