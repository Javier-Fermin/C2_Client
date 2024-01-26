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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.GenericType;
import model.Tournament;
import rest.TournamentRESTClient;

/**
 *
 * @author fdeys
 */
public class TournamentManageImplementation implements TournamentManage{

    private TournamentRESTClient tournamentClient;
    private static final Logger LOGGER=Logger.getLogger(TournamentManageImplementation.class.getName());

    public TournamentManageImplementation() {
        tournamentClient = new TournamentRESTClient();
    }
    
    
    @Override
    public void createTournament(Tournament tournament) throws CreateException {
        try{
            tournamentClient.create_XML(tournament);
            LOGGER.info("TournamentManage: tournament created.");
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception creating tournament:", e.getMessage());
            throw new CreateException("Error creating a Tournament: " + e.getMessage());
        }
    }

    @Override
    public void updateTournament(Tournament tournament) throws UpdateException {
        try{
            tournamentClient.update_XML(tournament);
            LOGGER.info("TournamentManage: tournament updated.");
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception updating tournament:", e.getMessage());
            throw new UpdateException("Error updating a Tournament: " + e.getMessage());
        }
    }

    @Override
    public void deleteTournament(Tournament tournament) throws DeleteException {
        try{
            tournamentClient.delete(tournament.getIdTournament().toString());
            LOGGER.info("TournamentManage: tournament deleted.");
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception deleting tournament:", e.getMessage());
            throw new DeleteException("Error deleting a Tournament: " + e.getMessage());
        }
    }

    @Override
    public Tournament findTournament(Integer id) throws ReadException {
        Tournament tournament = null;
        
        try{
            LOGGER.info("TournamentManage: Finding tournament.");
            tournament = tournamentClient.findTournamentById_XML(Tournament.class, id.toString());
            
            if(tournament!=null){
                LOGGER.log(Level.INFO, "TournamentManage: tournament id=", tournament.getIdTournament());
            }
            
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournament:", e.getMessage());
            throw new ReadException("Error finding the Tournament: " + e.getMessage());
        }
        
        return tournament;
    }

    @Override
    public List<Tournament> findAllTournaments() throws ReadException {
        List<Tournament> tournaments = null;
        
        try{
            LOGGER.info("TournamentManage: Finding all tournaments.");
            tournaments = tournamentClient.findAllTournaments_XML(new GenericType<List<Tournament>>() {});
            
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournaments:", e.getMessage());
            throw new ReadException("Error finding all Tournaments: " + e.getMessage());
        }
        
        return tournaments;
    }

    @Override
    public List<Tournament> findTournamentByName(String name) throws ReadException {
        List<Tournament> tournaments = null;
        
        try{
            LOGGER.info("TournamentManage: Finding tournaments by name.");
            tournaments = tournamentClient.findTournamentByName_XML(new GenericType<List<Tournament>>() {}, name);
            
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournaments:", e.getMessage());
            throw new ReadException("Error finding Tournaments by name: " + e.getMessage());
        }
        
        return tournaments;
    }

    @Override
    public List<Tournament> findTournamentByBestOf(String bestOf) throws ReadException {
        List<Tournament> tournaments = null;
        
        try{
            LOGGER.info("TournamentManage: Finding tournaments by format.");
            tournaments = tournamentClient.findTournamentByFormat_XML(new GenericType<List<Tournament>>() {}, bestOf);
            
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournaments:", e.getMessage());
            throw new ReadException("Error finding Tournaments by format: " + e.getMessage());
        }
        
        return tournaments;
    }

    @Override
    public List<Tournament> findTournamentByDate(String date) throws ReadException {
        List<Tournament> tournaments = null;
        
        try{
            LOGGER.info("TournamentManage: Finding tournaments by date.");
            tournaments = tournamentClient.findTournamentByDate_XML(new GenericType<List<Tournament>>() {}, date);
            
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournaments:", e.getMessage());
            throw new ReadException("Error finding Tournaments by date: " + e.getMessage());
        }
        
        return tournaments;
    }

    @Override
    public Tournament findTournamentByMatch() throws ReadException { //parameter match needed
        Tournament tournament = null;
        
        try{
            LOGGER.info("TournamentManage: Finding tournament.");
            tournament = tournamentClient.findTournamentById_XML(Tournament.class, tournament.toString()); //change tournament to match
            
            if(tournament!=null){
                LOGGER.log(Level.INFO, "TournamentManage: tournament id=", tournament.getIdTournament());
            }
            
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournament:", e.getMessage());
            throw new ReadException("Error finding the Tournament: " + e.getMessage());
        }
        
        return tournament;
    }
    
}
