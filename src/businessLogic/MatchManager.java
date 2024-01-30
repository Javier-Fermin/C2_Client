/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.ReadException;
import exceptions.UpdateException;
import java.util.List;
import model.Match;

/**
 *
 * @author imape
 */
public interface MatchManager {
   /**
     * Finds a set of {@link Match} objects.
     * 
     * @return A set of all Matches played .
     * @throws ReadException If there is any Exception during processing.
     */
    public List<Match> findAllMatches() throws ReadException;
    
    public List<Match> findAMatch(Integer id) throws ReadException;

    /**
     * Finds a {@link Match} by its id that is played in a specific Tournament.
     * 
     * @param id the id for the Tournament to be found.
     * @return A set of {@link Match} that are played in a specific Tournament.
     * @throws ReadException If there is any Exception during processing.
     */
    public List<Match> findMatchesByTournamentId(Integer id) throws ReadException;

    /**
     * Finds a {@link Match} by its id that is played in a specific League.
     * 
     * @param id the id for the Tournament to be found.
     * @return A set of {@link Match} that are played in a specific League.
     * @throws ReadException If there is any Exception during processing.
     */
    public List<Match> findMatchesByLeagueId(Integer id) throws ReadException;

    /**
     * Creates a {@link Match} in the underlying application storage.
     * 
     * @param match The new created match.
     * @throws CreateException If there is any Exception during processing.
     */
    public void createMatch(Match match) throws CreateException;

    /**
     * Deletes a {@link Match} in the underlying application storage.
     * 
     * @param match The match to be found and deleted.
     * @throws DeleteException If there is any Exception during processing.
     */
    public void deleteMatch(Match match) throws DeleteException;

    /**
     * Updated a {@link Match} in the underlying application storage.
     * 
     * @param match The match updated with new changes.
     * @throws UpdateException If there is any Exception during processing.
     */
    public void updateMatch(Match match) throws UpdateException;
    
    /**
     *
     * @param nickname
     * @return
     * @throws FindException
     */
    public List<Match> findMatchesByUserNickname(String nickname) throws ReadException;
    
    public Match findMatchByDescription(String description) throws ReadException;
}
