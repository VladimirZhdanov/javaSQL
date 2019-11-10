package com.foxminded.university.dao.layers;

/**
 * CourseConnection DAO
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public interface CoursesConnectionDAO {

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
}
