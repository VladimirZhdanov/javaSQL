package com.foxminded.university;

import java.util.Objects;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class Student {
    private int id;
    private int groupId;
    private String firstName;
    private String lastName;

    public Student(String firstName, String lastName, int groupId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupId = groupId;
    }

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Student(int id, int groupId, String firstName, String lastName) {
        this.id = id;
        this.groupId = groupId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
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
}
