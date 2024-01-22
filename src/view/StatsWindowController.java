package view;

import businessLogic.MatchManager;
import businessLogic.MatchManagerFactory;
import businessLogic.PlayerManager;
import businessLogic.PlayerManagerFactory;
import businessLogic.StatsManager;
import businessLogic.StatsManagerFactory;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.ReadException;
import exceptions.UpdateException;
import exceptions.WrongFilterException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Match;
import model.Player;
import model.Stats;
import model.StatsId;
import model.Team;
import model.User;
import model.UserType;
import tableCells.EditingCellMatchDescription;
import tableCells.EditingCellPlayer;

public class StatsWindowController {

    private Stage stage;
    private Logger LOGGER = Logger.getLogger(StatsWindowController.class.getName());
    private StatsManager statsManager = StatsManagerFactory.getStatsManager();
    private PlayerManager playerManager = PlayerManagerFactory.getPlayerManager();
    private MatchManager matchManager = MatchManagerFactory.getMatchManager();
    private ObservableList<Stats> stats;

    @FXML
    private Pane statsViewMainPane;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private AnchorPane secondaryAnchorPane;

    @FXML
    private ImageView decorativeImage;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private TableView statsTableView;

    @FXML
    private TableColumn<Stats, Player> tcPlayer;

    @FXML
    private TableColumn<Stats, Match> tcDescription;

    @FXML
    private TableColumn<Stats, Date> tcDate;

    @FXML
    private TableColumn tcKDA;

    @FXML
    private TableColumn<Stats, String> tcKills;

    @FXML
    private TableColumn<Stats, String> tcDeaths;

    @FXML
    private TableColumn<Stats, String> tcAssists;

    @FXML
    private TableColumn<Stats, Team> tcTeam;

    @FXML
    private ContextMenu tvContextMenu;

    @FXML
    private MenuItem cmAdd;

    @FXML
    private MenuItem cmDelete;

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
        LOGGER.info("Opening Stats window");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //La ventana no sera redimensionable ni modal
        stage.setResizable(false);
        //Se establece el titulo de la ventana a "Stats"
        stage.setTitle("Stats");

        //--------------------------------BUTTONS-------------------------------
        LOGGER.info("Setting up the buttons");
        tbRefresh.setOnAction(this::handleRefreshOnAction);
        cmRefresh.setOnAction(this::handleRefreshOnAction);

        addBtn.setOnAction(this::handleAddOnAction);
        cmAdd.setOnAction(this::handleAddOnAction);
        tbAdd.setOnAction(this::handleAddOnAction);

        deleteBtn.setOnAction(this::handleDeleteOnAction);
        cmDelete.setOnAction(this::handleDeleteOnAction);
        tbDelete.setOnAction(this::handleDeleteOnAction);

        PrintBtn.setOnAction(this::handlePrintOnAction);
        tbPrint.setOnAction(this::handlePrintOnAction);
        cmPrint.setOnAction(this::handlePrintOnAction);

        //---------------------------------TABLE--------------------------------
        LOGGER.info("Prepairing the table");
        Callback<TableColumn<Stats, Player>, TableCell<Stats, Player>> cellPlayerFactory
                = (TableColumn<Stats, Player> p) -> new EditingCellPlayer();
        tcPlayer.setCellFactory(cellPlayerFactory);
        tcPlayer.setCellValueFactory(
                new PropertyValueFactory<>("Player"));
        tcPlayer.setOnEditCommit((TableColumn.CellEditEvent<Stats, Player> t) -> {
            try {
                Stats stat = ((Stats) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                Player found = playerManager.findPlayerByNickname(t.getNewValue().getNickname());
                Player old = new Player(t.getOldValue());
                stat.setPlayer(found);
                if (found != null) {
                    if (!stat.getMatch().getDescription().isEmpty()) {
                        if (old.getNickname().isEmpty()) {
                            LOGGER.info("Creating stat: "+stat.toString());
                            stat.setId(new StatsId(stat.getMatch().getId().toString(), stat.getPlayer().getId().toString()));
                            statsManager.createStats(stat);
                        } else {
                            LOGGER.info("Updating stat: "+stat.toString());
                            stat.setPlayer(t.getNewValue());
                            statsManager.updateStats(stat);
                        }
                    }
                }
            } catch (ReadException ex) {
                Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CreateException ex) {
                Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UpdateException ex) {
                Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Callback<TableColumn<Stats, Match>, TableCell<Stats, Match>> cellMatchDescriptionFactory
                = (TableColumn<Stats, Match> p) -> new EditingCellMatchDescription();
        tcDescription.setCellFactory(cellMatchDescriptionFactory);
        tcDescription.setCellValueFactory(
                new PropertyValueFactory<>("Match"));
        tcDescription.setOnEditCommit((TableColumn.CellEditEvent<Stats, Match> t) -> {
            try {
                Stats stat = ((Stats) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                Match found = matchManager.findMatchByDescription(t.getNewValue().getDescription());
                Match old = new Match(t.getOldValue());
                stat.setMatch(found);
                if (found != null) {
                    if (!stat.getPlayer().getNickname().isEmpty()) {
                        if (old.getDescription().isEmpty()) {
                            //tsat.setId(new StatsId(stat.getMatch().getId().toString(), stat.getPlayer().getId().toString()));
                            statsManager.createStats(stat);
                        } else {
                            statsManager.updateStats(stat);
                        }
                    }
                }
            } catch (ReadException ex) {
                Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (CreateException ex) {
                Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UpdateException ex) {
                Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        tcDate.setCellValueFactory(
                new PropertyValueFactory<>("Date"));

        tcKills.setCellFactory(TextFieldTableCell.<Stats>forTableColumn());
        tcKills.setCellValueFactory(
                new PropertyValueFactory<>("Kills"));
        tcKills.setOnEditCommit((TableColumn.CellEditEvent<Stats, String> t) -> {
            Stats stat = ((Stats) t.getTableView().getItems().get(
                    t.getTablePosition().getRow()));
            try {
                stat.setKills(t.getNewValue());
                statsManager.updateStats(stat);

            } catch (UpdateException ex) {
                stat.setKills(t.getOldValue());
                Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        tcDeaths.setCellFactory(TextFieldTableCell.<Stats>forTableColumn());
        tcDeaths.setCellValueFactory(
                new PropertyValueFactory<>("Deaths"));
        tcDeaths.setOnEditCommit((TableColumn.CellEditEvent<Stats, String> t) -> {
            Stats stat = ((Stats) t.getTableView().getItems().get(
                    t.getTablePosition().getRow()));
            try {
                stat.setDeaths(t.getNewValue());
                statsManager.updateStats(stat);

            } catch (UpdateException ex) {
                stat.setDeaths(t.getOldValue());
                Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        tcAssists.setCellFactory(TextFieldTableCell.<Stats>forTableColumn());
        tcAssists.setCellValueFactory(
                new PropertyValueFactory<>("Assists"));
        tcAssists.setOnEditCommit((TableColumn.CellEditEvent<Stats, String> t) -> {
            Stats stat = ((Stats) t.getTableView().getItems().get(
                    t.getTablePosition().getRow()));
            try {
                stat.setAssists(t.getNewValue());
                statsManager.updateStats(stat);
            } catch (UpdateException ex) {
                stat.setAssists(t.getOldValue());
                Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

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
                Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        statsTableView.getSelectionModel().selectedItemProperty().addListener(this::handleSelectTableChange);

        statsTableView.setItems(stats);

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
                    statsTableView.setItems(stats);
                    statsTableView.refresh();
                    break;
                case "ID":
                    String filter = tfFilter.getText();
                    if (!filter.matches("[0-9]+\\s{1}[0-9]+")) {
                        throw new WrongFilterException("To search by ID you must provide 2 numbers");
                    }
                    String[] ids = filter.split(" ");
                    stats = FXCollections.observableArrayList(statsManager.findStatById(ids[0], ids[1]));
                    statsTableView.setItems(stats);
                    statsTableView.refresh();
                    break;
                case "League Name":
                    if (!tfFilter.getText().matches("[a-zA-Z]+")) {
                        throw new WrongFilterException("A league name must contain only letters");
                    }
                    stats = FXCollections.observableArrayList(statsManager.findStatsByLeagueName(tfFilter.getText()));
                    statsTableView.setItems(stats);
                    statsTableView.refresh();
                    break;
                case "Match Desc.":
                    if (!tfFilter.getText().matches("[a-zA-Z0-9]+")) {
                        throw new WrongFilterException("A match description must contain only letters or numbers");
                    }
                    stats = FXCollections.observableArrayList(statsManager.findStatsByMatchDescription(tfFilter.getText()));
                    statsTableView.setItems(stats);
                    statsTableView.refresh();
                    break;
                case "Player Nickname":
                    if (!tfFilter.getText().matches("[a-zA-Z0-9]+")) {
                        throw new WrongFilterException("A player nickname must contain only letters or numbers");
                    }
                    stats = FXCollections.observableArrayList(statsManager.findStatsByPlayerNickname(tfFilter.getText()));
                    statsTableView.setItems(stats);
                    statsTableView.refresh();
                    break;
                case "Tournament Name":
                    if (!tfFilter.getText().matches("[a-zA-Z]+")) {
                        throw new WrongFilterException("A tournament name must contain only letters");
                    }
                    stats = FXCollections.observableArrayList(statsManager.findStatsByTournamentName(tfFilter.getText()));
                    statsTableView.setItems(stats);
                    statsTableView.refresh();
                    break;
            }
        } catch (ReadException ex) {
            Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WrongFilterException ex) {
            Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
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
        Player player =new Player();
        player.setNickname("");
        Match match = new Match();
        match.setDescription("");
        stats.add(new Stats(null, "0", "0", "0", Team.BLUE_TEAM, player, match));
        statsTableView.refresh();
    }

    public void handleDeleteOnAction(ActionEvent event) {
        try {
            Stats toDelete = (Stats) statsTableView.getSelectionModel().getSelectedItem();
            statsManager.deleteStats(toDelete);
            stats.remove(toDelete);
            statsTableView.getSelectionModel().clearSelection();
            statsTableView.refresh();
        } catch (DeleteException ex) {
            Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleRefreshOnAction(ActionEvent event) {
        statsTableView.refresh();
    }

    public void handlePrintOnAction(ActionEvent event) {
        //PRINT
    }

    public void handleClearOnAction(ActionEvent event) {
        tfFilter.setText("");
        cbFilter.getSelectionModel().select("ALL");
        searchBtn.fire();
    }

    public void handleHelpOnAction(ActionEvent event) {
        //MANUAL
    }
}
