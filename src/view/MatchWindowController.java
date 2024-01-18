package view;

import businessLogic.MatchManager;
import businessLogic.MatchManagerFactory;
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
import model.Match;
import model.Team;

public class MatchWindowController {

    protected static final Logger LOGGER = Logger.getLogger(MatchWindowController.class.getName());

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

    @FXML
    private void initialize() {
        // Puedes agregar código de inicialización aquí si es necesario.
    }

    public void setMainStage(Stage stage) {
        this.stage = stage;
    }

    public void initStage(Parent root) {
        MatchManager manager;
        ObservableList<Match> matches;
        try {
            //Receives a User object from the SignInWindow window.
            //LOGGER.info("Init Main Window");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Matches");
            tvMatches.setEditable(true);
            tcLeagueTournament.setEditable(false);
            
            ObservableList<String> parameters = FXCollections.observableArrayList("All", "Tournament"
            , "League","Description", "Nickname");
            cbParameters.setItems(parameters);
            cbParameters.getSelectionModel().selectFirst();
            manager = MatchManagerFactory.getMatchManager();

            matches = FXCollections.observableArrayList(manager.findAllMatches());

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

            stage.show();

        } catch (Exception ex) {
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void handleButtonAddMatchOnAction(ActionEvent event) {
        btnAddMatch.requestFocus();
        LOGGER.info("Adding new row to the table");

    }

    public void handleButtonDeleteMatchOnAction(ActionEvent event) {
        btnDeleteMatch.requestFocus();
        LOGGER.info("Deleting row to the table");
    }

    public void handleButtonFilterRemovalOnAction(ActionEvent event) {
        btnFilterRemoval.requestFocus();
        LOGGER.info("Removing filters");
    }

    public void handleButtonSearchOnAction(ActionEvent event) {
        btnSearch.requestFocus();
        LOGGER.info("Searching...");
    }

    public void handleButtonPrintOnAction(ActionEvent event) {
        btnPrint.requestFocus();
        LOGGER.info("Printing document");
    }

}
