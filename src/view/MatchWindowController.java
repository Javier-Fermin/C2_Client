package view;

import businessLogic.LeagueManage;
import businessLogic.LeagueManageFactory;
import businessLogic.MatchManager;
import businessLogic.MatchManagerFactory;
import businessLogic.PlayerManager;
import businessLogic.PlayerManagerFactory;
import businessLogic.TournamentManage;
import businessLogic.TournamentManageFactory;
import exceptions.BadUserException;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.DescriptionAlreadyExsistsException;
import exceptions.DescriptionNotFoundException;
import exceptions.NoResultFoundException;
import exceptions.OperationAbortException;
import exceptions.ReadException;
import exceptions.UpdateException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import javax.ws.rs.InternalServerErrorException;
import model.League;
import model.Match;
import model.Player;
import model.Team;
import model.Tournament;
import model.User;
import model.UserType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import tableCells.DateMatchCellPicker;

/**
 * Sets the main stage for the controller.
 *
 * @author Imanol
 */
public class MatchWindowController {

    protected static final Logger LOGGER = Logger.getLogger(MatchWindowController.class.getName());
    private final MatchManager manager = MatchManagerFactory.getMatchManager();

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
    private Button btnInfo;

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

    /**
     * Sets the main stage for the controller.
     *
     * @param stage The main stage of the application.
     */
    public void setMainStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the stage with the specified parameters.
     *
     * @param root The root node of the scene.
     * @param user The user object representing the current user.
     * @param tournament The tournament associated with the match window (can be
     * null).
     * @param league The league associated with the match window (can be null).
     */
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
                cmAdd.setVisible(false);
                cmDelete.setVisible(false);
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
            btnInfo.setOnAction(this::handleHelpOnAction);

            cbParameters.valueProperty().addListener(this::handleComboBoxParametersOnAction);

            cmAdd.setOnAction(this::handleButtonAddMatchOnAction);
            cmDelete.setOnAction(this::handleButtonDeleteMatchOnAction);
            cmPrint.setOnAction(this::handleButtonPrintOnAction);
            cmHelp.setOnAction(this::handleHelpOnAction);
            cmRefresh.setOnAction(this::handleRefreshOnAction);

            tcDescription.setOnEditCommit((TableColumn.CellEditEvent<Match, String> t) -> {
                try {
                    Match match = ((Match) t.getTableView().getItems().get(t.getTablePosition().getRow()));

                    manager.checkDescriptionForMatchAlreadyExisting(t.getNewValue());
                    match.setDescription(t.getNewValue());
                    manager.updateMatch(match);

                } catch (UpdateException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex.getMessage());
                    new Alert(Alert.AlertType.ERROR, "Unable to update the match", ButtonType.OK).show();
                } catch (ReadException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex.getMessage());
                    new Alert(Alert.AlertType.ERROR, "Unable to check if description already exists", ButtonType.OK).show();
                } catch (DescriptionAlreadyExsistsException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex.getMessage());
                    new Alert(Alert.AlertType.ERROR, "The description already exists", ButtonType.OK).show();
                } finally {
                    tvMatches.refresh();
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
                } finally {
                    tvMatches.refresh();
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
                } finally {
                    tvMatches.refresh();
                }
            });
            tcPlayedDate.setOnEditCancel((TableColumn.CellEditEvent<Match, Date> t) -> {
                new Alert(Alert.AlertType.ERROR, "Please introduce a valid date", ButtonType.OK).show();
                tvMatches.refresh();
            });
            

            tcLeague.setOnEditCommit((TableColumn.CellEditEvent<Match, String> t) -> {
                Match match = null;
                LeagueManage leagueManager = new LeagueManageFactory().getLeagueManage();
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
                } catch (IndexOutOfBoundsException e) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, e);
                    new Alert(Alert.AlertType.ERROR, "No leagues were found with that name", ButtonType.OK).show();
                } finally {
                    tvMatches.refresh();
                }

            });

            tcTournament.setOnEditCommit((TableColumn.CellEditEvent<Match, String> t) -> {
                Match match = null;
                TournamentManage tournamentManager = new TournamentManageFactory().getTournamentManageImplementation();
                try {

                    match = ((Match) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    match.setTournament(tournamentManager.findTournamentByName(t.getNewValue()));
                    manager.updateMatch(match);
                } catch (UpdateException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    new Alert(Alert.AlertType.ERROR, "Unable to update the match", ButtonType.OK).show();
                } catch (ReadException e) {
                    new Alert(Alert.AlertType.ERROR, "Tournament not found", ButtonType.OK).show();
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, e);
                } catch (IndexOutOfBoundsException e) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, e);
                    new Alert(Alert.AlertType.ERROR, "No tournaments were found with that name", ButtonType.OK).show();
                } finally {
                    tvMatches.refresh();
                }
            });

            stage.show();

        } catch (ReadException ex) {
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the "Add Match" button action. Creates a new match, adds it to
     * the database, refreshes the match table, and displays an information
     * alert.
     *
     * @param event The ActionEvent triggered by the "Add Match" button.
     */
    public void handleButtonAddMatchOnAction(ActionEvent event) {
        btnAddMatch.requestFocus();
        Match match = new Match(null, Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()), null, null, null, null, null);

        try {
            manager.createMatch(match);
            cbParameters.setValue("All");
            btnSearch.fire();

            tvMatches.scrollTo(tvMatches.getItems().size() - 1);
            tvMatches.refresh();
            new Alert(Alert.AlertType.INFORMATION, "New row added below", ButtonType.OK).show();
        } catch (CreateException ex) {
            new Alert(Alert.AlertType.ERROR, "Unable to create the match", ButtonType.OK).show();
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Handles the "Delete Match" button action. Deletes the selected match,
     * updates the match table, and displays a confirmation dialog and
     * information alerts.
     *
     * @param event The ActionEvent triggered by the "Delete Match" button.
     */
    public void handleButtonDeleteMatchOnAction(ActionEvent event) {
        btnDeleteMatch.requestFocus();
        LOGGER.info("Deleting row to the table");

        try {
            Match toDelete = (Match) tvMatches.getSelectionModel().getSelectedItem();
            if (toDelete == null) {
                throw new OperationAbortException("Please select a match to delete");
            }
            ButtonType result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete the selected match?", ButtonType.OK, ButtonType.CANCEL).showAndWait().orElse(ButtonType.CANCEL);
            if (result == ButtonType.OK) {

                manager.deleteMatch(toDelete);
                matches.remove(toDelete);
                tvMatches.setItems(matches);
                tvMatches.getSelectionModel().clearSelection();
                tvMatches.refresh();

                new Alert(Alert.AlertType.INFORMATION, "Match deleted!", ButtonType.OK).show();
            } else {
                throw new OperationAbortException("Operation cancelled");
            }

        } catch (DeleteException ex) {
            Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "Unable to delete the match", ButtonType.OK).show();

        } catch (OperationAbortException ex) {
            LOGGER.info("Deleting cancelled..");
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).show();
        }
    }

    /**
     * Handles the "Remove Filters" button action. Resets search bar, parameter
     * combo box, and disables the search bar based on user type.
     *
     * @param event The ActionEvent triggered by the "Remove Filters" button.
     */
    public void handleButtonFilterRemovalOnAction(ActionEvent event) {
        btnFilterRemoval.requestFocus();
        tfSearchBar.setText("");
        cbParameters.getSelectionModel().selectFirst();
        if (user.getUserType().equals(UserType.PLAYER)) {
            tfSearchBar.setDisable(true);
        }
        LOGGER.info("Removing filters");
    }

    /**
     * Handles the "Search" button action. Searches for matches based on the
     * selected parameter and updates the match table accordingly.
     *
     * @param event The ActionEvent triggered by the "Search" button.
     */
    public void handleButtonSearchOnAction(ActionEvent event) {
        btnSearch.requestFocus();
        try {
            switch (cbParameters.getValue()) {
                case "All":
                    LOGGER.info("Searching all matches...");
                    matches = FXCollections.observableArrayList(manager.findAllMatches());
                    tvMatches.setItems(matches);
                    tvMatches.refresh();
                    break;

                case "Tournament":
                    LOGGER.info("Searching tournament matches...");
                    if (!tfSearchBar.getText().isEmpty() || !tfSearchBar.getText().equals("")) {
                        TournamentManage tournamentManager = TournamentManageFactory.getTournamentManageImplementation();
                        Tournament tournament = tournamentManager.findTournamentByName(tfSearchBar.getText());
                        Integer tournamentId = tournament.getIdTournament();
                        matches = FXCollections.observableArrayList(manager.findMatchesByTournamentId(tournamentId));
                        tvMatches.setItems(matches);
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
                        LeagueManage leagueManager = LeagueManageFactory.getLeagueManage();
                        Integer leagueID = leagueManager.findLeagueByName(tfSearchBar.getText()).get(0).getId();
                        matches = FXCollections.observableArrayList(manager.findMatchesByLeagueId(leagueID));
                        tvMatches.setItems(matches);
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
                        matches = FXCollections.observableArrayList(manager.findMatchesByUserNickname(tfSearchBar.getText()));
                        tvMatches.setItems(matches);
                        if (tvMatches.getItems().isEmpty()) {
                            throw new BadUserException("No player found");
                        }
                        tvMatches.refresh();

                    } else {
                        throw new BadUserException("Please introduce a player");
                    }
                    break;
                case "Description":
                    LOGGER.info("Searching tournament matches by description...");
                    if (tfSearchBar.getText().equals("") || tfSearchBar.getText().isEmpty()) {
                        throw new BadUserException("Please introduce a description");
                    } else {
                        matches = FXCollections.observableArrayList(manager.findMatchByDescription(tfSearchBar.getText()));
                        tvMatches.setItems(matches);
                        if (tvMatches.getItems().isEmpty()) {
                            throw new DescriptionNotFoundException("No matches found for the selected player");
                        }
                        tvMatches.refresh();
                    }

                    break;
                case "My Matches":
                    LOGGER.info("Searching my matches...");
                    matches = FXCollections.observableArrayList(manager.findMatchesByUserNickname(user.getName()));
                    tvMatches.setItems(matches);
                    tvMatches.refresh();
            }
        } catch (ReadException ex) {
            LOGGER.info("Unable to read matches...");
            new Alert(Alert.AlertType.ERROR, "Unable to find the match/es", ButtonType.OK).show();
        } catch (IndexOutOfBoundsException | InternalServerErrorException ex) {
            LOGGER.info("No matches were found");
            new Alert(Alert.AlertType.ERROR, "Unable to find the match/es", ButtonType.OK).show();
        } catch (BadUserException ex) {
            LOGGER.info("No player found");
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).show();
        } catch (DescriptionNotFoundException ex) {
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).show();
        } catch (NoResultFoundException ex) {
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
            new Alert(Alert.AlertType.ERROR, "No matches related to the description", ButtonType.OK).show();
        }
    }

    /**
     * Handles the "Print" button action. Generates and displays a JasperReports
     * document based on the MatchesReport.jrxml template.
     *
     * @param event The ActionEvent triggered by the "Print" button.
     */
    public void handleButtonPrintOnAction(ActionEvent event) {
        LOGGER.info("Printing document");
        btnPrint.requestFocus();
        try {
            JasperReport report = JasperCompileManager.compileReport(getClass().getClassLoader().getResourceAsStream("reports/MatchesReport.jrxml"));
            JRBeanCollectionDataSource dataItems = new JRBeanCollectionDataSource((Collection<Match>) this.tvMatches.getItems());
            Map<String, Object> parameters = new HashMap<>();

            URL url = this.getClass().getClassLoader().getResource("resources/images/Icon.jpg");
            parameters.put("logo", url);

            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataItems);
            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Unable to print a report", ButtonType.OK).showAndWait();
            LOGGER.info("Unable to make the report\n");
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    /**
     * Handles the ComboBox parameter selection action. Adjusts the search bar
     * state and prompt text based on the selected parameter.
     *
     * @param observable The property being observed (not used in this method).
     * @param oldValue The old parameter value.
     * @param newValue The new parameter value.
     */
    public void handleComboBoxParametersOnAction(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        switch (newValue) {
            case "All":
                tfSearchBar.setDisable(true);
                tfSearchBar.clear();
                tfSearchBar.setPromptText("");
                tfSearchBar.setText("");
                break;

            case "Tournament":
                tfSearchBar.setDisable(false);
                tfSearchBar.setPromptText("League name/ leave blank");
                tfSearchBar.setText("");

                break;
            case "League":
                tfSearchBar.setDisable(false);
                tfSearchBar.setPromptText("Tournament name/ leave blank");
                tfSearchBar.setText("");

                break;
            case "Nickname":
                tfSearchBar.setDisable(false);
                tfSearchBar.setPromptText("Nickname");
                tfSearchBar.setText("");
                break;
            case "Description":
                tfSearchBar.setDisable(false);
                tfSearchBar.setPromptText("Description");
                tfSearchBar.setText("");
                break;
            case "My matches":
                tfSearchBar.setDisable(true);
                tfSearchBar.setPromptText("");
                tfSearchBar.setText("");
                break;

        }
    }

    /**
     * Handles the "Refresh" button action. Refreshes the content of the match
     * table.
     *
     * @param event The ActionEvent triggered by the "Refresh" button.
     */
    public void handleRefreshOnAction(ActionEvent event) {
        tvMatches.refresh();
    }

    /**
     * Handles the "Help" button action. Displays a window with an Html page
     * with information about the MatchWindow
     *
     * @param event The ActionEvent triggered by the "Help" button.
     */
    public void handleHelpOnAction(ActionEvent event) {
         try {
            //shows the help window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HelpMatch.fxml"));
            Parent root = (Parent) loader.load();
            MatchHelpController helpController
                    = ((MatchHelpController) loader.getController());
            //Initializes and shows help stage
            helpController.initAndShowStage(root);
        } catch (IOException ex) {
            //shows the error if error
            new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK).showAndWait();
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setStage(Stage stage) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
