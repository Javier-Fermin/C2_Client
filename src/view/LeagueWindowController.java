/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import businessLogic.LeagueManage;
import businessLogic.LeagueManageFactory;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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

/**
 *
 * @author 2dam
 */
public class LeagueWindowController {

    protected static final Logger LOGGER = Logger.getLogger(LeagueWindowController.class.getName());

    ObservableList<League> leagueList;

    LeagueManage leagueManage = LeagueManageFactory.getRegistrable();

    @FXML
    private TableView tvLeagues;
    @FXML
    private TableColumn tcName, tcDescription, tcStartDate, tcEndDate;
    @FXML
    private Button btnMatches, btnDelete, btnCreate, btnSearch, btnPrint, btnInfo, tbInfoButton, tbCreateButton, tbDeleteButton, tbPrintButton, btnClear;
    @FXML
    private ComboBox cbSeachType;
    @FXML
    private TextField tfsearch;
    @FXML
    private ToolBar tbLeague;

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
            LOGGER.info("Init League Window");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Leagues Manage");
            btnDelete.setDisable(true);
            btnMatches.setDisable(true);
            if (user.getUserType().equals(UserType.PLAYER)) {
                btnCreate.setDisable(true);
            }

            if (!user.getUserType().equals(UserType.PLAYER)) {
                tvLeagues.setEditable(true);
            }
            tfsearch.setDisable(true);

            // create a menu 
            ContextMenu leagueContextMenu = new ContextMenu();

            // create menuitems 
            MenuItem miSearch = new MenuItem("Search");
            MenuItem miCreate = new MenuItem("Create");
            MenuItem miDelete = new MenuItem("Delete");

            // add menu items to menu 
            leagueContextMenu.getItems().add(miSearch);
            leagueContextMenu.getItems().add(miCreate);
            leagueContextMenu.getItems().add(miDelete);
            
            /////////MENU DE CONTEXTO PARA QUE APAREZCA CON EL CLICK DERECHO////////////////////////////////
            
            /**
             * When user click to exit form the window, ask confirmation to
             * close the app
             */
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit Storio?").showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        return;
                    }
                    event.consume();
                }
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

            cbSeachType.setItems(FXCollections.observableArrayList("ALL", "FINISHED", "UNSTARTED", "NAME", "MATCH"));

            leagueList = FXCollections.observableArrayList(leagueManage.findAllLeagues());

            tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tcName.setCellFactory(TextFieldTableCell.<League>forTableColumn());
            tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            tcDescription.setCellFactory(TextFieldTableCell.<League>forTableColumn());

            final Callback<TableColumn<League, Date>, TableCell<League, Date>> dateCellFactory
                    = (TableColumn<League, Date> param) -> new DateLeagueCellPicker();

            tcStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
            tcStartDate.setCellFactory(dateCellFactory);
            tcEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            tcEndDate.setCellFactory(dateCellFactory);

            tvLeagues.setItems(leagueList);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(LeagueWindowController.class.getName()).log(Level.SEVERE, null, "Inicialize error" + e.getMessage());
        }
    }

    @FXML
    private void createLeagueButtonAction(ActionEvent event) {

    }

}
