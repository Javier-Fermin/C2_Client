/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businessLogic;

/**
 *
 * @author Fran
 */
public class TournamentManageFactory {
    public static TournamentManage getTournamentManageImplementation(){
        return (TournamentManage) new TournamentManageImplementation();
    }
}
