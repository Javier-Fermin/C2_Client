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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import rest.PlayerRESTClient;

/**
 *
 * @author javie
 */
public class PlayerManagerImplementation implements PlayerManager{
    private PlayerRESTClient client = new PlayerRESTClient();
    private static final Logger LOGGER = Logger.getLogger(PlayerManagerImplementation.class.getName());
    
    @Override
    public Player findPlayerByMail(String email) throws ReadException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Player> findPlayers() throws ReadException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void createPlayer(Player player) throws CreateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deletePlayer(Player player) throws DeleteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updatePlayer(Player player) throws UpdateException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Player findPlayerById(Integer id) throws ReadException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Player findPlayerByNickname(String nickname) throws ReadException {
        Player player = null;
        try{
            LOGGER.log(Level.INFO, "PlayerManager: Finding player by nickname: {0} (XML).", nickname);
            player = client.findPlayerByNickname_XML(Player.class, nickname);
        }catch(Exception ex){
            LOGGER.log(Level.SEVERE,
                    "StatsManager: Exception finding desired player, {0}",
                    ex.getMessage());
            throw new ReadException("Error finding desired stats:\n"+ex.getMessage());
        }
        return player;
    }
    
}
