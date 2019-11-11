package com.foxminded.university.dao.layers;

import com.foxminded.university.domain.Course;
import java.util.List;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public interface CourseDAO {
    /**
     * Gets course by ID.
     *
     * @param courseId - course id
     * @return Course instance
     */
    Course getCourse(int courseId);

    /**
     * Inserts courses into the course table
     */
    void insertCourses(List<Course> courses);

    /**
     * Gets all courses.
     *
     * @return - all courses
     */
    List<Course> getAllCourses();

    /**
     * Gets courses related to passed student id.
     *
     * @param studentId - student id
     * @return - courses
     */
    List<Course> getByStudentId(int studentId);

    /**
     * Removes course from a student
     *
     * @param studentId - student id
     * @param courseId - course id
     * @return - removed / didn't remove - boolean
     */
    boolean removeCourse(int studentId, int courseId);
}
