package com.foxminded.university.domain;

/**
 * Course class.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class Course {
    private int id;
    private String name;
    private String description;

    /**
     * Constructor of the class.
     */
    public Course() {
    }

    /**
     * Constructor of the class.
     *
     * @param name - name
     * @param description - description
     */
    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Constructor of the class.
     *
     * @param id - id
     * @param name - name
     * @param description - description
     */
    public Course(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Course{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + '}';
    }
}
