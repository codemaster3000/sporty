package at.sporty.team1.webservice.soap;

import at.sporty.team1.shared.api.ejb.ITournamentControllerEJB;
import at.sporty.team1.shared.api.services.ITournamentResultService;
import at.sporty.team1.shared.dtos.MatchDTO;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.util.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJB;
import javax.jws.WebService;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by f00 on 27.12.15.
 * Service Implementation
 */
@WebService(endpointInterface = "at.sporty.team1.shared.api.services.ITournamentResultService")
public class TournamentResultService implements ITournamentResultService {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String SERVER_ERROR = "server-error";
    private static final String UNKNOWN_MATCH_ID = "Unknown match id received.";

    @EJB private ITournamentControllerEJB _tournamentController;

    @Override
    public String getTournamentResult(String matchId) {

        /* Validating Input */
        if (matchId == null) return JSONObject.formatPairAsJSON(SERVER_ERROR, UNKNOWN_MATCH_ID);

        try {

            /* Getting match by id via EJB remote controller */
            MatchDTO matchDTO = _tournamentController.findMatchById(Integer.parseInt(matchId));
            if (matchDTO == null) return JSONObject.formatPairAsJSON(SERVER_ERROR, UNKNOWN_MATCH_ID);


            /* Converting MatchDTO to JSON object */
            return new JSONObject().appendObject(matchDTO).toString();

        } catch (UnknownEntityException  e) {

            String reason = String.format("An error occurred while searching Match by id #%s.", matchId);
            LOGGER.error(reason, e);

            return JSONObject.formatPairAsJSON(SERVER_ERROR, reason);

        } catch (IllegalAccessException | IntrospectionException | InvocationTargetException e) {

            String reason = String.format("An error occurred while mapping Match #%s to JSON.", matchId);
            LOGGER.error(reason, e);

            return JSONObject.formatPairAsJSON(SERVER_ERROR, reason);
        }
    }
}