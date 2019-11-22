package com.foxminded.university.ui;

/**
 * Base implementation of user action.(Will be used as inner class for event)
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class BaseAction implements UserAction {
    private final int key;
    private final String name;

    /**
     * Constructor of the class.
     *
     * @param key - key of the action
     * @param name - action name
     */
    public BaseAction(final int key, final String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * Gets key of action.
     *
     * @return - key
     */
    @Override
    public int key() {
        return this.key;
    }

    /**
     * Executes passed action.
     *
     * @param input - input
     */
    @Override
    public void execute(Input input) {

    }

    /**
     * Gets information of the action.
     *
     * @return - information o the action
     */
    @Override
    public String info() {
        return String.format("%s. %s", this.key, this.name);
    }
}
