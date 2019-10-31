package com.foxminded.university;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class CoursesConnection {
    private int idOfStudent;
    private int idOfCourse;

    public CoursesConnection(int idOfStudent, int idOfCourse) {
        this.idOfStudent = idOfStudent;
        this.idOfCourse = idOfCourse;
    }

    public int getIdOfStudent() {
        return idOfStudent;
    }

    public int getIdOfCourse() {
        return idOfCourse;
    }
}
