package com.foxminded.university.dao;

import com.foxminded.university.domain.Course;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exceptions.DAOException;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
class GeneratorTestDataTest {

    public GeneratorTestData generatorTestData = new GeneratorTestData();

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetCourses1() {
        boolean actual = generatorTestData.getCourses().size() > 0;
        assertTrue(actual,
                "Should return true if list of courses more than 0");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetCourses2() {
        String courseName = generatorTestData.getCourses().get(0).getName();
        String courseDescription = generatorTestData.getCourses().get(0).getDescription();
        boolean actual = false;

        if (courseName != null && courseDescription != null) {
            actual = true;
        }
        assertTrue(actual,
                "Should return true if a course had name and description");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetStudents1() {
        boolean actual = generatorTestData.getStudents().size() > 0;
        assertTrue(actual,
                "Should return true if list of students more than 0");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetStudents2() {
        String studentFirstName = generatorTestData.getStudents().get(0).getFirstName();
        String studentLastName = generatorTestData.getStudents().get(0).getLastName();
        int groupId = generatorTestData.getStudents().get(0).getGroupId();
        boolean actual = false;

        if (studentFirstName != null && studentLastName != null && groupId != 0) {
            actual = true;
        }
        assertTrue(actual,
                "Should return true if a student had names and group id");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetGroups1() {
        boolean actual = generatorTestData.getGroups().size() > 0;
        assertTrue(actual,
                "Should return true if list of groups more than 0");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetGroups2() {
        String name = generatorTestData.getGroups().iterator().next().getName();
        boolean actual = false;

        if (name != null) {
            actual = true;
        }
        assertTrue(actual,
                "Should return true if a group had name");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndAssignCoursesToStudent() {
        List<Student> students = generatorTestData.getStudents();
        List<Course> courses = generatorTestData.getCourses();
        boolean actual = generatorTestData.assignCoursesToStudent(students, courses).size() > 0;
        assertTrue(actual,
                "Should return true if list of students more than 0");
    }

    @Test
    public void shouldReturnTrueWhenAssignCoursesToStudentAndCheckSizeOfCoursesInRandomStudent1() {
        List<Student> students = generatorTestData.getStudents();
        List<Course> courses = generatorTestData.getCourses();
        students = generatorTestData.assignCoursesToStudent(students, courses);

        boolean actual = students.get(students.size() - 1).getCourses().size() > 0;
        assertTrue(actual,
                "Should return true if list of courses was exist in a random student");
    }

    @Test
    public void shouldReturnTrueWhenAssignCoursesToStudentAndCheckSizeOfCoursesInRandomStudent2() {
        List<Student> students = generatorTestData.getStudents();
        List<Course> courses = generatorTestData.getCourses();
        students = generatorTestData.assignCoursesToStudent(students, courses);

        boolean actual = students.get(students.size() - 2).getCourses().size() > 0;
        assertTrue(actual,
                "Should return true if list of courses was exist in a random student");
    }

    @Test
    public void shouldReturnTrueWhenAssignCoursesToStudentAndCheckSizeOfCoursesInRandomStudent3() {
        List<Student> students = generatorTestData.getStudents();
        List<Course> courses = generatorTestData.getCourses();
        students = generatorTestData.assignCoursesToStudent(students, courses);

        boolean actual = students.get(students.size() - 100).getCourses().size() > 0;
        assertTrue(actual,
                "Should return true if list of courses was exist in a random student");
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed1() {
        List<Course> courses = generatorTestData.getCourses();
        Exception exception = assertThrows(DAOException.class, () ->
                generatorTestData.assignCoursesToStudent(null, courses));
        assertEquals("Null was passed", exception.getMessage());
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed2() {
        List<Student> students = generatorTestData.getStudents();
        Exception exception = assertThrows(DAOException.class, () ->
                generatorTestData.assignCoursesToStudent(students, null));
        assertEquals("Null was passed", exception.getMessage());
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed3() {
        Exception exception = assertThrows(DAOException.class, () ->
                generatorTestData.assignCoursesToStudent(null, null));
        assertEquals("Null was passed", exception.getMessage());
    }
}