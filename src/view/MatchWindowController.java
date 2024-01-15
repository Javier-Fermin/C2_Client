package view;

import businessLogic.MatchManager;
import businessLogic.MatchManagerFactory;
import businessLogic.MatchManagerImplementation;
import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;
import exceptions.ReadException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Match;

public class MatchWindowController {

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
    private TableView<String> tvMatches;

    @FXML
    private TableColumn<String, String> tvDescription;

    @FXML
    private TableColumn<String, String> tcPlayedDate;

    @FXML
    private TableColumn<String, String> tcWinnerTeam;

    @FXML
    private TableColumn<String, String> tcLeagueTournament;

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
        try {
            //Receives a User object from the SignInWindow window.
            LOGGER.info("Init Main Window");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            
            MatchManager manager = MatchManagerFactory.getMatchManager();
            
            ArrayList<Match> matches = (ArrayList<Match>) manager.findAllMatches();
            
            stage.show();
        } catch (ReadException ex) {
            Logger.getLogger(MatchWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }

    // Puedes agregar métodos adicionales según sea necesario.
}
