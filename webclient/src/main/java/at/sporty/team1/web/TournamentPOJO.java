package at.sporty.team1.web;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import at.sporty.team1.shared.api.ejb.ITournamentControllerEJB;
import at.sporty.team1.shared.dtos.TournamentDTO;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.persistence.Id;

@ManagedBean
@RequestScoped
@Named(value = "tournamentPOJO")
public class TournamentPOJO implements Serializable {

    /**
     * default ID
     */
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;

    private List<TournamentDTO> tournamentList;

    @EJB
	private ITournamentControllerEJB _tournamentController;

    public TournamentPOJO() {
        tournamentList = _tournamentController.searchAllTournaments();
    }

    public List<TournamentDTO> getAllTournaments() {
        return tournamentList;
    }

    public List<String> getTournamentDates() {
        List<String> dates = new ArrayList<String>();

        for (TournamentDTO t : tournamentList) {
            dates.add(t.getDate());
        }

        return dates;
    }

    public List<String> getTournamentLocations() {
        List<String> locations = new ArrayList<String>();

        for (TournamentDTO t : tournamentList) {
            locations.add(t.getLocation());
        }

        return locations;
    }

    public List<String> getTournamentSports() {
        List<String> sports = new ArrayList<String>();

        for (TournamentDTO t : tournamentList) {
            sports.add(t.getDepartment().getSport());
        }

        return sports;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
