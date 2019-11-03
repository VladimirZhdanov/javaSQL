package com.foxminded.university;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.Integer.parseInt;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class Menu {

    /**
     * Line separator depends on user's OS.
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private Input input;
    private final Consumer<String> output;
    private UniversitySQL universitySQL;
    private List<UserAction> actions = new ArrayList<>();

    public Menu(Input input, Consumer<String> output, UniversitySQL universitySQL) {
        this.input = input;
        this.output = output;
        this.universitySQL = universitySQL;
    }

    public List<Integer> getRangeOfMenu() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < this.actions.size(); i++) {
            result.add(this.actions.get(i).key());
        }
        return result;
    }

    private class AddStudent extends BaseAction {

        public AddStudent(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, UniversitySQL universitySQL) {
            String firstName = input.ask("Enter a first name of the student :");
            String lastName = input.ask("Enter a last name of the student :");
            String groupId = input.ask("Enter a group id for the student :");
            while (!groupId.matches("[0-9]+")) {
                groupId = input.ask("Enter a group id for teh student :");
            }
            Student student = new Student(firstName, lastName, parseInt(groupId));
            if(!universitySQL.add(student)) {
                output.accept("The student has not been added!");
            } else {
                System.out.printf("Student ID: %d, first name: %s, last name: %s, group ID: %d added.%s", student.getId(), student.getFirstName(), student.getLastName(), student.getGroupId(), LINE_SEPARATOR);
            }
        }
    }

    private class AddCourseToStudent extends BaseAction {

        public AddCourseToStudent(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, UniversitySQL universitySQL) {
            String studentId = input.ask("Enter the student id :");
            String courseId = input.ask("Enter a course id :");
            while (!(studentId.matches("[0-9]+") && courseId.matches("[0-9]+"))) {
                studentId = input.ask("Enter the student id :");
                courseId = input.ask("Enter a course id :");
            }
            if(!universitySQL.addStudentToCourse(parseInt(studentId), parseInt(courseId))) {
                output.accept("The course can't be added!");
            } else {
                System.out.printf("Course ID: %s has been added to student ID: %s%s", courseId, studentId, LINE_SEPARATOR);
            }
        }
    }

    private class RemoveCourseToStudent extends BaseAction {

        public RemoveCourseToStudent(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, UniversitySQL universitySQL) {
            String studentId = input.ask("Enter the student id :");
            String courseId = input.ask("Enter a course id :");
            while (!(studentId.matches("[0-9]+") && courseId.matches("[0-9]+"))) {
                studentId = input.ask("Enter the student id :");
                courseId = input.ask("Enter a course id :");
            }
            if(!universitySQL.removeCourse(parseInt(studentId), parseInt(courseId))) {
                output.accept("The course can't be removed!");
            } else {
                System.out.printf("Course ID: %s has been removed from student ID: %s%s", courseId, studentId, LINE_SEPARATOR);
            }
        }
    }

    private class RemoveStudent extends BaseAction {

        public RemoveStudent(int key, String name) {
            super(key, name);
        }

        @Override
        public void execute(Input input, UniversitySQL universitySQL) {
            String id = input.ask("Enter student id :");
            while (!id.matches("[0-9]+")) {
                id = input.ask("Enter student id :");
            }
            if (!universitySQL.delete(parseInt(id))) {
                output.accept("The id is not exist!");
            } else {
                output.accept(String.format("------------ The student (id: %s) is not longer in our university -----------", id));
            }
        }
    }

    private class ExitProgram extends BaseAction {
        private final StartUI ui;

        public ExitProgram(StartUI ui, int key, String name) {
            super(key, name);
            this.ui = ui;
        }

        @Override
        public void execute(Input input, UniversitySQL universitySQL) {
            this.ui.stop();
        }

        @Override
        public String info() {
            return String.format("%s. %s", this.key(), "Exit Program");
        }
    }

    public void fillActions(StartUI ui) {
        this.actions.add(this.new AddStudent(0, "Add a new student"));
        this.actions.add(this.new RemoveStudent(1, "Remove a student from the university"));
        this.actions.add(this.new AddCourseToStudent(2, "Add a course to a student"));
        this.actions.add(this.new RemoveCourseToStudent(3, "Remove a course from a student"));
        this.actions.add(this.new ExitProgram(ui, 4, "Exit Program"));
    }

    public void select(int key) {
        this.actions.get(key).execute(input, universitySQL);
    }

    public void show() {
        System.out.println("Menu.");
        for (UserAction action : this.actions) {
            if (action != null) {
                System.out.println(action.info());
            }
        }
    }
}
