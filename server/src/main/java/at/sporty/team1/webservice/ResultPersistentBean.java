package at.sporty.team1.webservice;

import at.sporty.team1.domain.Match;
import at.sporty.team1.persistence.PersistenceFacade;

import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by f00 on 14.12.15.
 */
@Stateless
@WebService(serviceName="ResultService")
public class ResultPersistentBean /*implements ResultPersistentBeanRemote*/ {


    @WebMethod(operationName="getResult")
    public String getResult(String matchId)  {
        //return entityManager.createQuery("From Books").getResultList();

        Match match = PersistenceFacade.getNewGenericDAO(Match.class).findById(matchId);

        String jsonResult;
        return jsonResult = "{\"matchId\":\"" + matchId + "\"" + ", \"result\":\"" + match.getResult() + "\"}";
    }
}