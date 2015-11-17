package at.sporty.team1.application.controller;

import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.Tournament;
import at.sporty.team1.domain.Match;
import at.sporty.team1.domain.interfaces.ITeam;
import at.sporty.team1.domain.interfaces.ITournament;
import at.sporty.team1.persistence.PersistenceFacade;
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

    public TournamentController() throws RemoteException{
    	super();
    }

    @Override
    public List<TournamentDTO> searchAllTournaments()
    throws RemoteException, UnknownEntityException {

        try {

            List<Tournament> tournaments = PersistenceFacade.getNewGenericDAO(Tournament.class).findAll();
            if (tournaments == null) throw new UnknownEntityException(ITournament.class);


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

        //TODO this is not yet finished + not reviewed!
        List<MatchDTO> matches = tournamentDTO.getMatches();

        MatchDTO newMatch = new MatchDTO();

        newMatch.setTime(time);
        newMatch.setTeam1(team1);
        newMatch.setTeam2(team2);
        newMatch.setCourt(place);

        matches.add(newMatch);

        try {
            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentDTO.getId());
            PersistenceFacade.getNewTournamentDAO().saveOrUpdate(tournament);

            return
        } catch (PersistenceException e) {
            LOGGER.error("An Error occurs during adding a new Match to the Tournament: ", e);
            return null;
        }
    }

    @Override
    public void assignTeamToTournament(Integer tournamentId, Integer teamId)
    throws RemoteException, UnknownEntityException {

        if (tournamentId == null) throw new UnknownEntityException(ITournament.class);
        if (teamId == null) throw new UnknownEntityException(ITeam.class);

        try {

            Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentId);
            if (tournament == null) throw new UnknownEntityException(ITournament.class);

            Team team = PersistenceFacade.getNewTeamDAO().findById(teamId);
            if (team == null) throw new UnknownEntityException(ITeam.class);

            PersistenceFacade.forceLoadLazyProperty(tournament, Tournament::getTeams);
            tournament.addTeam(team);

            PersistenceFacade.getNewTournamentDAO().saveOrUpdate(tournament);
        } catch (PersistenceException e) {
            LOGGER.error("An error occurs while adding a team to a Tournament: ", e);
        }
    }




	@Override
	public void createOrSaveTournament(TournamentDTO tournamentDTO)
    throws RemoteException, ValidationException {
		// TODO Auto-generated method stub
		
	}

    @Override
    public void createNewMatch(Integer tournamentId, String team1, String team2, String time, String location)
    throws RemoteException, UnknownEntityException {

        if (tournamentId == null) throw new UnknownEntityException(ITournament.class);


        Tournament tournament = PersistenceFacade.getNewTournamentDAO().findById(tournamentId);

        Match match = new Match();

//        match.setTime(time); //TODO + time converter
        match.setTeam1(team1);
        match.setTeam2(team2);
        match.setLocation(location);

        try {


            PersistenceFacade.getNewTournamentDAO().saveOrUpdate(tournament);

        } catch (PersistenceException e) {
            LOGGER.error("An Error occurs during adding a new Match to the Tournament: ", e);
        }
    }
}
