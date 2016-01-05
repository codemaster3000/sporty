package at.sporty.team1.application.controller.real;

import at.sporty.team1.application.auth.AccessPolicy;
import at.sporty.team1.application.auth.BasicAccessPolicies;
import at.sporty.team1.application.controller.real.api.ITournamentController;
import at.sporty.team1.domain.Match;
import at.sporty.team1.domain.Tournament;
import at.sporty.team1.domain.interfaces.IMatch;
import at.sporty.team1.domain.interfaces.ITournament;
import at.sporty.team1.misc.InputSanitizer;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.shared.dtos.MatchDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.enums.DataType;
import at.sporty.team1.shared.enums.UserRole;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import javax.persistence.PersistenceException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TournamentController represents the logic controller for a tournament
 */
public class TournamentController implements ITournamentController {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger();
    private static final Mapper MAPPER = new DozerBeanMapper();

    public TournamentController() {
    }

    @Override
    public boolean isAbleToPerformChanges(TournamentDTO tournamentDTO, SessionDTO session) {

        //1 STEP
        if (tournamentDTO == null || tournamentDTO.getDepartment() == null) return false;

        //2 STEP
        return LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),

                AccessPolicy.and(
                    BasicAccessPolicies.isInPermissionBound(UserRole.DEPARTMENT_HEAD),

                    AccessPolicy.or(
                        //create new tournament (new tournaments doesn't have id))
                        AccessPolicy.simplePolicy(user -> tournamentDTO.getTournamentId() == null),
                        BasicAccessPolicies.isDepartmentHead(tournamentDTO.getDepartment().getDepartmentId())
                    )
                )
            )
        );
    }

    @Override
    public TournamentDTO findTournamentById(Integer tournamentId)
    throws UnknownEntityException {

        /* Validating Input */
        if (tournamentId == null) throw new UnknownEntityException(ITournament.class);

        /* Is valid, moving forward */
        try {

            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentId);
            if (tournament == null) throw new UnknownEntityException(ITournament.class);

            //converting tournament to tournament DTO
            return MAPPER.map(tournament, TournamentDTO.class);

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching for Match #{}.", tournamentId, e);
            return null;
        }
    }

    @Override
    public List<TournamentDTO> searchAllTournaments() {

        try {
            /* pulling a TournamentDAO and getting all Tournaments */
            List<Tournament> tournaments = PersistenceFacade.getNewTournamentDAO().findAll();

            //checking if there are any results
            if (tournaments.isEmpty()) return null;

            //Converting all Tournaments to TournamentDTO
            return tournaments.stream()
                    .map(tournament -> MAPPER.map(tournament, TournamentDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while getting all Tournaments ", e);
            return null;
        }
    }

    @Override
    public List<TournamentDTO> searchTournamentsBySport(String sport)
    throws ValidationException {

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(sport, DataType.TEXT)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            List<? extends ITournament> rawResults = PersistenceFacade.getNewTournamentDAO().findBySport(sport);

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to TournamentDTO
            return rawResults.stream()
                    .map(tournament -> MAPPER.map(tournament, TournamentDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching Tournaments by Sport \"{}\".", sport, e);
            return null;
        }
    }

    @Override
    public List<TournamentDTO> searchTournamentsByDate(String eventDate)
    throws ValidationException {

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(eventDate, DataType.SQL_DATE)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            List<? extends ITournament> rawResults = PersistenceFacade.getNewTournamentDAO().findByEventDate(eventDate);

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to TournamentDTO
            return rawResults.stream()
                    .map(tournament -> MAPPER.map(tournament, TournamentDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching Tournaments by Event Date \"{}\".", eventDate, e);
            return null;
        }
    }

    @Override
    public List<TournamentDTO> searchTournamentsByLocation(String location)
    throws ValidationException {

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (!inputSanitizer.isValid(location, DataType.TEXT)) {
            throw inputSanitizer.getPreparedValidationException();
        }

        /* Is valid, moving forward */
        try {

            List<? extends ITournament> rawResults = PersistenceFacade.getNewTournamentDAO().findByLocation(location);

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to TournamentDTO
            return rawResults.stream()
                    .map(tournament -> MAPPER.map(tournament, TournamentDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching Tournaments by Location \"{}\".", location, e);
            return null;
        }
    }

    @Override
    public List<String> searchAllTournamentTeams(Integer tournamentId)
    throws UnknownEntityException {

        if (tournamentId == null) throw new UnknownEntityException(ITournament.class);

        /* Is valid, moving forward */
        try {

            /* pulling a TournamentDAO and getting tournament by id */
            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentId);
            if (tournament == null) throw new UnknownEntityException(ITournament.class);

            PersistenceFacade.forceLoadLazyProperty(tournament, Tournament::getTeams);
            return new LinkedList<>(tournament.getTeams());

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while getting all teams by Tournament #{}.",
                tournamentId,
                e
            );
            return null;
        }
    }

    @Override
    public List<MatchDTO> searchAllTournamentMatches(Integer tournamentId)
    throws UnknownEntityException {

        if (tournamentId == null) throw new UnknownEntityException(ITournament.class);

        /* Is valid, moving forward */
        try {

            /* pulling a TournamentDAO and getting tournament by id */
            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentId);
            if (tournament == null) throw new UnknownEntityException(ITournament.class);

            PersistenceFacade.forceLoadLazyProperty(tournament, Tournament::getMatches);
            List<? extends IMatch> rawResults = tournament.getMatches();

            //checking if there are any results
            if (rawResults == null || rawResults.isEmpty()) return null;

            //Converting results to TeamDTO
            return rawResults.stream()
                    .map(match -> MAPPER.map(match, MatchDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while getting all matches for Tournament #{}.",
                tournamentId,
                e
            );
            return null;
        }
    }

    @Override
    public Integer createOrSaveTournament(TournamentDTO tournamentDTO, SessionDTO session)
    throws ValidationException, NotAuthorisedException {

        /* Checking access permissions */
        if (!isAbleToPerformChanges(tournamentDTO, session)) throw new NotAuthorisedException();

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

            Tournament tournament = MAPPER.map(tournamentDTO, Tournament.class);

            /* pulling a TournamentDAO and save the Tournament */
            PersistenceFacade.getNewTournamentDAO().saveOrUpdate(tournament);

            LOGGER.info("Tournament for \"{}\" was successfully saved.", tournamentDTO.getDate());
            return tournament.getTournamentId();

        } catch (PersistenceException e) {
            LOGGER.error("Error occurred while communicating with DB.", e);
            return null;
        }
    }

    @Override
    public MatchDTO findMatchById(Integer matchId)
    throws UnknownEntityException {

        /* Validating Input */
        if (matchId == null) throw new UnknownEntityException(IMatch.class);

        /* Is valid, moving forward */
        try {

            Match match = PersistenceFacade.getNewGenericDAO(Match.class).findById(matchId);
            if (match == null) throw new UnknownEntityException(IMatch.class);

            //converting match to match DTO
            return MAPPER.map(match, MatchDTO.class);

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred while searching for Match #{}.", matchId, e);
            return null;
        }
    }

    @Override
    public Integer createOrSaveMatch(Integer tournamentId, MatchDTO matchDTO, SessionDTO session)
    throws ValidationException, UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        //1 STEP
        if (matchDTO == null) throw new NotAuthorisedException();

        //2 STEP
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),

                AccessPolicy.and(
                    BasicAccessPolicies.isInPermissionBound(UserRole.DEPARTMENT_HEAD),

                    AccessPolicy.or(
                        //create new match (new matches doesn't have id))
                        AccessPolicy.simplePolicy(user -> matchDTO.getMatchId() == null),
                        BasicAccessPolicies.isDepartmentHeadOfTournament(tournamentId)
                    )
                )
            )
        )) throw new NotAuthorisedException();

        /* Validating Input */
        InputSanitizer inputSanitizer = new InputSanitizer();
        if (
            !inputSanitizer.isValid(matchDTO.getLocation(), DataType.TEXT) ||
            !inputSanitizer.isValid(matchDTO.getReferee(), DataType.TEXT) ||
            !inputSanitizer.isValid(matchDTO.getTeam1(), DataType.TEXT) ||
            !inputSanitizer.isValid(matchDTO.getTeam2(), DataType.TEXT) ||
            !inputSanitizer.isValid(matchDTO.getResultTeam1(), DataType.TEXT) ||
            !inputSanitizer.isValid(matchDTO.getResultTeam2(), DataType.TEXT) ||
            !inputSanitizer.isValid(matchDTO.getDate(), DataType.TEXT)
        ) {
            throw inputSanitizer.getPreparedValidationException();
        }

        if (tournamentId == null) throw new UnknownEntityException(ITournament.class);

        /* Is valid, moving forward */
        try {

            /* pulling a TournamentDAO and save the Tournament */
            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentId);
            if (tournament == null) throw new UnknownEntityException(ITournament.class);

            IMatch match = MAPPER.map(matchDTO, Match.class);

            PersistenceFacade.forceLoadLazyProperty(tournament, Tournament::getMatches);
            tournament.addMatch(match);

            PersistenceFacade.getNewTournamentDAO().saveOrUpdate(tournament);

            return match.getMatchId();

        } catch (PersistenceException e) {
            LOGGER.error("An error occurred during adding a new Match to the Tournament: ", e);
            return null;
        }
    }

    @Override
    public void assignTeamToTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws UnknownEntityException, ValidationException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isTrainerOfTournament(tournamentId),
                BasicAccessPolicies.isDepartmentHeadOfTournament(tournamentId)
            )
        )) throw new NotAuthorisedException();

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
                "An error occurred while adding Team \"{}\" to the Tournament #{}",
                teamName,
                tournamentId,
                e
            );
        }
    }

    @Override
    public void removeTeamFromTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws ValidationException, UnknownEntityException, NotAuthorisedException {

        /* Checking access permissions */
        if (!LoginController.hasEnoughPermissions(
            session,
            AccessPolicy.or(
                BasicAccessPolicies.isInPermissionBound(UserRole.ADMIN),
                BasicAccessPolicies.isTrainerOfTournament(tournamentId),
                BasicAccessPolicies.isDepartmentHeadOfTournament(tournamentId)
            )
        )) throw new NotAuthorisedException();

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
            tournament.removeTeam(teamName);

            PersistenceFacade.getNewTournamentDAO().saveOrUpdate(tournament);
        } catch (PersistenceException e) {
            LOGGER.error(
                "An error occurred while removing Team \"{}\" from the Tournament #{}",
                teamName,
                tournamentId,
                e
            );
        }
    }
}
