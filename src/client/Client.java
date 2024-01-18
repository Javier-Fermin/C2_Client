/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import model.User;
import model.UserType;
import view.StatsWindowController;

/**
 *
 * @author javie
 */
public class Client extends javafx.application.Application{
    /**
     * A Logger for the logs
     */
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
    
    /**
     * Entry point for the JavaFX application. 
     * 
     * @param stage The primary window of the application
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {

        //Get the SignInFXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StatsGUI.fxml"));
        //Load the DOM
        Parent root = (Parent) loader.load();

        //Get the controller from SignIn
        StatsWindowController cont = ((StatsWindowController) loader.getController());

        //Set the stage
        cont.setStage(stage);
        //Initialize the window
        User user = new User("uwu", "abcd*1234", "688789901", "email@email.com", "Address", UserType.ADMIN);
        cont.initStage(root, user);
    }

    /**
     * Entry point for the Java application.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOGGER.info("Starting the application.");
        launch(args);
    }

}