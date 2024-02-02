/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stat;

import businessLogic.StatsManager;
import businessLogic.StatsManagerFactory;
import businessLogic.StatsManagerImplementation;
import client.Client;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Stats;
import model.User;
import model.UserType;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import org.testfx.matcher.control.TableViewMatchers;
import static org.testfx.matcher.control.TableViewMatchers.hasNumRows;
import view.StatsWindowController;

/**
 *
 * @author javie
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatsTest extends ApplicationTest{

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
    public void test00_createStat(){
        //Verify that the stat without id is created
        Integer startSize = statsTableView.getItems().size();
        clickOn("#addBtn");
        verifyThat(statsTableView, hasNumRows(startSize+1));
        assertEquals("Default stat was not added.",
                1,
                statsTableView.getItems()
                        .stream().filter(s -> s.getId()==null).count());
        //Verify that the stat is created in the database
        Node tableColumn = lookup("#tcPlayer").nth(startSize+1).query();
        clickOn(tableColumn).doubleClickOn(tableColumn).write("Player1");
        push(KeyCode.ENTER);
        
        tableColumn = lookup("#tcDescription").nth(startSize+1).query();
        doubleClickOn(tableColumn).write("Cuatro");
        push(KeyCode.ENTER);
        
        assertEquals("Stats was not created.",
                1,
                statsTableView.getItems()
                        .stream().filter(s -> s.getMatch().getDescription().equals("Cuatro")
                                &&s.getPlayer().getNickname().equals("Player1")).count());
    }
    
    @Test
    public void test01_updateStat(){
        Integer startSize = statsTableView.getItems().size();
        Node tableColumn = lookup("#tcPlayer").nth(startSize).query();
        clickOn(tableColumn).doubleClickOn(tableColumn).write("Player2");
        sleep(300);
        push(KeyCode.ENTER);
        
        tableColumn = lookup("#tcDescription").nth(startSize).query();
        doubleClickOn(tableColumn).write("Cinco");
        sleep(300);
        push(KeyCode.ENTER);
        
        tableColumn = lookup("#tcDate").nth(startSize).query();
        clickOn(tableColumn).doubleClickOn(tableColumn).write("02/02/2024");
        sleep(300);
        push(KeyCode.ENTER);
        
        Integer kills = Math.abs(new Random().nextInt(99));
        tableColumn = lookup("#tcKills").nth(startSize).query();
        clickOn(tableColumn).doubleClickOn(tableColumn).write(kills.toString());
        sleep(300);
        push(KeyCode.ENTER);
        
        Integer deaths = Math.abs(new Random().nextInt(99));
        tableColumn = lookup("#tcDeaths").nth(startSize).query();
        doubleClickOn(tableColumn).write(deaths.toString());
        sleep(300);
        push(KeyCode.ENTER);
        
        Integer assists = Math.abs(new Random().nextInt(99));
        tableColumn = lookup("#tcAssists").nth(startSize).query();
        doubleClickOn(tableColumn).write(assists.toString());
        sleep(300);
        push(KeyCode.ENTER);
        
        tableColumn = lookup("#tcTeam").nth(startSize).query();
        sleep(300);
        doubleClickOn(tableColumn).clickOn("ORANGE_TEAM");
        
        assertTrue("Stats was not updated.",
                startSize.equals(statsTableView.getItems().size()));
        assertEquals("Stats was not updated.",
                1,
                statsTableView.getItems()
                        .stream().filter(s -> s.getMatch().getDescription().equals("Cinco")
                                &&s.getPlayer().getNickname().equals("Player2")
                                &&s.getKills().equals(kills.toString())
                                &&s.getDeaths().equals(deaths.toString())
                                &&s.getAssists().equals(assists.toString())).count());
    }
    
    @Test
    public void test02_readStatById(){
        clickOn("#cbFilter").clickOn("ID").clickOn("#tfFilter").write("5 4").clickOn("#searchBtn");
        assertEquals("Failed at reading by ID.",
                1,
                statsTableView.getItems().size());
        assertEquals("Failed at reading by ID.",
                1,
                statsTableView.getItems()
                        .stream().filter(s -> s.getMatch().getId().equals(5)
                                &&s.getPlayer().getId().equals(4)).count());
        
    }
    
    @Test
    public void test03_readStatsByLeagueName(){
        clickOn("#tfFilter").doubleClickOn("#tfFilter").eraseText(1);
        clickOn("#cbFilter").clickOn("League Name").clickOn("#tfFilter").write("Tres").clickOn("#searchBtn");
        assertEquals("Failed at reading by League Name.",
                1,
                statsTableView.getItems()
                        .stream().filter(s -> s.getMatch().getLeague().getName().equals("Tres")).count());
    }
    
    @Test
    public void test04_readStatsByMatchDescription(){
        clickOn("#tfFilter").doubleClickOn("#tfFilter").eraseText(1);
        clickOn("#cbFilter").clickOn("Match Desc.").clickOn("#tfFilter").write("Cinco").clickOn("#searchBtn");
        assertEquals("Failed at reading by Match Description.",
                1,
                statsTableView.getItems()
                        .stream().filter(s -> s.getMatch().getDescription().equals("Cinco")).count());
    }
    
    @Test
    public void test05_readStatsByPlayerNickname(){
        clickOn("#tfFilter").doubleClickOn("#tfFilter").eraseText(1);
        clickOn("#cbFilter").clickOn("Player Nickname").clickOn("#tfFilter").write("Player2").clickOn("#searchBtn");
        assertEquals("Failed at reading by Player Nickname.",
                1,
                statsTableView.getItems()
                        .stream().filter(s -> s.getPlayer().getNickname().equals("Player2")
                                &&s.getMatch().getId().equals(5)).count());
    }
    
    @Test
    public void test06_readStatsByTournamentName(){
        clickOn("#tfFilter").doubleClickOn("#tfFilter").eraseText(1);
        clickOn("#cbFilter").clickOn("Tournament Name").clickOn("#tfFilter").write("Dos").clickOn("#searchBtn");
        assertEquals("Failed at reading by Tournament Name.",
                1,
                statsTableView.getItems()
                        .stream().filter(s -> s.getMatch().getTournament().getName().equals("Dos")).count());
    }
    
    @Test
    public void test07_deleteStat(){
        Node row = lookup(".table-row-cell").nth(0).query();
        clickOn(row);
        clickOn("#deleteBtn");
        clickOn("#cbFilter").clickOn("ALL").clickOn("#searchBtn");
        assertEquals("Stats was not created.",
                0,
                statsTableView.getItems()
                        .stream().filter(s -> s.getMatch().getDescription().equals("Cinco")
                                &&s.getPlayer().getNickname().equals("Player2")).count());
    }
}
