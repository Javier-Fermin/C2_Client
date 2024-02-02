
package businessLogic;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.DescriptionAlreadyExsistsException;
import exceptions.NoResultFoundException;
import exceptions.ReadException;
import exceptions.UpdateException;
import java.util.List;
import model.Match;

/**
 * This interface defines the business logic for managing Match entities.
 * It provides methods for various operations such as finding matches, finding a match by ID,
 * finding matches by tournament ID, finding matches by league ID, creating, updating,
 * and deleting matches, finding matches by user nickname, finding a match by description,
 * and checking if a match description already exists.
 *
 * @author Imanol
 */
public interface MatchManager {

    /**
     * Finds all matches played.
     * 
     * @return A list of all matches played.
     * @throws ReadException If there is any exception during processing.
     */
    public List<Match> findAllMatches() throws ReadException;

    /**
     * Finds a match by its ID.
     * 
     * @param id The ID of the match to be found.
     * @return A list containing the match with the specified ID.
     * @throws ReadException If there is any exception during processing.
     */
    public List<Match> findAMatch(Integer id) throws ReadException;

    /**
     * Finds matches played in a specific tournament.
     * 
     * @param id The ID of the tournament.
     * @return A list of matches played in the specified tournament.
     * @throws ReadException If there is any exception during processing.
     */
    public List<Match> findMatchesByTournamentId(Integer id) throws ReadException;

    /**
     * Finds matches played in a specific league.
     * 
     * @param id The ID of the league.
     * @return A list of matches played in the specified league.
     * @throws ReadException If there is any exception during processing.
     */
    public List<Match> findMatchesByLeagueId(Integer id) throws ReadException;

    /**
     * Creates a new match in the underlying application storage.
     * 
     * @param match The new match to be created.
     * @throws CreateException If there is any exception during processing.
     */
    public void createMatch(Match match) throws CreateException;

    /**
     * Deletes a match from the underlying application storage.
     * 
     * @param match The match to be deleted.
     * @throws DeleteException If there is any exception during processing.
     */
    public void deleteMatch(Match match) throws DeleteException;

    /**
     * Updates a match in the underlying application storage.
     * 
     * @param match The match with updated changes.
     * @throws UpdateException If there is any exception during processing.
     */
    public void updateMatch(Match match) throws UpdateException;
    
    /**
     * Finds matches associated with a user's nickname.
     * 
     * @param nickname The user's nickname.
     * @return A list of matches associated with the specified nickname.
     * @throws ReadException If there is any exception during processing.
     */
    public List<Match> findMatchesByUserNickname(String nickname) throws ReadException;
    
    /**
     * Finds a match by its description.
     * 
     * @param description The description of the match.
     * @return The match with the specified description.
     * @throws ReadException If there is any exception during processing.
     * @throws NoResultFoundException If no match is found with the given description.
     */
    public Match findMatchByDescription(String description) throws ReadException, NoResultFoundException;

    /**
     * Checks if a match with the specified description already exists.
     * 
     * @param description The description to be checked.
     * @throws ReadException If there is any exception during processing.
     * @throws DescriptionAlreadyExsistsException If a match with the specified description already exists.
     */
    public void checkDescriptionForMatchAlreadyExisting(String description) throws ReadException, DescriptionAlreadyExsistsException;
}
