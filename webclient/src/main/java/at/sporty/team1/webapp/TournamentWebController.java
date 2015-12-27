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
import java.util.Map;

@ManagedBean
@RequestScoped
public class TournamentWebController implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LogManager.getLogger();

    private SearchType _searchType;

    @EJB
    private ITournamentControllerEJB _tournamentController;

    public TournamentWebController() {
    }

    /* Getter and Setter */
    public SearchType getSearchType() {
        return _searchType;
    }

    public void setSearchType(SearchType searchType) {
        _searchType = searchType;
    }

    public boolean isUserLoggedIn() {
        return getSessionMap().containsKey(SessionConstants.ACTIVE_SESSION.getConstant());
    }

    /**
     * Wrapper-methode for Searching
     * @return List<TournamentDTO> or null
     */
    public List<TournamentDTO> searchTournaments(String searchQuery) {
        switch (getSearchType()) {
            //search by event date
            case EVENT_DATE: return getTournamentsByDate(searchQuery);

            //search by location
            case LOCATION: return getTournamentsByLocation(searchQuery);

            //search by sport
            case DEPARTMENT: return getTournamentsBySport(searchQuery);

            default: return getAllTournaments();
        }
    }

    public List<TournamentDTO> getAllTournaments() {
        return _tournamentController.searchAllTournaments();
    }

    public List<TournamentDTO> getTournamentsBySport(String sport) {
        try {

            return _tournamentController.searchTournamentsBySport(sport);

        } catch (ValidationException e) {
            LOGGER.error("Validation Exception while search Tournaments by Sport", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));

            return null;
        }
    }

    public List<TournamentDTO> getTournamentsByLocation(String location){
        try {

            return _tournamentController.searchTournamentsByLocation(location);

        } catch (ValidationException e) {
            LOGGER.error("Validation Exception while search Tournaments by Location", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));

            return null;
        }
    }

    public List<TournamentDTO> getTournamentsByDate(String date){
        try {

            return _tournamentController.searchTournamentsByDate(date);

        } catch (ValidationException e) {
            LOGGER.error("Validation Exception while search Tournaments by Date", e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));

            return null;
        }
    }

    private Map<String, Object> getSessionMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
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
