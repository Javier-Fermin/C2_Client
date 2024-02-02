/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package match;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.Match;
import model.Stats;
import model.User;
import model.UserType;
import org.glassfish.jersey.message.internal.XmlCollectionJaxbProvider.App;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isVisible;
import view.MatchWindowController;

/**
 *
 * @author Imanol
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MatchWindowTest extends ApplicationTest {

    private static int number;

    private TableView<Match> tvMatches;

    private void launch(Stage stage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/Matches.fxml"));
            Parent root = (Parent) loader.load();
            MatchWindowController controller = MatchWindowController.class
                    .cast(loader.getController());
            controller.setMainStage(stage);
            controller.initStage(root, new User(null, null, null, null, null, UserType.ADMIN), null, null);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        new MatchWindowTest().launch(stage);
        TableView<Object> tvCustomers = lookup("#tvMatches").queryTableView();
    }

    @Override
    public void stop() {
    }

    /*
    *CRUD OPERATIONS TESTTING
    *
     */
    @Test
    public void testCreateMatch() {
        tvMatches = lookup("#tvMatches").query();
        Integer size = tvMatches.getItems().size();
        clickOn("#btnAddMatch");

        ObservableList<Match> matches = tvMatches.getItems();

        assertEquals(tvMatches.getItems().size(), size + 1);

        int newRow = matches.size() - 1;
        Match newMatch = matches.get(newRow);

        Date now = newMatch.getPlayedDate();
        LocalDate dateInitLocal = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        assertEquals(newMatch.getDescription(), null);
        assertEquals(newMatch.getLeague(), null);
        assertEquals(newMatch.getPlayedDate(), now);
        assertEquals(newMatch.getTournament(), null);
        assertEquals(newMatch.getWinner(), null);

        press(KeyCode.ENTER);

    }

    @Test
    public void testUpdateMatchDescription() {
        tvMatches = lookup("#tvMatches").query();
        Node column = lookup("#tcDescription").nth(tvMatches.getItems().size()).query();

        clickOn(column);
        Integer row = tvMatches.getSelectionModel().getSelectedIndex();
        Node tcDescription = lookup("#tcDescription").nth(row + 1).query();

        clickOn(tcDescription);
        write("Test match" + new Random().nextInt());
        push(KeyCode.ENTER);

        Match newMatch = (Match) tvMatches.getSelectionModel().getSelectedItem();

        assertNotEquals(tcDescription, newMatch.getDescription());
    }

    @Test
    public void testUpdateMatchDate() {
        tvMatches = lookup("#tvMatches").query();
        Node column = lookup("#tcPlayedDate").nth(tvMatches.getItems().size()).query();

        clickOn(column);
        Integer row = tvMatches.getSelectionModel().getSelectedIndex();
        Node tcPlayedDate = lookup("#tcPlayedDate").nth(row + 1).query();

        clickOn(tcPlayedDate);
        push(KeyCode.ENTER);
        clickOn(".date-picker .arrow-button");
        clickOn(String.valueOf(LocalDate.now().minusDays(3).getDayOfMonth()));

        Match newMatch = (Match) tvMatches.getSelectionModel().getSelectedItem();

        assertNotEquals(tcPlayedDate, newMatch.getPlayedDate());
    }

    @Test
    public void testUpdateWinnerTeam() {
        tvMatches = lookup("#tvMatches").query();
        Node column = lookup("#tcWinnerTeam").nth(tvMatches.getItems().size()).query();

        clickOn(column);
        Integer row = tvMatches.getSelectionModel().getSelectedIndex();
        Node tcWinnerTeam = lookup("#tcWinnerTeam").nth(row + 1).query();

        doubleClickOn(tcWinnerTeam);
        push(KeyCode.DOWN);
        push(KeyCode.ENTER);

        Match newMatch = (Match) tvMatches.getSelectionModel().getSelectedItem();

        assertNotEquals(tcWinnerTeam, newMatch.getWinner());
    }

    @Test
    public void testUpdateLeague() {
        tvMatches = lookup("#tvMatches").query();
        Node column = lookup("#tcLeague").nth(tvMatches.getItems().size()).query();

        clickOn(column);
        Integer row = tvMatches.getSelectionModel().getSelectedIndex();
        Node tcLeague = lookup("#tcLeague").nth(row + 1).query();

        clickOn(tcLeague);
        write("liga1");
        push(KeyCode.ENTER);

        Match newMatch = (Match) tvMatches.getSelectionModel().getSelectedItem();

        assertNotEquals(tcLeague, newMatch.getLeague());
    }

    @Test
    public void testUpdateTournament() {
        tvMatches = lookup("#tvMatches").query();
        Node column = lookup("#tcTournament").nth(tvMatches.getItems().size()).query();

        clickOn(column);
        Integer row = tvMatches.getSelectionModel().getSelectedIndex();
        Node tcTournament = lookup("#tcTournament").nth(row + 1).query();

        clickOn(tcTournament);
        write("Uno");
        push(KeyCode.ENTER);

        Match newMatch = (Match) tvMatches.getSelectionModel().getSelectedItem();

        assertNotEquals(tcTournament, newMatch.getTournament());
    }

    @Test
    public void testDeleteMatch() {
        Node column = lookup("#tcDescription").nth(0).query();
        clickOn(column);

        tvMatches = lookup("#tvMatches").query();
        Node row = lookup(".table-row-cell").nth(0).query();

        clickOn(row);
        Match toDelete = (Match) tvMatches.getSelectionModel().getSelectedItem();
        String name = toDelete.getDescription();
        clickOn("#btnDeleteMatch");
        //Verifica que aparezca el mensaje de confirmación del borrado.
        verifyThat("Are you sure you want to delete the selected match?", isVisible());
        press(KeyCode.ENTER);
        Node auxRow = lookup(".table-row-cell").nth(0).query();
        clickOn(auxRow);
        Match nexToDelete = (Match) tvMatches.getSelectionModel().getSelectedItem();
        assertNotEquals(toDelete, nexToDelete);

        List<Match> match = new ArrayList<>(tvMatches.getItems());
        Boolean notFound = true;
        for (Match aux : match) {
            if (aux.getClass().equals(toDelete)) {
                notFound = false;
            }
        }
        assertTrue(notFound);

        press(KeyCode.ENTER);
    }

    @Test
    public void testFindAll() {
        press(KeyCode.ENTER);
        tvMatches = lookup("#tvMatches").query();
        Node btnSearch = lookup("#btnSearch").query();
        List<Match> allMatches = new ArrayList<>(tvMatches.getItems());
        Match match = null;
        for (Match auxMatch : allMatches) {
            if (auxMatch != null) {
                match = match;
                break;
            }
        }
        clickOn(btnSearch);

        List<Match> matchesAux = new ArrayList<>(tvMatches.getItems());
        assertEquals(matchesAux, allMatches);

    }

    @Test
    public void testFindTournaments() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        List<Match> allMatches = new ArrayList<>(tvMatches.getItems());

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        clickOn(btnSearch);

        List<Match> matchesAux = new ArrayList<>(tvMatches.getItems());
        assertNotEquals(matchesAux, allMatches);

        for (Match matchAux : matchesAux) {
            assertTrue(!matchAux.getTournament().equals(null));
        }
    }

    @Test
    public void testFindLeague() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        List<Match> allMatches = new ArrayList<>(tvMatches.getItems());

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        clickOn(btnSearch);

        List<Match> matchesAux = new ArrayList<>(tvMatches.getItems());
        assertNotEquals(matchesAux, allMatches);

        for (Match matchAux : matchesAux) {
            assertTrue(!matchAux.getLeague().equals(null));
        }
    }
    
    @Test
    public void testFindTournamentsName() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        Node tfSearchBar = lookup("#tfSearchBar").query();
        List<Match> allMatches = new ArrayList<>(tvMatches.getItems());

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        clickOn(tfSearchBar);
        write("torneo1");
        clickOn(btnSearch);

        List<Match> matchesAux = new ArrayList<>(tvMatches.getItems());
        assertNotEquals(matchesAux, allMatches);

        for (Match matchAux : matchesAux) {
            assertTrue(matchAux.getTournament().getName().equalsIgnoreCase("torneo1"));
        }
    }

    @Test
    public void testFindLeagueName() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        Node tfSearchBar = lookup("#tfSearchBar").query();
        List<Match> allMatches = new ArrayList<>(tvMatches.getItems());

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        clickOn(tfSearchBar);
        write("liga1");
        clickOn(btnSearch);

        List<Match> matchesAux = new ArrayList<>(tvMatches.getItems());
        assertNotEquals(matchesAux, allMatches);

        for (Match matchAux : matchesAux) {
            assertTrue(matchAux.getLeague().getName().equalsIgnoreCase("liga1"));
        }

    }

    @Test
    public void testFindDescription() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        Node tfSearchBar = lookup("#tfSearchBar").query();
        List<Match> allMatches = new ArrayList<>(tvMatches.getItems());

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        clickOn(tfSearchBar);
        write("New match");
        clickOn(btnSearch);

        List<Match> matchesAux = new ArrayList<>(tvMatches.getItems());
        assertNotEquals(matchesAux, allMatches);

        for (Match matchAux : matchesAux) {
            assertTrue(matchAux.getDescription().equalsIgnoreCase("New match"));
        }

    }

    @Test
    public void testFindMatchByName() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        Node tfSearchBar = lookup("#tfSearchBar").query();
        List<Match> allMatches = new ArrayList<>(tvMatches.getItems());

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        clickOn(tfSearchBar);
        write("Paco");
        clickOn(btnSearch);

        List<Match> matchesAux = new ArrayList<>(tvMatches.getItems());
        assertNotEquals(matchesAux, allMatches);

        for (Match matchAux : matchesAux) {
            Set<Stats> stats = matchAux.getStats();
            for (Stats stat : stats) {
                assertEquals(stat.getPlayer().getName(), "Paco");
            }
        }

    }

    /*
    *LOGIC ERROR TESTTING
    *
     */

    //@Test
    public void testFindMatchByNameError() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        Node tfSearchBar = lookup("#tfSearchBar").query();

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        clickOn(tfSearchBar);
        write("NoUser");
        clickOn(btnSearch);

        verifyThat("No player found", isVisible());

        push(KeyCode.ENTER);
    }

    //@Test
    public void testFindMatchByNameInputError() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        Node tfSearchBar = lookup("#tfSearchBar").query();

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        clickOn(tfSearchBar);
        write("NoUser");
        clickOn(btnSearch);

        verifyThat("No player found", isVisible());

        push(KeyCode.ENTER);
    }

    //@Test
    public void testFindMatchByNameNoInputError() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        Node tfSearchBar = lookup("#tfSearchBar").query();

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        clickOn(tfSearchBar);
        clickOn(btnSearch);

        verifyThat("Please introduce a player", isVisible());

        push(KeyCode.ENTER);

    }

    //@Test
    public void testFindMatchByDescriptionInputError() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        Node tfSearchBar = lookup("#tfSearchBar").query();

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        clickOn(tfSearchBar);
        write("NoDescription");
        clickOn(btnSearch);

        verifyThat("No matches related to the description", isVisible());

        push(KeyCode.ENTER);
    }

    //@Test
    public void testFindMatchByDescriptionNoInputError() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        Node tfSearchBar = lookup("#tfSearchBar").query();

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        clickOn(tfSearchBar);
        clickOn(btnSearch);

        verifyThat("Please introduce a description", isVisible());

        push(KeyCode.ENTER);
    }
    
    //@Test
    public void testFindMatchByLeagueInputError() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        Node tfSearchBar = lookup("#tfSearchBar").query();

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        push(KeyCode.DOWN);
        clickOn(tfSearchBar);
        write("No league");
        clickOn(tfSearchBar);
        clickOn(btnSearch);

        verifyThat("Unable to find the match/es", isVisible());

        push(KeyCode.ENTER);
    }
    
    //@Test
    public void testFindMatchByTournamentInputError() {
        tvMatches = lookup("#tvMatches").query();
        Node cbParameters = lookup("#cbParameters").query();
        Node btnSearch = lookup("#btnSearch").query();
        Node tfSearchBar = lookup("#tfSearchBar").query();

        clickOn(cbParameters);
        push(KeyCode.DOWN);
        clickOn(tfSearchBar);
        write("No tournament");
        clickOn(tfSearchBar);
        clickOn(btnSearch);

        verifyThat("Unable to find the match/es", isVisible());

        push(KeyCode.ENTER);
    }
    
    //@Test
    public void testUpdateMatchDescriptionError() {
        tvMatches = lookup("#tvMatches").query();

        clickOn("#btnAddMatch");
        push(KeyCode.ENTER);
        
        Integer row = tvMatches.getSelectionModel().getSelectedIndex();
        Node tcDescription = lookup("#tcDescription").nth(row + 2).query();

        doubleClickOn(tcDescription);
        write("New match");
        push(KeyCode.ENTER);
        
        verifyThat("The description already exists", isVisible());

        push(KeyCode.ENTER);
        
    }
    
    //@Test
    public void testUpdateLeagueError() {
        tvMatches = lookup("#tvMatches").query();
        
        Integer row = tvMatches.getSelectionModel().getSelectedIndex();
        Node tc = lookup("#tcLeague").nth(row + 2).query();

        doubleClickOn(tc);
        write("asdasd");
        push(KeyCode.ENTER);
        
        verifyThat("No leagues were found with that name", isVisible());

        push(KeyCode.ENTER);
        
    }
    
    //@Test
    public void testUpdateTournamentError() {
        tvMatches = lookup("#tvMatches").query();
        
        Integer row = tvMatches.getSelectionModel().getSelectedIndex();
        Node tc = lookup("#tcTournament").nth(row + 2).query();

        doubleClickOn(tc);
        write("asdasd");
        push(KeyCode.ENTER);
        
        verifyThat("No tournaments were found with that name", isVisible());

        push(KeyCode.ENTER);
        
    }
    
    //@Test
    public void testUpdateDateError() {
        tvMatches = lookup("#tvMatches").query();
        
        Integer row = tvMatches.getSelectionModel().getSelectedIndex();
        Node tc = lookup("#tcPlayedDate").nth(row + 2).query();
        Node tc2 = lookup("#tcPlayedDate").nth(row - 1).query();

        doubleClickOn(tc2);
        write("asdasd");
        push(KeyCode.ENTER);
        
        verifyThat("Please introduce a valid date", isVisible());
        clickOn(tc);

        push(KeyCode.ENTER);
        
    }

}
