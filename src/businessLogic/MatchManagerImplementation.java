/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import exceptions.ReadException;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import model.Match;
import rest.MatchRESTClient;

/**
 *
 * @author imape
 */
public class MatchManagerImplementation implements MatchManager{
    //REST users web client
    private MatchRESTClient webClient;
    private static final Logger LOGGER=Logger.getLogger("javafxapplicationud3example");
    
    public MatchManagerImplementation(){
        webClient = new MatchRESTClient();
    }
    @Override
    public <Match> Match findMatchesByUserNickname(Class<Match> responseType,String nickname) {
        Match match;
        try{
            MatchRESTClient rest = new MatchRESTClient();
            
            match = rest.findMatchesByUserNickname_XML(responseType, nickname);
        }catch(Exception e){
            throw new UnsupportedOperationException("Not supported yet."); 
        }
        
        return (match);
    }

    @Override
    public List<Match> findAllMatches() throws ReadException{
        List<Match> matches = null;
        try{
            LOGGER.info("MatchesManager: Finding all matches from REST service (XML).");
            matches = webClient.findAllMatches_XML(new GenericType<List<Match>>() {});
        }catch(Exception ex){
            
        }
        return matches;
    }
    
}
