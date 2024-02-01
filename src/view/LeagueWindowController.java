/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import businessLogic.LeagueManage;
import businessLogic.LeagueManageFactory;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.ReadException;
import exceptions.UpdateException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.League;
import model.User;
import model.UserType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import tableCells.DateLeagueCellPicker;

/**
 *
 * @author 2dam
 */
public class LeagueWindowController {

    protected static final Logger LOGGER = Logger.getLogger(LeagueWindowController.class.getName());

    ObservableList<League> leagueList;

    //buisness logic interface
    LeagueManage leagueManage = LeagueManageFactory.getLeagueManage();

    //user to use
    private User user;

    //All FXML items to use
    @FXML
    private TableView tvLeagues;
    @FXML
    private TableColumn<League, String> tcName, tcDescription;
    @FXML
    private TableColumn<League, Date> tcStartDate, tcEndDate;
    @FXML
    private Button btnMatches, btnDelete, btnCreate, btnSearch, btnPrint, btnInfo, tbInfoButton, tbCreateButton, tbDeleteButton, tbPrintButton, btnClear;
    @FXML
    private ComboBox cbSeachType;
    @FXML
    private TextField tfsearch;
    @FXML
    private ToolBar tbLeague;
    @FXML
    private ContextMenu leagueContextMenu;
    @FXML
    private MenuItem miSearch, miCreate, miDelete;

    /**
     * The window stage
     */
    private Stage stage; //This window Stage

    /**
     * Setter for the window stage
     *
     * @param stage the window stage to set
     */
    public void setLeagueStage(Stage stage) {
        this.stage = stage;
    }

    //Init stage
    public void initStage(Parent root, User user) {
        try {
            this.user = user;
            MenuBarController.setUser(user);
            MenuBarController.setStage(stage);
            //Window inicialice
            LOGGER.info("Init League Window");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //Set relizable to false
            stage.setResizable(false);
            stage.setTitle("Leagues Manage");

            // add menu items to menu 
            leagueContextMenu.getItems().add(miSearch);
            leagueContextMenu.getItems().add(miCreate);
            leagueContextMenu.getItems().add(miDelete);

            //Delete and Matches button diable by default
            btnDelete.setDisable(true);
            miDelete.setDisable(true);
            tbDeleteButton.setDisable(true);
            btnMatches.setDisable(true);
            tfsearch.setDisable(true);

            //If user is player, diable create button
            if (user.getUserType().equals(UserType.PLAYER)) {
                btnCreate.setDisable(true);
                miCreate.setDisable(true);
                tbCreateButton.setDisable(true);
            } else {
                //if user admin, table editable
                tvLeagues.setEditable(true);
            }
            /**
             * When user click to exit form the window, ask confirmation to
             * close the app
             */
            stage.setOnCloseRequest((WindowEvent event) -> {
                Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?").showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    return;
                }
                event.consume();
            });

            stage.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, (javafx.scene.input.KeyEvent event) -> {
                if (KeyCode.ESCAPE == event.getCode()) {
                    Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?").showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        Platform.exit();
                    }
                    event.consume();
                }
            });

            //check if item is selected, and enable delete and matches button or disable it
            tvLeagues.getSelectionModel().selectedItemProperty().addListener((ObservableValue observableValue, Object oldValue, Object newValue) -> {
                if (tvLeagues.getSelectionModel().getSelectedItem() != null) {
                    btnMatches.setDisable(false);
                    if (!user.getUserType().equals(UserType.PLAYER)) {
                        btnDelete.setDisable(false);
                        miDelete.setDisable(false);
                        tbDeleteButton.setDisable(false);
                    }
                } else {
                    btnMatches.setDisable(true);
                    btnDelete.setDisable(true);
                    tbDeleteButton.setDisable(true);
                    miDelete.setDisable(true);
                }
            });

            //fill the combobox
            cbSeachType.setItems(FXCollections.observableArrayList("ALL", "FINISHED", "UNSTARTED", "NAME", "MATCH"));
            //set the first value
            cbSeachType.getSelectionModel().selectFirst();

            //enable tfSearch if NAME or MATCH are combobox values
            cbSeachType.getSelectionModel().selectedItemProperty().addListener((ObservableValue observableValue, Object oldValue, Object newValue) -> {
                //Check whether item is selected and set value of selected item to Label
                if (cbSeachType.getSelectionModel().getSelectedItem().equals("NAME") || cbSeachType.getSelectionModel().getSelectedItem().equals("MATCH")) {
                    tfsearch.setDisable(false);
                } else {
                    tfsearch.setDisable(true);
                }
            });

            //find the leagues to fill the table
            leagueList = FXCollections.observableArrayList(leagueManage.findAllLeagues());

            //set editable properties to name colum
            tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tcName.setCellFactory(TextFieldTableCell.<League>forTableColumn());
            tcName.setOnEditCommit((TableColumn.CellEditEvent<League, String> t) -> {
                try {
                    //search if exist a league with the same name
                    ObservableList<League> findLeague = FXCollections.observableArrayList(leagueManage.findLeagueByName(t.getNewValue()));;
                    League league = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    //if the new value is empty, throws exception
                    if (t.getNewValue().isEmpty()) {
                        throw new UpdateException();
                    } else {
                        //if exits a league with the same name, the name have numbers or value is greater
                        //than 20, throws exception
                        if (!findLeague.isEmpty() || !t.getNewValue().matches("[A-za-z\\s]+")
                                || t.getNewValue().length() > 20) {
                            throw new UpdateException();
                        } else {
                            //update name of the league
                            league.setName(t.getNewValue());
                            leagueManage.updateLeague(league);
                            tvLeagues.refresh();
                        }
                    }
                } catch (ReadException | UpdateException e) {
                    if (t.getNewValue().isEmpty()) {
                        //shows alert if value empty
                        new Alert(Alert.AlertType.ERROR, "all fields are required to fill", ButtonType.OK).showAndWait();
                    } else {
                        //shows alert if value incorrect
                        if (!t.getNewValue().matches("[A-za-z\\s]+")
                                || t.getNewValue().length() > 20) {
                            new Alert(Alert.AlertType.ERROR, "The name value is incorrect", ButtonType.OK).showAndWait();
                        } else {
                            //shosw alert if server is down
                            new Alert(Alert.AlertType.ERROR, "Server Update Error", ButtonType.OK).showAndWait();
                        }
                    }
                    Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Update error" + e.getMessage());
                    tvLeagues.refresh();
                }
            });

            //set editable properties to description colum
            tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            tcDescription.setCellFactory(TextFieldTableCell.<League>forTableColumn());
            tcDescription.setOnEditCommit((TableColumn.CellEditEvent<League, String> t) -> {
                try {
                    //if the value is empty or have more than 120 characters, throw exception
                    League league = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    if (t.getNewValue().isEmpty() || t.getNewValue().length() > 120) {
                        throw new UpdateException();
                    } else {
                        //update description
                        league.setDescription(t.getNewValue());
                        leagueManage.updateLeague(league);
                        refreshTable();
                    }
                } catch (UpdateException e) {
                    if (t.getNewValue().isEmpty()) {
                        //shows alert if value is empty
                        new Alert(Alert.AlertType.ERROR, "all fields are required to fill", ButtonType.OK).showAndWait();
                    } else {
                        if (t.getNewValue().length() > 120) {
                            //shows alert if value is greater than 120
                            new Alert(Alert.AlertType.ERROR, "The description value is incorrect", ButtonType.OK).showAndWait();
                        } else {
                            //shows if server is down
                            new Alert(Alert.AlertType.ERROR, "Server Update Error", ButtonType.OK).showAndWait();
                        }
                    }
                    Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Update error" + e.getMessage());
                    tvLeagues.refresh();
                }
            });

            //callback for the datecellpicker for League
            final Callback<TableColumn<League, Date>, TableCell<League, Date>> dateCellFactory
                    = (TableColumn<League, Date> param) -> new DateLeagueCellPicker();

            //set editable properties to StartDate colum
            tcStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            tcStartDate.setCellFactory(dateCellFactory);
            tcStartDate.setOnEditCommit((TableColumn.CellEditEvent<League, Date> t) -> {
                League league = t.getTableView().getItems().get(t.getTablePosition().getRow());
                try {
                    //if value is incorrect, throws exception
                    if (league.getEndDate().before(t.getNewValue())) {
                        throw new UpdateException();
                    } else {
                        //update startdate league
                        league.setStartDate(t.getNewValue());
                        leagueManage.updateLeague(league);
                        tvLeagues.refresh();
                    }
                } catch (UpdateException e) {
                    if (league.getEndDate().before(t.getNewValue())) {
                        //shows alert if value is incorrect
                        new Alert(Alert.AlertType.ERROR, "The StartDate value is incorrect", ButtonType.OK).showAndWait();
                    } else {
                        //shows alert if server is down
                        new Alert(Alert.AlertType.ERROR, "Server Update Error", ButtonType.OK).showAndWait();
                    }
                    Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Update error" + e.getMessage());
                    tvLeagues.refresh();
                }
            });
            tcStartDate.setOnEditCancel((TableColumn.CellEditEvent<League, Date> t) -> {
                //shows alert if cancel edit
                new Alert(Alert.AlertType.ERROR, "Start date update cancel", ButtonType.OK).showAndWait();
                tvLeagues.refresh();
            });
            tcEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            tcEndDate.setCellFactory(dateCellFactory);
            tcEndDate.setOnEditCommit((TableColumn.CellEditEvent<League, Date> t) -> {
                League league = t.getTableView().getItems().get(t.getTablePosition().getRow());
                try {
                    //if value is incorrect, throws exception
                    if (t.getNewValue().before(league.getStartDate())) {
                        throw new UpdateException();
                    } else {
                        //update enddate league
                        league.setEndDate(t.getNewValue());
                        leagueManage.updateLeague(league);
                        tvLeagues.refresh();
                    }
                } catch (UpdateException e) {
                    if (t.getNewValue().before(league.getStartDate())) {
                        //shows alert if value is incorrect
                        new Alert(Alert.AlertType.ERROR, "The EndDate value is incorrect", ButtonType.OK).showAndWait();
                    } else {
                        //shows alert if server is down
                        new Alert(Alert.AlertType.ERROR, "Server update Error", ButtonType.OK).showAndWait();
                    }
                    Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Update error" + e.getMessage());
                    tvLeagues.refresh();
                }
            });
            tcEndDate.setOnEditCancel((TableColumn.CellEditEvent<League, Date> t) -> {
                //shows alert if cancel edit
                new Alert(Alert.AlertType.ERROR, "End date update cancel", ButtonType.OK).showAndWait();
                refreshTable();
            });

            //All setOnACtion for all buttons
            tbInfoButton.setOnAction(this::help);
            btnInfo.setOnAction(this::help);
            miCreate.setOnAction(this::createLeagueButtonAction);
            btnCreate.setOnAction(this::createLeagueButtonAction);
            tbCreateButton.setOnAction(this::createLeagueButtonAction);
            btnClear.setOnAction(this::ClearLeagueTableButtonAction);
            miSearch.setOnAction(this::SearchLeagueButtonAction);
            btnSearch.setOnAction(this::SearchLeagueButtonAction);
            btnDelete.setOnAction(this::DeleteLeagueButtonAction);
            miDelete.setOnAction(this::DeleteLeagueButtonAction);
            tbDeleteButton.setOnAction(this::DeleteLeagueButtonAction);
            tbPrintButton.setOnAction(this::printReport);
            btnPrint.setOnAction(this::printReport);
            btnMatches.setOnAction(this::matchesByLeague);

            //set all the items in the table
            tvLeagues.setItems(leagueList);

            //show the window
            stage.show();

        } catch (ReadException e) {
            //shows alert if window not show
            Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, "Initialize window error", e.getMessage());
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
            stage.close();
        }
    }

    //create method 
    @FXML
    private void createLeagueButtonAction(ActionEvent event) {
        //league with default items to create
        League league = new League(null, Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()), "Default name", "Default description");
        try {
            //search if the league name already exist
            ObservableList<League> findLeague = FXCollections.observableArrayList(leagueManage.findLeagueByName(league.getName()));
            if (findLeague.isEmpty()) {
                //if not, create the league
                leagueManage.createLeague(league);
                refreshTable();
            } else {
                //clear selection and throws exception
                tvLeagues.getSelectionModel().clearSelection();
                throw new ReadException();
            }
        } catch (CreateException ex) {
            //shows alert if server is down in created
            new Alert(Alert.AlertType.ERROR, "Create server error", ButtonType.OK).showAndWait();
            Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Create server error");
        } catch (ReadException e) {
            Boolean found = false;
            for (Object l : tvLeagues.getItems()) {
                if (((League) l).compareTo(league) == 0) {
                    found = true;
                    break;
                }
            }
            //if name exist, shows alert
            if (found) {
                new Alert(Alert.AlertType.ERROR, "The name already exist", ButtonType.OK).showAndWait();
                Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "The name already exist");
            } else {
                //if server downs in search name to valid the name, shows alert
                new Alert(Alert.AlertType.ERROR, "Create find server error", ButtonType.OK).showAndWait();
                Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Create find server error");
            }
        }
    }

    //clear the textfield and put all the leagues 
    @FXML
    private void ClearLeagueTableButtonAction(ActionEvent event) {
        try {
            cbSeachType.getSelectionModel().selectFirst();
            tfsearch.setText("");
            ObservableList<League> finishedLeagueList = FXCollections.observableArrayList(leagueManage.findAllLeagues());
            tvLeagues.setItems(finishedLeagueList);
        } catch (ReadException ex) {
            //shows alert if server is down
            new Alert(Alert.AlertType.ERROR, "Server search error", ButtonType.OK).showAndWait();
            Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Server search exception" + ex.getMessage());
        }

    }

    //search for league button
    @FXML
    private void SearchLeagueButtonAction(ActionEvent event) {
        try {
            //if combobox select all, find all leagues
            if (cbSeachType.getSelectionModel().getSelectedItem().equals("ALL")) {
                ObservableList<League> finishedLeagueList = FXCollections.observableArrayList(leagueManage.findAllLeagues());
                tvLeagues.setItems(finishedLeagueList);
                //if combobox select FINISHED, find all finished leagues
            } else if (cbSeachType.getSelectionModel().getSelectedItem().equals("FINISHED")) {
                String date = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toString();
                ObservableList<League> finishedLeagueList = FXCollections.observableArrayList(leagueManage.findAllFinishLeagues(date));
                tvLeagues.setItems(finishedLeagueList);
                //if combobox select UNSTARTED, find all unstarted leagues
            } else if (cbSeachType.getSelectionModel().getSelectedItem().equals("UNSTARTED")) {
                String date = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toString();
                ObservableList<League> unstartedLeagueList = FXCollections.observableArrayList(leagueManage.findAllUnstartedLeagues(date));
                tvLeagues.setItems(unstartedLeagueList);
                //if combobox select NAME, find the league
            } else if (cbSeachType.getSelectionModel().getSelectedItem().equals("NAME")) {
                //if the textfield is empty, throws exception
                if (tfsearch.getText().trim().isEmpty()) {
                    throw new ReadException();
                } else {
                    //if not matches with character only, throws exception
                    if (!tfsearch.getText().matches("[A-za-z\\s]+")) {
                        throw new ReadException();
                    } else {
                        ObservableList<League> finishedLeagueList = FXCollections.observableArrayList(leagueManage.findLeagueByName(tfsearch.getText()));
                        tvLeagues.setItems(finishedLeagueList);
                    }
                }
                //if combobox select MATCH, find the league for that match
            } else if (cbSeachType.getSelectionModel().getSelectedItem().equals("MATCH")) {
                //if the textfield is empty, throws exception
                if (tfsearch.getText().trim().isEmpty()) {
                    throw new ReadException();
                } else {
                    //if not matches with numbers only, throws exception
                    if (!tfsearch.getText().matches("[0-9]+")) {
                        throw new ReadException();
                    } else {
                        ObservableList<League> MatchLeagueList = FXCollections.observableArrayList(leagueManage.findLeagueForMatch(Integer.parseInt(tfsearch.getText())));
                        tvLeagues.setItems(MatchLeagueList);
                    }
                }
            }
            tvLeagues.getSelectionModel().clearSelection();
        } catch (ReadException e) {
            //if text of textfield is empty, shows alert
            if (tfsearch.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "this field is required to fill", ButtonType.OK).showAndWait();
                Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "this field is required to fill" + e.getMessage());
            }//if not matches with numbers only, throws exception
            else if (cbSeachType.getSelectionModel().getSelectedItem().equals("MATCH") && !tfsearch.getText().matches("[0-9]+")) {
                new Alert(Alert.AlertType.ERROR, "Values can only be numbers", ButtonType.OK).showAndWait();
                Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Values can only be numbers" + e.getMessage());
            }//if not matches with character only, throws exception
            else if (cbSeachType.getSelectionModel().getSelectedItem().equals("NAME") && !tfsearch.getText().matches("[A-za-z\\s]+")) {
                new Alert(Alert.AlertType.ERROR, "Values can only be characters", ButtonType.OK).showAndWait();
                Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Values can only be characters" + e.getMessage());
            } else {
                //if server is down, shows alert
                new Alert(Alert.AlertType.ERROR, "Server search error", ButtonType.OK).showAndWait();
                Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Server search exception" + e.getMessage());
            }

        }
    }

    //delete for the leagues
    @FXML
    private void DeleteLeagueButtonAction(ActionEvent event) {
        try {
            //ask if wants to delete the league
            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this League?").showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //delete the league
                League league = (League) tvLeagues.getSelectionModel().getSelectedItem();
                leagueManage.deleteLeague(league);
                refreshTable();
                //shows alert for delete correct
                new Alert(Alert.AlertType.CONFIRMATION, "League delete correctly", ButtonType.OK).showAndWait();
                tvLeagues.getSelectionModel().clearSelection();
            } else {
                //shows alert if cancels the edit
                new Alert(Alert.AlertType.CONFIRMATION, "Cancel league delete", ButtonType.OK).showAndWait();
                tvLeagues.getSelectionModel().clearSelection();
            }
        } catch (DeleteException e) {
            //shows alert if server is down
            Logger.getLogger(LeagueWindowController.class
                    .getName()).log(Level.SEVERE, null, "Server delete error" + e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Server delete Error", ButtonType.OK).showAndWait();
        }
    }

    //method for the printo of the report
    @FXML
    private void printReport(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/LeagueReport.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<League>) this.tvLeagues.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
            Logger.getLogger(LeagueWindowController.class
                    .getName()).log(Level.SEVERE, null, e.getMessage());
        }
    }

    //method to find matches for a leguae
    @FXML
    private void matchesByLeague(ActionEvent event) {
        try {
            //get the selected league for the search
            League league = (League) tvLeagues.getSelectionModel().getSelectedItem();
            //ask if he wants to close the window
            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to go to this league matches?").showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //shows the matches window and close the leagues window
                Stage stageM = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Matches.fxml"));
                Parent root = (Parent) loader.load();
                MatchWindowController cont = ((MatchWindowController) loader.getController());
                cont.setMainStage(stageM);
                cont.initStage(root, user, null, league);
                stage.close();
            }
        } catch (IOException ex) {
            //shows alert if any exception
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
            Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //method for the help window
    @FXML
    private void help(ActionEvent event) {
        try {
            //shows the help window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Help.fxml"));
            Parent root = (Parent) loader.load();
            LeagueHelpController helpController
                    = ((LeagueHelpController) loader.getController());
            //Initializes and shows help stage
            helpController.initAndShowStage(root);
        } catch (IOException ex) {
            //shows the error if error
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
            Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //method to refresh the table
    private void refreshTable() {
        btnSearch.fire();
        tvLeagues.getSelectionModel().clearSelection();
    }
}
