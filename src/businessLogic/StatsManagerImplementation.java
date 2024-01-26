/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import client.Client;
import exceptions.CreateException;
import exceptions.DeleteException;
import exceptions.ReadException;
import exceptions.UpdateException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import model.Stats;
import rest.StatsRESTClient;

/**
 *
 * @author javie
 */
public class StatsManagerImplementation implements StatsManager{
    private StatsRESTClient client = new StatsRESTClient();
    private static final Logger LOGGER = Logger.getLogger(StatsManagerImplementation.class.getName());
    
    @Override
    public Stats findStatById(String matchId, String playerId) throws ReadException {
        Stats stat = null;
        try{
            LOGGER.log(Level.INFO, "StatsManager: Finding stat by ID: {0}{1} (XML).", new Object[]{matchId, playerId});
            //Ask webClient for all users' data.
            if("JSON".equals(ResourceBundle.getBundle("resources.Client").getString("MEDIA_TYPE"))){
                stat = client.find_JSON(Stats.class, matchId, playerId);
            }else{
                stat = client.find_XML(Stats.class, matchId, playerId);
            }
        }catch(Exception ex){
            LOGGER.log(Level.SEVERE,
                    "StatsManager: Exception finding desired stat, {0}",
                    ex.getMessage());
            throw new ReadException("Error finding desired stat:\n"+ex.getMessage());
        }
        return stat;
    }

    @Override
    public List<Stats> findAllStats() throws ReadException {
        List<Stats> stats = null;
        try{
            LOGGER.info("StatsManager: Finding all stats from REST service (XML).");
            //Ask webClient for all users' data.
            if("JSON".equals(ResourceBundle.getBundle("resources.Client").getString("MEDIA_TYPE"))){
                stats = client.findAll_JSON(new GenericType<List<Stats>>() {});
            }else{
                stats = client.findAll_XML(new GenericType<List<Stats>>() {});
            }
        }catch(Exception ex){
            LOGGER.log(Level.SEVERE,
                    "StatsManager: Exception finding desired stats, {0}",
                    ex.getMessage());
            throw new ReadException("Error finding desired stats:\n"+ex.getMessage());
        }
        return stats;
    }

    @Override
    public List<Stats> findStatsByPlayerNickname(String nickname) throws ReadException {
        List<Stats> stats = null;
        try{
            LOGGER.log(Level.INFO, "StatsManager: Finding stats by player nickname: {0} (XML).", nickname);
            //Ask webClient for all users' data.
            if("JSON".equals(ResourceBundle.getBundle("resources.Client").getString("MEDIA_TYPE"))){
                stats = client.findStatsByPlayerNickname_JSON(new GenericType<List<Stats>>() {},nickname);
            }else{
                stats = client.findStatsByPlayerNickname_XML(new GenericType<List<Stats>>() {},nickname);
            }
        }catch(Exception ex){
            LOGGER.log(Level.SEVERE,
                    "StatsManager: Exception finding desired stats, {0}",
                    ex.getMessage());
            throw new ReadException("Error finding desired stats:\n"+ex.getMessage());
        }
        return stats;
    }

    @Override
    public List<Stats> findStatsByMatchDescription(String matchDescription) throws ReadException {
        List<Stats> stats = null;
        try{
            LOGGER.log(Level.INFO, "StatsManager: Finding stats by match description: {0} (XML).", matchDescription);
            //Ask webClient for all users' data.
            if("JSON".equals(ResourceBundle.getBundle("resources.Client").getString("MEDIA_TYPE"))){
                stats = client.findStatsByMatchDescription_JSON(new GenericType<List<Stats>>() {}, matchDescription);
            }else{
                stats = client.findStatsByMatchDescription_XML(new GenericType<List<Stats>>() {}, matchDescription);
            }
        }catch(Exception ex){
            LOGGER.log(Level.SEVERE,
                    "StatsManager: Exception finding desired stats, {0}",
                    ex.getMessage());
            throw new ReadException("Error finding desired stats:\n"+ex.getMessage());
        }
        return stats;
    }

    @Override
    public List<Stats> findStatsByLeagueName(String leagueName) throws ReadException {
        List<Stats> stats = null;
        try{
            LOGGER.log(Level.INFO, "StatsManager: Finding stats by league name: {0} (XML).", leagueName);
            //Ask webClient for all users' data.
            if("JSON".equals(ResourceBundle.getBundle("resources.Client").getString("MEDIA_TYPE"))){
                stats = client.findStatsByLeagueName_JSON(new GenericType<List<Stats>>() {}, leagueName);
            }else{
                stats = client.findStatsByLeagueName_XML(new GenericType<List<Stats>>() {}, leagueName);
            }
        }catch(Exception ex){
            LOGGER.log(Level.SEVERE,
                    "StatsManager: Exception finding desired stats, {0}",
                    ex.getMessage());
            throw new ReadException("Error finding desired stats:\n"+ex.getMessage());
        }
        return stats;
    }

    @Override
    public List<Stats> findStatsByTournamentName(String tournamentName) throws ReadException {
        List<Stats> stats = null;
        try{
            LOGGER.log(Level.INFO, "StatsManager: Finding stats by torunament name: {0} (XML).", tournamentName);
            //Ask webClient for all users' data.
            if("JSON".equals(ResourceBundle.getBundle("resources.Client").getString("MEDIA_TYPE"))){
                stats = client.findStatsByTournamentName_JSON(new GenericType<List<Stats>>() {}, tournamentName);
            }else{
                stats = client.findStatsByTournamentName_XML(new GenericType<List<Stats>>() {}, tournamentName);
            }
        }catch(Exception ex){
            LOGGER.log(Level.SEVERE,
                    "StatsManager: Exception finding desired stats, {0}",
                    ex.getMessage());
            throw new ReadException("Error finding desired stats:\n"+ex.getMessage());
        }
        return stats;
    }

    @Override
    public void createStats(Stats stats) throws CreateException {
        LOGGER.log(Level.INFO, "StatsManager: Creating stat: {0} (XML).", stats.toString());
        if("JSON".equals(ResourceBundle.getBundle("resources.Client").getString("MEDIA_TYPE"))){
            client.create_JSON(stats);
        }else{
            client.create_XML(stats);
        }
    }

    @Override
    public void updateStats(Stats stats) throws UpdateException {
        LOGGER.log(Level.INFO, "StatsManager: Updating stat: {0} (XML).", stats.toString());
        if("JSON".equals(ResourceBundle.getBundle("resources.Client").getString("MEDIA_TYPE"))){
            client.update_JSON(stats);
        }else{
            client.update_XML(stats);
        }
    }

    @Override
    public void deleteStats(Stats stats) throws DeleteException {
        LOGGER.log(Level.INFO, "StatsManager: Deleting stat: {0} (XML).", stats.toString());
        client.delete(stats.getId().getMatchId().toString(), stats.getId().getPlayerId().toString());
    }
    
}
