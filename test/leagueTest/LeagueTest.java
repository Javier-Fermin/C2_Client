/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leagueTest;

import client.Client;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeoutException;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import model.League;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isDisabled;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TextInputControlMatchers.hasText;

/**
 *
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LeagueTest extends ApplicationTest {

    League league = new League(null,
            Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),
            Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),
            "Default name",
            "Default description");
    private TableView table = lookup("#tvLeagues").queryTableView();

    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Client.class);
    }

    @Test
    @Ignore
    public void test01_InitialState() {
        clickOn("#usernameText");
        write("root");//Puerta trasera user
        clickOn("#passwordText");
        write("abcd*1234");//Puerta trasera contraseña
        clickOn("#signInButton");
        clickOn("#windowOptionMenu");
        clickOn("#allLeaguesMenuItem");
        verifyThat("#leagueViewPane", isVisible());
        //Buttons verify
        verifyThat("#btnDelete", isDisabled());
        verifyThat("#btnMatches", isDisabled());
        verifyThat("#btnCreate", isEnabled());
        verifyThat("#btnMatches", isDisabled());
        verifyThat("#btnClean", isEnabled());
        //tfSearch verify
        verifyThat("#tfsearch", isVisible());
        verifyThat("#tfsearch", isDisabled());
        verifyThat("#tfsearch", hasText(""));
        //cbSeachType verify
        verifyThat("#cbSeachType", isVisible());
        verifyThat("#cbSeachType", hasText("ALL"));

    }

    /**
     *
     */
    @Test
    @Ignore
    public void test02_CreateLeagueTest() {
        //create default league

        //get row count before create
        Integer rowCount = table.getItems().size();
        //create
        verifyThat("#btnCreate", isEnabled());
        clickOn("#btnCreate");
        //verify the table has a new row
        assertEquals("Row not added", rowCount + 1, table.getItems().size());
        //search the new league in the table
        Boolean found = false;
        for (Object l : table.getItems()) {
            if (((League) l).compareTo(league) == 0) {
                found = true;
                break;
            }
        }
        //verify the new league is created
        assertTrue("league not found in the table", found);
        //delete the league created
        Node row = lookup(".table-row-cell").nth(rowCount).query();
        clickOn(row);
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    @Ignore
    public void test03_UpdateName() {
        //create the league to update
        clickOn("#btnCreate");
        //Select the object in the table
        Integer size = table.getItems().size();
        Node row = lookup("#tcName").nth(size).query();
        //get the selected item
        League selectedLeague = (League) table.getSelectionModel().getSelectedItem();
        doubleClickOn(row);
        //erase the name a update the new name
        eraseText(1);
        write("new Default name");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        //verify the name is visible
        verifyThat("new Default name", isVisible());
        //verify the update is done correctly 
        clickOn(row);
        assertNotEquals("Name update error", selectedLeague, (League) table.getSelectionModel().getSelectedItem());
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    @Ignore
    public void test04_UpdateDescription() {
        //create the league to update
        clickOn("#btnCreate");
        //Select the object in the table
        Integer size = table.getItems().size();
        Node row = lookup("#tcDescription").nth(size).query();
        //get the selected item
        League selectedLeague = (League) table.getSelectionModel().getSelectedItem();
        doubleClickOn(row);
        //erase the description a update the new description
        eraseText(1);
        write("new Default description");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        //verify the description is visible
        verifyThat("new Default description", isVisible());
        //verify the update is done correctly 
        clickOn(row);
        assertNotEquals("description update error", selectedLeague, (League) table.getSelectionModel().getSelectedItem());
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    @Ignore
    public void test05_UpdateStartDate() {
        //create the league to update
        clickOn("#btnCreate");
        //Select the object in the table
        Integer size = table.getItems().size();
        Node row = lookup("#tcStartDate").nth(size).query();
        doubleClickOn(row);
        doubleClickOn(row);
        doubleClickOn(row);
        //erase the startdate a update the new startdate
        eraseText(1);
        //write new date
        write("18/01/1970");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        //verify the new date is visible
        verifyThat("18/01/1970", isVisible());
        //verify the league is updated
        assertNotEquals("start date update error", league.getStartDate(), ((League) table.getSelectionModel().getSelectedItem()).getStartDate());
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    @Ignore
    public void test06_UpdateEndDate() {
        //create the league to update
        clickOn("#btnCreate");
        //Select the object in the table
        Integer size = table.getItems().size();
        Node row = lookup("#tcEndDate").nth(size).query();
        doubleClickOn(row);
        doubleClickOn(row);
        doubleClickOn(row);
        //erase the EndDte a update the new EndDate
        eraseText(1);
        //write new date
        write("18/01/2300");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("18/01/2300", isVisible());
        assertNotEquals("end date update error", league.getEndDate(), ((League) table.getSelectionModel().getSelectedItem()).getEndDate());
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    @Ignore
    public void test07_SearchAll() {
        //get items from the table
        ObservableList<League> leagueList = table.getItems();
        verifyThat("#cbSeachType", isEnabled());
        verifyThat("#btnSearch", isEnabled());
        //get number of rows of the table
        Integer rows = table.getItems().size();
        //search by ALL
        clickOn("#cbSeachType");
        clickOn("ALL");
        clickOn("#btnSearch");
        //verify the table items
        assertEquals("League searched correctly", rows.intValue(), table.getItems().size());
        assertEquals("League searched correctly", leagueList, table.getItems());
    }

    @Test
    @Ignore
    public void test08_SearchLeagueByName() {
        //create the league to find
        clickOn("#btnCreate");
        //get items from the table
        ObservableList<League> leagueList = table.getItems();
        verifyThat("#cbSeachType", isEnabled());
        verifyThat("#btnSearch", isEnabled());
        //search by NAME
        clickOn("#cbSeachType");
        clickOn("NAME");
        verifyThat("#tfsearch", isEnabled());
        clickOn("#tfsearch");
        write("Default name");
        clickOn("#btnSearch");
        //verify league searched correctly
        assertEquals("League search correctly error", 1, table.getItems().size());
        assertNotEquals("League search correctly error", leagueList, table.getItems());
        Node row = lookup("#tcEndDate").nth(table.getItems().size()).query();
        clickOn(row);
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    @Ignore
    public void test09_SearchLeagueByMatch() {
        //get items from the table
        ObservableList<League> leagueList = table.getItems();
        verifyThat("#cbSeachType", isEnabled());
        verifyThat("#btnSearch", isEnabled());
        clickOn("#btnClear");
        //get rows from the table
        Integer rows = table.getItems().size();
        //search by MATCH
        clickOn("#cbSeachType");
        clickOn("MATCH");
        verifyThat("#tfsearch", isEnabled());
        clickOn("#tfsearch");
        write("999");
        clickOn("#btnSearch");
        //verify the are no leagues in the table
        assertNotEquals("League search correct error", rows.intValue(), table.getItems().size());
        assertNotEquals("League search correct error", leagueList, table.getItems());
    }

    @Test
    @Ignore
    public void test10_SearchLeagueByStartDate() {
        //get items from the table
        ObservableList<League> leagueList = table.getItems();
        verifyThat("#cbSeachType", isEnabled());
        verifyThat("#btnSearch", isEnabled());
        clickOn("#btnClear");
        //get rows from the table
        Integer rows = table.getItems().size();
        //search by UNSTARTED
        clickOn("#cbSeachType");
        clickOn("UNSTARTED");
        clickOn("#btnSearch");
        //verify the are diferent leagues in the table
        assertNotEquals("League search correct error", rows.intValue(), table.getItems().size());
        assertNotEquals("League search correctl error", leagueList, table.getItems());

    }

    @Test
    @Ignore
    public void test11_SearchLeagueByEndDate() {
        //clear the table
        verifyThat("#cbSeachType", isEnabled());
        verifyThat("#btnSearch", isEnabled());
        clickOn("#btnClear");
        //get items and the siza from the table
        ObservableList<League> leagueList = table.getItems();
        Integer rows = table.getItems().size();
        //search finished leagues
        clickOn("#cbSeachType");
        clickOn("FINISHED");
        clickOn("#btnSearch");
        assertNotEquals("League searched correctly", rows.intValue(), table.getItems().size());
        assertNotEquals("League searched correctly", leagueList, table.getItems());
    }

    @Test
    @Ignore
    public void test12_DeleteLeague() {
        //verify button delete is disabled
        verifyThat("#btnDelete", isDisabled());
        //create a new league to delete
        clickOn("#btnCreate");
        //Find the league to delete
        Integer rowCount = table.getItems().size();
        Node row = lookup(".table-row-cell").nth(rowCount - 1).query();
        clickOn(row);
        League league = (League) table.getSelectionModel().getSelectedItem();
        //Delete the league
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        //verify the table has one row less
        assertEquals("League deleted correctly", rowCount - 1, table.getItems().size());
        //search the league in the table
        Boolean found = false;
        for (Object l : table.getItems()) {
            if (((League) l).compareTo(league) == 0) {
                found = true;
                break;
            }
        }
        //verify the league is delete
        assertFalse("League created", found);
    }

}
