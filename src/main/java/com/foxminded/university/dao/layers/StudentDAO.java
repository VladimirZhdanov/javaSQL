package com.foxminded.university.dao.layers;

import com.foxminded.university.domain.Student;
import java.util.List;

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
}
