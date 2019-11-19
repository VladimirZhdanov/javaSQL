package com.foxminded.university.ui;

import com.foxminded.university.dao.CourseSQL;
import com.foxminded.university.dao.ExecutorQuery;
import com.foxminded.university.dao.GroupSQL;
import com.foxminded.university.dao.StudentSQL;
import com.foxminded.university.dao.connection.Config;
import com.foxminded.university.dao.UniversitySQL;
import com.foxminded.university.dao.connection.DataSource;
import java.util.List;
import java.util.function.Consumer;

/**
 * Start point of the application.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class StartApplication {
    private static final String POSTGRES_PROPERTIES = "postgres.properties";
    private static final String UNIVERSITY_PROPERTIES = "university.properties";
    private Config configPostgres;
    private Config configUniversity;
    private ExecutorQuery executorQuery;

    /**
     * Connection pool and connection fabric (postgres).
     */
    private DataSource dataSourcePostgres;

    /**
     * Connection pool and connection fabric (university).
     */
    private DataSource dataSourceUniversity;

    /**
     * Input
     */
    private final Input input;

    private final UniversitySQL universitySQL;
    private StudentSQL studentDAO;
    private GroupSQL groupDAO;
    private CourseSQL courseDAO;

    /**
     * Output.
     */
    private final Consumer<String> output;

    /**
     * Flag run/stop the application.
     */
    private boolean working;

    /**
     * Constructor of the class
     *
     * @param input - input
     * @param output - output
     */
    public StartApplication(Input input, Consumer<String> output, DataSource dataSourcePostgres, DataSource dataSourceUniversity, ExecutorQuery executorQuery) {
        this.dataSourcePostgres = dataSourcePostgres;
        this.dataSourceUniversity = dataSourceUniversity;
        this.universitySQL = new UniversitySQL(dataSourcePostgres, dataSourceUniversity, executorQuery);
        this.groupDAO = new GroupSQL(dataSourceUniversity);
        this.studentDAO = new StudentSQL(dataSourceUniversity);
        this.courseDAO = new CourseSQL(dataSourceUniversity);
        this.input = input;
        this.output = output;
        working = true;
    }

    /**
     * Initialises an instance of the class
     */
    public void init() {
        universitySQL.setDateBase();
        Menu menu = new Menu(input, output, groupDAO, studentDAO, courseDAO);
        menu.fillActions(this);
        List<Integer> range = menu.getRangeOfMenu();
        do {
            menu.show();
            menu.select(input.ask("Select: ", range));
        } while (working);
    }

    /**
     * Stops the application.
     */
    public void stop() {
        universitySQL.dropDataBase();
        this.working = false;
    }

    /**
     * Main method to run the application.
     *
     * @param args - args
     */
    public static void main(String[] args) {
        Config configPost = new Config();
        Config configUni = new Config();
        configPost.loadProperties(POSTGRES_PROPERTIES);
        configUni.loadProperties(UNIVERSITY_PROPERTIES);
        DataSource dataSourcePost = new DataSource(configPost);
        DataSource dataSourceUni = new DataSource(configUni);
        ExecutorQuery executorQuery = new ExecutorQuery();
        new StartApplication(new ValidateInput(
                new ConsoleInput()), System.out::println, dataSourcePost, dataSourceUni, executorQuery).init();
    }
}
