package businessLogic;

import exceptions.AuthenticationException;
import exceptions.UserAlreadyExistsException;
import model.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This interface encapsules all the methods for the treatment of user data for
 * signUp and singIn
 * 
 * @author Javier, Fran, Imanol, Emil
 */
public interface Registrable {
    /**
     * This method should sign in a given user in the database
     * 
     * @param user the user to sign in
     * @return the user that has been signed in 
     * @throws AuthenticationException in case the given user credentials are wrong
     */
    public User signIn(User user) throws AuthenticationException;
    
    /**
     * This method should sign up a user in the database
     * 
     * @param user the user to sign up
     * @return the user that has been signed up
     * @throws UserAlreadyExistsException in case the given user already exists in the database
     */
    public User signUp(User user) throws UserAlreadyExistsException;
    
    /**
     * This method sends a mail to the User that requests it with a new password
     * 
     * @param user The <code>User</code> trying to recover its password
     */
    public void recoverPassword(User user);
}
