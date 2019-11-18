package com.foxminded.university.dao;

import com.foxminded.university.dao.connection.DataSource;
import com.foxminded.university.dao.layers.CourseDAO;
import com.foxminded.university.dao.layers.GroupDAO;
import com.foxminded.university.dao.layers.StudentDAO;
import com.foxminded.university.domain.Course;
import com.foxminded.university.domain.Group;
import com.foxminded.university.domain.Student;
import com.foxminded.university.exceptions.DAOException;
import java.util.List;
import java.util.Set;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

/**
 * University DAO
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class UniversitySQL {
    private ExecutorQuery executorQuery;

    private static final String DROP_DB = "dropDB.SQL";
    private static final String CREATE_DB = "dataBaseCreation.SQL";
    private static final String CREATE_TABLES = "tablesCreation.SQL";

    private CourseDAO courseDAO;
    private StudentDAO studentDAO;
    private GroupDAO groupDAO;
    /**
     * Generated data.
     */
    private GeneratorTestData generatorTestData;

    /**
     * Connection pool and connection fabric (postgres).
     */
    private DataSource dataSourcePostgres;

    /**
     * Connection pool and connection fabric (university).
     */
    private DataSource dataSourceUniversity;

    /**
     * Constructor of the class.
     *
     * @param dataSourcePostgres - dataSource for postgres
     * @param dataSourceUniversity - dataSource for admin
     */
    public UniversitySQL(DataSource dataSourcePostgres, DataSource dataSourceUniversity, ExecutorQuery executorQuery) {
        this.executorQuery = executorQuery;

        if (dataSourcePostgres == null && dataSourceUniversity == null) {
            throw new DAOException("Null was passed to the constructor...");
        }
        generatorTestData = new GeneratorTestData();
        this.dataSourcePostgres = dataSourcePostgres;
        this.dataSourceUniversity = dataSourceUniversity;
        courseDAO = new CourseSQL(dataSourceUniversity);
        groupDAO = new GroupSQL(dataSourceUniversity);
        studentDAO = new StudentSQL(dataSourceUniversity);
    }

    /**
     * Creates DB, user, tables and insert test data into the tables.
     */
    public void setDateBase() {
        executorQuery.execute(dataSourcePostgres, DROP_DB);
        executorQuery.execute(dataSourcePostgres, CREATE_DB);
        executorQuery.execute(dataSourceUniversity, CREATE_TABLES);

        //get test data
        List<Student> students = generatorTestData.getStudents();
        Set<Group> groups = generatorTestData.getGroups();
        List<Course> courses = generatorTestData.getCourses();

        //insert test data
        studentDAO.insert(students);
        groupDAO.insert(groups);
        courseDAO.insert(courses);

        //get students and courses with id and generate relationship between students and courses
        students = studentDAO.getAllStudents();
        courses = courseDAO.getAllCourses();
        students = generatorTestData.assignCoursesToStudent(students, courses);

        //insert the relationship between students and courses
        studentDAO.insertRelationshipStudentsToCourses(students);
    }

    /**
     * Drops database.
     */
    public void dropDataBase() {
        dataSourceUniversity.closeConnection();
        executorQuery.execute(dataSourcePostgres, DROP_DB);
    }
}
