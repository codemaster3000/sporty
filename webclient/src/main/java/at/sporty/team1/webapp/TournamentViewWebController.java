package at.sporty.team1.webapp;

import at.sporty.team1.shared.api.ejb.ITournamentControllerEJB;
import at.sporty.team1.shared.dtos.MatchDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.util.SessionConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by sereGkaluv on 03.01.16.
 */
@ManagedBean
@ViewScoped
public class TournamentViewWebController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String TOURNAMENT_ID = "tournamentId";

    @EJB
    private ITournamentControllerEJB _tournamentController;

    private Integer _tournamentId;
    private TournamentDTO _activeTournament;
    private MatchDTO _activeMatch;

    @PostConstruct
    public void init() {
        try {

            String id = getRequestParameterMap().get(TOURNAMENT_ID);
            if (id != null && !id.trim().isEmpty()) _tournamentId = Integer.parseInt(id);

        } catch (NumberFormatException e) {
            String context = "Error occurred while parsing id parameter from get request.";

            LOGGER.error(context, e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(context));
        }
    }

    public TournamentViewWebController() {
    }

    public TournamentDTO getActiveTournament() {

        if (_activeTournament == null || !_activeTournament.getTournamentId().equals(_tournamentId)) {

            try {
                _activeTournament = _tournamentController.findTournamentById(_tournamentId);

            } catch (UnknownEntityException e) {
                String context = "Error occurred while searching for tournament.";

                LOGGER.error(context, e);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(context));

                return null;
            }
        }
        return _activeTournament;
    }

    public MatchDTO getActiveMatch() {
        return _activeMatch;
    }

    public void setActiveMatch(MatchDTO activeMatch) {
        _activeMatch = activeMatch;
    }

    public List<MatchDTO> getTournamentMatches() {
        try {

            return _tournamentController.searchAllTournamentMatches(_tournamentId);

        } catch (UnknownEntityException e) {
            String context = "Error occurred while searching matches for tournament.";

            LOGGER.error(context, e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(context));

            return null;
        }
    }

    public boolean isAbleToPerformChanges() {
        Object activeSession = getSessionMap().get(SessionConstants.ACTIVE_SESSION.getConstant());

        return false;
    }

    private static Map<String, String> getRequestParameterMap() {
        return getExternalContext().getRequestParameterMap();
    }

    private static Map<String, Object> getSessionMap() {
        return getExternalContext().getSessionMap();
    }

    private static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
}
