package com.foxminded.university;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class GenerateTestData {
    private List<String> firstNames;
    private List<String> lastNames;
    private Random random;

    public GenerateTestData() {
        this.random = new Random();
        firstNames = Arrays.asList("Liam", "Noah", "William", "James", "Oliver", "Benjamin", "Elijah", "Lucas",
                "Mason", "Logan", "Alexander", "Ethan", "Jacob", "Michael", "Daniel", "Henry", "Jackson", "Sebastian", "Aiden", "Matthew");
        lastNames = Arrays.asList("Smith", "Johnson", "Williams", "Jones", "Rodríguez", "Torres", "Reyes", "Ruíz",
                "Aguilar", "Ortíz", "Moreno", "Chávez", "Ramos", "Herrera", "Medina", "Vargas", "Castro", "Guzmán", "Fernández", "Rojas");
    }

    public List<CoursesConnection> setStudentsToCourses() {
        List<CoursesConnection> coursesConnections = new ArrayList<>();
        for (int i = 1; i <= 200; i++) {
            int amountOfCourses = random.nextInt(3) + 1;
            for (int j = 0; j < amountOfCourses; j++) {
                coursesConnections.add(new CoursesConnection(i, random.nextInt(10) + 1));
            }
        }
        return coursesConnections;
    }

    public List<Student> getStudents() {
        return setStudentsToGroups(generateBunchOfStudents());
    }

    private List<Student> setStudentsToGroups(Set<Student> students) {
        List<Student> result = new ArrayList<>();
        Queue<Student> listOfStudent = new LinkedList<>(students);
        int amountOfStudentToAdd = random.nextInt(20) + 10;
        for (int i = 1; i <= 10; i++) {
            System.out.println(amountOfStudentToAdd);
            for (int j = 0; j < amountOfStudentToAdd; j++) {
                Student student = listOfStudent.poll();
                if (student != null) {
                    student.setGroupId(i);
                    result.add(student);
                }
            }
            amountOfStudentToAdd = random.nextInt(20) + 10;
        }
        return result;
    }

    public Set<Group> generateBunchOfNamesForGroup() {
        Set<Group> result = new HashSet<>();
        while (result.size() < 10) {
            result.add(generateGroup());
        }
        return result;
    }

    public Set<Student> generateBunchOfStudents() {
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

    public static void main(String[] args) {
        GenerateTestData generateTestData = new GenerateTestData();

        /*Set<Group> namesForGroups = generateTestData.generateBunchOfNamesForGroup();
        //namesForGroups.forEach(x -> System.out.println(x.getName()));

        Set<Student> students = generateTestData.generateBunchOfStudents();
        //students.forEach(student -> System.out.println(student.getFirstName() + " " + student.getLastName()));

        List<Student> students2 = generateTestData.setStudentsToGroups(students);
        students2.forEach(student ->  System.out.println(student.getFirstName() + " " + student.getLastName() + " " + student.getGroupId()));*/

        List<CoursesConnection> test = generateTestData.setStudentsToCourses();
        test.forEach(x -> System.out.println(x.getIdOfStudent() + " " + x.getIdOfCourse()));
    }
}
