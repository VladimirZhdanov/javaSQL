package com.foxminded.university;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public interface UserAction {
    int key();
    void execute(Input input, UniversitySQL universitySQL);
    String info();
}
