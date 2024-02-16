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
import java.net.ConnectException;
import java.util.Date;
import java.util.List;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ProcessingException;
import model.Tournament;

/**
 *
 * @author Fran
 */
public interface TournamentManage {
    
    public void createTournament(Tournament tournament) throws CreateException, ConnectException, ProcessingException;
    
    public void updateTournament(Tournament tournament) throws UpdateException, ConnectException, ProcessingException;
    
    public void deleteTournament(Tournament tournament) throws DeleteException, ConnectException, ProcessingException;
    
    public Tournament findTournament(Integer id) throws ReadException, ConnectException, ProcessingException;
    
    public List<Tournament> findAllTournaments() throws ReadException, ConnectException, ProcessingException;
    
    public Tournament findTournamentByName(String name) throws ReadException, ConnectException, ProcessingException;
    
    public List<Tournament> findTournamentByBestOf(String bestOf) throws ReadException, ConnectException, ProcessingException;
     
    public List<Tournament> findTournamentByDate(String date) throws ReadException, ConnectException, ProcessingException;
    
    public Tournament findTournamentByMatch(String matchDescription) throws ReadException, ConnectException, ProcessingException;
}
