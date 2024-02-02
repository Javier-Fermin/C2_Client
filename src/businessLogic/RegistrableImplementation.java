package businessLogic;

import exceptions.AuthenticationException;
import exceptions.UserAlreadyExistsException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javax.ws.rs.core.GenericType;
import model.Player;
import model.Admin;
import model.User;
import model.UserType;
import rest.AdminRESTClient;
import rest.PlayerRESTClient;
import rest.UserRESTClient;

public class RegistrableImplementation implements Registrable {

    private UserRESTClient userClient = new UserRESTClient();
    private PlayerRESTClient playerClient = new PlayerRESTClient();
    private AdminRESTClient adminClient = new AdminRESTClient();

    @Override
    public User signIn(User user) throws AuthenticationException {
        List<User> found = userClient.logIn_XML(user, new GenericType<List<User>>() {
        });
        return found.get(0);
    }

    @Override
    public User signUp(User user) throws UserAlreadyExistsException {
        userClient.createUser_XML(user);
        if (user.getUserType() == UserType.PLAYER) {
            playerClient.createPlayer_XML(new Player(true,
                    user.getName(),
                    user.getName(),
                    user.getPasswd(),
                    user.getPhone(),
                    user.getEmail(),
                    user.getAddress(),
                    UserType.PLAYER, null));
        } else {
            adminClient.createAdmin_XML(new Admin(Date.valueOf(LocalDate.now()),
                    user.getName(),
                    user.getPasswd(),
                    user.getPhone(),
                    user.getEmail(),
                    user.getAddress(),
                    UserType.ADMIN));
        }
        return user;
    }

    @Override
    public void recoverPassword(User user) {
        userClient.recoverPassword_XML(User.class, user.getEmail());
    }

}
