package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.dtos.MatchDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.util.List;


public interface ITournamentController extends IRemoteController {

	/**
     * Search for all Tournaments.
     *
     * @return List<TournamentDTO> List of all Tournaments.
     * @throws RemoteException
     */
    List<TournamentDTO> searchAllTournaments()
    throws RemoteException;


    /**
     * Search for tournamentList by sport.
     *
     * @param sport Sport name (Department) to be searched.
     * @return List<TournamentDTO> List of all tournaments that are assigned to given sport, or null.
     * @throws RemoteException
     * @throws ValidationException
     */
    List<TournamentDTO> searchTournamentsBySport(String sport)
    throws RemoteException, ValidationException;


    /**
     * Search for all Tournaments.
     *
     * @param eventDate Date of the event to be searched.
     * @return List<TournamentDTO> List of all tournaments that are assigned to given event date, or null.
     * @throws RemoteException
     * @throws ValidationException
     */
    List<TournamentDTO> searchTournamentsByDate(String eventDate)
    throws RemoteException, ValidationException;


    /**
     * Search for all Tournaments.
     *
     * @param location Location to be searched.
     * @return List<TournamentDTO> List of all tournaments that are assigned to given location, or null.
     * @throws RemoteException
     * @throws ValidationException
     */
    List<TournamentDTO> searchTournamentsByLocation(String location)
    throws RemoteException, ValidationException;
    
    /**
     * Search for all tournament teams.
     *
     * @param tournamentId Id of of the target tournament (will be used for search).
     * @return List<TournamentDTO> List of all teams assigned to the given tournament.
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    List<String> searchAllTournamentTeams(Integer tournamentId)
    throws RemoteException, UnknownEntityException;

    /**
     * Search for all Matches.
     *
     * @param tournamentId Id of of the target tournament (will be used for search).
     * @return List<MatchDTO> List of all Matches.
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    List<MatchDTO> searchAllTournamentMatches(Integer tournamentId)
    throws RemoteException, UnknownEntityException;

	/**
     * Creates new or saves old tournament in data storage with data from the DTO.
     *
     * @param tournamentDTO DTO for tournament creation or save.
     * @return Integer Id of the updated or saved entity.
     * @throws RemoteException
     * @throws ValidationException
     */
    Integer createOrSaveTournament(TournamentDTO tournamentDTO)
    throws RemoteException, ValidationException;

    /**
     * Creates new match for a given tournament.
     *
     * @param tournamentId Id of a tournament for which given match will be created.
     * @param matchDTO DTO for match creation or save.
     * @throws RemoteException
     * @throws ValidationException
     * @throws UnknownEntityException
     */
    void createNewMatch(Integer tournamentId, MatchDTO matchDTO)
    throws RemoteException, ValidationException, UnknownEntityException;

	/**
     * Assigns given team by name to the given tournament.
     *
     * @param teamName Name of the team to be assigned to the given tournament.
     * @param tournamentId Id of a tournament to which given team will be assigned.
     * @throws RemoteException
     * @throws ValidationException
     * @throws UnknownEntityException
     */
	void assignTeamToTournament(String teamName, Integer tournamentId)
    throws RemoteException, ValidationException, UnknownEntityException;
}
