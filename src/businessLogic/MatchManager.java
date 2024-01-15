/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

import exceptions.ReadException;
import java.util.List;
import model.Match;

/**
 *
 * @author imape
 */
public interface MatchManager {
    public <Match> Match findMatchesByUserNickname(Class<Match> responseType,String nickname);
    public List<Match> findAllMatches() throws ReadException;  
}
