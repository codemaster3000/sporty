package at.sporty.team1.webservice;

import at.sporty.team1.domain.Match;
import at.sporty.team1.persistence.PersistenceFacade;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.PersistenceException;

/**
 * Created by f00 on 14.12.15.
 */
@Stateless
@WebService(serviceName="resultService")
public class ResultPersistentBean /*implements ResultPersistentBeanRemote*/ {
    private static final Logger LOGGER = LogManager.getLogger();

    @WebMethod(operationName="getResult")
    public String getResult(String matchId)  {

        try {

            //TODO replace with real JSON -> import com.google.gson;

            /* Validating Input */
            if (matchId == null) return "{\"response\":\"unknown entity\"}";

            Match match = PersistenceFacade.getNewGenericDAO(Match.class).findById(matchId);
            if (match == null) return "{\"response\":\"unknown entity\"}";

            StringBuilder sb = new StringBuilder();

            sb.append("{");
            sb.append("\"matchId\":\"").append(matchId).append("\"");
            sb.append(",");
            sb.append("\"resultTeam1\":\"").append(match.getResultTeam1()).append("\"");
            sb.append(",");
            sb.append("\"resultTeam2\":\"").append(match.getResultTeam2()).append("\"");
            sb.append("}");

            return sb.toString();


        } catch (PersistenceException e) {
            LOGGER.error(
                    "An error occurred while searching Match by id #{}.",
                    matchId,
                    e
            );
            return null;
        }
    }
}
