/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import view.TournamentWinController;

/**
 *
 * @author javie
 */
public class Client extends javafx.application.Application{

    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Get the FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/tournamentsWindow.fxml"));
        //Load the DOM
        Parent root = ((Parent) loader.load());
        
        TournamentWinController cont = ((TournamentWinController) loader.getController());
        
        cont.setMainStage(primaryStage);
        cont.initStage(root);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOGGER.info("Starting the application.");
        launch(args);
    }
}
