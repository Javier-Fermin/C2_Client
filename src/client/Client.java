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
import view.SignInController;
import view.TournamentWinController;

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
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignInWindowFXML.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/tournamentsWindow.fxml"));
        
        //Load the DOM
        Parent root = (Parent) loader.load();

        //Get the controller from SignIn
//        SignInController cont = ((SignInController) loader.getController());
        TournamentWinController cont = ((TournamentWinController) loader.getController());
        
        User user = new User(null,null,null,null,null,UserType.ADMIN);
        
        //Set the stage
//        cont.setStage(stage);
        cont.setMainStage(stage);
        //Initialize the window
//        cont.initStage(root);
        cont.initStage(root,user);
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