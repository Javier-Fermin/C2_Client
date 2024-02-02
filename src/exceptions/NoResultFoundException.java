/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author imape
 */
public class NoResultFoundException extends Exception {

    /**
     * Creates a new instance of <code>NoResultFoundException</code> without
     * detail message.
     */
    public NoResultFoundException() {
    }

    /**
     * Constructs an instance of <code>NoResultFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoResultFoundException(String msg) {
        super(msg);
    }
}
