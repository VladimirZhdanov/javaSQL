package com.foxminded.university.exceptions;

/**
 * @author Vladimir Zhdanov (mailto:constHomeSpb@gmail.com)
 * @since 0.1
 */
public class DAOException extends RuntimeException {

    /**
     * Constructor of the class.
     *
     * @param msg - massage
     */
    public DAOException(String msg) {
        super(msg);
    }
}
