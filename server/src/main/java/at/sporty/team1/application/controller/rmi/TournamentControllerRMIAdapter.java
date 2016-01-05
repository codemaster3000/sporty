package at.sporty.team1.application.controller.rmi;

import at.sporty.team1.application.controller.real.TournamentController;
import at.sporty.team1.application.controller.real.api.ITournamentController;
import at.sporty.team1.application.controller.util.RemoteObject;
import at.sporty.team1.shared.api.rmi.ITournamentControllerRMI;
import at.sporty.team1.shared.dtos.MatchDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Created by sereGkaluv on 10-Dec-15.
 */
@RemoteObject(name = "TOURNAMENT_CONTROLLER_RMI")
public class TournamentControllerRMIAdapter extends UnicastRemoteObject implements ITournamentControllerRMI {
    private static final long serialVersionUID = 1L;
    private final ITournamentController _controller;

    public TournamentControllerRMIAdapter()
    throws RemoteException {
        super();

        _controller = new TournamentController();
    }

    @Override
    public boolean isAbleToPerformChanges(TournamentDTO tournamentDTO, SessionDTO session)
    throws RemoteException {

        return _controller.isAbleToPerformChanges(tournamentDTO, session);
    }

    @Override
    public TournamentDTO findTournamentById(Integer tournamentId)
    throws RemoteException, UnknownEntityException {

        return _controller.findTournamentById(tournamentId);
    }

    @Override
    public List<TournamentDTO> searchAllTournaments()
    throws RemoteException {

        return _controller.searchAllTournaments();
    }

    @Override
    public List<TournamentDTO> searchTournamentsBySport(String sport)
    throws RemoteException, ValidationException {

        return _controller.searchTournamentsBySport(sport);
    }

    @Override
    public List<TournamentDTO> searchTournamentsByDate(String eventDate)
    throws RemoteException, ValidationException {

        return _controller.searchTournamentsByDate(eventDate);
    }

    @Override
    public List<TournamentDTO> searchTournamentsByLocation(String location)
    throws RemoteException, ValidationException {

        return _controller.searchTournamentsByLocation(location);
    }

    @Override
    public List<String> searchAllTournamentTeams(Integer tournamentId)
    throws RemoteException, UnknownEntityException {

        return _controller.searchAllTournamentTeams(tournamentId);
    }

    @Override
    public List<MatchDTO> searchAllTournamentMatches(Integer tournamentId)
    throws RemoteException, UnknownEntityException {

        return _controller.searchAllTournamentMatches(tournamentId);
    }

    @Override
    public Integer createOrSaveTournament(TournamentDTO tournamentDTO, SessionDTO session)
    throws RemoteException, ValidationException, NotAuthorisedException {

        return _controller.createOrSaveTournament(tournamentDTO, session);
    }

    @Override
    public MatchDTO findMatchById(Integer matchId)
    throws RemoteException, UnknownEntityException {

        return _controller.findMatchById(matchId);
    }

    @Override
    public Integer createOrSaveMatch(Integer tournamentId, MatchDTO matchDTO, SessionDTO session)
    throws RemoteException, ValidationException, UnknownEntityException, NotAuthorisedException {

        return _controller.createOrSaveMatch(tournamentId, matchDTO, session);
    }

    @Override
    public void assignTeamToTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws RemoteException, ValidationException, UnknownEntityException, NotAuthorisedException {

        _controller.assignTeamToTournament(teamName, tournamentId, session);
    }

    @Override
    public void removeTeamFromTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws RemoteException, ValidationException, UnknownEntityException, NotAuthorisedException {

        _controller.removeTeamFromTournament(teamName, tournamentId, session);
    }
}
