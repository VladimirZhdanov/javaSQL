package com.foxminded.university.ui;

import com.foxminded.university.exceptions.MenuOutException;
import java.util.List;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class SimulateInput implements Input {
    /**
     * This field contains the sequence of user responses.
     */
    private final List<String> value;

    /**
     * Counter of ask() call.
     */
    private int position = 0;

    public SimulateInput(final List<String> value) {
        this.value = value;
    }

    /**
     * Each time you call the ask method, we increase the counter and
     * the next time it calls, it will return to us a new value.
     *
     * @param question - request
     * @return - response
     */
    @Override
    public String ask(String question) {
        return this.value.get(this.position++);
    }

    /**
     * Gets user input from a source.
     *
     * @param question - request
     * @param range - range of the menu
     * @return - key
     */
    @Override
    public int ask(String question, List<Integer> range) {
        int key = Integer.parseInt(this.value.get(this.position++));
        boolean exist = false;
        for (int val : range) {
            if (val == key) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            throw new MenuOutException("Out of menu range.");
        }
        return key;
    }
}
