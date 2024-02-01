/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tournament;


import client.Client;
import java.util.concurrent.TimeoutException;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import org.junit.BeforeClass;
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
public class TournamentWindowTest  extends ApplicationTest{
    
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
    
    //setUpClass method with a BeforeClass tag
    @Override
    public void stop(){}
    
    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Client.class);
    }
    
    //Test 0: Acceso a la ventana
    public void test00_openTournamentWindow(){
        clickOn("#usernameText");
        write("manolo@gmail.com");
        clickOn("#passwordText");
        write("abcd*1234");
        clickOn("#signInButton");
        //verifyThat pane de la ventana de stats es visible y luego clickar en el menu item de See all turnaments
        verifyThat("#tournamentPane", isVisible());
    }
    //Test 1: Comprobar la inicialización del Stage y sus componentes
    public void test01_initializeTournamentWindow(){
       verifyThat("#lTournaments", isVisible());
       verifyThat(tournamentBackground, (ImageView iv1) -> iv1.isVisible());
       verifyThat(tournamentSmoke, (ImageView iv2) -> iv2.isVisible());
       verifyThat(tournamentAgentImage, (ImageView iv3) -> iv3.isVisible());
       verifyThat("#chbTournamentSearch", isVisible());
       verifyThat("#chbTournamentSearch", (ChoiceBox c) -> c.getSelectionModel().getSelectedItem().toString().equals("ALL"));
       verifyThat("#tfTournamentSearch", isVisible());
       verifyThat("#tfTournamentSearch", hasText(""));
       verifyThat("#tfTournamentSearch", isDisabled());
//       verifyThat("#tvTournament", isVisible());
//       verify las tableColumns
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
    
    //Test 2: Comprobar que los botones se habilitan o no cuando deben
    public void test02_buttonsAreEnabled(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        verifyThat("#tfTournamentSearch", isEnabled());
        verifyThat("#bTournamentSearch", isDisabled());
        clickOn("#tfTournamentSearch");
        write("a");
        verifyThat("#bTournamentSearch", isEnabled());
        doubleClickOn("#tfTournamentSearch");
        eraseText(1);
        verifyThat("#bTournamentSearch", isDisabled());
        clickOn("#chbTournamentSearch");
        type(KeyCode.UP);
        type(KeyCode.ENTER);
        verifyThat("#tfTournamentSearch", isDisabled());
        verifyThat("#bTournamentSearch", isEnabled());
        clickOn("Uno");
        verifyThat("#bTournamentMatches", isEnabled());
        verifyThat("#bTournamentDelete", isEnabled());
        clickOn("#lTournaments");
        verifyThat("#bTournamentMatches", isDisabled());
        verifyThat("#bTournamentDelete", isDisabled());
    }
    
    //Test 3: Comprobar que el cleanButton funcione
    public void test03_checkCleanButton(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("1");
        clickOn("#bTournamentClean");
        verifyThat("#tfTournamentSearch", hasText(""));
        verifyThat("#chbTournamentSearch", (ChoiceBox ch03) -> ch03.getSelectionModel().isSelected(0));
    }
    //Test 4: Comprobar que se realiza la busqueda filtrada por id del Tournament
    public void test04_checkSearchId(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("1");
        clickOn("#bTournamentSearch");
    }
    //Test 5: Comprobar que se realiza la busqueda filtrada por nombre
    public void test05_checkSearchName(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("Dos");
        clickOn("#bTournamentSearch");
    }
    //Test 6: Comprobar que se realiza la busqueda filtrada por bestOf
    public void test06_checkSearchBestOf(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("3");
        clickOn("#bTournamentSearch");
    }
    //Test 7: Comprobar que se realiza la busqueda filtrada por date
    public void test07_checkSearchDate(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("28/01/2024");
        clickOn("#bTournamentSearch");
    }
    //Test 8: Comprobar que se realiza la busqueda filtrada por id de un match
    public void test08_checkSearchMatchId(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
        clickOn("#tfTournamentSearch");
        write("1");
        clickOn("#bTournamentSearch");
    }
    //Test 9: Comprobar que se realiza la busqueda de todos los torneos
    public void test09_checkSearchAll(){
        clickOn("#chbTournamentSearch");
        type(KeyCode.UP);
        type(KeyCode.UP);
        type(KeyCode.UP);
        type(KeyCode.UP);
        type(KeyCode.UP);
        type(KeyCode.ENTER);
        clickOn("#bTournamentSearch");
    }
    //Test 10: Comprobar que el createButton funcione
    public void test10_checkAdd(){
        clickOn("#chbTournamentAdd");
        
    }
    //Test 11: Comprobar que se puede editar una celda de la columna tcName
    
    //Test 12: Comprobar que se puede editar una celda de la columna tcDescription
    
    //Test 13: Comprobar que se puede editar una celda de la columna tcFormat
    
    //Test 14: Comprobar que se puede editar una celda de la columna tcDate
    
    //Test 15: Comprobar que el deleteButton funcione
    
    //Test 16: Comprobar que el printButton funcione
    
    //Test 17: Comprobar que el matchesButton funcione
    
    //Test 18: Comprobar que el helpButton funcione
    
    
}
