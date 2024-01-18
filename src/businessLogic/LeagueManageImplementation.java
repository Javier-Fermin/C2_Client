/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import exceptions.*;
import model.League;
import model.Match;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import rest.LeagueRESTClient;

/**
 * EJB class for League entity
 *
 * @author Emil
 */
public class LeagueManageImplementation implements LeagueManage {

    private LeagueRESTClient leagueWebClient;
    private static final Logger LOGGER=Logger.getLogger(LeagueManageImplementation.class.getName());

    public LeagueManageImplementation() {
        leagueWebClient = new LeagueRESTClient();
    }

    /**
     * Create a league
     *
     * @param league to create the league
     * @throws CreateException if have any errors
     */
    @Override
    public void createLeague(League league) throws CreateException {
        try {
            leagueWebClient.create_XML(league);
            LOGGER.info("LeagueManage: league cerated.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "LeagueManage: Exception creating league:", e.getMessage());
            throw new CreateException("Error creating a League " + e.getMessage());
        }
    }

    /**
     * Update a league
     *
     * @param league to update
     * @throws UpdateException if have any errors
     */
    @Override
    public void updateLeague(League league) throws UpdateException {
        try {
            leagueWebClient.update_XML(league);
            LOGGER.info("LeagueManage: league Updated.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "LeagueManage: Exception updating league:", e.getMessage());
            throw new UpdateException("Error updating a League " +e.getMessage());
        }
    }

    /**
     * Delete a League
     *
     * @param league to delete
     * @throws DeleteException if have any errors
     */
    @Override
    public void deleteLeague(League league) throws DeleteException {
        try {
            leagueWebClient.remove(league.getId().toString());
            LOGGER.info("LeagueManage: league deleted.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "LeagueManage: Exception deleting league:", e.getMessage());
            throw new DeleteException("Error deleting a League " +e.getMessage());
        }
    }

    /**
     * Find a league by id
     *
     * @param id to find the league
     * @return league finded by id
     * @throws ReadException if have any errors
     */
    @Override
    public League findOneLeague(Integer id) throws ReadException {
        League league = null;
        try {
            LOGGER.info("LeagueManager: Finding league by id.");
            league = leagueWebClient.findPackById_XML(League.class, id.toString());
            if (league != null) {
                LOGGER.log(Level.INFO, "LeagueManage: league", league.getId());
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "LeagueManage: Exception finding league:", e.getMessage());
            throw new ReadException("Error finding the League " +e.getMessage());
        }
        return league;
    }

    /**
     * Find all Leagues
     *
     * @return leagues finded
     * @throws ReadException if have any errors
     */
    @Override
    public List<League> findAllLeagues() throws ReadException {
        List<League> leagues = null;
        try {
            LOGGER.info("LeagueManager: Finding all league.");
            leagues = leagueWebClient.findAll_XML(new GenericType<List<League>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "LeagueManage: Exception finding leagues:", e.getMessage());
            throw new ReadException("Error finding all Leagues " +e.getMessage());

        }
        return leagues;
    }

    /**
     * Find league by name
     *
     * @param name to find the league
     * @return league finded by the name
     * @throws ReadException if have any errors
     */
    @Override
    public List<League> findLeagueByName(String name) throws ReadException {
        List<League> leagues = null;
        try {
            LOGGER.info("LeagueManager: Finding league by name.");
            leagues = leagueWebClient.findLeagueByName_XML(new GenericType<List<League>>() {}, name);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "LeagueManage: Exception finding league:", e.getMessage());
            throw new ReadException("Error findin League by name " +e.getMessage());
        }
        return leagues;
    }

    /**
     * find all finished leagues
     *
     * @param date
     * @return leagues finished
     * @throws ReadException if have any errors
     */
    @Override
    public List<League> findAllFinishLeagues(String date) throws ReadException {
        List<League> leagues = null;
        try {
            LOGGER.info("LeagueManager: Finding all finished leagues.");
            leagues = leagueWebClient.findAllFinishLeagues_XML(new GenericType<List<League>>() {}, date);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "LeagueManage: Exception finding leagues:", e.getMessage());
            throw new ReadException("Error finding all finished Leagues " +e.getMessage());
        }
        return leagues;
    }

    /**
     * Find all unstarted leagues
     *
     * @param date to compare with the end date
     * @return leagues unstarted
     * @throws ReadException if have any errors
     */
    @Override
    public List<League> findAllUnstartedLeagues(String date) throws ReadException {
        List<League> leagues = null;
        try {
            LOGGER.info("LeagueManager: Finding all unstarted leagues.");
            leagues = leagueWebClient.findAllUnstartedLeagues_XML(new GenericType<List<League>>() {}, date);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "LeagueManage: Exception finding leagues:", e.getMessage());
            throw new ReadException("Error finding all unstarted Leagues " +e.getMessage());
        }
        return leagues;
    }

    /**
     * Find league by match id
     *
     * @param id to find the league of the match
     * @return league by the match id
     * @throws ReadException if have any errors
     */
    @Override
    public List<League> findLeagueForMatch(Integer id) throws ReadException {
        List<League> leagues = null;
        try {
            LOGGER.info("LeagueManager: Finding league by id.");
            leagues = leagueWebClient.findLeagueForMatch_XML(new GenericType<List<League>>() {}, id.toString());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "LeagueManage: Exception finding league:", e.getMessage());
            throw new ReadException("Error finding League by the match " +e.getMessage());
        }
        return leagues;
    }

}
