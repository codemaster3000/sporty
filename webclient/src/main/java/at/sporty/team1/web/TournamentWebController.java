package at.sporty.team1.web;

import java.util.List;

import javax.ejb.EJB;

import at.sporty.team1.shared.api.ejb.ITournamentControllerEJB;
import at.sporty.team1.shared.dtos.TournamentDTO;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class TournamentWebController implements Serializable {
    private static final long serialVersionUID = 1L;

    @EJB
    private ITournamentControllerEJB _tournamentController;

    public TournamentWebController() {
    }

    public List<TournamentDTO> getAllTournaments() {
        return _tournamentController.searchAllTournaments();
    }
}
