package com.foxminded.university.entities;

import java.util.Objects;

/**
 * Group class.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class Group {
    private int id;
    private String name;

    /**
     * Constructor of the class.
     *
     * @param name - name
     */
    public Group(String name) {
        this.name = name;
    }

    /**
     * Constructor of the class.
     *
     * @param id - id
     * @param name - name
     */
    public Group(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        return id == group.id && Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
