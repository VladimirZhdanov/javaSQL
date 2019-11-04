package com.foxminded.university;

import java.util.List;

/**
 * Validate input
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class ValidateInput implements Input {

    /**
     * Input from a source.
     */
    private final Input input;

    /**
     * Constructor of the class
     *
     * @param input - input
     */
    public ValidateInput(final Input input) {
        this.input = input;
    }

    /**
     * Gets user input from a source.
     *
     * @param question - request
     * @return - response
     */
    @Override
    public String ask(String question) {
        return input.ask(question);
    }

    /**
     * Gets user input from a source.
     *
     * @param question - request
     * @param range - range of the menu
     * @return - response
     */
    public int ask(String question, List<Integer> range) {
        boolean invalid = true;
        int value = -1;
        do {
            try {
                value = input.ask(question, range);
                invalid = false;
            } catch (MenuOutException moe) {
                System.out.println("Please select key from menu.");
            } catch (NumberFormatException nfe) {
                System.out.println("Please enter validate data again.");
            }
        } while (invalid);
        return value;
    }
}
