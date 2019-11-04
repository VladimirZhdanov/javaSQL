package com.foxminded.university.exceptions;

/**
 * Custom exception for menu(when a user input number of menu that is out of range.
 *
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class MenuOutException extends RuntimeException {

    /**
     * Constructor of the class.
     *
     * @param msg - massage
     */
    public MenuOutException(String msg) {
        super(msg);
    }
}
