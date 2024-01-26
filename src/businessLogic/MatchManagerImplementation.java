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
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import model.Match;
import rest.MatchRESTClient;

/**
 *
 * @author imape
 */
public class MatchManagerImplementation implements MatchManager {

    //REST users web client
    private MatchRESTClient webClient;
    private static final Logger LOGGER = Logger.getLogger("javafxapplicationud3example");

    public MatchManagerImplementation() {
        webClient = new MatchRESTClient();
    }

    @Override
    public List<Match> findAllMatches() throws ReadException {
        List<Match> matches = null;
        try {
            LOGGER.info("MatchesManager: Finding all matches from REST service (XML).");
            matches = webClient.findAllMatches_XML(new GenericType<List<Match>>() {
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return matches;
    }

    @Override
    public List<Match> findAllTournamentMatches() throws ReadException {
        List<Match> matches = null;
        try {
            
        } catch (Exception ex) {

        }
        return matches;
    }

    @Override
    public List<Match> findAllLeagueMatches() throws ReadException {
        List<Match> matches = null;
        
        try{
            
        }catch(Exception ex){
            
        }
        return matches;
    }

    @Override
    public Match findAMatch(Integer id) throws ReadException {
        Match match = null;
        try {
            LOGGER.info("MatchesManager: Finding all matches from REST service (XML).");
            
        } catch (Exception ex) {

        }
        return match;
    }

    @Override
    public List<Match> findMatchesByTournamentId(Integer id) throws ReadException {
        List<Match> matches = null;
        try {
            LOGGER.info("MatchesManager: Finding all matches from REST service (XML).");
            matches = webClient.findMatchesByTournamentId_XML(new GenericType<List<Match>>(){}, id);
        } catch (Exception ex) {

        }
        return matches;
    }

    @Override
    public List<Match> findMatchesByLeagueId(Integer id) throws ReadException {
        List<Match> matches = null;
        try {
            LOGGER.info("MatchesManager: Finding all matches from REST service (XML).");
            matches = webClient.findMatchesByLeagueId_XML(new GenericType<List<Match>>(){}, id);
        } catch (Exception ex) {

        }
        return matches;
    }

    @Override
    public void createMatch(Match match) throws CreateException {
        try {
            LOGGER.info("MatchesManager: Create match in REST service (XML).");
            webClient.createMatch_XML(match);
        } catch (Exception e) {
        }
    }

    @Override
    public void deleteMatch(Match match) throws DeleteException {
        
        try{
            LOGGER.info("MatchesManager: Deleting selected match");
            webClient.delete(match.getId().toString());
        }catch(Exception ex){
            
        }
    }

    @Override
    public void updateMatch(Match match) throws UpdateException {
        try {
            LOGGER.info("Updating match...");
            webClient.updateMatch_XML(match);
            LOGGER.info("Match updated!");
        } catch (Exception e) {
        }
    }

    @Override
    public List<Match> findMatchesByUserNickname(String nickname) throws ReadException {
        List<Match> matches = null;
        try {
            LOGGER.info("MatchesManager: Finding all matches from REST service (XML).");
            matches = webClient.findMatchesByUserNickname_XML(new GenericType<List<Match>>(){}, nickname);
        } catch (Exception ex) {

        }
        return matches;
    }

}
