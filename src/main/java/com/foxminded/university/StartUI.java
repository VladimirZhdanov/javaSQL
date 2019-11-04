package com.foxminded.university;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class StartUI {
    private final Input input;
    private final UniversitySQL universitySQL;
    private final Consumer<String> output;
    private boolean working;

    public StartUI(Input input, UniversitySQL universitySQL, Consumer<String> output) {
        this.input = input;
        this.universitySQL = universitySQL;
        this.output = output;
        working = true;
    }

    public void init() {
        universitySQL.init();
        Menu menu = new Menu(input, output, universitySQL);
        menu.fillActions(this);
        List<Integer> range = menu.getRangeOfMenu();
        do {
            menu.show();
            menu.select(input.ask("Select: ", range));
        } while (working);
    }

    public void stop() {
        this.working = false;
    }

    public static void main(String[] args) {
        Config config = new Config();
        new StartUI(new ValidateInput(
                new ConsoleInput()
        ), new UniversitySQL(config), System.out::println).init();
    }
}
