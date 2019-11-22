package com.foxminded.university.ui;

import com.foxminded.university.dao.UniversitySQL;

/**
 * Class for user action.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public interface UserAction {

    /**
     * Gets key of action.
     *
     * @return - key
     */
    int key();

    /**
     * Executes passed action.
     *
     * @param input - input
     */
    void execute(Input input);

    /**
     * Gets information of the action.
     *
     * @return - information o the action
     */
    String info();
}
