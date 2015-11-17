package at.sporty.team1.rmi.api;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;


public interface ITournamentController extends Remote, Serializable {

	/**
     * Search for all Tournaments.
     *
     * @return List<TournamentDTO> List of all Tournaments
     * @throws RemoteException
	 * @throws UnknownEntityException 
     */
    List<TournamentDTO> searchAllTournaments()
    throws RemoteException, UnknownEntityException;
    
    /**
     * Search for all Tournaments.
     *
     * @return List<TournamentDTO> List of all Tournaments
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    List<String> searchAllTournamentTeams(Integer tournamentId)
    throws RemoteException, UnknownEntityException;
	
	/**
     * Creates new or saves old tournament in data storage with data from the DTO.
     *
     * @param tournamentDTO DTO for tournament creation or save
     * @throws RemoteException
     * @throws ValidationException
     */
    void createOrSaveTournament(TournamentDTO tournamentDTO)
    throws RemoteException, ValidationException;

    /**
     * create a new match in a Tournament
     *
     * @param tournamentId
     * @param team1
     * @param team2
     * @param time
     * @param location
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    void createNewMatch(Integer tournamentId, String team1, String team2, String time, String location)
    throws RemoteException, UnknownEntityException;

	/**
     * assignTeamToTournament()
     *
     * @param teamName
     * @param tournamentId
     * @throws RemoteException
     * @throws UnknownEntityException
     */
	void assignTeamToTournament(String teamName, Integer tournamentId)
    throws RemoteException, UnknownEntityException;
}
