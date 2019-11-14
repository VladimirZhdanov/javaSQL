package com.foxminded.university.ui;

import com.foxminded.university.dao.layers.CourseDAO;
import com.foxminded.university.dao.layers.GroupDAO;
import com.foxminded.university.dao.layers.StudentDAO;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.Integer.parseInt;

/**
 * Menu od the application.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class Menu {

    /**
     * Line separator depends on user's OS.
     */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /**
     * Input.
     */
    private Input input;

    /**
     * Output
     */
    private final Consumer<String> output;

    private StudentDAO studentDAO;
    private GroupDAO groupDAO;
    private CourseDAO courseDAO;

    /**
     * Actions(0 - add student, 1 - remover student by id etc).
     */
    private List<UserAction> actions = new ArrayList<>();

    /**
     * Constructor of the class.
     *
     * @param input - input
     * @param output - output
     */
    public Menu(Input input, Consumer<String> output, GroupDAO groupDAO, StudentDAO studentDAO, CourseDAO courseDAO) {
        this.input = input;
        this.output = output;
        this.groupDAO = groupDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
    }

    /**
     * Gets range of the menu(actions)
     *
     * @return - range of the menu(actions)
     */
    public List<Integer> getRangeOfMenu() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < this.actions.size(); i++) {
            result.add(this.actions.get(i).key());
        }
        return result;
    }

    /**
     * Inner class to add student.
     */
    private class AddStudent extends BaseAction {

        /**
         * Constructor of the inner class.
         *
         * @param key - key of action
         * @param name - name of the action
         */
        public AddStudent(int key, String name) {
            super(key, name);
        }

        /**
         * Executes passed action.
         *
         * @param input - input
         */
        @Override
        public void execute(Input input) {
            String firstName = input.ask("Enter a first name of the student :");
            String lastName = input.ask("Enter a last name of the student :");
            String groupId = input.ask("Enter a group id for the student :");
            while (!groupId.matches("[0-9]+")) {
                groupId = input.ask("Enter a group id for teh student :");
            }
            Student student = new Student(parseInt(groupId), firstName, lastName);
            if (!studentDAO.insert(student)) {
                output.accept("The student has not been added!");
            } else {
                System.out.printf("Student ID: %d, first name: %s, last name: %s, group ID: %d added.%s", student.getId(), student.getFirstName(), student.getLastName(), student.getGroupId(), LINE_SEPARATOR);
            }
        }
    }

    /**
     * Inner class to add course to student.
     */
    private class AddCourseToStudent extends BaseAction {

        /**
         * Constructor of the inner class.
         *
         * @param key - key of action
         * @param name - name of the action
         */
        public AddCourseToStudent(int key, String name) {
            super(key, name);
        }

        /**
         * Executes passed action.
         *
         * @param input - input
         */
        @Override
        public void execute(Input input) {
            String studentId = input.ask("Enter the student id :");
            String courseId = input.ask("Enter a course id :");
            while (!(studentId.matches("[0-9]+") && courseId.matches("[0-9]+"))) {
                studentId = input.ask("Enter the student id :");
                courseId = input.ask("Enter a course id :");
            }
            if (!studentDAO.insertCourseToStudentById(parseInt(studentId), parseInt(courseId))) {
                output.accept("The course can't be added!");
            } else {
                System.out.printf("Course ID: %s has been added to student ID: %s%s", courseId, studentId, LINE_SEPARATOR);
            }
        }
    }

    /**
     * Inner class to removeCourseByStudentIdAndCourseId course from student.
     */
    private class RemoveCourseFromStudent extends BaseAction {

        /**
         * Constructor of the inner class.
         *
         * @param key - key of action
         * @param name - name of the action
         */
        public RemoveCourseFromStudent(int key, String name) {
            super(key, name);
        }

        /**
         * Executes passed action.
         *
         * @param input - input
         */
        @Override
        public void execute(Input input) {
            String studentId = input.ask("Enter the student id :");
            String courseId = input.ask("Enter a course id :");
            while (!(studentId.matches("[0-9]+") && courseId.matches("[0-9]+"))) {
                studentId = input.ask("Enter the student id :");
                courseId = input.ask("Enter a course id :");
            }
            if (!courseDAO.removeCourseByStudentIdAndCourseId(parseInt(studentId), parseInt(courseId))) {
                output.accept("The course can't be removed!");
            } else {
                System.out.printf("Course ID: %s has been removed from student ID: %s%s", courseId, studentId, LINE_SEPARATOR);
            }
        }
    }

    /**
     * Inner class to find all groups with less or equals student count.
     */
    private class FindGroups extends BaseAction {

        /**
         * Constructor of the inner class.
         *
         * @param key  - key of action
         * @param name - name of the action
         */
        public FindGroups(int key, String name) {
            super(key, name);
        }

        /**
         * Executes passed action.
         *
         * @param input - input
         */
        @Override
        public void execute(Input input) {
            String amountStudents = input.ask("Enter student amount :");
            while (!amountStudents.matches("[0-9]+")) {
                amountStudents = input.ask("Enter student amount :");
            }
            List<Group> groups = groupDAO.getGroupsByStudentCount(parseInt(amountStudents));
            if (groups.size() == 0) {
                System.out.println("Groups have not found.");
            } else {
                groups.forEach(System.out::println);
            }
        }
    }

    /**
     * Inner class to find all students related to course with given name.
     */
    private class FindStudents extends BaseAction {

        /**
         * Constructor of the inner class.
         *
         * @param key  - key of action
         * @param name - name of the action
         */
        public FindStudents(int key, String name) {
            super(key, name);
        }

        /**
         * Executes passed action.
         *
         * @param input         - input
         */
        @Override
        public void execute(Input input) {
            String courseName = input.ask("Enter course name :");
            List<Student> students = studentDAO.getStudentsByCourse(courseName);
            if (students.size() == 0) {
                System.out.println("Students have not found.");
            } else {
                students.forEach(System.out::println);
            }
        }
    }

    /**
     * Inner class to removeCourseByStudentIdAndCourseId student from university.
     */
    private class RemoveStudent extends BaseAction {

        /**
         * Constructor of the inner class.
         *
         * @param key - key of action
         * @param name - name of the action
         */
        public RemoveStudent(int key, String name) {
            super(key, name);
        }

        /**
         * Executes passed action.
         *
         * @param input - input
         */
        @Override
        public void execute(Input input) {
            String id = input.ask("Enter student id :");
            while (!id.matches("[0-9]+")) {
                id = input.ask("Enter student id :");
            }
            if (!studentDAO.removeStudentById(parseInt(id))) {
                output.accept("The id is not exist!");
            } else {
                output.accept(String.format("------------ The student (id: %s) is not longer in our university -----------", id));
            }
        }
    }


    /**
     * Inner class to exit from the program.
     */
    private class ExitProgram extends BaseAction {

        /**
         * Start point of the application.
         */
        private final StartApplication ui;

        /**
         * Constructor of the inner class.
         *
         * @param key - key of action
         * @param name - name of the action
         */
        public ExitProgram(StartApplication ui, int key, String name) {
            super(key, name);
            this.ui = ui;
        }

        /**
         * Executes passed action.
         *
         * @param input - input
         */
        @Override
        public void execute(Input input) {
            this.ui.stop();
        }
    }

    /**
     * Fill up actions list.
     *
     * @param ui - Start point of the application.
     */
    public void fillActions(StartApplication ui) {
        this.actions.add(this.new FindGroups(0, "Find all groups with less or equals student count"));
        this.actions.add(this.new FindStudents(1, "Find all students related to course with given name"));
        this.actions.add(this.new AddStudent(2, "Add new student"));
        this.actions.add(this.new RemoveStudent(3, "Delete student by STUDENT_ID"));
        this.actions.add(this.new AddCourseToStudent(4, "Add a student to the course (from a list)"));
        this.actions.add(this.new RemoveCourseFromStudent(5, "Remove the student from one of his or her courses"));
        this.actions.add(this.new ExitProgram(ui, 6, "Exit Program"));
    }

    /**
     * Executes passed action.
     *
     * @param key - key of the action
     */
    public void select(int key) {
        this.actions.get(key).execute(input);
    }

    /**
     * Shows menu of the application.
     */
    public void show() {
        System.out.println("Menu.");
        for (UserAction action : this.actions) {
            if (action != null) {
                System.out.println(action.info());
            }
        }
    }
}
