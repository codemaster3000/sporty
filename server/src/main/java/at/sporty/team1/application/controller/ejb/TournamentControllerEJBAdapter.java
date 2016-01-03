package at.sporty.team1.application.controller.ejb;

import at.sporty.team1.application.controller.real.TournamentController;
import at.sporty.team1.application.controller.real.api.ITournamentController;
import at.sporty.team1.shared.api.ejb.ITournamentControllerEJB;
import at.sporty.team1.shared.dtos.MatchDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by f00 on 13.12.15.
 */
@Stateless(name = "TOURNAMENT_CONTROLLER_EJB")
public class TournamentControllerEJBAdapter implements ITournamentControllerEJB {
    private static final long serialVersionUID = 1L;
    private transient final ITournamentController _controller;

    public TournamentControllerEJBAdapter() {
        _controller = new TournamentController();
    }

    @Override
    public void assignTeamToTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws ValidationException, UnknownEntityException, NotAuthorisedException {

        _controller.assignTeamToTournament(teamName, tournamentId, session);
    }

    @Override
    public TournamentDTO findTournamentById(Integer tournamentId)
    throws UnknownEntityException {

        return _controller.findTournamentById(tournamentId);
    }

    @Override
    public List<TournamentDTO> searchAllTournaments() {

        return _controller.searchAllTournaments();
    }

    @Override
    public List<TournamentDTO> searchTournamentsBySport(String sport)
    throws ValidationException {

        return _controller.searchTournamentsBySport(sport);
    }

    @Override
    public List<TournamentDTO> searchTournamentsByDate(String eventDate)
    throws ValidationException {

        return _controller.searchTournamentsByDate(eventDate);
    }

    @Override
    public List<TournamentDTO> searchTournamentsByLocation(String location)
    throws ValidationException {

        return _controller.searchTournamentsByLocation(location);
    }

    @Override
    public List<String> searchAllTournamentTeams(Integer tournamentId)
    throws UnknownEntityException {

        return _controller.searchAllTournamentTeams(tournamentId);
    }

    @Override
    public List<MatchDTO> searchAllTournamentMatches(Integer tournamentId)
    throws UnknownEntityException {

        return _controller.searchAllTournamentMatches(tournamentId);
    }

    @Override
    public Integer createOrSaveTournament(TournamentDTO tournamentDTO, SessionDTO session)
    throws ValidationException, NotAuthorisedException {

        return _controller.createOrSaveTournament(tournamentDTO, session);
    }

    @Override
    public MatchDTO findMatchById(Integer matchId)
    throws UnknownEntityException {

        return _controller.findMatchById(matchId);
    }

    @Override
    public Integer createNewMatch(Integer tournamentId, MatchDTO matchDTO, SessionDTO session)
    throws ValidationException, UnknownEntityException, NotAuthorisedException {

        return _controller.createNewMatch(tournamentId, matchDTO, session);
    }

    @Override
    public void removeTeamFromTournament(String teamName, Integer tournamentId, SessionDTO session)
    throws ValidationException, UnknownEntityException, NotAuthorisedException {

        _controller.removeTeamFromTournament(teamName, tournamentId, session);
    }
}
