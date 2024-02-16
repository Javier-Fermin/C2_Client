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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericType;
import model.Match;
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
    public void createTournament(Tournament tournament) throws CreateException, ConnectException, ProcessingException {
        try{
            tournamentClient.create_XML(tournament);
            LOGGER.info("TournamentManage: tournament created.");
        }catch(WebApplicationException e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception creating tournament:", e.getMessage());
            throw new CreateException("Error creating a Tournament: " + e.getMessage());
        }
    }

    @Override
    public void updateTournament(Tournament tournament) throws UpdateException, ConnectException, ProcessingException {
        try{
            tournamentClient.update_XML(tournament);
            LOGGER.info("TournamentManage: tournament updated.");
        }catch(WebApplicationException e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception updating tournament:", e.getMessage());
            throw new UpdateException("Error updating a Tournament: " + e.getMessage());
        }
    }

    @Override
    public void deleteTournament(Tournament tournament) throws DeleteException, ConnectException, ProcessingException {
        try{
            tournamentClient.delete(tournament.getIdTournament().toString());
            LOGGER.info("TournamentManage: tournament deleted.");
        }catch(WebApplicationException e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception deleting tournament:", e.getMessage());
            throw new DeleteException("Error deleting a Tournament: " + e.getMessage());
        }
    }

    @Override
    public Tournament findTournament(Integer id) throws ReadException, ConnectException, ProcessingException {
        Tournament tournament = null;
        
        try{
            LOGGER.info("TournamentManage: Finding tournament.");
            tournament = tournamentClient.findTournamentById_XML(Tournament.class, id.toString());
            
            if(tournament!=null){
                LOGGER.log(Level.INFO, "TournamentManage: tournament id=", tournament.getIdTournament());
            }
            
        }catch(WebApplicationException e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournament:", e.getMessage());
            throw new ReadException("Error finding the Tournament: " + e.getMessage());
        }
        
        return tournament;
    }

    @Override
    public List<Tournament> findAllTournaments() throws ReadException, ConnectException, ProcessingException {
        List<Tournament> tournaments = null;
        
        try{
            LOGGER.info("TournamentManage: Finding all tournaments.");
            tournaments = tournamentClient.findAllTournaments_XML(new GenericType<List<Tournament>>() {});
            
        }catch(WebApplicationException e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournaments:", e.getMessage());
            throw new ReadException("Error finding all Tournaments: " + e.getMessage());
        }
        
        return tournaments;
    }

    @Override
    public Tournament findTournamentByName(String name) throws ReadException, ConnectException, ProcessingException {
        Tournament tournament = null;
        
        try{
            LOGGER.info("TournamentManage: Finding tournaments by name.");
            tournament = tournamentClient.findTournamentByName_XML(Tournament.class, name);
            
        }catch(WebApplicationException e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournaments:", e.getMessage());
            throw new ReadException("Error finding Tournaments by name: " + e.getMessage());
        }
        
        return tournament;
    }

    @Override
    public List<Tournament> findTournamentByBestOf(String bestOf) throws ReadException, ConnectException, ProcessingException {
        List<Tournament> tournaments = null;
        
        try{
            LOGGER.info("TournamentManage: Finding tournaments by format.");
            tournaments = tournamentClient.findTournamentByFormat_XML(new GenericType<List<Tournament>>() {}, bestOf);
            
        }catch(WebApplicationException e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournaments:", e.getMessage());
            throw new ReadException("Error finding Tournaments by format: " + e.getMessage());
        }
        
        return tournaments;
    }

    @Override
    public List<Tournament> findTournamentByDate(String date) throws ReadException, ConnectException, ProcessingException {
        List<Tournament> tournaments = null;
        
        try{
            LOGGER.info("TournamentManage: Finding tournaments by date.");
            tournaments = tournamentClient.findTournamentByDate_XML(new GenericType<List<Tournament>>() {}, date);
            
        }catch(WebApplicationException e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournaments:", e.getMessage());
            throw new ReadException("Error finding Tournaments by date: " + e.getMessage());
        }
        
        return tournaments;
    }

    @Override
    public Tournament findTournamentByMatch(String matchDescription) throws ReadException, ConnectException, ProcessingException {
        Tournament tournament = null;
        
        
        try{
            MatchManager matchManager = MatchManagerFactory.getMatchManager();
            Match match = matchManager.findMatchByDescription(matchDescription);
            if(match == null){
                throw new Exception();
            }else{
                LOGGER.info("TournamentManage: Finding tournament.");
                tournament = tournamentClient.findTournamentByMatch_XML(Tournament.class, match);
            }
            
            if(tournament!=null){
                LOGGER.log(Level.INFO, "TournamentManage: tournament id=", tournament.getIdTournament());
            }
            
        }catch(WebApplicationException ex){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournament:", ex.getMessage());
            throw new ReadException("Error finding the Tournament: " + ex.getMessage());
        }catch(Exception e){
            LOGGER.log(Level.SEVERE, "TournamentManage: Exception finding tournament's match:", e.getMessage());
            throw new ReadException("Error finding the Tournament's match: " + e.getMessage());
        }
        
        return tournament;
    }
    
}
