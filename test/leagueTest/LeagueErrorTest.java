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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
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
public class LeagueErrorTest extends ApplicationTest {

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
        write("");//Puerta trasera user
        clickOn("#passwordText");
        write("");//Puerta trasera contraseña
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

    @Test
    @Ignore
    public void test02_CreateLeagueTestError() {
        //Default league
        League league = new League(null,
                Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                "Default name",
                "Default description");
        //get size from the table
        Integer rowCount = table.getItems().size();
        //try to create 2 default leagues
        verifyThat("#btnCreate", isEnabled());
        clickOn("#btnCreate");
        verifyThat("Default name", isVisible());
        clickOn("#btnCreate");
        //verify alert shows
        verifyThat("The name already exist", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        //verify only one league is added
        assertEquals("League create errors correct", rowCount + 1, table.getItems().size());
        Boolean found = false;
        for (Object l : table.getItems()) {
            if (((League) l).compareTo(league) == 0) {
                found = true;
                break;
            }
        }
        assertTrue("league not found in the table", found);
        Node row = lookup("#tcName").nth(rowCount + 1).query();
        clickOn(row);
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    @Ignore
    public void test03_UpdateNameError() {
        clickOn("#btnCreate");
        Integer size = table.getItems().size();
        Node row = lookup("#tcName").nth(size).query();
        League selectedLeague = (League) table.getSelectionModel().getSelectedItem();
        doubleClickOn(row);
        eraseText(1);
        write("");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("all fields are required to fill", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        row = lookup("#tcName").nth(size).query();
        doubleClickOn(row);
        eraseText(1);
        write("789789");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("The name value is incorrect", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("Default name", isVisible());
        assertNotEquals("League name update errors correct", selectedLeague, (League) table.getSelectionModel().getSelectedItem());
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    @Ignore
    public void test04_UpdateDescriptionError() {
        clickOn("#btnCreate");
        Integer size = table.getItems().size();
        Node row = lookup("#tcDescription").nth(size).query();
        League selectedLeague = (League) table.getSelectionModel().getSelectedItem();
        doubleClickOn(row);
        eraseText(1);
        write("");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("all fields are required to fill", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        row = lookup("#tcDescription").nth(size).query();
        doubleClickOn(row);
        eraseText(1);
        write("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("The description value is incorrect", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("Default description", isVisible());
        assertNotEquals("League description update errors correct", selectedLeague, (League) table.getSelectionModel().getSelectedItem());
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    
    @Test
    @Ignore
    public void test05_UpdateStartDateError() {
        clickOn("#btnCreate");
        Integer size = table.getItems().size();
        Node row = lookup("#tcStartDate").nth(size).query();
        clickOn(row);
        League selectedLeague = (League) table.getSelectionModel().getSelectedItem();
        doubleClickOn(row);
        doubleClickOn(row);
        doubleClickOn(row);
        eraseText(1);
        write("");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("Start date update cancel", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        row = lookup("#tcStartDate").nth(size).query();
        doubleClickOn(row);
        doubleClickOn(row);
        doubleClickOn(row);
        eraseText(1);
        write("01/01/2300");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("The StartDate value is incorrect", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertEquals("Start date update error", selectedLeague, (League) table.getSelectionModel().getSelectedItem());
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    
    @Test
    @Ignore
    public void test06_UpdateEndDateError() {
        clickOn("#btnCreate");
        Integer size = table.getItems().size();
        Node row = lookup("#tcEndDate").nth(size).query();
        clickOn(row);
        League selectedLeague = (League) table.getSelectionModel().getSelectedItem();
        doubleClickOn(row);
        doubleClickOn(row);
        doubleClickOn(row);
        eraseText(1);
        write("");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("End date update cancel", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        row = lookup("#tcEndDate").nth(size).query();
        doubleClickOn(row);
        doubleClickOn(row);
        doubleClickOn(row);
        eraseText(1);
        write("01/01/1970");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("The EndDate value is incorrect", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertEquals("End ate update error", selectedLeague, (League) table.getSelectionModel().getSelectedItem());
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
    }

    @Test
    @Ignore
    public void test08_SearchLeagueByNameError() {
        //get items from the table
        ObservableList<League> leagueList = table.getItems();
        verifyThat("#cbSeachType", isEnabled());
        verifyThat("#btnSearch", isEnabled());
        //search by NAME
        clickOn("#cbSeachType");
        clickOn("NAME");
        verifyThat("#tfsearch", isEnabled());
        clickOn("#tfsearch");
        write("");
        clickOn("#btnSearch");
        verifyThat("this field is required to fill", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        clickOn("#tfsearch");
        write("789");
        clickOn("#btnSearch");
        verifyThat("Values can only be characters", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertEquals("League search correctly", leagueList, table.getItems());
    }

    @Test
    @Ignore
    public void test09_SearchLeagueByMatchError() {
        //get items from the table
        ObservableList<League> leagueList = table.getItems();
        verifyThat("#cbSeachType", isEnabled());
        verifyThat("#btnSearch", isEnabled());
        //search by NAME
        clickOn("#cbSeachType");
        clickOn("MATCH");
        verifyThat("#tfsearch", isEnabled());
        clickOn("#tfsearch");
        write("");
        clickOn("#btnSearch");
        verifyThat("this field is required to fill", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        clickOn("#tfsearch");
        write("abcd");
        clickOn("#btnSearch");
        verifyThat("Values can only be numbers", isVisible());
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        assertEquals("League search correctly", leagueList, table.getItems());
    }

}
