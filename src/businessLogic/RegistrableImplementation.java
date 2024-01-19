package businessLogic;

import exceptions.AuthenticationException;
import exceptions.UserAlreadyExistsException;
import java.util.List;
import javax.ws.rs.core.GenericType;
import model.User;
import rest.UserRESTClient;

public class RegistrableImplementation implements Registrable {
    private UserRESTClient client = new  UserRESTClient();
    
    @Override
    public User signIn(User user) throws AuthenticationException {
        List<User> found = client.logIn_XML(user,new GenericType<List<User>>(){});
        return found.get(0);
    }

    @Override
    public User signUp(User user) throws UserAlreadyExistsException {
        client.createUser_XML(user);
        return user;
    }

}