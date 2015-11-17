package at.sporty.team1.rmi.api;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;


public interface ITournamentController extends Remote, Serializable{

	/**
     * Search for all Tournaments.
     *
     * @return List<TournamentDTO> List of all Tournaments
     * @throws RemoteException
     */
    List<TournamentDTO> getAllTournaments()
    throws RemoteException;
    
    /**
     * Search for all Tournaments.
     *
     * @return List<TournamentDTO> List of all Tournaments
     * @throws RemoteException
     */
    List<String> getAllTournamentteams()
    throws RemoteException;
	
	/**
     * Creates new or saves old tournament in data storage with data from the DTO.
     *
     * @param tournamentDTO DTO for tournament creation or save
     * @throws RemoteException
     * @throws ValidationException
     */
    void createOrSaveTournament(TournamentDTO tournamentDTO)
    throws RemoteException, ValidationException;
	
}
