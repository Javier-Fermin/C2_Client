/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import businessLogic.LeagueManage;
import businessLogic.LeagueManageFactory;
import com.lowagie.text.pdf.PdfName;
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
import javafx.scene.control.TableColumn.CellEditEvent;
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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author 2dam
 */
public class LeagueWindowController {

    protected static final Logger LOGGER = Logger.getLogger(LeagueWindowController.class.getName());

    ObservableList<League> leagueList;

    LeagueManage leagueManage = LeagueManageFactory.getRegistrable();
    
    private User user;
    
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

    public void initStage(Parent root, User user) {
        try {
            this.user = user;
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
            btnMatches.setDisable(true);
            tfsearch.setDisable(true);

            //If user is player, diable create button
            if (user.getUserType().equals(UserType.PLAYER)) {
                btnCreate.setDisable(true);
                miCreate.setDisable(true);
            } else {
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

            tvLeagues.getSelectionModel().selectedItemProperty().addListener((ObservableValue observableValue, Object oldValue, Object newValue) -> {
                //Check whether item is selected and set value of selected item to Label
                if (tvLeagues.getSelectionModel().getSelectedItem() != null) {
                    btnMatches.setDisable(false);
                    if (!user.getUserType().equals(UserType.PLAYER)) {
                        btnDelete.setDisable(false);
                        miDelete.setDisable(false);
                    }
                } else {
                    btnMatches.setDisable(true);
                    btnDelete.setDisable(true);
                    miDelete.setDisable(true);
                }
            });

            cbSeachType.setItems(FXCollections.observableArrayList("ALL", "FINISHED", "UNSTARTED", "NAME", "MATCH"));
            cbSeachType.getSelectionModel().selectFirst();

            cbSeachType.getSelectionModel().selectedItemProperty().addListener((ObservableValue observableValue, Object oldValue, Object newValue) -> {
                //Check whether item is selected and set value of selected item to Label
                if (cbSeachType.getSelectionModel().getSelectedItem().equals("NAME") || cbSeachType.getSelectionModel().getSelectedItem().equals("MATCH")) {
                    tfsearch.setDisable(false);
                } else {
                    tfsearch.setDisable(true);
                }
            });

            leagueList = FXCollections.observableArrayList(leagueManage.findAllLeagues());

            tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tcName.setCellFactory(TextFieldTableCell.<League>forTableColumn());
            tcName.setOnEditCommit((TableColumn.CellEditEvent<League, String> t) -> {
                ObservableList<League> findLeague = null;
                try {
                    League league = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    findLeague = FXCollections.observableArrayList(leagueManage.findLeagueByName(t.getNewValue()));
                    if (t.getNewValue().isEmpty()) {
                        throw new UpdateException();
                    } else {
                        if (!findLeague.isEmpty() || t.getNewValue().matches("[0-9]+")
                                || t.getNewValue().length() > 20) {
                            throw new UpdateException();
                        } else {
                            league.setName(t.getNewValue());
                            leagueManage.updateLeague(league);
                            refreshTable();
                        }
                    }
                } catch (ReadException | UpdateException e) {
                    if (t.getNewValue().isEmpty()) {
                        new Alert(Alert.AlertType.ERROR, "all fields are required to fill", ButtonType.OK).showAndWait();
                    } else {
                        if (!findLeague.isEmpty() || t.getNewValue().matches("[0-9]+")
                                || t.getNewValue().length() > 20) {
                            new Alert(Alert.AlertType.ERROR, "The name value is incorrect", ButtonType.OK).showAndWait();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Update Error", ButtonType.OK).showAndWait();
                        }
                    }
                    Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Update error" + e.getMessage());
                    refreshTable();
                }
            });
            tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            tcDescription.setCellFactory(TextFieldTableCell.<League>forTableColumn());
            tcDescription.setOnEditCommit((TableColumn.CellEditEvent<League, String> t) -> {
                try {
                    League league = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    if (t.getNewValue().isEmpty() || t.getNewValue().length() > 121) {
                        throw new UpdateException();
                    } else {
                        league.setDescription(t.getNewValue());
                        leagueManage.updateLeague(league);
                        refreshTable();
                    }
                } catch (UpdateException e) {
                    if (t.getNewValue().isEmpty()) {
                        new Alert(Alert.AlertType.ERROR, "all fields are required to fill", ButtonType.OK).showAndWait();
                    } else {
                        if (t.getNewValue().length() > 121) {
                            new Alert(Alert.AlertType.ERROR, "The description value is incorrect", ButtonType.OK).showAndWait();
                        } else {
                            new Alert(Alert.AlertType.ERROR, "Update Error", ButtonType.OK).showAndWait();
                        }
                    }
                    Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Update error" + e.getMessage());
                    refreshTable();
                }
            });

            final Callback<TableColumn<League, Date>, TableCell<League, Date>> dateCellFactory
                    = (TableColumn<League, Date> param) -> new DateLeagueCellPicker();

            tcStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            tcStartDate.setCellFactory(dateCellFactory);
            tcStartDate.setOnEditCommit((TableColumn.CellEditEvent<League, Date> t) -> {
                try {
                    if (t.getNewValue().toString().isEmpty()) {
                        new Alert(Alert.AlertType.ERROR, "all fields are required to fill", ButtonType.OK).showAndWait();
                        throw new UpdateException();
                    } else {
                        League league = t.getTableView().getItems().get(t.getTablePosition().getRow());
                        if (league.getEndDate().before(t.getNewValue())) {
                            new Alert(Alert.AlertType.ERROR, "The StartDate value is incorrect", ButtonType.OK).showAndWait();
                            throw new UpdateException();
                        } else {
                            league.setStartDate(t.getNewValue());
                            leagueManage.updateLeague(league);
                            refreshTable();
                        }
                    }
                } catch (UpdateException e) {
                    refreshTable();
                    Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Update error" + e.getMessage());
                }
            });
            tcEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            tcEndDate.setCellFactory(dateCellFactory);
            tcEndDate.setOnEditCommit((TableColumn.CellEditEvent<League, Date> t) -> {
                try {
                    League league = t.getTableView().getItems().get(t.getTablePosition().getRow());
                    if (t.getNewValue().toString().isEmpty()) {
                        new Alert(Alert.AlertType.ERROR, "all fields are required to fill", ButtonType.OK).showAndWait();
                        //NO FUNCIONA
                        //tvLeagues.edit(t.getTablePosition().getRow(), tcName);
                        throw new UpdateException();
                    } else {
                        if (t.getNewValue().before(league.getStartDate())) {
                            new Alert(Alert.AlertType.ERROR, "The EndDate value is incorrect", ButtonType.OK).showAndWait();
                            //NO FUNCIONA
                            //tvLeagues.edit(t.getTablePosition().getRow(), tcName);
                            throw new UpdateException();
                        } else {
                            league.setEndDate(t.getNewValue());
                            leagueManage.updateLeague(league);
                            refreshTable();
                        }
                    }
                } catch (UpdateException e) {
                    Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Update error" + e.getMessage());
                    refreshTable();
                }
            });

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

            tvLeagues.setItems(leagueList);

            stage.show();

        } catch (Exception e) {
            Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Inicialize error" + e.getMessage());
        }
    }

    @FXML
    private void createLeagueButtonAction(ActionEvent event) {
        try {
            League league = new League(null, Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()), "Default name", "Default description");
            ObservableList<League> findLeague = FXCollections.observableArrayList(leagueManage.findLeagueByName(league.getName()));
            if (findLeague.isEmpty()) {
                leagueManage.createLeague(league);
                refreshTable();
//                tvLeagues.getSelectionModel().selectLast();
//                tvLeagues.edit(tvLeagues.getSelectionModel().getSelectedIndex()+1, tcName);
            } else {
                tvLeagues.getSelectionModel().clearSelection();
                throw new ReadException();
            }
        } catch (CreateException e) {
            Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Create error" + e.getMessage());
        } catch (ReadException ex) {
            new Alert(Alert.AlertType.ERROR, "The name already exist", ButtonType.OK).showAndWait();
            Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "The name already exist");
        }
    }

//    @FXML
//    private void updateLeagueButtonAction(ActionEvent event) {
//
//    }
    @FXML
    private void ClearLeagueTableButtonAction(ActionEvent event) {
        cbSeachType.getSelectionModel().selectFirst();
        tfsearch.setText("");
        refreshTable();
    }

    @FXML
    private void SearchLeagueButtonAction(ActionEvent event) {
        try {
            if (cbSeachType.getSelectionModel().getSelectedItem().equals("ALL")) {
                refreshTable();
            } else if (cbSeachType.getSelectionModel().getSelectedItem().equals("FINISHED")) {
                String date = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toString();
                ObservableList<League> finishedLeagueList = FXCollections.observableArrayList(leagueManage.findAllFinishLeagues(date));
                tvLeagues.setItems(finishedLeagueList);
            } else if (cbSeachType.getSelectionModel().getSelectedItem().equals("UNSTARTED")) {
                String date = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toString();
                ObservableList<League> unstartedLeagueList = FXCollections.observableArrayList(leagueManage.findAllUnstartedLeagues(date));
                tvLeagues.setItems(unstartedLeagueList);
            } else if (cbSeachType.getSelectionModel().getSelectedItem().equals("NAME")) {
                if (tfsearch.getText().trim().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "this field is required to fill", ButtonType.OK).showAndWait();
                    throw new ReadException();
                } else {
                    if (!tfsearch.getText().matches("[A-za-z\\s]+")) {
                        new Alert(Alert.AlertType.ERROR, "Values can only be characters", ButtonType.OK).showAndWait();
                        throw new ReadException();
                    }
                    ObservableList<League> finishedLeagueList = FXCollections.observableArrayList(leagueManage.findLeagueByName(tfsearch.getText()));
                    tvLeagues.setItems(finishedLeagueList);
                }
            } else if (cbSeachType.getSelectionModel().getSelectedItem().equals("MATCH")) {
                if (tfsearch.getText().trim().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "this field is required to fill", ButtonType.OK).showAndWait();
                    throw new ReadException();
                } else {
                    if (!tfsearch.getText().matches("[0-9]+")) {
                        new Alert(Alert.AlertType.ERROR, "Values can only be numbers", ButtonType.OK).showAndWait();
                        throw new ReadException();
                    }
                    ObservableList<League> MatchLeagueList = FXCollections.observableArrayList(leagueManage.findLeagueForMatch(Integer.parseInt(tfsearch.getText())));
                    tvLeagues.setItems(MatchLeagueList);
                }
            }
            tvLeagues.getSelectionModel().clearSelection();

        } catch (ReadException e) {
            Logger.getLogger(LeagueWindowController.class
                    .getName()).log(Level.SEVERE, null, "Search Error" + e.getMessage());
        }
    }

    @FXML
    private void DeleteLeagueButtonAction(ActionEvent event) {
        try {
            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this League?").showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                League league = (League) tvLeagues.getSelectionModel().getSelectedItem();
                leagueManage.deleteLeague(league);
                refreshTable();
                new Alert(Alert.AlertType.CONFIRMATION, "League delete correctly", ButtonType.OK).showAndWait();
                tvLeagues.getSelectionModel().clearSelection();
            } else {
                new Alert(Alert.AlertType.CONFIRMATION, "Cancel league delete", ButtonType.OK).showAndWait();
                tvLeagues.getSelectionModel().clearSelection();

            }
        } catch (DeleteException e) {
            Logger.getLogger(LeagueWindowController.class
                    .getName()).log(Level.SEVERE, null, "Delete error" + e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Delete Error", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void printReport(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/LeagueReport.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<League>) this.tvLeagues.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void matchesByLeague(ActionEvent event) {
        //Get the SignInFXML
        try {
            Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to go to this league matches?").showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Stage stageM = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Matches.fxml"));
                Parent root = (Parent) loader.load();
                MatchWindowController cont = ((MatchWindowController) loader.getController());
                cont.setMainStage(stageM);
                cont.initStage(root, user, null, null);
                stage.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refreshTable() {
        try {
            ObservableList<League> refreshList = FXCollections.observableArrayList(leagueManage.findAllLeagues());
            tvLeagues.setItems(refreshList);
            tvLeagues.getSelectionModel().clearSelection();
        } catch (Exception e) {
            Logger.getLogger(LeagueWindowController.class
                    .getName()).log(Level.SEVERE, null, "Refresh error" + e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Refresh Error", ButtonType.OK).showAndWait();
        }

    }
}
