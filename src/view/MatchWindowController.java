package view;

import businessLogic.MatchManager;
import businessLogic.MatchManagerFactory;
import businessLogic.MatchManagerImplementation;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.UpdateException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import model.League;
import model.Match;
import model.Team;
import model.Tournament;
import model.User;

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
    private TableColumn<Match, String> tcLeagueTournament;

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

    @FXML
    private void initialize() {
        // Puedes agregar cÃƒÂ³digo de inicializaciÃƒÂ³n aquÃƒÂ­ si es necesario.
    }

    public void setMainStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root, User user, Tournament tournament, League league) {
        MatchManager manager;

        try {
            //Receives a User object from the SignInWindow window.
            //LOGGER.info("Init Main Window");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Matches");

            tvMatches.setEditable(true);

            tcLeagueTournament.setEditable(false);

            tfSearchBar.setDisable(true);

            ObservableList<String> parameters = FXCollections.observableArrayList("All", "Tournament",
                    "League", "Description", "Nickname");
            cbParameters.setItems(parameters);
            cbParameters.getSelectionModel().selectFirst();
            manager = MatchManagerFactory.getMatchManager();
            if(tournament == null && league != null){
            
            }else if(tournament != null && league == null){
                
            }else{
                matches = FXCollections.observableArrayList(manager.findAllMatches());
            }
            

            tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            tcDescription.setCellFactory(TextFieldTableCell.<Match>forTableColumn());

            tcLeagueTournament.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Match, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Match, String> param) {
                    if (param.getValue().getLeague() != null && param.getValue().getTournament() != null) {
                        return new SimpleStringProperty(param.getValue().getLeague().getName()).concat("/" + param.getValue().getTournament().getName());
                    } else if (param.getValue().getLeague() != null) {
                        return new SimpleStringProperty(param.getValue().getLeague().getName());
                    } else if (param.getValue().getTournament() != null) {
                        return new SimpleStringProperty(param.getValue().getTournament().getName());
                    }
                    return null;
                }
            });
            tcLeagueTournament.setCellFactory(TextFieldTableCell.forTableColumn());

            final Callback<TableColumn<Match, Date>, TableCell<Match, Date>> dateCellFactory
                    = (TableColumn<Match, Date> param) -> new DateMatchCellPicker();

            tcPlayedDate.setCellValueFactory(new PropertyValueFactory<>("playedDate"));
            tcPlayedDate.setCellFactory(dateCellFactory);

            tcWinnerTeam.setCellValueFactory(new PropertyValueFactory<>("winner"));
            ObservableList<Team> teams = FXCollections.observableArrayList(Team.BLUE_TEAM, Team.ORANGE_TEAM);
            tcWinnerTeam.setCellFactory(ComboBoxTableCell.forTableColumn(teams));

            tvMatches.setItems(matches);

            btnAddMatch.setOnAction(this::handleButtonAddMatchOnAction);
            btnDeleteMatch.setOnAction(this::handleButtonDeleteMatchOnAction);
            btnFilterRemoval.setOnAction(this::handleButtonFilterRemovalOnAction);
            btnSearch.setOnAction(this::handleButtonSearchOnAction);
            btnPrint.setOnAction(this::handleButtonPrintOnAction);

            tcDescription.setOnEditCommit((TableColumn.CellEditEvent<Match, String> t) -> {
                try {
                    ((Match) t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());

                    t.getTableView().edit(t.getTablePosition().getRow(), null);

                    manager.updateMatch(((Match) t.getTableView().getItems().get(t.getTablePosition().getRow())));
                   
                } catch (UpdateException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            
            tcWinnerTeam.setOnEditCommit((TableColumn.CellEditEvent<Match, Team> t) -> {
                try {
                    Match match =((Match) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                    match.setWinner(t.getNewValue());

                    //t.getTableView().edit(t.getTablePosition().getRow(), null);

                    manager.updateMatch(match);
                } catch (UpdateException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            tcPlayedDate.setOnEditCommit((TableColumn.CellEditEvent<Match, Date> t) -> {
                try {
                    ((Match) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPlayedDate(t.getNewValue());

                    t.getTableView().edit(t.getTablePosition().getRow(), null);

                    manager.updateMatch(((Match) t.getTableView().getItems().get(t.getTablePosition().getRow())));
                } catch (UpdateException ex) {
                    Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
                    
                }catch (Exception ex){
                    new Alert(Alert.AlertType.ERROR, "Bad format", ButtonType.OK).show();
                }
            });
            

            cbParameters.valueProperty().addListener(this::handleComboBoxParametersOnAction);

            stage.show();

        } catch (Exception ex) {
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleButtonAddMatchOnAction(ActionEvent event) {
        btnAddMatch.requestFocus();
        Match match = new Match(null, null, null, null, null, null, null);
        matches.add(match);
        try {
            manager.createMatch(match);
        } catch (CreateException ex) {
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tvMatches.refresh();

    }

    public void handleButtonDeleteMatchOnAction(ActionEvent event) {
        btnDeleteMatch.requestFocus();
        LOGGER.info("Deleting row to the table");
        try {
            Match toDelete = (Match) tvMatches.getSelectionModel().getSelectedItem();
            manager = MatchManagerFactory.getMatchManager();
            manager.deleteMatch(toDelete);
            matches.remove(toDelete);
            tvMatches.getSelectionModel().clearSelection();
            tvMatches.refresh();
        } catch (DeleteException ex) {
            Logger.getLogger(StatsWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleButtonFilterRemovalOnAction(ActionEvent event) {
        btnFilterRemoval.requestFocus();
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
                    if (!tfSearchBar.getText().equalsIgnoreCase("") || !tfSearchBar.getText().isEmpty()) {
                        manager = new MatchManagerImplementation();

                    }

                    break;

                case "League":
                    LOGGER.info("Searching league matches...");
                    manager = new MatchManagerImplementation();
                    tvMatches.setItems((ObservableList<Match>) FXCollections.observableArrayList(manager.findAllMatches()).stream().filter(match -> match.getTournament().getIdTournament().toString() == ""));
                    tvMatches.refresh();
                    break;

                case "Nickname":
                    LOGGER.info("Searching player matches");
                    if (!tfSearchBar.getText().equals("") || !tfSearchBar.getText().isEmpty()) {
                        manager = new MatchManagerImplementation();
                        tvMatches.setItems(FXCollections.observableArrayList(manager.findMatchesByUserNickname(tfSearchBar.getText())));
                        if (tvMatches.getItems().isEmpty()) {
                            new Alert(Alert.AlertType.ERROR, "No players found", ButtonType.OK).show();
                        }
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Please input a nickname", ButtonType.OK).show();
                    }
                    break;
                case "Description":
                    LOGGER.info("Searching tournament matches by description...");

                    break;

            }
        } catch (Exception ex) {

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

        }
    }

}
