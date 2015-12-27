package at.sporty.team1.webservice.soap;

import at.sporty.team1.webservice.old.ResultPersistentBean;

import javax.jws.WebService;

/**
 * Created by f00 on 27.12.15.
 * Service Implementation
 */
@WebService(endpointInterface = "at.sporty.team1.webservice.soap.IResultService")
public class ResultServiceImpl implements IResultService{

    @Override
    public String getResult(String matchId) {

        ResultPersistentBean resultPersistentBean = new ResultPersistentBean();
        return resultPersistentBean.getResult(matchId);
    }

}