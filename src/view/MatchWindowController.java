package view;

import businessLogic.LeagueManage;
import businessLogic.LeagueManageFactory;
import businessLogic.MatchManager;
import businessLogic.MatchManagerFactory;
import businessLogic.MatchManagerImplementation;
import businessLogic.PlayerManager;
import businessLogic.PlayerManagerFactory;
import businessLogic.TournamentManage;
import businessLogic.TournamentManageFactory;
import exceptions.BadFormatException;
import exceptions.BadUserException;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.OperationAbortException;
import exceptions.ReadException;
import exceptions.UpdateException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.naming.ldap.ManageReferralControl;
import javax.ws.rs.InternalServerErrorException;
import model.League;
import model.Match;
import model.Player;
import model.Team;
import model.Tournament;
import model.User;
import model.UserType;

public class MatchWindowController {

    protected static final Logger LOGGER = Logger.getLogger(MatchWindowController.class.getName());
    private MatchManager manager = MatchManagerFactory.getMatchManager();

    @FXML
    private Pane rootPane;

    @FXML
    private ImageView imgBackground;

    @FXML
    private ImageView imgSmoke1;

    @FXML
    private ImageView imgValkyrie;

    @FXML
    private ImageView imgSmoke2;

    @FXML
    private ImageView imgLaser;

    @FXML
    private ImageView imgSmoke3;

    @FXML
    private ImageView imgCamera1;

    @FXML
    private ImageView imgCamera2;

    @FXML
    private ImageView imgShine;

    @FXML
    private Text tMatchesBG;

    @FXML
    private TableView<Match> tvMatches;

    @FXML
    private TableColumn<Match, String> tcDescription;

    @FXML
    private TableColumn<Match, Date> tcPlayedDate;

    @FXML
    private TableColumn<Match, Team> tcWinnerTeam;

    @FXML
    private TableColumn<Match, String> tcLeague;

    @FXML
    private TableColumn<Match, String> tcTournament;

    @FXML
    private ContextMenu cmContextMenu;

    @FXML
    private MenuItem cmAdd;

    @FXML
    private MenuItem cmDelete;

    @FXML
    private MenuItem cmPrint;

    @FXML
    private MenuItem cmRefresh;

    @FXML
    private MenuItem cmHelp;

    @FXML
    private TextField tfSearchBar;

    @FXML
    private Button btnSearch;

    @FXML
    private ComboBox<String> cbParameters;

    @FXML
    private Button btnAddMatch;

    @FXML
    private Button btnDeleteMatch;

    @FXML
    private Button btnPrint;

    @FXML
    private Text tMenuFG;

    @FXML
    private Button btnFilterRemoval;

    @FXML
    private HBox mbMenuBar;

    private Stage stage;

    private ObservableList<Match> matches;

    private User user = null;

    private Player player = null;

    @FXML
    private void initialize() {
        // Puedes agregar cÃƒÂ³digo de inicializaciÃƒÂ³n aquÃƒÂ­ si es necesario.
    }

    public void setMainStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root, User user, Tournament tournament, League league) {
        ObservableList<String> parameters;
        try {
            //Seting menu bar stage 
            MenuBarController.setStage(stage);
            //Receives a User object from the SignInWindow window and assings it to the window.
            this.user = user;
            //Getting the player object
            if (user.getUserType().equals(UserType.PLAYER)) {
                PlayerManager playerManager = PlayerManagerFactory.getPlayerManager();
                player = playerManager.findPlayerById(user.getId());
            }
            //LOGGER.info("Init Main Window");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Matches");

            tvMatches.setEditable(true);

            tcLeague.setEditable(true);

            tcTournament.setEditable(true);

            tfSearchBar.setDisable(true);

            tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            tcDescription.setCellFactory(TextFieldTableCell.<Match>forTableColumn());

            tcLeague.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Match, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Match, String> param) {
                    League league = param.getValue().getLeague();
                    if (league != null) {
                        return new SimpleStringProperty(league.getName());
                    } else {
                        return new SimpleStringProperty("");
                    }
                }
            });
            tcLeague.setCellFactory(TextFieldTableCell.<Match>forTableColumn());

            tcTournament.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Match, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Match, String> param) {
                    Tournament tournament = param.getValue().getTournament();
                    if (tournament != null) {
                        return new SimpleStringProperty(tournament.getName());
                    } else {
                        return new SimpleStringProperty("");
                    }

                }
            });
            tcTournament.setCellFactory(TextFieldTableCell.<Match>forTableColumn());

            final Callback<TableColumn<Match, Date>, TableCell<Match, Date>> dateCellFactory
                    = (TableColumn<Match, Date> param) -> new DateMatchCellPicker();

            tcPlayedDate.setCellValueFactory(new PropertyValueFactory<>("playedDate"));
            tcPlayedDate.setCellFactory(dateCellFactory);

            tcWinnerTeam.setCellValueFactory(new PropertyValueFactory<>("winner"));
            ObservableList<Team> teams = FXCollections.observableArrayList(Team.BLUE_TEAM, Team.ORANGE_TEAM);
            tcWinnerTeam.setCellFactory(ComboBoxTableCell.forTableColumn(teams));

            if (user.getUserType().equals(UserType.ADMIN)) {
                parameters = FXCollections.observableArrayList("All", "Tournament",
                        "League", "Description", "Nickname");
                cbParameters.setItems(parameters);
                matches = FXCollections.observableArrayList(manager.findAllMatches());
            } else {
                parameters = FXCollections.observableArrayList("My Matches", "All", "Tournament",
                        "League", "Description", "Nickname");
                cbParameters.setItems(parameters);
                matches = FXCollections.observableArrayList(manager.findMatchesByUserNickname(user.getName()));
                btnAddMatch.setVisible(false);
                btnDeleteMatch.setVisible(false);
                tvMatches.setEditable(false);
            }

            if (tournament != null) {
                matches = FXCollections.observableArrayList(manager.findMatchesByTournamentId(tournament.getIdTournament()));
            } else if (league != null) {
                matches = FXCollections.observableArrayList(manager.findMatchesByLeagueId(league.getId()));
            }
            tvMatches.setItems(matches);

            cbParameters.getSelectionModel().selectFirst();

            btnAddMatch.setOnAction(this::handleButtonAddMatchOnAction);
            btnDeleteMatch.setOnAction(this::handleButtonDeleteMatchOnAction);
            btnFilterRemoval.setOnAction(this::handleButtonFilterRemovalOnAction);
            btnSearch.setOnAction(this::handleButtonSearchOnAction);
            btnPrint.setOnAction(this::handleButtonPrintOnAction);
            cbParameters.valueProperty().addListener(this::handleComboBoxParametersOnAction);

            cmAdd.setOnAction(this::handleButtonAddMatchOnAction);
            cmDelete.setOnAction(this::handleButtonDeleteMatchOnAction);
            cmPrint.setOnAction(this::handleButtonPrintOnAction);
            cmHelp.setOnAction(this::handleHelpOnAction);
            cmRefresh.setOnAction(this::handleRefreshOnAction);

            tcDescription.setOnEditCommit((TableColumn.CellEditEvent<Match, String> t) -> {
                try {
                    Match match = ((Match) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    match.setDescription(t.getNewValue());
                    manager.updateMatch(match);

                } catch (UpdateException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    new Alert(Alert.AlertType.ERROR, "Unable to update the match", ButtonType.OK).show();
                }
            });

            tcWinnerTeam.setOnEditCommit((TableColumn.CellEditEvent<Match, Team> t) -> {
                try {
                    Match match = ((Match) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    match.setWinner(t.getNewValue());
                    manager.updateMatch(match);
                } catch (UpdateException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    new Alert(Alert.AlertType.ERROR, "Unable to update the match", ButtonType.OK).show();
                }
            });

            tcPlayedDate.setOnEditCommit((TableColumn.CellEditEvent<Match, Date> t) -> {
                try {
                    Match match = ((Match) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    match.setPlayedDate(t.getNewValue());
                    manager.updateMatch(match);
                } catch (UpdateException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    new Alert(Alert.AlertType.ERROR, "Unable to update the match", ButtonType.OK).show();
                }
            });

            tcLeague.setOnEditCommit((TableColumn.CellEditEvent<Match, String> t) -> {
                Match match = null;
                LeagueManage leagueManager = new LeagueManageFactory().getRegistrable();
                try {

                    match = ((Match) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                    match.setLeague(leagueManager.findLeagueByName(t.getNewValue()).get(0));
                    manager.updateMatch(match);
                } catch (UpdateException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    new Alert(Alert.AlertType.ERROR, "Unable to update the match", ButtonType.OK).show();
                } catch (ReadException e) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, e);
                    new Alert(Alert.AlertType.ERROR, "Unable to find the league", ButtonType.OK).show();
                }
            });

            tcTournament.setOnEditCommit((TableColumn.CellEditEvent<Match, String> t) -> {
                Match match = null;
                TournamentManage tournamentManager = new TournamentManageFactory().getTournamentManageImplementation();
                try {

                    match = ((Match) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                    match.setTournament(tournamentManager.findTournamentByName(t.getNewValue()).get(0));
                    manager.updateMatch(match);
                } catch (UpdateException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    new Alert(Alert.AlertType.ERROR, "Unable to update the match", ButtonType.OK).show();
                } catch (ReadException e) {
                    new Alert(Alert.AlertType.ERROR, "Tournament not found", ButtonType.OK).show();
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, e);
                }
            });

            stage.show();

        } catch (Exception ex) {
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleButtonAddMatchOnAction(ActionEvent event) {
        btnAddMatch.requestFocus();
        Match match = new Match(null, null, null, null, null, null, null);

        try {
            tvMatches.setItems(FXCollections.observableArrayList(manager.findAllMatches()));
            tvMatches.refresh();
            manager.createMatch(match);
            matches.add(match);
            tvMatches.scrollTo(tvMatches.getItems().size() - 1);
            new Alert(Alert.AlertType.INFORMATION, "New row added below", ButtonType.OK).show();
        } catch (CreateException ex) {
            new Alert(Alert.AlertType.ERROR, "Unable to create the match", ButtonType.OK).show();
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);

        } catch (ReadException ex) {
            new Alert(Alert.AlertType.ERROR, "Unable to read matches the match", ButtonType.OK).show();
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tvMatches.setItems(matches);
        tvMatches.refresh();

    }

    public void handleButtonDeleteMatchOnAction(ActionEvent event) {
        btnDeleteMatch.requestFocus();
        LOGGER.info("Deleting row to the table");

        try {
            ButtonType result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected match?", ButtonType.OK, ButtonType.CANCEL).showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {
                tvMatches.setItems(FXCollections.observableArrayList(manager.findAllMatches()));
                tvMatches.refresh();
                Match toDelete = (Match) tvMatches.getSelectionModel().getSelectedItem();
                manager.deleteMatch(toDelete);
                matches.remove(toDelete);
                tvMatches.setItems(matches);
                tvMatches.getSelectionModel().clearSelection();
                tvMatches.refresh();
                new Alert(Alert.AlertType.INFORMATION, "Match deleted!", ButtonType.OK).show();
            } else {
                throw new OperationAbortException();
            }

        } catch (DeleteException ex) {
            Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to delete the match", ButtonType.OK).show();

        } catch (ReadException ex) {
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to read the matches", ButtonType.OK).show();
        } catch (OperationAbortException ex) {
            LOGGER.info("Deleting cancelled..");
            new Alert(Alert.AlertType.ERROR, "Operation cancelled", ButtonType.OK).show();
        }
    }

    public void handleButtonFilterRemovalOnAction(ActionEvent event) {
        btnFilterRemoval.requestFocus();
        tfSearchBar.setText("");
        cbParameters.getSelectionModel().selectFirst();
        if (user.getUserType().equals(UserType.PLAYER)) {
            tfSearchBar.setDisable(true);
        }
        LOGGER.info("Removing filters");
    }

    public void handleButtonSearchOnAction(ActionEvent event) {
        btnSearch.requestFocus();
        try {
            switch (cbParameters.getValue()) {
                case "All":
                    LOGGER.info("Searching all matches...");
                    tvMatches.setItems(FXCollections.observableArrayList(manager.findAllMatches()));
                    break;

                case "Tournament":
                    LOGGER.info("Searching tournament matches...");
                    if (!tfSearchBar.getText().isEmpty() || !tfSearchBar.getText().equals("")) {
                        TournamentManage tournamentManager = TournamentManageFactory.getTournamentManageImplementation();
                        List<Tournament> tournament = tournamentManager.findTournamentByName(tfSearchBar.getText());
                        Integer tournamentId = tournament.get(0).getIdTournament();
                        ObservableList<Match> matchesAux = (ObservableList<Match>) FXCollections.observableArrayList(manager.findMatchesByTournamentId(tournamentId));
                        tvMatches.setItems(matchesAux);
                        tvMatches.refresh();
                    } else {
                        matches = FXCollections.observableArrayList(manager.findAllMatches());
                        matches = FXCollections.observableArrayList(matches.stream().filter(m -> m.getTournament() != null).collect(Collectors.toList()));
                        tvMatches.setItems(matches);
                        tvMatches.refresh();
                    }

                    break;

                case "League":
                    LOGGER.info("Searching league matches...");
                    if (!tfSearchBar.getText().isEmpty() || !tfSearchBar.getText().equals("")) {
                        LeagueManage leagueManager = LeagueManageFactory.getRegistrable();
                        Integer leagueID = leagueManager.findLeagueByName(tfSearchBar.getText()).get(0).getId();
                        tvMatches.setItems((ObservableList<Match>) FXCollections.observableArrayList(manager.findMatchesByLeagueId(leagueID)));
                        tvMatches.refresh();

                    } else {
                        matches = FXCollections.observableArrayList(manager.findAllMatches());
                        matches = FXCollections.observableArrayList(matches.stream().filter(m -> m.getLeague() != null).collect(Collectors.toList()));
                        tvMatches.setItems(matches);
                        tvMatches.refresh();

                    }

                    tvMatches.refresh();
                    break;

                case "Nickname":
                    LOGGER.info("Searching player matches");
                    if (!tfSearchBar.getText().equals("") || !tfSearchBar.getText().isEmpty()) {
                        tvMatches.setItems(FXCollections.observableArrayList(manager.findMatchesByUserNickname(tfSearchBar.getText())));
                        if (tvMatches.getItems().isEmpty()) {
                            throw new BadUserException("No player found");
                        }
                    } else {
                        throw new BadUserException("Please introduce a player");
                    }
                    break;
                case "Description":
                    LOGGER.info("Searching tournament matches by description...");
                    if (tfSearchBar.getText().equals("") || tfSearchBar.getText().isEmpty()) {
                        new Alert(Alert.AlertType.ERROR, "Please introduce a description", ButtonType.OK).show();
                    } else {
                        tvMatches.setItems((ObservableList<Match>) FXCollections.observableArrayList(this.manager.findMatchByDescription(tfSearchBar.getText())));
                        tvMatches.refresh();
                    }

                    break;
                case "My Matches":
                    LOGGER.info("Searching my matches...");
                    tvMatches.setItems(FXCollections.observableArrayList(manager.findMatchesByUserNickname(user.getName())));
                    tvMatches.refresh();

            }
        } catch (ReadException ex) {
            LOGGER.info("Unable to read matches...");
            new Alert(Alert.AlertType.ERROR, "Unable to find the match/es", ButtonType.OK).show();
        } catch (IndexOutOfBoundsException ex) {
            Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            LOGGER.info("No matches were found");
            new Alert(Alert.AlertType.ERROR, "Unable to find the match/es", ButtonType.OK).show();
        } catch (InternalServerErrorException ex) {
            LOGGER.info("No matches were found");
            new Alert(Alert.AlertType.ERROR, "Unable to find the match/es", ButtonType.OK).show();
        } catch (BadUserException ex) {
            LOGGER.info("No player found");
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).show();
        }
    }

    public void handleButtonPrintOnAction(ActionEvent event) {
        btnPrint.requestFocus();
        LOGGER.info("Printing document");
    }

    public void handleComboBoxParametersOnAction(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        switch (newValue) {
            case "All":
                tfSearchBar.setDisable(true);
                tfSearchBar.clear();
                break;

            case "Tournament":
                tfSearchBar.setDisable(false);

                break;
            case "League":
                tfSearchBar.setDisable(false);

                break;
            case "Nickname":
                tfSearchBar.setDisable(false);
                break;
            case "Description":
                tfSearchBar.setDisable(false);
                break;
            case "My matches":
                tfSearchBar.setDisable(true);
                break;

        }
    }

    public void handleRefreshOnAction(ActionEvent event) {
        tvMatches.refresh();
    }

    public void handleHelpOnAction(ActionEvent event) {

    }

}
