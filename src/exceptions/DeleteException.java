/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
<<<<<<< HEAD

package exceptions;

    /**
     * Creates a new instance of <code>DeleteExceptopn</code> without detail
     * message.
     * @author 2dam
     */
public class DeleteException extends Exception{
    
     /**
     * Creates a new instance of <code>CreateException</code> without detail message.
=======
package exceptions;

/**
 *
 * @author javie
 */
public class DeleteException extends Exception {

    /**
     * Creates a new instance of <code>DeleteException</code> without detail
     * message.
>>>>>>> testing
     */
    public DeleteException() {
    }

    /**
<<<<<<< HEAD
     * Constructs an instance of <code>DeleteExceptopn</code> with the specified
=======
     * Constructs an instance of <code>DeleteException</code> with the specified
>>>>>>> testing
     * detail message.
     *
     * @param msg the detail message.
     */
    public DeleteException(String msg) {
        super(msg);
    }
}
