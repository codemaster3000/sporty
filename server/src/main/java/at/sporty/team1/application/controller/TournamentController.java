package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Match;
import at.sporty.team1.domain.Tournament;
import at.sporty.team1.domain.interfaces.IMatch;
import at.sporty.team1.domain.interfaces.ITournament;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.ITournamentController;
import at.sporty.team1.rmi.dtos.MatchDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.DataType;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.persistence.PersistenceException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TournamentController represents the logic controller for a tournament
 */
public class TournamentController extends UnicastRemoteObject implements ITournamentController {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger();
    private static final Mapper MAPPER = new DozerBeanMapper();

    public TournamentController() throws RemoteException{
    	super();
    }

    @Override
    public List<TournamentDTO> searchAllTournaments()
    throws RemoteException {

        try {

            /* pulling a TournamentDAO and getting all Tournaments */
            List<Tournament> tournaments = PersistenceFacade.getNewGenericDAO(Tournament.class).findAll();

            //checking if there are an results
            if (tournaments.isEmpty()) return null;

            //Converting all Tournaments to TournamentDTO
            return tournaments.stream()
                    .map(tournament -> MAPPER.map(tournament, TournamentDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurs while getting all Tournaments ", e);
            return null;
        }
    }

    @Override
    public List<String> searchAllTournamentTeams(Integer tournamentId)
    throws RemoteException, UnknownEntityException {

        if (tournamentId == null) throw new UnknownEntityException(ITournament.class);

        /* Is valid, moving forward */
        try {

            /* pulling a TournamentDAO and getting tournament by id */
            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentId);
            if (tournament == null) throw new UnknownEntityException(ITournament.class);

            PersistenceFacade.forceLoadLazyProperty(tournament, Tournament::getTeams);
            return tournament.getTeams();

        } catch (PersistenceException e) {
            LOGGER.error(
                "An Error occurs while getting all teams by Tournament #{}.",
                tournamentId,
                e
            );
            return null;
        }
    }

    @Override
    public List<MatchDTO> searchAllTournamentMatches(Integer tournamentId)
    throws RemoteException, UnknownEntityException {

        //TODO Match entity and MatchDTO

        if (tournamentId == null) throw new UnknownEntityException(ITournament.class);

        /* Is valid, moving forward */
        try {

            /* pulling a TournamentDAO and getting tournament by id */
            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentId);
            if (tournament == null) throw new UnknownEntityException(ITournament.class);

            List<Match> rawResults = PersistenceFacade.getNewMatchDAO().findByTournament(tournament);

            //checking if there are an results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to TeamDTO
            return rawResults.stream()
                    .map(match -> MAPPER.map(match, MatchDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error(
                "An Error occurs while getting all matches for Tournament #{}.",
                tournamentId,
                e
            );
            return null;
        }
    }

    @Override
    public void assignTeamToTournament(String teamName, Integer tournamentId)
    throws RemoteException, UnknownEntityException, ValidationException {

        /* Validating teamName */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(teamName, DataType.TEXT)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        if (tournamentId == null) throw new UnknownEntityException(ITournament.class);

        /* Is valid, moving forward */
        try {

            /* pulling a TournamentDAO and update Tournament */
            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentId);
            if (tournament == null) throw new UnknownEntityException(ITournament.class);

            PersistenceFacade.forceLoadLazyProperty(tournament, Tournament::getTeams);
            tournament.addTeam(teamName);

            PersistenceFacade.getNewTournamentDAO().saveOrUpdate(tournament);
        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurs while adding a Team \"{}\" to a Tournament #{}",
                teamName,
                tournamentId,
                e
            );
        }
    }

	@Override
	public void createOrSaveTournament(TournamentDTO tournamentDTO)
    throws RemoteException, ValidationException {

        if (tournamentDTO == null) return;

		 /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (
            !inputSanitizer.isValid(tournamentDTO.getLocation(), DataType.TEXT) ||
            !inputSanitizer.isValid(tournamentDTO.getDate(), DataType.SQL_DATE)
        ) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            /* pulling a MemberDAO and save the Member */
            PersistenceFacade.getNewTournamentDAO().saveOrUpdate(
                MAPPER.map(tournamentDTO, Tournament.class)
            );

            LOGGER.info("Tournament for \"{}\" was successfully saved.", tournamentDTO.getDate());

        } catch (PersistenceException e) {
            LOGGER.error("Error occurs while communicating with DB.", e);
        }
	}

    @Override
    public void createNewMatch(Integer tournamentId, MatchDTO matchDTO)
    throws RemoteException, ValidationException, UnknownEntityException {

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (
            !inputSanitizer.isValid(matchDTO.getLocation(), DataType.TEXT) ||
            !inputSanitizer.isValid(matchDTO.getReferee(), DataType.TEXT) ||
            !inputSanitizer.isValid(matchDTO.getResult(), DataType.TEXT) ||
            !inputSanitizer.isValid(matchDTO.getTeam1(), DataType.TEXT) ||
            !inputSanitizer.isValid(matchDTO.getTeam2(), DataType.TEXT) ||
            !inputSanitizer.isValid(matchDTO.getTime(), DataType.TEXT)
        ) {
            throw inputSanitizer.getPreparedValidationException();
        }

        if (tournamentId == null) throw new UnknownEntityException(ITournament.class);

        /* Is valid, moving forward */
        try {

            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentId);
            if (tournament == null) throw new UnknownEntityException(ITournament.class);

            /* pulling a MemberDAO and save the Member */
            IMatch match = MAPPER.map(matchDTO, Match.class);

            //TODO uncomment when Matches will be bind in Tournament
//            PersistenceFacade.forceLoadLazyProperty(tournament, Tournament::getMatches);
//            tournament.addMatch(match);

            PersistenceFacade.getNewTournamentDAO().saveOrUpdate(tournament);

        } catch (PersistenceException e) {
            LOGGER.error("An Error occurs during adding a new Match to the Tournament: ", e);
        }
    }
}
