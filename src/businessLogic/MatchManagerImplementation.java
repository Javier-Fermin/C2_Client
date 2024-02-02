package businessLogic;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.DescriptionAlreadyExsistsException;
import exceptions.NoResultFoundException;
import exceptions.ReadException;
import exceptions.UpdateException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import model.Match;
import rest.MatchRESTClient;

/**
 * Implementation of the MatchManager interface that interacts with a RESTful
 * service.
 *
 * @author Imanol
 */
public class MatchManagerImplementation implements MatchManager {

    private MatchRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("javafxapplicationud3example");

    /**
     * Constructs a new MatchManagerImplementation. Initializes the
     * MatchRESTClient for communication with the REST service.
     */
    public MatchManagerImplementation() {
        webClient = new MatchRESTClient();
    }

    /**
     * Finds all matches played.
     *
     * @return A list of all matches played.
     * @throws ReadException If there is any exception during processing.
     */
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
     /**
     * Finds matches played in a specific tournament.
     * 
     * @param id The ID of the tournament.
     * @return A list of matches played in the specified tournament.
     * @throws ReadException If there is any exception during processing.
     */
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

   /**
     * Finds matches played in a specific league.
     * 
     * @param id The ID of the league.
     * @return A list of matches played in the specified league.
     * @throws ReadException If there is any exception during processing.
     */
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

    /**
     * Creates a new match in the underlying application storage.
     * 
     * @param match The new match to be created.
     * @throws CreateException If there is any exception during processing.
     */
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
     /**
     * Deletes a match from the underlying application storage.
     * 
     * @param match The match to be deleted.
     * @throws DeleteException If there is any exception during processing.
     */
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
     /**
     * Updates a match in the underlying application storage.
     * 
     * @param match The match with updated changes.
     * @throws UpdateException If there is any exception during processing.
     */
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
    /**
     * Finds matches associated with a user's nickname.
     * 
     * @param nickname The user's nickname.
     * @return A list of matches associated with the specified nickname.
     * @throws ReadException If there is any exception during processing.
     */
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
    /**
     * Finds a match by its description.
     * 
     * @param description The description of the match.
     * @return The match with the specified description.
     * @throws ReadException If there is any exception during processing.
     * @throws NoResultFoundException If no match is found with the given description.
     */
    @Override
    public Match findMatchByDescription(String description) throws ReadException, NoResultFoundException {
        Match match = null;
        try {
            LOGGER.info("MatchesManager: Finding a match by description from REST service (XML).");
            match = webClient.findMatchByDescription_XML(Match.class, description);
        } catch (InternalServerErrorException ex) {
            LOGGER.log(Level.SEVERE, "Error finding a match by description", ex.getMessage());
            throw new ReadException(ex.getMessage());
        } catch (NotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Error finding a match by description", ex.getMessage());
            throw new NoResultFoundException(ex.getMessage());
        }
        return match;
    }
    /**
     * Checks if a match with the specified description already exists.
     * 
     * @param description The description to be checked.
     * @throws ReadException If there is any exception during processing.
     * @throws DescriptionAlreadyExsistsException If a match with the specified description already exists.
     */
    @Override
    public void checkDescriptionForMatchAlreadyExisting(String description) throws ReadException, DescriptionAlreadyExsistsException {
        Match match = null;
        try {
            LOGGER.info("MatchesManager: Finding a match by description from REST service (XML).");
            match = webClient.findMatchByDescription_XML(Match.class, description);
            //If match founded the description already exists and that´s an exception for us
            throw new DescriptionAlreadyExsistsException("Desciption already exists");
        } catch (InternalServerErrorException ex) {
            LOGGER.log(Level.SEVERE, "Error finding a match by description", ex.getMessage());
            throw new ReadException(ex.getMessage());
        } catch (NotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Checking new description: OK");

        }

    }
}
