/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

import client.Client;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import javafx.scene.control.TableView;
import model.Stats;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

/**
 *
 * @author javie
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatsExceptionsTest extends ApplicationTest{

    private static final Logger LOGGER = Logger.getLogger(StatsTest.class.getName());
    private static TableView<Stats> statsTableView;
    
    @BeforeClass
    public static void setUpClass() throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(Client.class);
    }    

    @Override
    public void stop() {
    }
    
    @Test
    public void test(){
        clickOn("#usernameText");
        write("root");
        clickOn("#passwordText");
        write("abcd*1234");
        clickOn("#signInButton");
        statsTableView = lookup("#statsTableView").queryTableView();
        verifyThat("#statsViewMainPane",isVisible());
    }
}
