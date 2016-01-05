package at.sporty.team1.webapp;

import at.sporty.team1.shared.api.ejb.ITournamentControllerEJB;
import at.sporty.team1.shared.dtos.MatchDTO;
import at.sporty.team1.shared.dtos.SessionDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;
import at.sporty.team1.util.SessionConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by sereGkaluv on 03.01.16.
 */
@ManagedBean
@RequestScoped
public class TournamentViewWebController implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String MATCH_RESULTS_EDIT_FORM = "matchEditForm:results";

    @EJB
    private ITournamentControllerEJB _tournamentController;

    private Map<Integer, MatchDTO> _matchesMap;

    public TournamentViewWebController() {
    }

    public TournamentDTO getActiveTournament() {
        return (TournamentDTO) getSessionMap().get(SessionConstants.ACTIVE_TOURNAMENT.getConstant());
    }

    public MatchDTO getActiveMatch() {
        return (MatchDTO) getSessionMap().get(SessionConstants.ACTIVE_MATCH.getConstant());
    }

    public Integer getActiveMatchId() {
        MatchDTO activeMatch = getActiveMatch();
        return activeMatch != null ? activeMatch.getMatchId() : null;
    }

    public void setActiveMatchId(Integer activeMatchId) {
        getSessionMap().put(
            SessionConstants.ACTIVE_MATCH.getConstant(),
            _matchesMap.get(activeMatchId)
        );
    }

    public List<MatchDTO> getTournamentMatches() {
        try {

            TournamentDTO activeTournament = getActiveTournament();
            if (activeTournament != null && activeTournament.getTournamentId() != null) {

                List<MatchDTO> matches = _tournamentController.searchAllTournamentMatches(activeTournament.getTournamentId());
                if (matches != null && !matches.isEmpty()) {

                    _matchesMap = matches.stream().collect(Collectors.toMap(MatchDTO::getMatchId, Function.identity()));
                }
                return matches;

            } else return null;

        } catch (UnknownEntityException e) {
            String context = "Error occurred while searching matches for tournament.";

            LOGGER.error(context, e);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(context));

            return null;
        }
    }

    public String getResultTeam1() {
        return getActiveMatch().getResultTeam1();
    }

    public void setResultTeam1(String resultTeam1) {
        getSessionMap().put(
            SessionConstants.ACTIVE_MATCH.getConstant(),
            getActiveMatch().setResultTeam1(resultTeam1)
        );
    }

    public String getResultTeam2() {
        return getActiveMatch().getResultTeam2();
    }

    public void setResultTeam2(String resultTeam2) {
        getSessionMap().put(
            SessionConstants.ACTIVE_MATCH.getConstant(),
            getActiveMatch().setResultTeam2(resultTeam2)
        );
    }

    public boolean getIsFinalResults() {
        return getActiveMatch().getIsFinalResults();
    }

    public void setIsFinalResults(boolean isFinalResults) {
        getSessionMap().put(
            SessionConstants.ACTIVE_MATCH.getConstant(),
            getActiveMatch().setIsFinalResults(isFinalResults)
        );
    }

    public boolean getRequestedEdit() {
        Boolean requestedEdit = (Boolean) getSessionMap().get(SessionConstants.REQUESTED_EDIT.getConstant());
        return requestedEdit != null && requestedEdit;
    }

    public void setRequestedEdit(boolean requestedEdit) {
        getSessionMap().put(
            SessionConstants.REQUESTED_EDIT.getConstant(),
            requestedEdit
        );
    }

    public boolean isAbleToPerformChanges() {
        return _tournamentController.isAbleToPerformChanges(
            getActiveTournament(),
            getActiveSession()
        );
    }

    public void saveChangesForActiveMatch() {

        try {

            _tournamentController.createOrSaveMatch(
                getActiveTournament().getTournamentId(),
                getActiveMatch(),
                getActiveSession()
            );

            setRequestedEdit(false);

        } catch (ValidationException e) {

            String context = "Error occurred while saving match results. Entered data is not valid.";

            LOGGER.error(context, e);
            FacesContext.getCurrentInstance().addMessage(MATCH_RESULTS_EDIT_FORM, new FacesMessage(context));

        } catch (UnknownEntityException e) {

            String context = "Error occurred while saving match. Unknown match, please reload this page.";

            LOGGER.error(context, e);
            FacesContext.getCurrentInstance().addMessage(MATCH_RESULTS_EDIT_FORM, new FacesMessage(context));

        } catch (NotAuthorisedException e) {

            String context = "Error occurred while saving match. Not enough permissions.";

            LOGGER.error(context, e);
            FacesContext.getCurrentInstance().addMessage(MATCH_RESULTS_EDIT_FORM, new FacesMessage(context));
        }
    }

    private SessionDTO getActiveSession() {
        return (SessionDTO) getSessionMap().get(SessionConstants.ACTIVE_SESSION.getConstant());
    }

    private static Map<String, Object> getSessionMap() {
        return getExternalContext().getSessionMap();
    }

    private static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
}
