/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import tableCells.DateTournamentPicker;
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
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.ReadException;
import exceptions.UpdateException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collection;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import model.User;
import model.UserType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.tabulator.TablePosition;
import net.sf.jasperreports.view.JasperViewer;
import tableCells.DateTournamentPicker;

/**
 * Controller class responsable of the Tournament Window
 * 
 * @author Fran
 */
public class TournamentWinController {

    /**
     * The Logger for the logs.
     */
    protected static final Logger LOGGER = Logger.getLogger(TournamentWinController.class.getName());

    /**
     * The ObservableList for the TableView model.
     */
    private ObservableList<Tournament> tournaments;
    
    /**
     * The User tha logs in the application.
     */
    private User user;

    /**
     * The manager class that makes all the Tournament CRUD operations.
     */
    private TournamentManage tournamentable = TournamentManageFactory.getTournamentManageImplementation();

    /**
     * All the ImageViews of the window, used as background decoration or as button icons.
     */
    @FXML
    private ImageView tournamentBackground, tournamentSmoke, bSearchImage, bCleanImage, bHelpImage, bPrintImage, bDeleteImage, bAddImage, tournamentAgentImage;

    /**
     * The Label that works like a title in the window's pane.
     */
    @FXML
    private Label lTournamenTitle;


    @FXML
    private Separator tournamentSeparator;

    /**
     * The ChoiceBox that shows all the possible filtered research options.
     */
    @FXML
    private ChoiceBox chbTournamentSearch;

    /**
     * The TextField where the user writes the value of the parameter used in the Read operations.
     */
    @FXML
    private TextField tfTournamentSearch;

    /**
     * The TableView that obtains, store in and shows the ObservableList data.
     */
    @FXML
    private TableView tvTournaments;

    /**
     * The columns of the TableView that handle String values.
     */
    @FXML
    private TableColumn<Tournament, String> tcName, tcDescription, tcBestOf;

    /**
     * The column of the TableView that handle Date values
     */
    @FXML
    private TableColumn<Tournament, Date> tcDate;

    /**
     * All the buttons in the window that call some of the controllers methods when triggered. 
     */
    @FXML
    private Button bTournamentSearch, bTournamentMatches, bTournamentClean, bTournamentHelp, bTournamentPrint, bTournamentDelete, bTournamentAdd;

    /**
     * The TableView ContextMenu
     */
    @FXML
    private ContextMenu tournamentContextMenu;
    
    /**
     * The ContextMenu options that allows the User call some of the controllers methods when triggered.
     */
    @FXML
    private MenuItem cmCreate, cmDelete, cmMatch, cmPrint;
    

    private Stage stage;

    public void setMainStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root, User user) {
        try {
            LOGGER.info("Init Tournament Window");
            this.user=user;
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Tournaments");
            stage.setResizable(false);
            
            bTournamentDelete.setDisable(true);
            bTournamentMatches.setDisable(true);
            if (user.getUserType().equals(UserType.PLAYER)) {
                bTournamentAdd.setDisable(true);
            }

            if (user.getUserType().equals(UserType.ADMIN)) {
                tvTournaments.setEditable(true);
            }

            tfTournamentSearch.setDisable(true);
            
            // Add the menuitems into the context menu 
            
            tournamentContextMenu.getItems().add(cmCreate);
            tournamentContextMenu.getItems().add(cmDelete);
            tournamentContextMenu.getItems().add(cmMatch);
            tournamentContextMenu.getItems().add(cmPrint);
            

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
            
            

//--------------------------------------------------- CHOICE BOX -------------------------------------------------------------------------------------

            chbTournamentSearch.setValue("ALL");
            chbTournamentSearch.setItems(FXCollections.observableArrayList("ALL", "ID", "NAME", "DATE", "FORMAT", "MATCH"));
            chbTournamentSearch.getSelectionModel().selectedItemProperty().addListener((ObservableValue observableValue, Object oldValue, Object newValue) -> {
                try {
                    tfTournamentSearch.clear();
                    //Check whether item is selected and set value of selected item to Label
                    if (!chbTournamentSearch.getSelectionModel().getSelectedItem().equals("ALL")) {
                        tfTournamentSearch.setDisable(false);
                    } else {
                        tfTournamentSearch.setDisable(true);
                    }

                    //change tfTournamentSearch promptext
                    changePrompText(chbTournamentSearch.getSelectionModel().getSelectedItem());

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong, please try again.", ButtonType.OK).showAndWait();
                }
            });

//--------------------------------------------------------- TABLE --------------------------------------------------------------------------------------


            
            tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
            if (user.getUserType().equals(UserType.ADMIN)) {
                tcName.setCellFactory(TextFieldTableCell.<Tournament>forTableColumn());
                tcName.setOnEditCommit((TableColumn.CellEditEvent<Tournament, String> t) -> {
                    try {
                        if(isNumeric(t.getNewValue())){
                            new Alert(Alert.AlertType.ERROR, "Value can only be characters", ButtonType.OK).showAndWait();
                        }else if(t.getNewValue().equals("") || t.getNewValue()==null){ 
                            new Alert(Alert.AlertType.ERROR, "This field can not be empty", ButtonType.OK).showAndWait();
                        }else{
                            Tournament updatedTournament = ((Tournament) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                            updatedTournament.setName(t.getNewValue());

                            tournamentable.updateTournament(updatedTournament);
                        }
                        refreshTable();
                    } catch (UpdateException ex) {
                        new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
                    }
                });
            }

            tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            if (user.getUserType().equals(UserType.ADMIN)) {
                tcDescription.setCellFactory(TextFieldTableCell.<Tournament>forTableColumn());
                tcDescription.setOnEditCommit((TableColumn.CellEditEvent<Tournament, String> t) -> {
                    try {
                        if(t.getNewValue().length()>120){
                            new Alert(Alert.AlertType.ERROR, "Description length surpased, please insert a description of 120 characters or less.", ButtonType.OK).showAndWait();
                        } else if(t.getNewValue()==null){
                            new Alert(Alert.AlertType.ERROR, "Description must be informed to be updated.", ButtonType.OK).showAndWait();
                        }else{
                            Tournament updatedTournament = ((Tournament) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                            updatedTournament.setDescription(t.getNewValue());

                            tournamentable.updateTournament(updatedTournament);
                        }
                        
                        refreshTable();
                    } catch (UpdateException ex) {
                        new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
                    }
                });
            }

            tcBestOf.setCellValueFactory(new PropertyValueFactory<>("bestOf"));
            if (user.getUserType().equals(UserType.ADMIN)) {
                tcBestOf.setCellFactory(TextFieldTableCell.<Tournament>forTableColumn());
                tcBestOf.setOnEditCommit((TableColumn.CellEditEvent<Tournament, String> t) -> {
                    try {
                        if(isNumeric(t.getNewValue())){
                            Tournament updatedTournament = ((Tournament) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                            updatedTournament.setBestOf(t.getNewValue());

                            tournamentable.updateTournament(updatedTournament);
                        }else if(t.getNewValue().equals("")){ 
                            new Alert(Alert.AlertType.ERROR, "This field can not be empty.", ButtonType.OK).showAndWait();
                        } else{
                            new Alert(Alert.AlertType.ERROR, "Value can only be numbers.", ButtonType.OK).showAndWait();
                        }
                        refreshTable();
                    } catch (UpdateException ex) {
                        new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
                    }
                });
            }

            final Callback<TableColumn<Tournament, Date>, TableCell<Tournament, Date>> dateCellFactory
                    = (TableColumn<Tournament, Date> param) -> new DateTournamentPicker();
            
            tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
            tcDate.setCellFactory(dateCellFactory);
            if(user.getUserType().equals(UserType.PLAYER))
                tcDate.setEditable(false);
            
            if (user.getUserType().equals(UserType.ADMIN)) {  
                
                tcDate.setOnEditCommit((TableColumn.CellEditEvent<Tournament, Date> t) -> {
                    try {
                        if(t.getNewValue()==null){
                            throw new UpdateException();
                        }else{
                            Tournament updatedTournament = ((Tournament) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                            updatedTournament.setDate(t.getNewValue());

                            LOGGER.info(updatedTournament.getDate().toString());

                            tournamentable.updateTournament(updatedTournament);

                            refreshTable();
                        }
                        
                    } catch (UpdateException ex) {
                        if(t.getNewValue()==null){
                            new Alert(Alert.AlertType.ERROR, "Please check a date please.", ButtonType.OK).showAndWait();
                            refreshTable();
                            tvTournaments.getFocusModel().focus(t.getTablePosition());
                        }else{
                            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
                        }
                        
                    }
                });
                
            }

            
            tvTournaments.focusedProperty().addListener(((observable, oldValue, newValue) -> {
                if(!newValue)
                    tvTournaments.getSelectionModel().clearSelection();
            }));
            
            tvTournaments.getSelectionModel().selectedItemProperty().addListener((ObservableValue observableValue,
                    Object oldValue, Object newValue) -> {
                //Check whether item is selected and set value of selected item to Label
                if (tvTournaments.getSelectionModel().getSelectedItem() != null) {
                    bTournamentMatches.setDisable(false);
                    if (user.getUserType().equals(UserType.ADMIN)) {
                        bTournamentDelete.setDisable(false);
                        cmDelete.setDisable(false);
                    }
                } else {
                    bTournamentMatches.setDisable(true);
                    bTournamentDelete.setDisable(true);
                    cmDelete.setDisable(true);
                }

            });
            
            tvTournaments.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> {
                if(e.getButton() == MouseButton.SECONDARY){
                    tournamentContextMenu.show(tvTournaments, e.getSceneX(), e.getSceneY());
                }
            });

        //  ------------------------------- BUTTON ONACTIONS ---------------------------------------
            bTournamentSearch.setOnAction(this::searchTournament);
            
            bTournamentMatches.setOnAction(this::seeTournamentsMatches);
            cmMatch.setOnAction(this::seeTournamentsMatches);
            
            bTournamentClean.setOnAction(this::cleanTournamentSearch);
            
            bTournamentHelp.setOnAction(this::seeTournamentHelpWindow);

            bTournamentPrint.setOnAction(this::printTournament);
            cmPrint.setOnAction(this::printTournament);
            
            bTournamentDelete.setOnAction(this::deleteTournament);
            cmDelete.setOnAction(this::deleteTournament);
            
            bTournamentAdd.setOnAction(this::addTournament);
            cmCreate.setOnAction(this::addTournament);

            MenuBarController.setStage(stage);
            
            tournaments = FXCollections.observableArrayList(tournamentable.findAllTournaments());
            
            tvTournaments.setItems(tournaments);
            
        } catch (ReadException ex) {
            new Alert(Alert.AlertType.ERROR, "An error occurred loading the Tournament window.", ButtonType.OK).showAndWait();
            LOGGER.severe(ex.getMessage());
        } finally{
            stage.show();
        }
    }
    
    /**
     * This is the filtered research method. First it checks that the ChoiceBox selected item isn't 'ALL' and
     * that the TextField tfTournamentSearch is informed. Then it checks if the content of the TextField follows 
     * the ChoiceBox selected option requirements. If it is so, the method calls a specific tournament manager 
     * method and stores all the data in the TableView. If an Exception happens during this proccess, the window
     * shows an Alert informing the User of the problem.
     * 
     * @param event 
     */
    @FXML
    private void searchTournament(ActionEvent event) {
        try {
            if ((tfTournamentSearch.getText().equals("") || tfTournamentSearch.getText().isEmpty()) && (!chbTournamentSearch.getSelectionModel().getSelectedItem().toString().equals("ALL"))) {
                new Alert(Alert.AlertType.ERROR, "This field is required to fill", ButtonType.OK).showAndWait();
            } else {
                switch (chbTournamentSearch.getSelectionModel().getSelectedItem().toString()) {
                    case "ALL":
                        tournaments = FXCollections.observableArrayList(tournamentable.findAllTournaments());
                        refreshTable();
                        break;
                    case "ID":
                        String tfSearchText = tfTournamentSearch.getText();
                        if (isNumeric(tfSearchText)) {
                            tournaments = FXCollections.observableArrayList(tournamentable.findTournament(new Integer(tfSearchText)));
                            refreshTable(tournaments);
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Value can only be numbers", ButtonType.OK).showAndWait();
                        }
                        break;
                    case "NAME":
                        tfSearchText = tfTournamentSearch.getText();
                        if (!isNumeric(tfSearchText)) {
                            tournaments = FXCollections.observableArrayList(tournamentable.findTournamentByName(tfSearchText));
                            refreshTable(tournaments);
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Value can only be characters", ButtonType.OK).showAndWait();
                        }
                        break;
                    case "DATE":
                        try {
                            String insertedDate = tfTournamentSearch.getText();
                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                            format.parse(insertedDate);
                            
                            DateTimeFormatter dTf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            LocalDate localDate = LocalDate.parse(insertedDate, dTf);
                            String date = localDate.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime().toString();

                            tournaments = FXCollections.observableArrayList(tournamentable.findTournamentByDate(date));
                            refreshTable(tournaments);
                    
                        } catch (ParseException ex) {
                            new Alert(Alert.AlertType.ERROR, "Incorrect date format", ButtonType.OK).showAndWait();
                        }
                        break;
                    case "FORMAT":
                        tfSearchText = tfTournamentSearch.getText();
                        if (isNumeric(tfSearchText)) {
                            tournaments = FXCollections.observableArrayList(tournamentable.findTournamentByBestOf(tfSearchText));
                            refreshTable(tournaments);
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Value can only be numbers", ButtonType.OK).showAndWait();
                        }
                        break;
                    case "MATCH":
                        String matchId = tfTournamentSearch.getText();
                        if (isNumeric(matchId)) {
                            tournaments = FXCollections.observableArrayList(tournamentable.findTournamentByMatch(matchId));
                            refreshTable(tournaments);
                        }else {
                            new Alert(Alert.AlertType.ERROR, "Value can only be numbers", ButtonType.OK).showAndWait();
                        }
                        break;
                }
            }
        } catch (ReadException ex) {
            LOGGER.severe(ex.getMessage());
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    /**
     * This is the method that allows to see all the matches of a previously selected Tournament.
     * 
     * @param event 
     */
    @FXML
    private void seeTournamentsMatches(ActionEvent event) {
        try {
            Tournament selectedTournament = (Tournament) tvTournaments.getSelectionModel().getSelectedItem();
            
            Stage sStage = new Stage();
            
            //Get the SignInFXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Matches.fxml"));
            //Load the DOM
            Parent root = (Parent) loader.load();

            //Get the controller from SignIn
            MatchWindowController cont = ((MatchWindowController) loader.getController());

            //Set the stage
            cont.setMainStage(sStage);
            //Initialize the window
            cont.initStage(root,user,selectedTournament,null);
            
            //close this window
            stage.close();
            
        } catch (IOException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
            LOGGER.severe(ex.getMessage());
        }
    }

    /**
     * This method clears the filtered research.
     * @param event 
     */
    @FXML
    private void cleanTournamentSearch(ActionEvent event) {
        try {
            chbTournamentSearch.getSelectionModel().selectFirst();
            tfTournamentSearch.setText("");
            refreshTable();
        } catch (Exception ex) {
            LOGGER.severe(ex.getMessage());
            new Alert(Alert.AlertType.ERROR, "Something went wrong, impossible to refresh the table.", ButtonType.OK).showAndWait();
        }
    }

    /**
     * This method shows an emerging window with an explanation of this window's functions
     * @param event 
     */
    @FXML
    private void seeTournamentHelpWindow(ActionEvent event) {
        try{
            LOGGER.info("Loading help view...");
            //Load node graph from fxml file
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/view/Help.fxml"));
            
            Parent root = (Parent)loader.load();
            
            HelpWindowController helpController=((HelpWindowController)loader.getController());
            
            //Initializes and shows help stage
            helpController.initAndShowStage(root);
        }catch(IOException ex){
                //If there is an error show message and
                //log it.
                new Alert(Alert.AlertType.ERROR,"Error al mostrar ventana de ayuda:\n"+ex.getMessage(), ButtonType.OK).showAndWait();
                LOGGER.severe(ex.getMessage());
        }
    }

    /**
     * This method prints a report of all the data that is shown in the TableView
     * @param event 
     */
    @FXML
    private void printTournament(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/reportTournament.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Tournament>) this.tvTournaments.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    /**
     * This is the method that allows to delete from the server database the selected tournament
     * and then it updates the table content.
     * @param event 
     */
    @FXML
    private void deleteTournament(ActionEvent event) {
        try {
            Optional<ButtonType> resultDelete = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Tournament?").showAndWait();
            if (resultDelete.isPresent() && resultDelete.get() == ButtonType.OK) {
                tournamentable.deleteTournament((Tournament) tvTournaments.getSelectionModel().getSelectedItem());
                tvTournaments.getItems().remove(tvTournaments.getSelectionModel().getSelectedItem());
                tvTournaments.refresh();
                new Alert(Alert.AlertType.CONFIRMATION, "Tournament droped correctly", ButtonType.OK).showAndWait();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Tournament delete canceled", ButtonType.OK).showAndWait();
            }
            tvTournaments.getSelectionModel().clearSelection();
        } catch (DeleteException ex) {
            LOGGER.severe(ex.getMessage());
            new Alert(Alert.AlertType.ERROR, "Delete error:" + ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    /**
     * This method creates a Tournament object, calls the tournament manager so it can be saved in the server and
     * then refresh the content of the table.
     * @param event 
     */
    @FXML
    private void addTournament(ActionEvent event) {
        
        Tournament newTournament = new Tournament(null, "T name", "T description", "1", null, null);
        try {
            Tournament findTournament = null;
            Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            newTournament.setDate(today);
            
            findTournament = tournamentable.findTournamentByName(newTournament.getName());
            if(findTournament==null){
                tournamentable.createTournament(newTournament);
                refreshTable();
            }else{
                throw new ReadException();
            }
            
            
        } catch (CreateException ex) {
            LOGGER.severe(ex.getMessage());
            new Alert(Alert.AlertType.ERROR, "An error has occurred while adding the new Tournament" + ex.getMessage(), ButtonType.OK).showAndWait();
        } catch (ReadException ex) {
            Boolean found = false;
            for (Object t : tvTournaments.getItems()) {
                if (((Tournament) t).equals(newTournament)) {
                    found = true;
                    break;
                }
            }
            //if name exist, shows alert
            if (found) {
                new Alert(Alert.AlertType.ERROR, "The tournament already exists.", ButtonType.OK).showAndWait();
                Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "The tournament already exists.");
            } else {
                Logger.getLogger(TournamentWinController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    /**
     * This method refreshes the content of the table, showing a filtered list of tournaments.
     * @param tournaments 
     */
    private void refreshTable(ObservableList<Tournament> tournaments) {
        tvTournaments.getItems().clear();
        tvTournaments.setItems(tournaments);
    }

    /**
     * This method refreshes the content of the table, showing all the registered tournaments.
     */
    private void refreshTable() {
        try {
            ObservableList<Tournament> refreshedTableItems = FXCollections.observableArrayList(tournamentable.findAllTournaments());
            tvTournaments.getItems().clear();
            tvTournaments.setItems(refreshedTableItems);

        } catch (ReadException ex) {
            LOGGER.severe(ex.getMessage());
            new Alert(Alert.AlertType.ERROR, "Unexpected error while refreshing the table content" + ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    /**
     * Method that checks if the written value of the TextField is composed by a number
     * @param tfSearchText the content inside the TextField
     * @return true if the TextField contains only numbers and false if it doesn't
     */
    private boolean isNumeric(String tfSearchText) {
        if (tfSearchText == null) {
            return false;
        }
        try {
            int tfSearchIsNumber = Integer.parseInt(tfSearchText);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Method used to change the PrompText of the TextField to show an example of a valid
     * value for the chosen filtered research.
     * @param selectedItem 
     */
    private void changePrompText(Object selectedItem) {
        switch (selectedItem.toString()) {
            case "ALL":
                tfTournamentSearch.setPromptText("");
                break;
            case "ID":
                tfTournamentSearch.setPromptText("e.g.: 1");
                break;
            case "NAME":
                tfTournamentSearch.setPromptText("e.g.: EUSports");
                break;
            case "DATE":
                tfTournamentSearch.setPromptText("e.g.: 11/06/2023");
                break;
            case "FORMAT":
                tfTournamentSearch.setPromptText("e.g.: 5");
                break;
            case "MATCH":
                tfTournamentSearch.setPromptText("e.g.: 2");
                break;
        }
    }

}
