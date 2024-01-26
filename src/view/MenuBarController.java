/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.User;
import model.UserType;

/**
 *
 * @author javie
 */
public class MenuBarController implements Initializable{
    /**
     * The Logger for the logs
     */
    protected static final Logger LOGGER = Logger.getLogger(MenuBarController.class.getName());

    private static User user;
    
    /**
     * The window stage
     */
    private static Stage stage;
    
    @FXML
    private MenuItem allLeaguesMenuItem; 
    
    @FXML
    private MenuItem allMatchesMenuItem;
    
    @FXML
    private MenuItem allTournamentsMenuItem;
    
    @FXML
    private MenuItem allStatsMenuItem;
    
    @FXML
    private MenuItem logOutMenuItem;

    /**
     * Setter for the window stage
     * 
     * @param stage the window stage to set
     */
    public void setMainStage(Stage stage) {
        this.stage = stage;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logOutMenuItem.setOnAction(this::handleExitMenuItemOnAction);
        allStatsMenuItem.setOnAction(this::handleStatsMenuItemOnAction);
        allTournamentsMenuItem.setOnAction(this::handleTournamentMenuItemOnAction);
        allLeaguesMenuItem.setOnAction(this::handleLeagueMenuItemOnAction);
        allMatchesMenuItem.setOnAction(this::handleMatchMenuItemOnAction);
    }
    
    public void handleExitMenuItemOnAction(ActionEvent ae){
        try {
            Stage sStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignInWindowFXML.fxml"));
            Parent root = (Parent) loader.load();
            SignInController cont = ((SignInController) loader.getController());
            cont.setStage(sStage);
            cont.initStage(root);
            stage.close();
        } catch (IOException ex) {
            Logger.getLogger(MenuBarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void handleStatsMenuItemOnAction(ActionEvent ae){
        try {
            Stage sStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StatsGUI.fxml"));
            Parent root = (Parent) loader.load();
            StatsWindowController cont = ((StatsWindowController) loader.getController());
            cont.setStage(sStage);
            cont.initStage(root, user);
            stage.close();
        } catch (IOException ex) {
            Logger.getLogger(MenuBarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void handleTournamentMenuItemOnAction(ActionEvent ae){
        try {
            Stage sStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/tournamentsWindow.fxml"));
            Parent root = (Parent) loader.load();
            TournamentWinController cont = ((TournamentWinController) loader.getController());
            cont.setMainStage(sStage);
            cont.initStage(root/*, user*/);
            stage.close();
        } catch (IOException ex) {
            Logger.getLogger(MenuBarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void handleLeagueMenuItemOnAction(ActionEvent ae){
        try {
            Stage sStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/leagueWindow.fxml"));
            Parent root = (Parent) loader.load();
            LeagueWindowController cont = ((LeagueWindowController) loader.getController());
            cont.setLeagueStage(sStage);
            cont.initStage(root, user);
            stage.close();
        } catch (IOException ex) {
            Logger.getLogger(MenuBarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void handleMatchMenuItemOnAction(ActionEvent ae){
        try {
            Stage sStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Matches.fxml"));
            Parent root = (Parent) loader.load();
            MatchWindowController cont = ((MatchWindowController) loader.getController());
            cont.setMainStage(sStage);
            cont.initStage(root/*, user*/);
            stage.close();
        } catch (IOException ex) {
            Logger.getLogger(MenuBarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        MenuBarController.user = user;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        MenuBarController.stage = stage;
    }
    
    
}
