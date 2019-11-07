package com.foxminded.university.ui;

import com.foxminded.university.sql.Config;
import com.foxminded.university.sql.UniversitySQL;
import java.util.List;
import java.util.function.Consumer;

/**
 * Start point of the application.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class StartUI {
    /**
     * Input
     */
    private final Input input;

    /**
     * SQL manager.
     */
    private final UniversitySQL universitySQL;

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
     * @param universitySQL - SQL manager.
     * @param output - output
     */
    public StartUI(Input input, UniversitySQL universitySQL, Consumer<String> output) {
        this.input = input;
        this.universitySQL = universitySQL;
        this.output = output;
        working = true;
    }

    /**
     * Initialises an instance of the class
     */
    public void init() {
        universitySQL.init();
        universitySQL.setTables();
        Menu menu = new Menu(input, output, universitySQL);
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
        this.working = false;
    }

    /**
     * Main method to run the application.
     *
     * @param args - args
     */
    public static void main(String[] args) {
        Config config = new Config();
        new StartUI(new ValidateInput(
                new ConsoleInput()
        ), new UniversitySQL(config), System.out::println).init();
    }
}
