package com.foxminded.university;

import java.util.List;

/**
 * Interface to get input from a source.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public interface Input {

    /**
     * Gets user input from a source.
     *
     * @param question - request
     * @return - response
     */
    String ask(String question);

    /**
     * Gets user input from a source.
     *
     * @param question - request
     * @param range - range of the menu
     * @return - response
     */
    int ask(String question, List<Integer> range);
}
