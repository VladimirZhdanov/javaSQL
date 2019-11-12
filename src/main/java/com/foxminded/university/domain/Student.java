package com.foxminded.university.domain;

import java.util.List;
import java.util.Objects;

/**
 * Student class.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class Student {
    private int id;
    private int groupId;
    private String firstName;
    private String lastName;
    private List<Course> courses;

    /**
     * Constructor of the class.
     *
     * @param firstName - first name
     * @param lastName - last name
     */
    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Constructor of the class.
     *
     * @param firstName - first name
     * @param lastName - last name
     * @param groupId - group id
     */
    public Student(String firstName, String lastName, int groupId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
    }

    /**
     * Constructor of the class.
     *
     * @param id - id
     * @param groupId - group id
     * @param firstName - first name
     * @param lastName - last name
     */
    public Student(int id, int groupId, String firstName, String lastName) {
        this.id = id;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student() {
    }

    /**
     * Constructor of the class.
     *
     * @param id - id
     * @param groupId - group id
     * @param firstName - first name
     * @param lastName - last name
     * @param courses - courses
     */
    public Student(int id, int groupId, String firstName, String lastName, List<Course> courses) {
        this.id = id;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.courses = courses;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return id == student.id
                && firstName.equals(student.firstName)
                && lastName.equals(student.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }

    @Override
    public String toString() {
        return "Student{"
                + "id=" + id
                + ", groupId=" + groupId
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + '}';
    }
}
