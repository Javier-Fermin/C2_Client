/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.Optional;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Tournament;
import businessLogic.TournamentManage;
import businessLogic.TournamentManageFactory;
import com.sun.deploy.util.StringUtils;
import exceptions.DeleteException;
import exceptions.ReadException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

/**
 *
 * @author Fran
 */
public class TournamentWinController {
    /**
     * The Logger for the logs
     */
    protected static final Logger LOGGER = Logger.getLogger(TournamentWinController.class.getName());
    
    private ObservableList<Tournament> tournaments;

    private TournamentManage tournamentable = TournamentManageFactory.getTournamentManageImplementation();
    
    @FXML
    private ImageView tournamentBackground, tournamentSmoke, bSearchImage, bCleanImage, bHelpImage, bPrintImage, bDeleteImage, bAddImage, tournamentAgentImage;
    
    @FXML
    private Label lTournamenTitle;
    
    @FXML
    private Separator tournamentSeparator;
    
    @FXML
    private ChoiceBox chbTournamentSearch;
            
    @FXML
    private TextField tfTournamentSearch; 
    
    @FXML
    private TableView tvTournaments;
    
    @FXML
    private TableColumn tcName, tcDescription, tcBestOf, tcDate;
    
    @FXML
    private Button bTournamentSearch, bTournamentMatches, bTournamentClean, bTournamentHelp, bTournamentPrint, bTournamentDelete, bTournamentAdd;
    
    private Stage stage;
    
    
    
    public void setMainStage(Stage stage) {
        this.stage = stage;
    }
    
    
    public void initStage(Parent root){ //parametro User
        try {
            LOGGER.info("Init Main Window");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Esports Six - Tournaments");
            stage.setResizable(false);
            //TODO componenetes de la ventana que estan deshabilitados o son editables
            bTournamentDelete.setDisable(true);
            bTournamentMatches.setDisable(true);
            //if typeUser=ADMIN
            bTournamentAdd.setDisable(true);
            //if typeUser=ADMIN
            tvTournaments.setEditable(true);
            tfTournamentSearch.setDisable(true);
            
            // Creates a context menu 
            ContextMenu tournamentContextMenu = new ContextMenu();

            // Creates menuitems 
            MenuItem cmSearch = new MenuItem("Search");
            MenuItem cmCreate = new MenuItem("Create");
            MenuItem cmDelete = new MenuItem("Delete");

            // Add the menuitems into the context menu 
            tournamentContextMenu.getItems().add(cmSearch);
            tournamentContextMenu.getItems().add(cmCreate);
            tournamentContextMenu.getItems().add(cmDelete);
            
            LOGGER.info("If the window want to exit, alert to verify");
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?").showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        return;
                    }
                    event.consume();
                }
            });

            stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                    if (KeyCode.ESCAPE == event.getCode()) {
                        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?").showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            Platform.exit();
                        }
                        event.consume();
                    }
                });
            
            chbTournamentSearch.setValue("ALL");
            chbTournamentSearch.setItems(FXCollections.observableArrayList("ALL", "ID", "NAME", "DATE", "FORMAT", "MATCH"));
            chbTournamentSearch.getSelectionModel().selectedItemProperty().addListener((ObservableValue observableValue, Object oldValue, Object newValue) -> {
                //Check whether item is selected and set value of selected item to Label
                if (!chbTournamentSearch.getSelectionModel().getSelectedItem().equals("ALL")) {
                    tfTournamentSearch.setDisable(false);
                } else {
                    tfTournamentSearch.setDisable(true);
                }
                
                //change tfTournamentSearch promptext
            });
            
            tournaments=FXCollections.observableArrayList(tournamentable.findAllTournaments());
            
            
            tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            tcBestOf.setCellValueFactory(new PropertyValueFactory<>("bestOf"));
            
            
            final Callback<TableColumn<Tournament, Date>, TableCell<Tournament, Date>> dateCellFactory = 
                              (TableColumn<Tournament, Date> param) -> new DateTournamentPicker();
            
            tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            tcDate.setCellFactory(dateCellFactory);
            

            tvTournaments.setItems(tournaments);
            
            tvTournaments.getSelectionModel().selectedItemProperty().addListener((ObservableValue observableValue, Object oldValue, Object newValue) -> {
                //Check whether item is selected and set value of selected item to Label
                if (tvTournaments.getSelectionModel().getSelectedItem() != null) {
                    bTournamentMatches.setDisable(false);
                    //if (!user.getUserType().equals(UserType.PLAYER)) {
                        bTournamentDelete.setDisable(false);
                        cmDelete.setDisable(false);
                    //}
                } else {
                    bTournamentMatches.setDisable(true);
                    bTournamentDelete.setDisable(true);
                    cmDelete.setDisable(true);
                }
            });
 

            //Asignar componenetes sus metodos
            bTournamentSearch.setOnAction(this::searchTournament);
            //bTournamentMatches.setOnAction(this::seeTournamentsMatches);
            bTournamentClean.setOnAction(this::cleanTournamentSearch);
           // bTournamentHelp.setOnAction(this::seeTournamentWindowManual);
          //  bTournamentPrint.setOnAction(this::printTournament);
            bTournamentDelete.setOnAction(this::deleteTournament);
          //  bTournamentAdd.setOnAction(this::addTournament);
            
            stage.show();
        
        } catch (ReadException ex) {
            Logger.getLogger(TournamentWinController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // metodos de componentes
    
    @FXML
    private void searchTournament(ActionEvent event) {
        try {
            if(tfTournamentSearch.equals("")){
                new Alert(Alert.AlertType.ERROR, "This field is required to fill", ButtonType.OK).showAndWait();
            }
            else{
                switch(chbTournamentSearch.getSelectionModel().getSelectedItem().toString()){
                    case "ALL":
                        tournaments=FXCollections.observableArrayList(tournamentable.findAllTournaments());
                        tvTournaments.setItems(tournaments);
                        tvTournaments.refresh();
                        break;
                    case "ID":
                        String tfSearchText=tfTournamentSearch.getText();
                        if(isNumeric(tfSearchText)){
                            tournaments=FXCollections.observableArrayList(tournamentable.findTournament(new Integer(tfSearchText)));
                            tvTournaments.setItems(tournaments);
                            tvTournaments.refresh();
                        }else{
                            new Alert(Alert.AlertType.ERROR, "Value can only be numbers", ButtonType.OK).showAndWait();
                        }
                        break;
                    case "NAME":
                        tfSearchText=tfTournamentSearch.getText();
                        if(!isNumeric(tfSearchText)){
                            tournaments=FXCollections.observableArrayList(tournamentable.findTournamentByName(tfSearchText));
                            tvTournaments.setItems(tournaments);
                            tvTournaments.refresh();
                        }else{
                            new Alert(Alert.AlertType.ERROR, "Value can only be characters", ButtonType.OK).showAndWait();
                        }
                        break;
                    case "DATE":
                        //TODO noche Buscar por fecha :c
                        tournaments=FXCollections.observableArrayList(tournamentable.findAllTournaments());
                        tvTournaments.setItems(tournaments);
                        tvTournaments.refresh();
                        break;
                    case "FORMAT":
                        tfSearchText=tfTournamentSearch.getText();
                        if(isNumeric(tfSearchText)){
                            tournaments=FXCollections.observableArrayList(tournamentable.findTournamentByBestOf(new Integer(tfSearchText)));
                            tvTournaments.setItems(tournaments);
                            tvTournaments.refresh();
                        }else{
                            new Alert(Alert.AlertType.ERROR, "Value can only be numbers", ButtonType.OK).showAndWait();
                        }
                        break;
                    case "MATCH":
                        //TODO Buscar por match: no tengo javabean de match
                        tournaments=FXCollections.observableArrayList(tournamentable.findAllTournaments());
                        tvTournaments.setItems(tournaments);
                        tvTournaments.refresh();
                        break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TournamentWinController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void seeTournamentsMatches(ActionEvent event) {
        try {
            if(tfTournamentSearch.equals("")){
                new Alert(Alert.AlertType.ERROR, "This field is required to fill", ButtonType.OK).showAndWait();
            }
            else{
                switch(chbTournamentSearch.getSelectionModel().getSelectedItem().toString()){
                    case "ALL":
                        tournaments=FXCollections.observableArrayList(tournamentable.findAllTournaments());
                        tvTournaments.setItems(tournaments);
                        tvTournaments.refresh();
                        ;
                    case "ID":
                        String tfSearchText=tfTournamentSearch.getText();
                        if(isNumeric(tfSearchText)){
                            tournaments=FXCollections.observableArrayList(tournamentable.findTournament(new Integer(tfSearchText)));
                            tvTournaments.setItems(tournaments);
                            tvTournaments.refresh();
                        }else{
                            new Alert(Alert.AlertType.ERROR, "Value can only be numbers", ButtonType.OK).showAndWait();
                        }
                    case "NAME":
                        tfSearchText=tfTournamentSearch.getText();
                        if(!isNumeric(tfSearchText)){
                            tournaments=FXCollections.observableArrayList(tournamentable.findTournamentByName(tfSearchText));
                            tvTournaments.setItems(tournaments);
                            tvTournaments.refresh();
                        }else{
                            new Alert(Alert.AlertType.ERROR, "Value can only be characters", ButtonType.OK).showAndWait();
                        }
                        ;
                    case "DATE":
                        //TODO noche Buscar por fecha :c
                        tournaments=FXCollections.observableArrayList(tournamentable.findAllTournaments());
                        tvTournaments.setItems(tournaments);
                        tvTournaments.refresh();
                        ;
                    case "FORMAT":
                        tfSearchText=tfTournamentSearch.getText();
                        if(isNumeric(tfSearchText)){
                            tournaments=FXCollections.observableArrayList(tournamentable.findTournamentByBestOf(new Integer(tfSearchText)));
                            tvTournaments.setItems(tournaments);
                            tvTournaments.refresh();
                        }else{
                            new Alert(Alert.AlertType.ERROR, "Value can only be numbers", ButtonType.OK).showAndWait();
                        }
                        ;
                    case "MATCH":
                        //TODO Buscar por match: no tengo javabean de match
                        tournaments=FXCollections.observableArrayList(tournamentable.findAllTournaments());
                        tvTournaments.setItems(tournaments);
                        tvTournaments.refresh();
                        ;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(TournamentWinController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void cleanTournamentSearch(ActionEvent event) {
        chbTournamentSearch.getSelectionModel().selectFirst();
        tfTournamentSearch.setText("");
        refreshTable();
    }

    @FXML
    private void seeTournamentWindowManual(ActionEvent event) {
        //yes
    }

    @FXML
    private void printTournament(ActionEvent event) {
        //yes
    }

    @FXML
    private void deleteTournament(ActionEvent event) {
        try {
            Optional<ButtonType> resultDelete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Tournament?").showAndWait();
            if (resultDelete.isPresent() && resultDelete.get() == ButtonType.OK) {
                tournamentable.deleteTournament((Tournament) tvTournaments.getSelectionModel().getSelectedItem());
                tvTournaments.getItems().remove(tvTournaments.getSelectionModel().getSelectedItem());
                tvTournaments.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "Tournament droped correctly", ButtonType.OK).showAndWait();
            }else{
                new Alert(Alert.AlertType.CONFIRMATION, "Tournament delete canceled", ButtonType.OK).showAndWait();
            }
            tvTournaments.getSelectionModel().clearSelection();
        } catch (DeleteException ex) {
            Logger.getLogger(TournamentWinController.class.getName()).log(Level.SEVERE, null, "Delete error" + ex.getMessage());
            new Alert(Alert.AlertType.ERROR, "Delete Error", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void addTournament(ActionEvent event) {
        //yes
    }

    private void refreshTable() {
        try {
            ObservableList<Tournament> refreshedTableItems = FXCollections.observableArrayList(tournamentable.findAllTournaments());
                    
            tvTournaments.setItems(refreshedTableItems);
            
        } catch (ReadException ex) {
            Logger.getLogger(TournamentWinController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isNumeric(String tfSearchText) {
        if(tfSearchText.equals(null)){
            return false;
        }try{
            int tfSearchIsNumber = Integer.parseInt(tfSearchText);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
    
}
