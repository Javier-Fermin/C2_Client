/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tournament;

import client.Client;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import model.Tournament;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextFlowMatchers.hasText;

/**
 * Test class that test the correct functionality of the Tournament window
 * @author Fran
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TournamentWindowTest extends ApplicationTest {

    //Images in the window
    private final ImageView tournamentBackground = new ImageView("resources/images/tournamentsBackground.jpg");
    private final ImageView tournamentSmoke = new ImageView("resources/images/smokeBackground.png");
    private final ImageView bSearchImage = new ImageView("resources/images/search.png");
    private final ImageView bCleanImage = new ImageView("resources/images/clear.png");
    private final ImageView bHelpImage = new ImageView("resources/images/info.png");
    private final ImageView bPrintImage = new ImageView("resources/images/print.png");
    private final ImageView bDeleteImage = new ImageView("resources/images/delete.png");
    private final ImageView bAddImage = new ImageView("resources/images/add.png");
    private final ImageView tournamentAgentImage = new ImageView("resources/images/smokeAgent.png");

    private TableView tournamentTable = lookup("#tvTournaments").queryTableView();

    //setUpClass method with a BeforeClass tag
    @Override
    public void stop() {
    }

    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Client.class);
    }

    //Test 0: Acceso a la ventana
//    @Ignore
    @Test
    public void test00_openTournamentWindow() {
        clickOn("#usernameText");
        write("root");
        clickOn("#passwordText");
        write("abcd*1234");
        clickOn("#signInButton");
        clickOn("#tournamentOptionMenu");
        clickOn("#allTournamentsMenuItem");
        verifyThat("#tournamentPane", isVisible());
    }

    //Test 1: Comprobar la inicialización del Stage y sus componentes
//    @Ignore
    @Test
    public void test01_initializeTournamentWindow() {
        verifyThat("#chbTournamentSearch", isVisible());
        verifyThat("#chbTournamentSearch", (ChoiceBox c) -> c.getSelectionModel().getSelectedItem().toString().equals("ALL"));
        verifyThat("#tfTournamentSearch", isVisible());
        verifyThat("#tfTournamentSearch", isDisabled());
        verifyThat("#bTournamentSearch", isVisible());
        verifyThat("#bTournamentSearch", isEnabled());
        verifyThat(bSearchImage, (ImageView iv4) -> iv4.isVisible());
        verifyThat("#bTournamentMatches", isVisible());
        verifyThat("#bTournamentMatches", isDisabled());
        verifyThat("#bTournamentClean", isVisible());
        verifyThat("#bTournamentClean", isEnabled());
        verifyThat(bCleanImage, (ImageView iv5) -> iv5.isVisible());
        verifyThat("#bTournamentHelp", isVisible());
        verifyThat("#bTournamentHelp", isEnabled());
        verifyThat(bHelpImage, (ImageView iv6) -> iv6.isVisible());
        verifyThat("#bTournamentPrint", isVisible());
        verifyThat("#bTournamentPrint", isEnabled());
        verifyThat(bPrintImage, (ImageView iv7) -> iv7.isVisible());
        verifyThat("#bTournamentDelete", isVisible());
        verifyThat("#bTournamentDelete", isDisabled());
        verifyThat(bDeleteImage, (ImageView iv8) -> iv8.isVisible());
        verifyThat("#bTournamentAdd", isVisible());
        verifyThat("#bTournamentAdd", isEnabled());
        verifyThat(bAddImage, (ImageView iv9) -> iv9.isVisible());
    }

    //Test 2: Comprobar que los botones se habilitan o no cuando deben
    @Ignore
    @Test
    public void test02_buttonsAreEnabled() {
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        verifyThat("#tfTournamentSearch", isEnabled());
        clickOn("#chbTournamentSearch");
        type(KeyCode.UP);
        type(KeyCode.ENTER);
        verifyThat("#tfTournamentSearch", isDisabled());
        clickOn("Uno");
        verifyThat("#bTournamentMatches", isEnabled());
        verifyThat("#bTournamentDelete", isEnabled());
        clickOn("#chbTournamentSearch");
        type(KeyCode.ENTER);
        verifyThat("#bTournamentMatches", isDisabled());
        verifyThat("#bTournamentDelete", isDisabled());
    }

    //Test 3: Comprobar que el cleanButton funcione
    @Ignore
    @Test
    public void test03_checkCleanButton() {
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        verifyThat("#tfTournamentSearch", isEnabled());
        clickOn("#tfTournamentSearch");
        write("1");
        clickOn("#bTournamentClean");
        verifyThat("#tfTournamentSearch", isDisabled());
        verifyThat("#chbTournamentSearch", (ChoiceBox ch03) -> ch03.getSelectionModel().isSelected(0));
    }

    //Test 4: Comprobar que se realiza la busqueda filtrada por id del Tournament
    @Ignore
    @Test
    public void test04_checkSearchId() {
        ObservableList<Tournament> tournaments = tournamentTable.getItems();
        Integer numRows = tournaments.size();
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("1");
        clickOn("#bTournamentSearch");
        
        assertNotEquals("The table didn't update.", tournaments, tournamentTable.getItems());
        assertNotEquals("The table didn't change.",numRows.intValue(), tournamentTable.getItems().size());
    }

    //Test 5: Comprobar que se realiza la busqueda filtrada por nombre
    @Ignore
    @Test
    public void test05_checkSearchName() {
        ObservableList<Tournament> tournaments = tournamentTable.getItems();
        
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("Dos");
        clickOn("#bTournamentSearch");
        
        assertNotEquals("The table didn't update.", tournaments, tournamentTable.getItems());
    }

    //Test 6: Comprobar que se realiza la busqueda filtrada por date
    @Ignore
    @Test
    public void test06_checkSearchDate() {
        ObservableList<Tournament> tournaments = tournamentTable.getItems();
        
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("28/01/2024");
        clickOn("#bTournamentSearch");
        
        assertNotEquals("The table didn't update.", tournaments, tournamentTable.getItems());
    }

    //Test 7: Comprobar que se realiza la busqueda filtrada por bestOf
    @Ignore
    @Test
    public void test07_checkSearchBestOf() {
        ObservableList<Tournament> tournaments = tournamentTable.getItems();
        
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("3");
        clickOn("#bTournamentSearch");
        
        assertNotEquals("The table didn't update.", tournaments, tournamentTable.getItems());
    }

    //Test 8: Comprobar que se realiza la busqueda filtrada por id de un match
    @Ignore
    @Test
    public void test08_checkSearchMatchId() {
        ObservableList<Tournament> tournaments = tournamentTable.getItems();
        
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("3");
        clickOn("#bTournamentSearch");
        
        assertNotEquals("The table didn't update.", tournaments, tournamentTable.getItems());
    }

    //Test 9: Comprobar que se realiza la busqueda de todos los torneos
    @Ignore
    @Test
    public void test09_checkSearchAll() {
        ObservableList<Tournament> tournaments = tournamentTable.getItems();
        
        clickOn("#chbTournamentSearch");
        //type(KeyCode.UP);
        type(KeyCode.UP);
        type(KeyCode.UP);
        type(KeyCode.UP);
        type(KeyCode.UP);
        type(KeyCode.ENTER);
        clickOn("#bTournamentSearch");
        
        assertNotEquals("The table didn't update.", tournaments, tournamentTable.getItems());
    }

    //Test 10: Comprobar que el createButton funcione
    @Ignore
    @Test
    public void test10_checkAdd() {
        //Get tvTournament row count before adding the new Tournament
        Integer rowCount = tournamentTable.getItems().size();
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Tournament newTournament = new Tournament(null, "T name", "T description", "1", today, null);

        //Click on bTournament to create a new Tournament and add it inside tvTournament
        verifyThat("bTournamentAdd", isEnabled());
        clickOn("#bTournamentAdd");

        //Check if there is one more row inside the table
        assertEquals("The row hasn't been added.", rowCount + 1, tournamentTable.getItems().size());
        
        //Check if the tournament was added
        assertEquals("Tournament add error", newTournament, tournamentTable.getItems().get(rowCount-1));
    }

    //Test 11: Comprobar que se puede editar una celda de la columna tcName
//    @Ignore
    @Test
    public void test11_updateName() {
        //Get the table size
        Integer rowCount = tournamentTable.getItems().size();

        //Get the last added Tournament tcName cell
        Node newRow = lookup("#tcName").nth(rowCount).query();

        //Select the cell
        clickOn(newRow);

        //Get the Tournament object to compare it after the name update
        Tournament selectedTournament = (Tournament) tournamentTable.getSelectionModel().getSelectedItem();
        String oldName = selectedTournament.getName();

        //Get tcName cell in edit mode and modify its value(by default is 0)
        doubleClickOn(newRow);
        eraseText(1);
        write("TestName");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        //Check that the new value is stored in the table
        verifyThat("TestName", isVisible());

        //Check the update was done correctly
        rowCount = tournamentTable.getItems().size()-1;
        newRow = lookup("#tcName").nth(rowCount).query();
        clickOn(newRow);
        Tournament updatedTournament = (Tournament) tournamentTable.getSelectionModel().getSelectedItem();
        assertNotEquals("Name update error", oldName, updatedTournament.getName());
        
        oldName=selectedTournament.getName();
        assertEquals("Name update error", selectedTournament, updatedTournament);
    }

    //Test 12: Comprobar que se puede editar una celda de la columna tcDescription
    @Ignore
    @Test
    public void test12_updateDescription() {
        //Get the table size
        Integer rowCount = tournamentTable.getItems().size()-1;

        //Get the last added Tournament tcDescription cell
        Node newRow = lookup("#tcDescription").nth(rowCount).query();

        //Select the cell
        clickOn(newRow);

        //Get the Tournament object to compare it after the description update
        Tournament selectedTournament = (Tournament) tournamentTable.getSelectionModel().getSelectedItem();

        //Get tcDescription cell in edit mode and modify its value(by default is 0)
        doubleClickOn(newRow);
        eraseText(1);
        write("Description Test");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        //Check that the new value is stored in the table
        verifyThat("Description Test", isVisible());

        //Check the update was done correctly
        clickOn(newRow);
        assertNotEquals("Description update error", selectedTournament, tournamentTable.getSelectionModel().getSelectedItem());
    }

    //Test 13: Comprobar que se puede editar una celda de la columna tcBestOf
    @Ignore
    @Test
    public void test13_updateBestOf() {
        //Get the table size
        Integer rowCount = tournamentTable.getItems().size()-2;

        //Get the last added Tournament tcBestOF cell
        Node newRow = lookup("#tcBestOf").nth(rowCount).query();

        //Select the cell
        clickOn(newRow);

        //Get the Tournament object to compare it after the bestOf update
        Tournament selectedTournament = (Tournament) tournamentTable.getSelectionModel().getSelectedItem();

        //Get tcBestOf cell in edit mode and modify its value(by default is 0)
        doubleClickOn(newRow);
        eraseText(1);
        write("9");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        //Check that the new value is stored in the table
        verifyThat("9", isVisible());

        //Check the update was done correctly
        clickOn(newRow);
        assertNotEquals("BestOf update error", selectedTournament, tournamentTable.getSelectionModel().getSelectedItem());
    }

    //Test 14: Comprobar que se puede editar una celda de la columna tcDate
    @Ignore
    @Test
    public void test14_updateDate() {
        //Get the table size
        Integer rowCount = tournamentTable.getItems().size()-3;

        //Get the last added Tournament tcDate cell
        Node newRow = lookup("#tcDate").nth(rowCount).query();

        //Select the cell
        clickOn(newRow);

        //Get the Tournament object to compare it after the date update
        Tournament selectedTournament = (Tournament) tournamentTable.getSelectionModel().getSelectedItem();

        //Get tcDate cell in edit mode and modify its value
        doubleClickOn(newRow);
        eraseText(1);
        write("31/10/2023");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        //Check that the new value is stored in the table
        verifyThat("31/10/2023", isVisible());

        //Check the update was done correctly
        clickOn(newRow);
        assertNotEquals("BestOf update error", selectedTournament, tournamentTable.getSelectionModel().getSelectedItem());
    }

    //Test 15: Comprobar que el deleteButton funcione
    @Ignore
    @Test
    public void test15_deleteTournament() {
        //Check that the bTournamentDelete button is disabled
        verifyThat("#bTournamentDelete", isDisabled());

        //Get the table size
        Integer rowCount = tournamentTable.getItems().size();

        //Get the last added Tournament tcName cell
        Node newRow = lookup("#tcName").nth(rowCount).query();

        //Select the cell
        clickOn(newRow);

        //Check that the bTournamentDelete button is now enabled
        verifyThat("#bTournamentDelete", isEnabled());

        //Get the Tournament object to compare it after the name update
        Tournament selectedTournament = (Tournament) tournamentTable.getSelectionModel().getSelectedItem();

        //Click the delete button
        clickOn("#bTournamentDelete");

        //Check if the confirmation pane is visible
        verifyThat("Are you sure you want to delete this Tournament?", isVisible());

        //Finish the delete
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        //Check if the row was deleted
        assertEquals("Tournament not deleted", rowCount - 1, tournamentTable.getItems().size());
    }

    //Test 16: Comprobar que el helpButton funcione
    @Ignore
    @Test
    public void test16_checkHelpButton() {
        //Check that the help button is enabled
        verifyThat("#bTournamentHelp", isVisible());

        //Click the button and check if the help window is visible
        clickOn("#bTournamentHelp");
        verifyThat("webView", isVisible());

        //Close the help window and go back to the Tournament window
        press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);
        verifyThat("Are you sure you want to exit?", isVisible());
        clickOn("Aceptar");
        verifyThat("#tournamentPane", isVisible());
    }

    //Test 17: Comprobar que el matchesButton funcione
    @Ignore
    @Test
    public void test17_checkMatchesButton() {
        //Check the matches button is disabled
        verifyThat("#bTournamentMatches", isDisabled());

        //Select the first row and check if the matches button is now enabled
        Node firstRow = lookup("#tcDate").nth(0).query();
        clickOn(firstRow);
        verifyThat("#bTournamentMatches", isVisible());

        //Click the button and check if the matches window is visible
        clickOn("#bTournamentMatches");
        verifyThat("#rootPane", isVisible());

        //Get back to the tournament window
        clickOn("#windowOptionMenu");
        clickOn("#allTournamentsMenuItem");
        verifyThat("#tournamentPane", isVisible());
    }
    
    //Test 18: Comprobar que se muestra un Alert de confirmacion al intentar cerrar la ventana
    @Ignore
    @Test
    public void test18_closeWindow(){
        press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);
        verifyThat("Are you sure you want to exit?", isVisible());
        clickOn("Aceptar");
    }
}
