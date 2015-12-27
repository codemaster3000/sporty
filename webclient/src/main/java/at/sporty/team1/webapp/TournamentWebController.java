package at.sporty.team1.webapp;

import at.sporty.team1.shared.api.ejb.ITournamentControllerEJB;
import at.sporty.team1.shared.dtos.MemberDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.exceptions.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@RequestScoped
public class TournamentWebController implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String SESSION_OBJECT = "SESSION_OBJECT";
    public SearchType searchType;

    @EJB
    private ITournamentControllerEJB _tournamentController;

    public TournamentWebController() {
    }

    public List<TournamentDTO> getAllTournaments() {
        return _tournamentController.searchAllTournaments();
    }

    public List<TournamentDTO> getTournamentsBySport(String sport) {
        try {
            return _tournamentController.searchTournamentsBySport(sport);
        } catch (ValidationException e) {
            LOGGER.error("Validationexception while search Tournaments by Sport", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }

        return null;
    }

    public List<TournamentDTO> getTournamentsByLocation(String location){

        try {
            return _tournamentController.searchTournamentsByLocation(location);
        } catch (ValidationException e) {
            LOGGER.error("Validationexception while search Tournaments by Location", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }

        return null;
    }

    public List<TournamentDTO> getTournamentsByDate(String date){

        try {
            return _tournamentController.searchTournamentsByDate(date);
        } catch (ValidationException e) {
            LOGGER.error("Validationexception while search Tournaments by Date", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
        }

        return null;
    }

    public boolean isUserLoggedIn(MemberDTO member){

        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(SESSION_OBJECT) != null;
    }

    /**
     * Wrapper-methode for Searching
     * @return List<TournamentDTO> or null
     */
    public List<TournamentDTO> getAllTournaments(String query) {
        switch (searchType) {
            case EVENT_DATE:
                //search by birthdate
                return getTournamentsByDate(query);
            case LOCATION:
                //search by location
                return getTournamentsByLocation(query);
            case DEPARTMENT:
                //search by sport
                return getTournamentsBySport(query);
            default:
                return null;
        }
    }

    /* Getter and Setter */
    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    /* private enum*/
    private enum SearchType {
        DEPARTMENT("sport"),
        EVENT_DATE("date of the event"),
        LOCATION("location");

        private final String _stringValue;

        SearchType(String stringValue) {
            _stringValue = stringValue;
        }

        @Override
        public String toString() {
            return _stringValue;
        }
    }
}
