/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author javie
 */
public class WrongFilterException extends Exception {

    /**
     * Creates a new instance of <code>WrongFilterException</code> without
     * detail message.
     */
    public WrongFilterException() {
    }

    /**
     * Constructs an instance of <code>WrongFilterException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public WrongFilterException(String msg) {
        super(msg);
    }
}
