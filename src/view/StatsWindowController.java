package view;

import businessLogic.MatchManager;
import businessLogic.MatchManagerFactory;
import businessLogic.PlayerManager;
import businessLogic.PlayerManagerFactory;
import businessLogic.StatsManager;
import businessLogic.StatsManagerFactory;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.InsufficientValuesException;
import exceptions.ReadException;
import exceptions.UpdateException;
import exceptions.WrongFilterException;
import exceptions.WrongValueException;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import model.Match;
import model.Player;
import model.Stats;
import model.StatsId;
import model.Team;
import model.User;
import model.UserType;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import tableCells.DateStatsCellPicker;
import tableCells.EditingCellMatchDescription;
import tableCells.EditingCellPlayer;
import tableCells.EditingStringCells;

/**
 * This is the controller responsible of the Stats window.
 *
 * @author javie
 */
public class StatsWindowController {

    /**
     * The stage of the window
     */
    private Stage stage;

    /**
     * Logger for the execution of the controller
     */
    private Logger LOGGER = Logger.getLogger(StatsWindowController.class.getName());

    /**
     * The manager to perform all Stats data operations
     */
    private StatsManager statsManager = StatsManagerFactory.getStatsManager();

    /**
     * The manager to perform all Player data operations
     */
    private PlayerManager playerManager = PlayerManagerFactory.getPlayerManager();

    /**
     * The manager to perform all Match data operations
     */
    private MatchManager matchManager = MatchManagerFactory.getMatchManager();

    /**
     * The ObserbaleList for the table view
     */
    private ObservableList<Stats> stats;

    /**
     * The Pane that contains everything in the Stats window
     */
    @FXML
    private Pane statsViewMainPane;

    /**
     * The ImageView with the background image of the view
     */
    @FXML
    private ImageView backgroundImage;

    /**
     * The AnchorPane containing every
     */
    @FXML
    private AnchorPane secondaryAnchorPane;

    /**
     *
     */
    @FXML
    private ImageView decorativeImage;

    /**
     *
     */
    @FXML
    private AnchorPane mainAnchorPane;

    /**
     *
     */
    @FXML
    private TableView statsTableView;

    /**
     *
     */
    @FXML
    private TableColumn<Stats, Player> tcPlayer;

    /**
     *
     */
    @FXML
    private TableColumn<Stats, Match> tcDescription;

    /**
     *
     */
    @FXML
    private TableColumn<Stats, Match> tcDate;

    /**
     *
     */
    @FXML
    private TableColumn tcKDA;

    /**
     *
     */
    @FXML
    private TableColumn<Stats, String> tcKills;

    /**
     *
     */
    @FXML
    private TableColumn<Stats, String> tcDeaths;

    /**
     *
     */
    @FXML
    private TableColumn<Stats, String> tcAssists;

    /**
     *
     */
    @FXML
    private TableColumn<Stats, Team> tcTeam;

    /**
     *
     */
    @FXML
    private ContextMenu tvContextMenu;

    /**
     *
     */
    @FXML
    private MenuItem cmAdd;

    /**
     *
     */
    @FXML
    private MenuItem cmDelete;

    /**
     *
     */
    @FXML
    private MenuItem cmPrint;

    @FXML
    private MenuItem cmRefresh;

    @FXML
    private MenuItem cmClear;

    @FXML
    private MenuItem cmHelp;

    @FXML
    private Label errorLbl;

    @FXML
    private ComboBox cbFilter;

    @FXML
    private TextField tfFilter;

    @FXML
    private Button searchBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private Button PrintBtn;

    @FXML
    private Button tbHelp;

    @FXML
    private Button tbAdd;

    @FXML
    private Button tbDelete;

    @FXML
    private Button tbClear;

    @FXML
    private Button tbRefresh;

    @FXML
    private Button tbPrint;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root, User user) {
        MenuBarController.setUser(user);
        MenuBarController.setStage(stage);
        LOGGER.info("Opening Stats window");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //The window is not modal nor resizable
        stage.setResizable(false);
        //The title of the window is set to "Stats"
        stage.setTitle("Stats");
        //Setting the icon of the window
        stage.getIcons().add(new Image("/resources/images/Icon.png"));
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    //If you accept, you will exit the application.
                    //If you cancel, you will return to the initial window.
                    Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?").showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        Platform.exit();
                    }
                    event.consume();
                }
            });

        //--------------------------------BUTTONS-------------------------------
        LOGGER.info("Setting up the buttons");
        tbRefresh.setOnAction(this::handleRefreshOnAction);
        cmRefresh.setOnAction(this::handleRefreshOnAction);
        LOGGER.info("Refresh buttons ready");

        addBtn.setOnAction(this::handleAddOnAction);
        cmAdd.setOnAction(this::handleAddOnAction);
        tbAdd.setOnAction(this::handleAddOnAction);
        LOGGER.info("Add buttons ready");

        deleteBtn.setOnAction(this::handleDeleteOnAction);
        cmDelete.setOnAction(this::handleDeleteOnAction);
        tbDelete.setOnAction(this::handleDeleteOnAction);
        LOGGER.info("Delete buttons ready");

        PrintBtn.setOnAction(this::handlePrintOnAction);
        tbPrint.setOnAction(this::handlePrintOnAction);
        cmPrint.setOnAction(this::handlePrintOnAction);
        LOGGER.info("Print buttons ready");

        //---------------------------------TABLE--------------------------------
        LOGGER.info("Prepairing the table");
        Callback<TableColumn<Stats, Player>, TableCell<Stats, Player>> cellPlayerFactory
                = (TableColumn<Stats, Player> p) -> new EditingCellPlayer();
        tcPlayer.setCellFactory(cellPlayerFactory);
        tcPlayer.setCellValueFactory(
                new PropertyValueFactory<>("Player"));
        tcPlayer.setOnEditCommit((TableColumn.CellEditEvent<Stats, Player> t) -> {
            try {
                if (!t.getNewValue().getNickname().isEmpty()) {
                    Stats stat = ((Stats) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
                    Player found = playerManager.findPlayerByNickname(t.getNewValue().getNickname());
                    Player old = new Player(t.getOldValue());
                    stat.setPlayer(found);
                    if (found != null) {
                        if (!stat.getMatch().getDescription().isEmpty()) {
                            if (old.getNickname().isEmpty()) {
                                LOGGER.info("Creating stat: " + stat.toString());
                                StatsId id = new StatsId(
                                            stat.getMatch().getId().toString(),
                                            stat.getPlayer().getId().toString());
                                stat.setId(id);
                                Stats toCreate = new Stats(
                                        id,
                                        stat.getKills(),
                                        stat.getDeaths(),
                                        stat.getAssists(),
                                        stat.getTeam(),
                                        null,
                                        null);
                                statsManager.createStats(toCreate);
                            } else {
                                LOGGER.info("Updating stat: " + stat.toString());
                                Stats toDelete = new Stats(
                                        stat.getId(),
                                        stat.getKills(),
                                        stat.getDeaths(),
                                        stat.getAssists(),
                                        stat.getTeam(),
                                        null, null);
                                statsManager.deleteStats(toDelete);
                                StatsId id = new StatsId(
                                    stat.getMatch().getId().toString(),
                                    stat.getPlayer().getId().toString());
                                stat.setId(id);
                                Stats toUpdate = new Stats(
                                        id,
                                        stat.getKills(),
                                        stat.getDeaths(),
                                        stat.getAssists(),
                                        stat.getTeam(),
                                        null, null);
                                statsManager.updateStats(toUpdate);
                            }
                        }
                    }
                }
            } catch (ReadException | CreateException | DeleteException | UpdateException ex) {
                errorLbl.setText(ex.getMessage());
                LOGGER.severe(ex.getMessage());
            } finally {
                statsTableView.refresh();
            }
        });
        LOGGER.info("Column for player nickname ready");

        Callback<TableColumn<Stats, Match>, TableCell<Stats, Match>> cellMatchDescriptionFactory
                = (TableColumn<Stats, Match> p) -> new EditingCellMatchDescription();
        tcDescription.setCellFactory(cellMatchDescriptionFactory);
        tcDescription.setCellValueFactory(
                new PropertyValueFactory<>("Match"));
        tcDescription.setOnEditCommit((TableColumn.CellEditEvent<Stats, Match> t) -> {
            try {
                if (!t.getNewValue().getDescription().isEmpty()) {
                    Stats stat = ((Stats) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
                    Match found = matchManager.findMatchByDescription(t.getNewValue().getDescription());
                    Match old = new Match(t.getOldValue());
                    stat.setMatch(found);
                    if (found != null) {
                        if (!stat.getPlayer().getNickname().isEmpty()) {
                            if (old.getDescription().isEmpty()) {
                                LOGGER.info("Creating stat: " + stat.toString());
                                StatsId id = new StatsId(
                                            stat.getMatch().getId().toString(),
                                            stat.getPlayer().getId().toString());
                                stat.setId(id);
                                Stats toCreate = new Stats(id,
                                        stat.getKills(),
                                        stat.getDeaths(),
                                        stat.getAssists(),
                                        stat.getTeam(),
                                        null,
                                        null);
                                statsManager.createStats(toCreate);
                            } else {
                                Stats toDelete = new Stats(
                                        stat.getId(),
                                        stat.getKills(),
                                        stat.getDeaths(),
                                        stat.getAssists(),
                                        stat.getTeam(),
                                        null, null);
                                statsManager.deleteStats(toDelete);
                                StatsId id = new StatsId(
                                    stat.getMatch().getId().toString(),
                                    stat.getPlayer().getId().toString());
                                stat.setId(id);
                                Stats toUpdate = new Stats(
                                        id,
                                        stat.getKills(),
                                        stat.getDeaths(),
                                        stat.getAssists(),
                                        stat.getTeam(),
                                        null, null);
                                statsManager.updateStats(toUpdate);
                            }
                        }
                    }
                }
            } catch (ReadException | CreateException | DeleteException | UpdateException ex) {
                errorLbl.setText(ex.getMessage());
                LOGGER.severe(ex.getMessage());
            } finally {
                statsTableView.refresh();
            }
        });
        LOGGER.info("Column for match description ready");

        Callback<TableColumn<Stats, Match>, TableCell<Stats, Match>> cellMatchDateFactory
                = (TableColumn<Stats, Match> p) -> new DateStatsCellPicker();
        tcDate.setCellFactory(cellMatchDateFactory);
        tcDate.setCellValueFactory(
                new PropertyValueFactory<>("Match"));
        tcDate.setOnEditCommit((TableColumn.CellEditEvent<Stats, Match> t) -> {
           try{
               Stats stat = ((Stats) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
               if(stat.getPlayer().getNickname().isEmpty() || stat.getMatch().getDescription().isEmpty()){
                   throw new InsufficientValuesException("The given values for match description and player nickname aren't valid or are empty");
               }
               matchManager.updateMatch(t.getNewValue());
               stat.setMatch(t.getNewValue());
               searchBtn.fire();
           }catch(InsufficientValuesException | UpdateException ex){
               errorLbl.setText(ex.getMessage());
               LOGGER.severe(ex.getMessage());
           }finally {
                statsTableView.refresh();
            }
        });
        /*tcDate.setOnEditCancel((TableColumn.CellEditEvent<Stats, Match> t) -> {
        });*/
        LOGGER.info("Column for match played date ready");

        Callback<TableColumn<Stats, String>, TableCell<Stats, String>> cellEditingCellFactory
                = (TableColumn<Stats, String> p) -> new EditingStringCells();
        
        tcKills.setCellFactory(cellEditingCellFactory);
        tcKills.setCellValueFactory(
                new PropertyValueFactory<>("Kills"));
        tcKills.setOnEditCommit((TableColumn.CellEditEvent<Stats, String> t) -> {
            Stats stat = ((Stats) t.getTableView().getItems().get(
                    t.getTablePosition().getRow()));
            try {
                if(stat.getPlayer().getNickname().isEmpty() || stat.getMatch().getDescription().isEmpty()){
                   throw new InsufficientValuesException("The given values for match description and player nickname aren't valid or are empty");
                }
                if(!t.getNewValue().matches("[0-9]+")){
                   throw new WrongValueException("You can only insert numbers in the kills field");
                }
                stat.setKills(t.getNewValue());
                Stats update = new Stats(
                        new StatsId(stat.getMatch().getId().toString(), stat.getPlayer().getId().toString()),
                        stat.getKills(),
                        stat.getDeaths(),
                        stat.getAssists(),
                        stat.getTeam(),
                        null, null);
                statsManager.updateStats(update);

            } catch (UpdateException | InsufficientValuesException | WrongValueException ex) {
                stat.setKills(t.getOldValue());
                errorLbl.setText(ex.getMessage());
                LOGGER.severe(ex.getMessage());
            } finally {
                statsTableView.refresh();
            }
        });
        LOGGER.info("Column for kills ready");

        tcDeaths.setCellFactory(cellEditingCellFactory);
        tcDeaths.setCellValueFactory(
                new PropertyValueFactory<>("Deaths"));
        tcDeaths.setOnEditCommit((TableColumn.CellEditEvent<Stats, String> t) -> {
            Stats stat = ((Stats) t.getTableView().getItems().get(
                    t.getTablePosition().getRow()));
            try {
                if(stat.getPlayer().getNickname().isEmpty() || stat.getMatch().getDescription().isEmpty()){
                   throw new InsufficientValuesException("The given values for match description and player nickname aren't valid or are empty");
                }
                if(!t.getNewValue().matches("[0-9]+")){
                   throw new WrongValueException("You can only insert numbers in the kills field");
                }
                stat.setDeaths(t.getNewValue());
                Stats update = new Stats(
                        new StatsId(stat.getMatch().getId().toString(), stat.getPlayer().getId().toString()),
                        stat.getKills(),
                        stat.getDeaths(),
                        stat.getAssists(),
                        stat.getTeam(),
                        null, null);
                statsManager.updateStats(update);

            } catch (UpdateException | WrongValueException | InsufficientValuesException ex) {
                stat.setDeaths(t.getOldValue());
                errorLbl.setText(ex.getMessage());
                LOGGER.severe(ex.getMessage());
            } finally {
                statsTableView.refresh();
            }
        });
        LOGGER.info("Column for deaths ready");

        tcAssists.setCellFactory(cellEditingCellFactory);
        tcAssists.setCellValueFactory(
                new PropertyValueFactory<>("Assists"));
        tcAssists.setOnEditCommit((TableColumn.CellEditEvent<Stats, String> t) -> {
            Stats stat = ((Stats) t.getTableView().getItems().get(
                    t.getTablePosition().getRow()));
            try {
                if(stat.getPlayer().getNickname().isEmpty() || stat.getMatch().getDescription().isEmpty()){
                   throw new InsufficientValuesException("The given values for match description and player nickname aren't valid or are empty");
                }
                if(!t.getNewValue().matches("[0-9]+")){
                   throw new WrongValueException("You can only insert numbers in the kills field");
                }
                stat.setAssists(t.getNewValue());
                Stats update = new Stats(
                        new StatsId(stat.getMatch().getId().toString(), stat.getPlayer().getId().toString()),
                        stat.getKills(),
                        stat.getDeaths(),
                        stat.getAssists(),
                        stat.getTeam(),
                        null, null);
                statsManager.updateStats(update);
            } catch (UpdateException | WrongValueException | InsufficientValuesException ex) {
                stat.setAssists(t.getOldValue());
                errorLbl.setText(ex.getMessage());
                LOGGER.severe(ex.getMessage());
            } finally {
                statsTableView.refresh();
            }
        });
        LOGGER.info("Column for assists ready");

        tcTeam.setCellFactory(ChoiceBoxTableCell.forTableColumn(Team.BLUE_TEAM, Team.ORANGE_TEAM));
        tcTeam.setCellValueFactory(
                new PropertyValueFactory<>("Team"));
        tcTeam.setOnEditCommit((TableColumn.CellEditEvent<Stats, Team> t) -> {
            Stats stat = ((Stats) t.getTableView().getItems().get(
                    t.getTablePosition().getRow()));
            try {
                stat.setTeam(t.getNewValue());
                statsManager.updateStats(stat);
            } catch (UpdateException ex) {
                stat.setTeam(t.getOldValue());
                errorLbl.setText(ex.getMessage());
                LOGGER.severe(ex.getMessage());
            }
        });
        LOGGER.info("Column for team ready");

        statsTableView.getSelectionModel().selectedItemProperty().addListener(this::handleSelectTableChange);
        statsTableView.setItems(stats);
        LOGGER.info("Table ready");
        //-------------------------USER PROFILE---------------------------------
        if (user.getUserType() == UserType.PLAYER) {
            statsTableView.setEditable(false);
            addBtn.setVisible(false);
            cmAdd.setVisible(false);
            tbAdd.setVisible(false);
            deleteBtn.setVisible(false);
            cmDelete.setVisible(false);
            tbDelete.setVisible(false);
        } else {
            statsTableView.setEditable(true);
            addBtn.setVisible(true);
            cmAdd.setVisible(true);
            tbAdd.setVisible(true);
            deleteBtn.setVisible(true);
            cmDelete.setVisible(true);
            tbDelete.setVisible(true);
        }

        //----------------------------FILTERS-----------------------------------
        cbFilter.getItems().addAll(
                "ALL",
                "ID",
                "League Name",
                "Match Desc.",
                "Player Nickname",
                "Tournament Name"
        );
        cbFilter.getSelectionModel().selectedItemProperty().addListener(this::handleSelectItemPropertyChange);
        cbFilter.getSelectionModel().select("ALL");
        tfFilter.textProperty().addListener(this::handleTextPropertyChange);
        searchBtn.setOnAction(this::handleSearchBtnOnAction);
        searchBtn.fire();
        stage.show();
    }

    public void handleSearchBtnOnAction(ActionEvent event) {
        try {
            switch (cbFilter.getSelectionModel().getSelectedItem().toString()) {
                case "ALL":
                    stats = FXCollections.observableArrayList(statsManager.findAllStats());
                    break;
                case "ID":
                    String filter = tfFilter.getText();
                    if (!filter.matches("[0-9]+\\s{1}[0-9]+")) {
                        throw new WrongFilterException("To search by ID you must provide 2 numbers");
                    }
                    String[] ids = filter.split(" ");
                    stats = FXCollections.observableArrayList(statsManager.findStatById(ids[0], ids[1]));
                    break;
                case "League Name":
                    if (!tfFilter.getText().matches("[a-zA-Z]+")) {
                        throw new WrongFilterException("A league name must contain only letters");
                    }
                    stats = FXCollections.observableArrayList(statsManager.findStatsByLeagueName(tfFilter.getText()));
                    break;
                case "Match Desc.":
                    if (!tfFilter.getText().matches("[a-zA-Z0-9]+")) {
                        throw new WrongFilterException("A match description must contain only letters or numbers");
                    }
                    stats = FXCollections.observableArrayList(statsManager.findStatsByMatchDescription(tfFilter.getText()));
                    break;
                case "Player Nickname":
                    if (!tfFilter.getText().matches("[a-zA-Z0-9]+")) {
                        throw new WrongFilterException("A player nickname must contain only letters or numbers");
                    }
                    stats = FXCollections.observableArrayList(statsManager.findStatsByPlayerNickname(tfFilter.getText()));
                    break;
                case "Tournament Name":
                    if (!tfFilter.getText().matches("[a-zA-Z]+")) {
                        throw new WrongFilterException("A tournament name must contain only letters");
                    }
                    stats = FXCollections.observableArrayList(statsManager.findStatsByTournamentName(tfFilter.getText()));
                    break;
            }
        } catch (ReadException | WrongFilterException ex) {
            errorLbl.setText(ex.getMessage());
            LOGGER.severe(ex.getMessage());
        } finally {
            statsTableView.setItems(stats);
            statsTableView.refresh();
        }
    }

    public void handleSelectTableChange(ObservableValue observable, Object oldValue, Object newValue) {
        if (newValue != null) {
            deleteBtn.setDisable(false);
            cmDelete.setDisable(false);
            tbDelete.setDisable(false);
        } else {
            deleteBtn.setDisable(true);
            cmDelete.setDisable(true);
            tbDelete.setDisable(true);
        }
    }

    public void handleSelectItemPropertyChange(ObservableValue observable, Object oldValue, Object newValue) {
        if (newValue.toString().equals("ALL")) {
            tfFilter.setText("");
            tfFilter.setDisable(true);
            searchBtn.setDisable(false);
        } else {
            tfFilter.setDisable(false);
            searchBtn.setDisable(true);
        }
    }

    public void handleTextPropertyChange(ObservableValue observable, String oldValue, String newValue) {
        if (!newValue.isEmpty()) {
            searchBtn.setDisable(false);
        } else {
            searchBtn.setDisable(true);
        }
    }

    public void handleAddOnAction(ActionEvent event) {
        Player player = new Player();
        player.setNickname("");
        Match match = new Match();
        match.setDescription("");
        match.setPlayedDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        stats.add(new Stats(null, "0", "0", "0", Team.BLUE_TEAM, player, match));
        statsTableView.refresh();
    }

    public void handleDeleteOnAction(ActionEvent event) {
        try {
            Stats selected = (Stats) statsTableView.getSelectionModel().getSelectedItem();
            Stats toDelete = new Stats(new StatsId(
                    selected.getMatch().getId().toString(),
                    selected.getPlayer().getId().toString()),
                selected.getKills(),
                selected.getDeaths(),
                selected.getAssists(),
                selected.getTeam(),
                null, null);
            statsManager.deleteStats(toDelete);
            stats.remove(toDelete);
            statsTableView.getSelectionModel().clearSelection();
        } catch (DeleteException ex) {
            errorLbl.setText(ex.getMessage());
            LOGGER.severe(ex.getMessage());
        } finally {
            statsTableView.refresh();
        }
    }

    public void handleRefreshOnAction(ActionEvent event) {
        statsTableView.refresh();
    }

    public void handlePrintOnAction(ActionEvent event) {
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/StatsReport.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Stats>) this.statsTableView.getItems());
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    public void handleClearOnAction(ActionEvent event) {
        tfFilter.setText("");
        cbFilter.getSelectionModel().select("ALL");
        searchBtn.fire();
    }
    
    /**
     * 
     * 
     * @param event 
     */
    public void handleHelpOnAction(ActionEvent event) {
        //MANUAL
    }
}