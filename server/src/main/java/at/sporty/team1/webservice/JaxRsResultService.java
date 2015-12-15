package at.sporty.team1.webservice;

import javax.ws.rs.*;

/**
 * Created by f00 on 15.12.15.
 */
@Path("/API")
public class JaxRsResultService {

    ResultPersistentBean _resultPersistentBean;

    public JaxRsResultService() {
    }

    @GET
    @Path("/getMatchResult")
    @Produces("text/plain")
    @Consumes("text/plain")
    public String getMatchResult(@QueryParam("matchId") String matchid) {
        return _resultPersistentBean.getResult(matchid);
    }

//    @GET
//    @Path("/{matchId:00[1-9]}")
//    @Produces("text/html")
//    @Consumes("text/plain")
//    public String sayHelloHtml(@PathParam("code") String code) {
//        return "<html><title>HelloWorld</title><body><b>Hello special agent " +
//                code + "</b></body></html>";
//    }
}
