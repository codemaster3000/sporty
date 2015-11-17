package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.Tournament;
import at.sporty.team1.domain.interfaces.ITournament;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.rmi.api.IMemberController;
import at.sporty.team1.rmi.api.ITournamentController;
import at.sporty.team1.rmi.dtos.MatchDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
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
public class TournamentController extends UnicastRemoteObject implements ITournamentController{
    
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LogManager.getLogger();
    private static final Mapper MAPPER = new DozerBeanMapper();

    /**
     * constructor
     */
    public TournamentController() throws RemoteException{
    	super();
    }

    /**
     * addTeamToTournament()
     *
     * @param teamId
     * @param tournamentId
     */
    public void addTeamToTournament(String teamId, String tournamentId) {

        try {

            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentId);
            Team team = PersistenceFacade.getNewTeamDAO().findById(teamId);

            //TODO why u no want method addTeam?!
            PersistenceFacade.forceLoadLazyProperty(tournament, Tournament::getTeams);
            tournament.addTeam(team);

            PersistenceFacade.getNewTournamentDAO().saveOrUpdate(tournament);
        } catch (PersistenceException e) {
            LOGGER.error("An error occured while adding a team to a Tournament: ", e);
        }
    }

    /**
     * create a new match in a Tournament
     *
     * @param team1
     * @param team2
     * @param date
     * @param tournamentDTO
     */
    public void createNewMatch(String team1, String team2, String date, TournamentDTO tournamentDTO) {
        //TODO this is not yet finished + not reviewed!
        List<MatchDTO> matches = tournamentDTO.getMatches();

        MatchDTO newMatch = new MatchDTO();

        newMatch.setTime(date);
        newMatch.setTeam1(team1);
        newMatch.setTeam2(team2);

        matches.add(newMatch);

        try {


            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentDTO.getId());
            PersistenceFacade.getNewTournamentDAO().saveOrUpdate(tournament);
        } catch (PersistenceException e) {
            LOGGER.error("An Error occured during adding a new Match to the Tournament: ", e);
        }
    }

    /**
     * @return
     * @throws RemoteException
     * @throws UnknownEntityException 
     */
    public List<TournamentDTO> getAllTournaments() throws RemoteException, UnknownEntityException {

        try {

            List<Tournament> tournaments = PersistenceFacade.getNewGenericDAO(Tournament.class).findAll();
            if (tournaments == null) throw new UnknownEntityException(ITournament.class);


            //checking if there are an results
            if (tournaments == null || tournaments.isEmpty()) return null;

            //Converting all Tournaments to TournamentDTO
            return tournaments.stream()
                    .map(tournament -> MAPPER.map(tournament, TournamentDTO.class))
                    .collect(Collectors.toList());

        } catch (PersistenceException e) {
            LOGGER.error(
                    "An error occurs while getting all Tournaments ",
                    e
            );
            return null;
        }

    }

	@Override
	public List<String> getAllTournamentteams() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createOrSaveTournament(TournamentDTO tournamentDTO) throws RemoteException, ValidationException {
		// TODO Auto-generated method stub
		
	}
}
