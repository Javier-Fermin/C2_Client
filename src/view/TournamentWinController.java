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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.tabulator.TablePosition;
import net.sf.jasperreports.view.JasperViewer;
import tableCells.DateTournamentPicker;

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
    
    private User user;

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
    private TableColumn<Tournament, String> tcName, tcDescription, tcBestOf;

    @FXML
    private TableColumn<Tournament, Date> tcDate;

    @FXML
    private Button bTournamentSearch, bTournamentMatches, bTournamentClean, bTournamentHelp, bTournamentPrint, bTournamentDelete, bTournamentAdd;

    @FXML
    private ContextMenu tournamentContextMenu;
    
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
            stage.setTitle("Esports Six - Tournaments");
            stage.setResizable(false);
            //TODO componenetes de la ventana que estan deshabilitados o son editables
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
            
            tfTournamentSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue==null && !chbTournamentSearch.getSelectionModel().getSelectedItem().equals("ALL")){
                    bTournamentSearch.setDisable(true);
                }else{
                    bTournamentSearch.setDisable(false);
                }
            });

//--------------------------------------------------- CHOICE BOX -------------------------------------------------------------------------------------

            chbTournamentSearch.setValue("ALL");
            chbTournamentSearch.setItems(FXCollections.observableArrayList("ALL", "ID", "NAME", "DATE", "FORMAT", "MATCH"));
            chbTournamentSearch.getSelectionModel().selectedItemProperty().addListener((ObservableValue observableValue, Object oldValue, Object newValue) -> {
                try {
                    //Check whether item is selected and set value of selected item to Label
                    if (!chbTournamentSearch.getSelectionModel().getSelectedItem().equals("ALL")) {
                        tfTournamentSearch.setDisable(false);
                    } else {
                        tfTournamentSearch.clear();
                        tfTournamentSearch.setDisable(true);
                        bTournamentSearch.setDisable(false);
                    }

                    //change tfTournamentSearch promptext
                    changePrompText(chbTournamentSearch.getSelectionModel().getSelectedItem());

                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, "Something went wrong, please try again.", ButtonType.OK).showAndWait();
                }
            });

//--------------------------------------------------------- TABLE --------------------------------------------------------------------------------------

            tournaments = FXCollections.observableArrayList(tournamentable.findAllTournaments());

            
            tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
            if (user.getUserType().equals(UserType.ADMIN)) {
                tcName.setCellFactory(TextFieldTableCell.<Tournament>forTableColumn());
                tcName.setOnEditCommit((TableColumn.CellEditEvent<Tournament, String> t) -> {
                    try {
                        if(isNumeric(t.getNewValue())){
                            new Alert(Alert.AlertType.ERROR, "Value can only be characters", ButtonType.OK).showAndWait();
                        } else{
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
                        Tournament updatedTournament = ((Tournament) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                        updatedTournament.setDescription(t.getNewValue());

                        tournamentable.updateTournament(updatedTournament);

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
                        } else{
                            new Alert(Alert.AlertType.ERROR, "Value can only be numbers", ButtonType.OK).showAndWait();
                        }
                        refreshTable();
                    } catch (UpdateException ex) {
                        Logger.getLogger(TournamentWinController.class.getName()).log(Level.SEVERE, null, ex);
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
                        LOGGER.info("Date old value: "+t.getOldValue()+"\nDate new value: "+t.getNewValue());
                        
                        Tournament updatedTournament = ((Tournament) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                        updatedTournament.setDate(t.getNewValue());

                        LOGGER.info(updatedTournament.getDate().toString());

                        tournamentable.updateTournament(updatedTournament);

                        refreshTable();
                    } catch (UpdateException ex) {
                        new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
                        tvTournaments.getFocusModel().focus(t.getTablePosition());
                    }
                });
                
                tcDate.setOnEditCancel((TableColumn.CellEditEvent<Tournament, Date> t) -> {
                    new Alert(Alert.AlertType.ERROR, "Please check a date please", ButtonType.OK).showAndWait();
                    refreshTable();
                });
                
            }

            tvTournaments.setItems(tournaments);

            tvTournaments.getSelectionModel().selectedItemProperty().addListener((ObservableValue observableValue,
                    Object oldValue, Object newValue) -> {

                //Check whether item is selected and set value of selected item to Label
                if (tvTournaments.getSelectionModel().getSelectedItem() != null) {
                    bTournamentMatches.setDisable(false);
                    if (!user.getUserType().equals(UserType.PLAYER)) {
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

            //Asignar componenetes sus metodos
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
            
            stage.show();

        } catch (ReadException ex) {
            new Alert(Alert.AlertType.ERROR, "An error occurred loading the Tournament window.", ButtonType.OK).showAndWait();
            Logger.getLogger(TournamentWinController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // metodos de componentes

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
                        DateTimeFormatter dTf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate localDate = LocalDate.parse(tfTournamentSearch.getText(), dTf);
                        String date = localDate.atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime().toString();
                        
                        tournaments = FXCollections.observableArrayList(tournamentable.findTournamentByDate(date));
                        refreshTable(tournaments);
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
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

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
            
        } catch (Exception ex) {
            Logger.getLogger(TournamentWinController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void cleanTournamentSearch(ActionEvent event) {
        try {
            chbTournamentSearch.getSelectionModel().selectFirst();
            tfTournamentSearch.setText("");
            refreshTable();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong, impossible to refresh the table.", ButtonType.OK).showAndWait();
        }
    }

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
        }catch(Exception ex){
                //If there is an error show message and
                //log it.
                new Alert(Alert.AlertType.ERROR,"Error al mostrar ventana de ayuda:\n", ButtonType.OK).showAndWait();
                LOGGER.log(Level.SEVERE,
                            "UI GestionUsuariosController: Error loading help window: {0}",
                            ex.getMessage());
        }
    }

    @FXML
    private void printTournament(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/reportTournament.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Tournament>) this.tvTournaments.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
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
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Tournament delete canceled", ButtonType.OK).showAndWait();
            }
            tvTournaments.getSelectionModel().clearSelection();
        } catch (DeleteException ex) {
            Logger.getLogger(TournamentWinController.class.getName()).log(Level.SEVERE, null, "Delete error" + ex.getMessage());
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void addTournament(ActionEvent event) {
        try {
            Tournament newTournament = new Tournament(null, "0", "0", "0", null, null);
            tournamentable.createTournament(newTournament);
            refreshTable();
        } catch (CreateException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }

    }

    private void refreshTable(ObservableList<Tournament> tournaments) {
        try {
            tvTournaments.getItems().clear();
            tvTournaments.setItems(tournaments);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong, impossible to refresh the table.", ButtonType.OK).showAndWait();
        }
    }

    private void refreshTable() {
        try {
            ObservableList<Tournament> refreshedTableItems = FXCollections.observableArrayList(tournamentable.findAllTournaments());
            tvTournaments.getItems().clear();
            tvTournaments.setItems(refreshedTableItems);

        } catch (ReadException ex) {
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private boolean isNumeric(String tfSearchText) {
        if (tfSearchText.equals(null)) {
            return false;
        }
        try {
            int tfSearchIsNumber = Integer.parseInt(tfSearchText);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

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
