package com.foxminded.university.ui;

import com.foxminded.university.exceptions.MenuOutException;
import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

/**
 * Interface to get input from console.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class ConsoleInput implements Input {

    /**
     * Input stream.
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * Gets user input from the console.
     *
     * @param question - request
     * @return - response
     */
    public String ask(String question) {
        System.out.print(question);
        return scanner.nextLine();
    }

    /**
     * Gets user input from the console.
     *
     * @param question - request
     * @param range - range of the menu
     * @return - response
     */
    public int ask(String question, List<Integer> range) {
        int key = parseInt(ask(question));
        boolean exist = false;
        for (Integer value : range) {
            if (value == key) {
                exist = true;
                break;
            }
        }
        if (!exist) {
            throw new MenuOutException("Out of menu range.");
        }
        return key;
    }
}
