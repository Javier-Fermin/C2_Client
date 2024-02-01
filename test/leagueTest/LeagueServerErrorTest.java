/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leagueTest;

import client.Client;
import java.util.concurrent.TimeoutException;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import model.League;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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
public class LeagueServerErrorTest extends ApplicationTest {

    private TableView table = lookup("#tvLeagues").queryTableView();

    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Client.class);
    }

    @Test
    public void test02_CreateLeagueServerErrorTest() {
        verifyThat("#btnCreate", isEnabled());
        clickOn("#btnCreate");
        verifyThat("Create find server error", isVisible());
    }

    @Test
    public void test03_UpdateServerError() {
        Integer size = table.getItems().size();
        Node row = lookup("#tcName").nth(size).query();
        doubleClickOn(row);
        eraseText(1);
        write("new Default name");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("Server update error", isVisible());
    }
    
    @Test
    public void test07_SearchServerError() {
        verifyThat("#cbSeachType", isEnabled());
        verifyThat("#btnSearch", isEnabled());
        clickOn("#btnClear");
        verifyThat("Server search error", isVisible());
    }


    @Test
    public void test12_DeleteLeagueServerError() {
        verifyThat("#btnDelete", isDisabled());
        Integer rowCount = table.getItems().size();
        Node row = lookup(".table-row-cell").nth(rowCount - 1).query();
        clickOn(row);
        clickOn("#btnDelete");
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        verifyThat("Server delete error", isVisible());
    }

}
