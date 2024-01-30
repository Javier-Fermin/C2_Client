package businessLogic;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.ReadException;
import exceptions.UpdateException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import model.Match;
import rest.MatchRESTClient;

public class MatchManagerImplementation implements MatchManager {

    private MatchRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("javafxapplicationud3example");

    public MatchManagerImplementation() {
        webClient = new MatchRESTClient();
    }

    @Override
    public List<Match> findAllMatches() throws ReadException {
        try {
            LOGGER.info("MatchesManager: Finding all matches from REST service (XML).");
            return webClient.findAllMatches_XML(new GenericType<List<Match>>() {
            });
        } catch (ClientErrorException e) {
            LOGGER.log(Level.SEVERE, "Error finding all matches", e);
            throw new ReadException();
        }
    }

    @Override
    public List<Match> findMatchesByTournamentId(Integer id) throws ReadException {
        try {
            LOGGER.info("MatchesManager: Finding matches by tournament ID from REST service (XML).");
            return webClient.findMatchesByTournamentId_XML(new GenericType<List<Match>>() {
            }, id);
        } catch (ClientErrorException ex) {
            LOGGER.log(Level.SEVERE, "Error finding matches by tournament ID", ex);
            throw new ReadException();
        }
    }

    @Override
    public List<Match> findMatchesByLeagueId(Integer id) throws ReadException {
        try {
            LOGGER.info("MatchesManager: Finding matches by league ID from REST service (XML).");
            return webClient.findMatchesByLeagueId_XML(new GenericType<List<Match>>() {
            }, id);
        } catch (ClientErrorException ex) {
            LOGGER.log(Level.SEVERE, "Error finding matches by league ID", ex);
            throw new ReadException();
        }
    }

    @Override
    public void createMatch(Match match) throws CreateException {
        try {
            LOGGER.info("MatchesManager: Create match in REST service (XML).");
            webClient.createMatch_XML(match);
        } catch (WebApplicationException e) {
            LOGGER.log(Level.SEVERE, "Error creating match", e);
            throw new CreateException();
        }
    }

    @Override
    public void deleteMatch(Match match) throws DeleteException {
        try {
            LOGGER.info("MatchesManager: Deleting selected match");
            webClient.delete(match.getId().toString());
        } catch (WebApplicationException ex) {
            LOGGER.log(Level.SEVERE, "Error deleting match", ex);
            throw new DeleteException();
        }
    }

    @Override
    public void updateMatch(Match match) throws UpdateException {
        try {
            LOGGER.info("Updating match...");
            webClient.updateMatch_XML(match);
            LOGGER.info("Match updated!");
        } catch (WebApplicationException e) {
            LOGGER.log(Level.SEVERE, "Error updating match", e);
            throw new UpdateException();
        }
    }

    @Override
    public List<Match> findMatchesByUserNickname(String nickname) throws ReadException {
        try {
            LOGGER.info("MatchesManager: Finding matches by user nickname from REST service (XML).");
            return webClient.findMatchesByUserNickname_XML(new GenericType<List<Match>>() {
            }, nickname);
        } catch (ClientErrorException ex) {
            LOGGER.log(Level.SEVERE, "Error finding matches by user nickname", ex);
            throw new ReadException();
        }
    }

    @Override
    public List<Match> findAMatch(Integer id) throws ReadException {
        List<Match> matches = null;
        try {
            LOGGER.info("MatchesManager: Finding a match by ID from REST service (XML).");
            matches = webClient.findAMatch_XML(new GenericType<List<Match>>() {
            }, id.toString());
        } catch (ClientErrorException ex) {
            LOGGER.log(Level.SEVERE, "Error finding a match by ID", ex);
            throw new ReadException();
        }
        return matches;
    }

    @Override
    public Match findMatchByDescription(String description) throws ReadException {
        Match match = null;
        try {
            LOGGER.info("MatchesManager: Finding a match by description from REST service (XML).");
            match = webClient.findMatchByDescription_XML(Match.class, description);
        } catch (ClientErrorException ex) {
            LOGGER.log(Level.SEVERE, "Error finding a match by description", ex);
            throw new ReadException();
        }
        return match;
    }
}
