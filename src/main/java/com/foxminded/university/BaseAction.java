package com.foxminded.university;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class BaseAction implements UserAction {
    private final int key;
    private final String name;

    public BaseAction(final int key, final String name) {
        this.key = key;
        this.name = name;
    }

    @Override
    public int key() {
        return this.key;
    }

    @Override
    public void execute(Input input, UniversitySQL universitySQL) {

    }

    @Override
    public String info() {
        return String.format("%s. %s", this.key, this.name);
    }
}
