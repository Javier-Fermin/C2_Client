/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

/**
 *
 * @author 2dam
 */
public class LeagueManageFactory {
    
    
     private static LeagueManage leagueManage = new LeagueManageImplementation();
    
    /**
     * Creates and returns a new instance of an object that implements the LeagueManage interface.
     *
     * @return a leagueManage object that can be used for registration purposes.
     */
    public static LeagueManage getRegistrable(){
        return leagueManage;
    }
}
