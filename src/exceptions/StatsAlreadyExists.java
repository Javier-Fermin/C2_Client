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
public class StatsAlreadyExists extends Exception {

    /**
     * Creates a new instance of <code>StatsAlreadyExists</code> without detail
     * message.
     */
    public StatsAlreadyExists() {
    }

    /**
     * Constructs an instance of <code>StatsAlreadyExists</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public StatsAlreadyExists(String msg) {
        super(msg);
    }
}
