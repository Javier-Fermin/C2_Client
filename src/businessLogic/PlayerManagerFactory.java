/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

/**
 *
 * @author javie
 */
public class PlayerManagerFactory {
    private static PlayerManager playerManager;
    
    public static PlayerManager getPlayerManager(){
        //If there is no poolable created it is created
        if(playerManager == null){
            playerManager = new PlayerManagerImplementation();
        }
        return playerManager; 
    }
}
