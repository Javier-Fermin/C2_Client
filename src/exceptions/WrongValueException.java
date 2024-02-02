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
public class WrongValueException extends Exception {

    /**
     * Creates a new instance of <code>WrongValueException</code> without detail
     * message.
     */
    public WrongValueException() {
    }

    /**
     * Constructs an instance of <code>WrongValueException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public WrongValueException(String msg) {
        super(msg);
    }
}
