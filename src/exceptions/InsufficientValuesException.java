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
public class InsufficientValuesException extends Exception {

    /**
     * Creates a new instance of <code>InsufficientValuesException</code>
     * without detail message.
     */
    public InsufficientValuesException() {
    }

    /**
     * Constructs an instance of <code>InsufficientValuesException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public InsufficientValuesException(String msg) {
        super(msg);
    }
}
