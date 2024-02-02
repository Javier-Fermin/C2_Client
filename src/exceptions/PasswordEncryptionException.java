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
public class PasswordEncryptionException extends Exception {

    /**
     * Creates a new instance of <code>PasswordEncryptionException</code>
     * without detail message.
     */
    public PasswordEncryptionException() {
    }

    /**
     * Constructs an instance of <code>PasswordEncryptionException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public PasswordEncryptionException(String msg) {
        super(msg);
    }
}
