package com.foxminded.university;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class StartUI {
    public static void main(String[] args) {
        Config config = new Config();
        UniversitySQL universitySQL = new UniversitySQL(config);
        universitySQL.init();
        //Student student = new Student("vlas", "lord", 3);
        universitySQL.addStudentToCourse(1, 1);
        universitySQL.addStudentToCourse(1, 777);
        universitySQL.addStudentToCourse(777, 1);
        //System.out.println(universitySQL.delete(student.getId()));
        universitySQL.removeCourse(1,1);

    }
}
