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
public class StatsManagerFactory {
    private static StatsManager statsManager;
    
    public static StatsManager getstatsManager(){
        //If there is no poolable created it is created
        if(statsManager == null){
            statsManager = new StatsManagerImplementation();
        }
        return statsManager; 
    }
}
