/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

import client.Client;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import model.Stats;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import static org.testfx.matcher.control.TableViewMatchers.hasNumRows;

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
    
    @Test
    public void test00_alreadyExistingStatException(){
        //Verify that the stat without id is created
        Integer startSize = statsTableView.getItems().size();
        clickOn("#addBtn");
        
        //Verify that the stat is created in the database
        Node tableColumn = lookup("#tcPlayer").nth(startSize+1).query();
        clickOn(tableColumn).doubleClickOn(tableColumn).write("Player1");
        push(KeyCode.ENTER);
        
        tableColumn = lookup("#tcDescription").nth(startSize+1).query();
        doubleClickOn(tableColumn).write("Cuatro");
        push(KeyCode.ENTER);
        
        clickOn("#addBtn");
        //Verify that the stat is created in the database
        tableColumn = lookup("#tcPlayer").nth(startSize+2).query();
        clickOn(tableColumn).doubleClickOn(tableColumn).write("Player1");
        push(KeyCode.ENTER);
        
        tableColumn = lookup("#tcDescription").nth(startSize+2).query();
        doubleClickOn(tableColumn).write("Cuatro");
        push(KeyCode.ENTER);
        
        verifyThat("The stat you are trying to create already exists", isVisible());
    }
}
