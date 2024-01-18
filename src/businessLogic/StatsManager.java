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
import model.Stats;

/**
 *
 * @author javie
 */
public interface StatsManager {
    /**
     * Finds a {@link Stats} by its id
     * 
     * @param id the id of the desired stats
     * @return the {@link Stats} with the given id
     * @throws ReadException if there is an exception during the method
     */
    public Stats findStatById(String matchId, String playerId) throws ReadException;
    
    /**
     * Finds a list of {@link Stats} with all the possible Stats
     * 
     * @return the list of {@link Stats} with all the data found 
     * @throws ReadException if there is any exception during the method
     */
    public List<Stats> findAllStats() throws ReadException;
    
    /**
     * Finds a list of {@link Stats} by a player nickname
     * 
     * @param nickname the player nickname to be searched
     * @return the list of {@link Stats} that fullfilled the requirements
     * @throws ReadException if there is any exception during the execution
     */
    public List<Stats> findStatsByPlayerNickname(String nickname) throws ReadException;
    
    /**
     * Finds a list of {@link Stats} by a match id
     * 
     * @param matchId the match id to be searched
     * @return the list of {@link Stats} that fullfilled the requirements
     * @throws ReadException if there is any exception during the method
     */
    public List<Stats> findStatsByMatchDescription(String matchDescription) throws ReadException;
    
    /**
     * Finds a list of {@link Stats} by the league name
     * 
     * @param leagueName the league name to be searched
     * @return the list of {@link Stats} that fullfilled the requirements
     * @throws ReadException if there is any exception during the execution
     */
    public List<Stats> findStatsByLeagueName(String leagueName) throws ReadException;
    
    /**
     * Finds a list of {@link Stats} by the tournament name
     * 
     * @param tournamentName the tournament name to be searched
     * @return the list of {@link Stats} that fullfilled the requirements
     * @throws ReadException if there is any exception during the execution
     */
    public List<Stats> findStatsByTournamentName(String tournamentName) throws ReadException;
    
    /**
     * Creates a Stats in the database
     * 
     * @param stats the Stats to be created
     * @throws CreateException  if there is any exception during the method
     */
    public void createStats(Stats stats) throws CreateException;
    
    /**
     * Updates a Stats data in the database
     * 
     * @param stats the Stats to be updated with the data changes
     * @throws UpdateException if there is any exception during the method
     */
    public void updateStats(Stats stats) throws UpdateException;
    
    /**
     * Deletes a Stats from the database
     * 
     * @param stats the desired Stats to be deleted
     * @throws DeleteException if there is any exception during the execution
     */
    public void deleteStats(Stats stats) throws DeleteException;
}
