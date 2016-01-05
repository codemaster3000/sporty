package at.sporty.team1.communication.facades.ejb.adapters;

import at.sporty.team1.communication.facades.api.ITournamentControllerUniversal;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.shared.api.ejb.ITournamentControllerEJB;
import at.sporty.team1.shared.dtos.MatchDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.util.List;

public class TournamentControllerEJBAdapter implements ITournamentControllerUniversal {

	private final ITournamentControllerEJB _iTournamentControllerEJB;
	
	public TournamentControllerEJBAdapter(ITournamentControllerEJB iTournamentControllerEJB) {
		_iTournamentControllerEJB = iTournamentControllerEJB;
	}

	@Override
	public List<TournamentDTO> searchAllTournaments()
	throws RemoteCommunicationException {
		return _iTournamentControllerEJB.searchAllTournaments();
	}

	@Override
	public List<TournamentDTO> searchTournamentsBySport(String sport)
	throws RemoteCommunicationException, ValidationException {
		return _iTournamentControllerEJB.searchTournamentsBySport(sport);
	}

	@Override
	public List<TournamentDTO> searchTournamentsByDate(String eventDate)
	throws RemoteCommunicationException, ValidationException {
		return _iTournamentControllerEJB.searchTournamentsByDate(eventDate);
	}

	@Override
	public List<TournamentDTO> searchTournamentsByLocation(String location)
	throws RemoteCommunicationException, ValidationException {
		return _iTournamentControllerEJB.searchTournamentsByLocation(location);
	}

	@Override
	public List<String> searchAllTournamentTeams(Integer tournamentId)
	throws RemoteCommunicationException, UnknownEntityException {
		return _iTournamentControllerEJB.searchAllTournamentTeams(tournamentId);
	}

	@Override
	public List<MatchDTO> searchAllTournamentMatches(Integer tournamentId)
	throws RemoteCommunicationException, UnknownEntityException {
		return _iTournamentControllerEJB.searchAllTournamentMatches(tournamentId);
	}

	@Override
	public Integer createOrSaveTournament(TournamentDTO tournamentDTO, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		return _iTournamentControllerEJB.createOrSaveTournament(tournamentDTO, session);
	}

	@Override
	public MatchDTO findMatchById(Integer matchId)
	throws RemoteCommunicationException, UnknownEntityException {
		return _iTournamentControllerEJB.findMatchById(matchId);
	}

	@Override
	public Integer createOrSaveMatch(Integer tournamentId, MatchDTO matchDTO, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, UnknownEntityException, NotAuthorisedException {
		return _iTournamentControllerEJB.createOrSaveMatch(tournamentId, matchDTO, session);
	}

	@Override
	public void assignTeamToTournament(String teamName, Integer tournamentId, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, UnknownEntityException, NotAuthorisedException {
		_iTournamentControllerEJB.assignTeamToTournament(teamName, tournamentId, session);
	}

	@Override
	public void removeTeamFromTournament(String teamName, Integer tournamentId, SessionDTO session)
	throws RemoteCommunicationException, ValidationException, UnknownEntityException, NotAuthorisedException {
		_iTournamentControllerEJB.removeTeamFromTournament(teamName, tournamentId, session);
	}
}
