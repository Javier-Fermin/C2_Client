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
import java.util.Date;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import model.Tournament;

/**
 *
 * @author Fran
 */
public interface TournamentManage {
    
    public void createTournament(Tournament tournament) throws CreateException;
    
    public void updateTournament(Tournament tournament) throws UpdateException;
    
    public void deleteTournament(Tournament tournament) throws DeleteException;
    
    public Tournament findTournament(Integer id) throws ReadException;
    
    public List<Tournament> findAllTournaments() throws ReadException;
    
    public List<Tournament> findTournamentByName(String name) throws ReadException;
    
    public List<Tournament> findTournamentByBestOf(Integer bestOf) throws ReadException;
     
    public List<Tournament> findTournamentByDate(String date) throws ReadException;
    
     public Tournament findTournamentByMatch() throws ReadException;
}
