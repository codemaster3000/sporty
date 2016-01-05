package at.sporty.team1.webapp;

import at.sporty.team1.shared.api.ejb.ITournamentControllerEJB;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.exceptions.ValidationException;
import at.sporty.team1.util.SessionConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@ManagedBean
@RequestScoped
public class TournamentSearchWebController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String SEARCH_QUERY = "searchForm:searchQuery";
    private static final String VALIDATION_EXCEPTION = "Server validation problem occurred. [%s]";
    private static final String TOURNAMENT_OVERVIEW_PAGE = "tournament_overview.jsf";

    @EJB
    private ITournamentControllerEJB _tournamentController;

    private String _searchQuery;
    private SearchType _searchType;

    public TournamentSearchWebController() {
    }

    /* Getter and Setter */
    public String getSearchQuery() {
        return _searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        _searchQuery = searchQuery;
    }

    public SearchType getSearchType() {
        return _searchType;
    }

    public void setSearchType(SearchType searchType) {
        _searchType = searchType;
    }

    public SearchType[] getAllSearchTypes() {
        return SearchType.values();
    }

    /**
     * Wrapper-method for Searching
     * @return List<TournamentDTO> or null
     */
    public List<TournamentDTO> getSearchResults() {
        String searchQuery = getSearchQuery();
        SearchType searchType = getSearchType();

        if (searchQuery != null && searchType != null) {
            switch (searchType) {
                //search by event date
                case EVENT_DATE: return getTournamentsByDate(searchQuery);

                //search by location
                case LOCATION: return getTournamentsByLocation(searchQuery);

                //search by sport
                case DEPARTMENT: return getTournamentsBySport(searchQuery);
            }
        }

        return getAllTournaments();
    }

    public void openTournamentView(TournamentDTO selectedTournament) {

        try {

            Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

            sessionMap.put(SessionConstants.ACTIVE_TOURNAMENT.getConstant(), selectedTournament);

            sessionMap.remove(SessionConstants.ACTIVE_MATCH.getConstant());
            sessionMap.remove(SessionConstants.REQUESTED_EDIT.getConstant());

            FacesContext.getCurrentInstance().getExternalContext().redirect(TOURNAMENT_OVERVIEW_PAGE);

        } catch (IOException e) {
            LOGGER.error(
                "Unable to redirect to {} from tournament search page.",
                TOURNAMENT_OVERVIEW_PAGE,
                e
            );
        }
    }

    private List<TournamentDTO> getAllTournaments() {
        return _tournamentController.searchAllTournaments();
    }

    private List<TournamentDTO> getTournamentsBySport(String sport) {
        try {

            return _tournamentController.searchTournamentsBySport(sport);

        } catch (ValidationException e) {
            LOGGER.error("Validation Exception while search Tournaments by Sport.", e);
            FacesContext.getCurrentInstance().addMessage(SEARCH_QUERY, new FacesMessage(
                String.format(VALIDATION_EXCEPTION, e.getReason())
            ));

            return null;
        }
    }

    private List<TournamentDTO> getTournamentsByLocation(String location){
        try {

            return _tournamentController.searchTournamentsByLocation(location);

        } catch (ValidationException e) {
            LOGGER.error("Validation Exception while search Tournaments by Location.", e);
            FacesContext.getCurrentInstance().addMessage(SEARCH_QUERY, new FacesMessage(
                String.format(VALIDATION_EXCEPTION, e.getReason())
            ));

            return null;
        }
    }

    private List<TournamentDTO> getTournamentsByDate(String date){
        try {

            return _tournamentController.searchTournamentsByDate(date);

        } catch (ValidationException e) {
            LOGGER.error("Validation Exception while search Tournaments by Date.", e);
            FacesContext.getCurrentInstance().addMessage(SEARCH_QUERY, new FacesMessage(
                String.format(VALIDATION_EXCEPTION, e.getReason())
            ));

            return null;
        }
    }

    /* private enum*/
    public enum SearchType {
        DEPARTMENT("SPORT"),
        EVENT_DATE("DATE OF THE EVENT"),
        LOCATION("LOCATION");

        private final String _stringValue;

        SearchType(String stringValue) {
            _stringValue = stringValue;
        }

        public String getStringValue() {
            return _stringValue;
        }

        @Override
        public String toString() {
            return _stringValue;
        }
    }
}
