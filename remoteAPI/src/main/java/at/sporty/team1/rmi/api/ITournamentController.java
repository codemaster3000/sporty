package at.sporty.team1.rmi.api;

import at.sporty.team1.rmi.dtos.MatchDTO;
import at.sporty.team1.rmi.dtos.SessionDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.NotAuthorisedException;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.util.List;


public interface ITournamentController extends IRemoteController {

	/**
     * Search for all Tournaments.
     *
     * @param session Session object.
     * @return List<TournamentDTO> List of all Tournaments.
     * @throws RemoteException
     */
    List<TournamentDTO> searchAllTournaments(SessionDTO session)
    throws RemoteException, NotAuthorisedException;


    /**
     * Search for tournamentList by sport.
     *
     * @param sport Sport name (Department) to be searched.
     * @param session Session object.
     * @return List<TournamentDTO> List of all tournaments that are assigned to given sport, or null.
     * @throws RemoteException
     * @throws ValidationException
     */
    List<TournamentDTO> searchTournamentsBySport(String sport, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException;


    /**
     * Search for all Tournaments.
     *
     * @param eventDate Date of the event to be searched.
     * @param session Session object.
     * @return List<TournamentDTO> List of all tournaments that are assigned to given event date, or null.
     * @throws RemoteException
     * @throws ValidationException
     */
    List<TournamentDTO> searchTournamentsByDate(String eventDate, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException;


    /**
     * Search for all Tournaments.
     *
     * @param location Location to be searched.
     * @param session Session object.
     * @return List<TournamentDTO> List of all tournaments that are assigned to given location, or null.
     * @throws RemoteException
     * @throws ValidationException
     */
    List<TournamentDTO> searchTournamentsByLocation(String location, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException;
    
    /**
     * Search for all tournament teams.
     *
     * @param tournamentId Id of of the target tournament (will be used for search).
     * @param session Session object.
     * @return List<TournamentDTO> List of all teams assigned to the given tournament.
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    List<String> searchAllTournamentTeams(Integer tournamentId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException;

    /**
     * Search for all Matches.
     *
     * @param tournamentId Id of of the target tournament (will be used for search).
     * @param session Session object.
     * @return List<MatchDTO> List of all Matches.
     * @throws RemoteException
     * @throws UnknownEntityException
     */
    List<MatchDTO> searchAllTournamentMatches(Integer tournamentId, SessionDTO session)
    throws RemoteException, UnknownEntityException, NotAuthorisedException;

	/**
     * Creates new or saves old tournament in data storage with data from the DTO.
     *
     * @param tournamentDTO DTO for tournament creation or save.
     * @param session Session object.
     * @return Integer Id of the updated or saved entity.
     * @throws RemoteException
     * @throws ValidationException
     */
    Integer createOrSaveTournament(TournamentDTO tournamentDTO, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException;

    /**
     * Creates new match for a given tournament.
     *
     * @param tournamentId Id of a tournament for which given match will be created.
     * @param matchDTO DTO for match creation or save.
     * @param session Session object.
     * @throws RemoteException
     * @throws ValidationException
     * @throws UnknownEntityException
     */
    void createNewMatch(Integer tournamentId, MatchDTO matchDTO, SessionDTO session)
    throws RemoteException, ValidationException, UnknownEntityException, NotAuthorisedException;

	/**
     * Assigns given team by name to the given tournament.
     *
     * @param teamName Name of the team to be assigned to the given tournament.
     * @param tournamentId Id of a tournament to which given team will be assigned.
     * @param session Session object.
     * @throws RemoteException
     * @throws ValidationException
     * @throws UnknownEntityException
     */
	void assignTeamToTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws RemoteException, ValidationException, UnknownEntityException, NotAuthorisedException;

    /**
     * Removes given team by name to the given tournament.
     *
     * @param teamName Name of the team to be removed from the given tournament.
     * @param tournamentId Id of a tournament from which given team will be removed.
     * @param session Session object.
     * @throws RemoteException
     * @throws ValidationException
     * @throws UnknownEntityException
     */
    void removeTeamFromTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws RemoteException, ValidationException, UnknownEntityException, NotAuthorisedException;
}
