/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tournament;

import client.Client;
import java.util.concurrent.TimeoutException;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import model.Tournament;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextFlowMatchers.hasText;

/**
 *
 * @author Fran
 */
public class TournamentErrorTest extends ApplicationTest {
    //Images in the window
    private final ImageView tournamentBackground = new ImageView("resources/images/tournamentsBackground.jpg");
    private final ImageView tournamentSmoke = new ImageView("resources/images/smokeBackground.png");
    private final ImageView bSearchImage = new ImageView("resources/images/search.png");
    private final ImageView bCleanImage = new ImageView("resources/images/clear.png");
    private final ImageView bHelpImage = new ImageView("resources/images/help.png");
    private final ImageView bPrintImage = new ImageView("resources/images/print.png");
    private final ImageView bDeleteImage = new ImageView("resources/images/delete.png");
    private final ImageView bAddImage = new ImageView("resources/images/add.png");
    private final ImageView tournamentAgentImage = new ImageView("resources/images/smokeAgent.png");

    private final String tooMuchText = "This String is over 120 characters, exactly"
                                    + " the maximum amount of characters that the "
                                    + "description's length is limited to.";
    
    private final TableView tournamentTable = lookup("#tvTournamnet").queryTableView();

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
    @Ignore
    @Test
    public void test00_openTournamentWindow() {
        clickOn("#usernameText");
        write("manolo@gmail.com");
        clickOn("#passwordText");
        write("abcd*1234");
        clickOn("#signInButton");
        clickOn("#windowOptionMenu");
        clickOn("#allTournamentsMenuItem");
        verifyThat("#tournamentPane", isVisible());
    }
    
    //Test 1: Comprobar la inicialización del Stage y sus componentes
    @Ignore
    @Test
    public void test01_initializeTournamentWindow() {
        verifyThat("#lTournaments", isVisible());
        verifyThat(tournamentBackground, (ImageView iv1) -> iv1.isVisible());
        verifyThat(tournamentSmoke, (ImageView iv2) -> iv2.isVisible());
        verifyThat(tournamentAgentImage, (ImageView iv3) -> iv3.isVisible());
        verifyThat("#chbTournamentSearch", isVisible());
        verifyThat("#chbTournamentSearch", (ChoiceBox c) -> c.getSelectionModel().getSelectedItem().toString().equals("ALL"));
        verifyThat("#tfTournamentSearch", isVisible());
        verifyThat("#tfTournamentSearch", hasText(""));
        verifyThat("#tfTournamentSearch", isDisabled());
        verifyThat(tournamentTable, isVisible());
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
        verifyThat("#bTournamentAdd", isDisabled());
        verifyThat(bAddImage, (ImageView iv9) -> iv9.isVisible());
    }
    
    //Test 2 Comprobar que salta un alert al buscar un campo vacio 
    @Ignore
    @Test
    public void test02_searchEmptyTextField_Error(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#bTournamentSearch");
        verifyThat("This field is required to fill", isVisible());
        type(KeyCode.ENTER);
        clickOn("#chbTournamentSearch");
        type(KeyCode.UP);
        type(KeyCode.ENTER);
    }
    
    //Test 3: Error en la busqueda por id de tournament
    @Ignore
    @Test
    public void test03_searchById_Error(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        verifyThat("#tfTournamentSearch", isEnabled());
        clickOn("#tfTournamentSearch");
        write("not a numeric value");
        clickOn("#bTournamentSearch");
        verifyThat("Value can only be numbers", isVisible());
        type(KeyCode.ENTER);
    }
    
    //Test 4: Error en la busqueda por name
    @Ignore
    @Test
    public void test04_searchByName_Error(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("45");
        clickOn("#bTournamentSearch");
        verifyThat("Value can only be characters", isVisible());
        type(KeyCode.ENTER);
    }
    
    //Test 5: Error en la busqueda por bestOf
    @Ignore
    @Test
    public void test05_searchByBestOf_Error(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        verifyThat("#tfTournamentSearch", isEnabled());
        clickOn("#tfTournamentSearch");
        write("not a numeric value");
        clickOn("#bTournamentSearch");
        verifyThat("Value can only be numbers", isVisible());
        type(KeyCode.ENTER);
    }
    
    //Test 6: Error en la busqueda por date
    @Ignore
    @Test
    public void test06_searchByDate_Error(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        verifyThat("#tfTournamentSearch", isEnabled());
        clickOn("#tfTournamentSearch");
        write("not a good date format");
        clickOn("#bTournamentSearch");
        verifyThat("Incorrect date format", isVisible());
        type(KeyCode.ENTER);
    }
    
    //Test 7: Error en la busqueda por id de match
    @Ignore
    @Test
    public void test07_searchByMatchId_Error(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        verifyThat("#tfTournamentSearch", isEnabled());
        clickOn("#tfTournamentSearch");
        write("not a numeric value");
        clickOn("#bTournamentSearch");
        verifyThat("Value can only be numbers", isVisible());
        type(KeyCode.ENTER);
        clickOn("#bTournamentClean");
    }
    
    //Test 8: Error en el update de name
    @Ignore
    @Test
    public void test08_nameUpdate_Error(){
        clickOn("#bTournamentAdd");
        
        //Get the table size
        Integer rowCount = tournamentTable.getItems().size();

        //Get the last added Tournament tcName cell
        Node newRow = lookup("#tcName").nth(rowCount).query();

        //Select the cell
        clickOn(newRow);
        
        //Get the Tournament object to compare it after the name update
        Tournament selectedTournament = (Tournament) tournamentTable.getSelectionModel().getSelectedItem();

        //Get tcName cell in edit mode and modify its value(by default is 0)
        doubleClickOn(newRow);
        eraseText(1);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        //Check if the alert is shown
        verifyThat("This field can not be empty", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        clickOn(newRow);
        assertEquals("Name update error bad catched", selectedTournament, tournamentTable.getSelectionModel().getSelectedItem());
        
        doubleClickOn(newRow);
        eraseText(1);
        write("7");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        //Check if the alert is shown
        verifyThat("Value can only be characters", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        clickOn(newRow);
        assertEquals("Name update error bad catched", selectedTournament, tournamentTable.getSelectionModel().getSelectedItem());
    }
    
    //Test 9: Error en el update de description
    @Ignore
    @Test
    public void test09_descriptionUpdate_Error(){
        //Get the table size
        Integer rowCount = tournamentTable.getItems().size();

        //Get the last added Tournament tcDescription cell
        Node newRow = lookup("#tcDescription").nth(rowCount).query();

        //Select the cell
        clickOn(newRow);

        //Get the Tournament object to compare it after the description update
        Tournament selectedTournament = (Tournament) tournamentTable.getSelectionModel().getSelectedItem();

        //Set tcDescription value to null
        doubleClickOn(newRow);
        eraseText(1);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        //Check if the alert is shown
        verifyThat("Description must be informed to be updated.", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        //Check the update wasn't done
        clickOn(newRow);
        assertEquals("Description update error bad catched", selectedTournament, tournamentTable.getSelectionModel().getSelectedItem());
        
        //Set tcDescription to a value that surpass the description max length
        doubleClickOn(newRow);
        eraseText(1);
        write(tooMuchText);
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        //Check if the alert is shown
        verifyThat("Description length surpased, please insert a description of 281 characters or less.", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        //Check the update wasn't done
        clickOn(newRow);
        assertEquals("Description update error bad catched", selectedTournament, tournamentTable.getSelectionModel().getSelectedItem());
    }
    
    //Test 10: Error en el update de bestOf
    @Ignore
    @Test
    public void test10_bestOfUpdate_Error(){
        //Get the table size
        Integer rowCount = tournamentTable.getItems().size();

        //Get the last added Tournament tcBestOF cell
        Node newRow = lookup("#tcBestOf").nth(rowCount).query();
        
        //Select the cell
        clickOn(newRow);

        //Get the Tournament object to compare it after the description update
        Tournament selectedTournament = (Tournament) tournamentTable.getSelectionModel().getSelectedItem();
        
        //Set tcBestOf value to null
        doubleClickOn(newRow);
        eraseText(1);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        //Check if the alert is shown
        verifyThat("This field can not be empty.", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        //Check the update wasn't done
        clickOn(newRow);
        assertEquals("BestOf update error bad catched", selectedTournament, tournamentTable.getSelectionModel().getSelectedItem());
        
        //Set tcBestOf to a non-numeric value
        doubleClickOn(newRow);
        eraseText(1);
        write("Not a number");
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        //Check if the alert is shown
        verifyThat("Value can only be numbers.", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        //Check the update wasn't done
        clickOn(newRow);
        assertEquals("BestOf update error bad catched", selectedTournament, tournamentTable.getSelectionModel().getSelectedItem());
    }
    
    //Test 11: Error en el update de date
    @Ignore
    @Test
    public void test11_dateUpdate_Error(){
        //Get the table size
        Integer rowCount = tournamentTable.getItems().size();

        //Get the last added Tournament tcDate cell
        Node newRow = lookup("#tcDate").nth(rowCount).query();

        //Select the cell
        clickOn(newRow);

        //Get the Tournament object to compare it after the date update
        Tournament selectedTournament = (Tournament) tournamentTable.getSelectionModel().getSelectedItem();
        
        //Set tcDate value to null
        doubleClickOn(newRow);
        eraseText(1);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        //Check if the alert is shown
        verifyThat("Please check a date please.", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        //Check the update wasn't done
        clickOn(newRow);
        assertEquals("Date update error bad catched.", selectedTournament, tournamentTable.getSelectionModel().getSelectedItem());
    }
    
    //Test 12: Error en el create de Tournament
    @Ignore
    @Test
    public void test12_tournamentAdd_Error(){
        //Get tvTournament row count before adding the new Tournament
        Integer rowCount = tournamentTable.getItems().size();
        
        //Click on bTournament to create a new Tournament and add it inside tvTournament
        verifyThat("bTournamentAdd", isEnabled());
        clickOn("#bTournamentAdd");
        
        //Click on bTournament to create a the same Tournament as before
        verifyThat("bTournamentAdd", isEnabled());
        clickOn("#bTournamentAdd");
        
        //Check if the alert is shown
        verifyThat("The tournament already exists.", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);

        //Check if there is one more row inside the table
        assertNotEquals("Add tournament error bad catches.", rowCount+1, tournamentTable.getItems().size());
    }
    
    //Test 13: Comprobar que se muestra un Alert de confirmacion al intentar cerrar la ventana
    @Ignore
    @Test
    public void test13_closeWindow(){
        press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);
        verifyThat("Are you sure you want to exit?", isVisible());
        clickOn("Aceptar");
    }
}
