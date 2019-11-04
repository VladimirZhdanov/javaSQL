package com.foxminded.university.entities;

/**
 * Class for many to many relationship between courses and students.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class CoursesConnection {
    private int studentId;
    private int courseId;

    /**
     * Constructor of the class.
     *
     * @param studentId - student id
     * @param courseId - course id
     */
    public CoursesConnection(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getCourseId() {
        return courseId;
    }
}
