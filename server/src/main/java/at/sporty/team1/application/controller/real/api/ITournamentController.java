package at.sporty.team1.application.controller.real.api;

import at.sporty.team1.shared.dtos.MatchDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.util.List;

/**
 * Created by sereGkaluv on 27-Nov-15.
 */
public interface ITournamentController extends IController {

	/**
     * Search for all Tournaments.
     *
     * @return List<TournamentDTO> List of all Tournaments.
     */
    List<TournamentDTO> searchAllTournaments();

    /**
     * Search for tournamentList by sport.
     *
     * @param sport Sport name (Department) to be searched.
     * @return List<TournamentDTO> List of all tournaments that are assigned to given sport, or null.
     * @throws ValidationException
     */
    List<TournamentDTO> searchTournamentsBySport(String sport)
    throws ValidationException;

    /**
     * Search for all Tournaments.
     *
     * @param eventDate Date of the event to be searched.
     * @return List<TournamentDTO> List of all tournaments that are assigned to given event date, or null.
     * @throws ValidationException
     */
    List<TournamentDTO> searchTournamentsByDate(String eventDate)
    throws ValidationException;

    /**
     * Search for all Tournaments.
     *
     * @param location Location to be searched.
     * @return List<TournamentDTO> List of all tournaments that are assigned to given location, or null.
     * @throws ValidationException
     */
    List<TournamentDTO> searchTournamentsByLocation(String location)
    throws ValidationException;
    
    /**
     * Search for all tournament teams.
     *
     * @param tournamentId Id of of the target tournament (will be used for search).
     * @return List<TournamentDTO> List of all teams assigned to the given tournament.
     * @throws UnknownEntityException
     */
    List<String> searchAllTournamentTeams(Integer tournamentId)
    throws UnknownEntityException;

    /**
     * Search for all Matches.
     *
     * @param tournamentId Id of of the target tournament (will be used for search).
     * @return List<MatchDTO> List of all Matches.
     * @throws UnknownEntityException
     */
    List<MatchDTO> searchAllTournamentMatches(Integer tournamentId)
    throws UnknownEntityException;

	/**
     * Creates new or saves old tournament in data storage with data from the DTO.
     *
     * @param tournamentDTO DTO for tournament creation or save.
     * @param session Session object.
     * @return Integer Id of the updated or saved entity.
     * @throws ValidationException
     * @throws NotAuthorisedException
     */
    Integer createOrSaveTournament(TournamentDTO tournamentDTO, SessionDTO session)
    throws ValidationException, NotAuthorisedException;

    /**
     * Search for Match with a given id.
     *
     * @param matchId target match (will be used for search).
     * @return MatchDTO searched match.
     * @throws UnknownEntityException
     */
    MatchDTO findMatchById(Integer matchId)
    throws UnknownEntityException;

    /**
     * Creates new match for a given tournament.
     *
     * @param tournamentId Id of a tournament for which given match will be created.
     * @param matchDTO DTO for match creation or save.
     * @param session Session object.
     * @return Integer Id of the updated or saved entity.
     * @throws ValidationException
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    Integer createNewMatch(Integer tournamentId, MatchDTO matchDTO, SessionDTO session)
    throws ValidationException, UnknownEntityException, NotAuthorisedException;

	/**
     * Assigns given team by name to the given tournament.
     *
     * @param teamName Name of the team to be assigned to the given tournament.
     * @param tournamentId Id of a tournament to which given team will be assigned.
     * @param session Session object.
     * @throws ValidationException
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
	void assignTeamToTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws ValidationException, UnknownEntityException, NotAuthorisedException;

    /**
     * Removes given team by name to the given tournament.
     *
     * @param teamName Name of the team to be removed from the given tournament.
     * @param tournamentId Id of a tournament from which given team will be removed.
     * @param session Session object.
     * @throws ValidationException
     * @throws UnknownEntityException
     * @throws NotAuthorisedException
     */
    void removeTeamFromTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws ValidationException, UnknownEntityException, NotAuthorisedException;
}
