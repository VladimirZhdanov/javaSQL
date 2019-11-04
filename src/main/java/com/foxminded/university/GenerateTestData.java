package com.foxminded.university;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * Class for generation test data into SQL data base.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class GenerateTestData {
    /**
     * List of first names.
     */
    private final List<String> firstNames;

    /**
     * List of last names.
     */
    private final List<String> lastNames;

    /**
     * Random instance to generate random numbers.
     */
    private Random random;

    /**
     * Constructor of the class.
     */
    public GenerateTestData() {
        this.random = new Random();
        firstNames = List.of("Liam", "Noah", "William", "James", "Oliver", "Benjamin", "Elijah", "Lucas",
                "Mason", "Logan", "Alexander", "Ethan", "Jacob", "Michael", "Daniel", "Henry", "Jackson", "Sebastian", "Aiden", "Matthew");
        lastNames = List.of("Smith", "Johnson", "Williams", "Jones", "Rodríguez", "Torres", "Reyes", "Ruíz",
                "Aguilar", "Ortíz", "Moreno", "Chávez", "Ramos", "Herrera", "Medina", "Vargas", "Castro", "Guzmán", "Fernández", "Rojas");
    }

    /**
     * Gets 10 courses.
     * @return - 10 courses
     */
    public List<Course> getCourses() {
        return List.of(new Course("Architecture", "Architecture of computer"),
                new Course("Engineering", "Computer Engineering"),
                new Course("History", "Computer History"),
                new Course("Linguistics", "Second language"),
                new Course("Philosophy", "Modern philosophy"),
                new Course("Building", "Self-build"),
                new Course("Sociology", "Communism and the impact to our capital countries"),
                new Course("Law", "How not to be fired on first work place"),
                new Course("Fashion", "How to be fashioned as programmer"),
                new Course("Chemistry", "How to cook the meth"));
    }

    /**
     * Assign courses(1-3 to each student) to students and get list of it: Student - Course
     *
     * @return - list of relationship: Student - Course
     */
    public List<CoursesConnection> getRelationshipBetweenStudentsAndCourses() {
        List<CoursesConnection> coursesConnections = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            int amountOfCourses = random.nextInt(3) + 1;
            for (int j = 0; j < amountOfCourses; j++) {
                coursesConnections.add(new CoursesConnection(i, random.nextInt(10) + 1));
            }
        }
        return coursesConnections;
    }

    /**
     * Gets 200 students with randomly assigned groups.
     *
     * @return - 200 students with randomly assigned groups
     */
    public List<Student> getStudents() {
        return setStudentsToGroups(generateStudents());
    }

    /**
     * Gets 10 uniq groups.
     *
     * @return - 10 uniq groups
     */
    public Set<Group> getGroups() {
        Set<Group> result = new HashSet<>();
        while (result.size() < 10) {
            result.add(generateGroup());
        }
        return result;
    }

    /**
     * Sets passed student to groups(1-10).
     *
     * @param students
     * @return
     */
    private List<Student> setStudentsToGroups(Set<Student> students) {
        List<Student> result = new ArrayList<>();
        Queue<Student> studentsQueue = new LinkedList<>(students);
        int studentAmountToAdd = random.nextInt(20) + 10;
        for (int i = 1; i <= 10; i++) {
            for (int j = 0; j < studentAmountToAdd; j++) {
                Student student = studentsQueue.poll();
                if (student != null) {
                    student.setGroupId(i);
                    result.add(student);
                }
            }
            studentAmountToAdd = random.nextInt(20) + 10;
        }
        result.addAll(studentsQueue);
        return result;
    }

    private Set<Student> generateStudents() {
        Set<Student> result = new HashSet<>();
        while (result.size() < 200) {
            result.add(generateStudent());
        }
        return result;
    }

    private Group generateGroup() {
        StringBuilder result = new StringBuilder();
        int number = random.nextInt(89) + 10;
        for (int i = 0; i < 2; i++) {
            int randomNumber = random.nextInt(26) + 'a';
            char randomCharOne = (char) randomNumber;
            result.append(randomCharOne);
        }
        result.append("-").append(number);
        return  new Group(result.toString().toUpperCase());
    }

    private Student generateStudent() {
        int randomNumber1 = random.nextInt(20);
        int randomNumber2 = random.nextInt(20);

        return new Student(firstNames.get(randomNumber1), lastNames.get(randomNumber2));
    }
}
