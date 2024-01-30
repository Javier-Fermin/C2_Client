/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author javie
 */
public class HelpWindowController{
    /**
     * The Logger for the logs
     */
    protected static final Logger LOGGER = Logger.getLogger(HelpWindowController.class.getName());

    @FXML
    private WebView webView;
    /**
     * Initializes and show the help window.
     * @param root The FXML document hierarchy root. 
     */
    public void initAndShowStage(Parent root) {
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Ayuda para la Gestion de Usuarios");
        stage.setResizable(false);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setOnShowing(this::handleWindowShowing);
        stage.show();
    }
    /**
     * Initializes window state. It implements behavior for WINDOW_SHOWING type 
     * event.
     * @param event  The window event 
     */
    private void handleWindowShowing(WindowEvent event){
        WebEngine webEngine = webView.getEngine();
        
        //Load help page.
        webEngine.load(getClass().getResource("/view/HelpTournament.html").toExternalForm());
    }
    
    
}
