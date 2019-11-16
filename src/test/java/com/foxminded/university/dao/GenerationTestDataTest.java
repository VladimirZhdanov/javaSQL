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
class GenerationTestDataTest {

    public GenerationTestData generationTestData = new GenerationTestData();

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetCourses1() {
        boolean actual = generationTestData.getCourses().size() > 0;
        assertTrue(actual,
                "Should return true if list of courses more than 0");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetCourses2() {
        String courseName = generationTestData.getCourses().get(0).getName();
        String courseDescription = generationTestData.getCourses().get(0).getDescription();
        boolean actual = false;

        if (courseName != null && courseDescription != null) {
            actual = true;
        }
        assertTrue(actual,
                "Should return true if a course had name and description");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetStudents1() {
        boolean actual = generationTestData.getStudents().size() > 0;
        assertTrue(actual,
                "Should return true if list of students more than 0");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetStudents2() {
        String studentFirstName = generationTestData.getStudents().get(0).getFirstName();
        String studentLastName = generationTestData.getStudents().get(0).getLastName();
        int groupId = generationTestData.getStudents().get(0).getGroupId();
        boolean actual = false;

        if (studentFirstName != null && studentLastName != null && groupId != 0) {
            actual = true;
        }
        assertTrue(actual,
                "Should return true if a student had names and group id");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetGroups1() {
        boolean actual = generationTestData.getGroups().size() > 0;
        assertTrue(actual,
                "Should return true if list of groups more than 0");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndGetGroups2() {
        String name = generationTestData.getGroups().iterator().next().getName();
        boolean actual = false;

        if (name != null) {
            actual = true;
        }
        assertTrue(actual,
                "Should return true if a group had name");
    }

    @Test
    public void shouldReturnTrueWhenGenerateTestDataAndAssignCoursesToStudent() {
        List<Student> students = generationTestData.getStudents();
        List<Course> courses = generationTestData.getCourses();
        boolean actual = generationTestData.assignCoursesToStudent(students, courses).size() > 0;
        assertTrue(actual,
                "Should return true if list of students more than 0");
    }

    @Test
    public void shouldReturnTrueWhenAssignCoursesToStudentAndCheckSizeOfCoursesInRandomStudent1() {
        List<Student> students = generationTestData.getStudents();
        List<Course> courses = generationTestData.getCourses();
        students = generationTestData.assignCoursesToStudent(students, courses);

        boolean actual = students.get(students.size() - 1).getCourses().size() > 0;
        assertTrue(actual,
                "Should return true if list of courses was exist in a random student");
    }

    @Test
    public void shouldReturnTrueWhenAssignCoursesToStudentAndCheckSizeOfCoursesInRandomStudent2() {
        List<Student> students = generationTestData.getStudents();
        List<Course> courses = generationTestData.getCourses();
        students = generationTestData.assignCoursesToStudent(students, courses);

        boolean actual = students.get(students.size() - 2).getCourses().size() > 0;
        assertTrue(actual,
                "Should return true if list of courses was exist in a random student");
    }

    @Test
    public void shouldReturnTrueWhenAssignCoursesToStudentAndCheckSizeOfCoursesInRandomStudent3() {
        List<Student> students = generationTestData.getStudents();
        List<Course> courses = generationTestData.getCourses();
        students = generationTestData.assignCoursesToStudent(students, courses);

        boolean actual = students.get(students.size() - 100).getCourses().size() > 0;
        assertTrue(actual,
                "Should return true if list of courses was exist in a random student");
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed1() {
        List<Course> courses = generationTestData.getCourses();
        Exception exception = assertThrows(DAOException.class, () ->
                generationTestData.assignCoursesToStudent(null, courses));
        assertEquals("Null was passed", exception.getMessage());
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed2() {
        List<Student> students = generationTestData.getStudents();
        Exception exception = assertThrows(DAOException.class, () ->
                generationTestData.assignCoursesToStudent(students, null));
        assertEquals("Null was passed", exception.getMessage());
    }

    @Test
    public void shouldThrowDAOExceptionWhenNullWasPassed3() {
        Exception exception = assertThrows(DAOException.class, () ->
                generationTestData.assignCoursesToStudent(null, null));
        assertEquals("Null was passed", exception.getMessage());
    }
}