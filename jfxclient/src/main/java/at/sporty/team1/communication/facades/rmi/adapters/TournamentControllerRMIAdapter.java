package at.sporty.team1.communication.facades.rmi.adapters;

import at.sporty.team1.communication.facades.api.ITournamentControllerUniversal;
import at.sporty.team1.shared.exceptions.RemoteCommunicationException;
import at.sporty.team1.shared.api.rmi.ITournamentControllerRMI;
import at.sporty.team1.shared.dtos.MatchDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.util.List;

public class TournamentControllerRMIAdapter implements ITournamentControllerUniversal {

	private final ITournamentControllerRMI _iTournamentControllerRMI;
	
	public TournamentControllerRMIAdapter(ITournamentControllerRMI iTournamentControllerRMI) {
		_iTournamentControllerRMI = iTournamentControllerRMI;
	}
	
	@Override
	public List<TournamentDTO> searchAllTournaments()
	throws RemoteCommunicationException {
		try {
			return _iTournamentControllerRMI.searchAllTournaments();
		} catch (RemoteException e) {		
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<TournamentDTO> searchTournamentsBySport(String sport)
    throws RemoteCommunicationException, ValidationException {
		try {
			return _iTournamentControllerRMI.searchTournamentsBySport(sport);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<TournamentDTO> searchTournamentsByDate(String eventDate)
    throws RemoteCommunicationException, ValidationException {
		try {
			return _iTournamentControllerRMI.searchTournamentsByDate(eventDate);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<TournamentDTO> searchTournamentsByLocation(String location)
    throws RemoteCommunicationException, ValidationException {
		try {
			return _iTournamentControllerRMI.searchTournamentsByLocation(location);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<String> searchAllTournamentTeams(Integer tournamentId)
    throws RemoteCommunicationException, UnknownEntityException {
		try {
			return _iTournamentControllerRMI.searchAllTournamentTeams(tournamentId);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public List<MatchDTO> searchAllTournamentMatches(Integer tournamentId)
    throws RemoteCommunicationException, UnknownEntityException {
		try {
			return _iTournamentControllerRMI.searchAllTournamentMatches(tournamentId);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public Integer createOrSaveTournament(TournamentDTO tournamentDTO, SessionDTO session)
    throws RemoteCommunicationException, ValidationException, NotAuthorisedException {
		try {
			return _iTournamentControllerRMI.createOrSaveTournament(tournamentDTO, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public Integer createNewMatch(Integer tournamentId, MatchDTO matchDTO, SessionDTO session)
    throws RemoteCommunicationException, ValidationException, UnknownEntityException, NotAuthorisedException {
		try {
			return _iTournamentControllerRMI.createNewMatch(tournamentId, matchDTO, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public void assignTeamToTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws RemoteCommunicationException, ValidationException, UnknownEntityException, NotAuthorisedException {
		try {
			_iTournamentControllerRMI.assignTeamToTournament(teamName, tournamentId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

	@Override
	public void removeTeamFromTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws RemoteCommunicationException, ValidationException, UnknownEntityException, NotAuthorisedException {
		try {
			_iTournamentControllerRMI.removeTeamFromTournament(teamName, tournamentId, session);
		} catch (RemoteException e) {
			throw new RemoteCommunicationException(e);
		}
	}

}
