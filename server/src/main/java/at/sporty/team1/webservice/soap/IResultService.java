package at.sporty.team1.webservice.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;


/**
 * Created by f00 on 27.12.15.
 * Service Endpoint Interface
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface IResultService{

    @WebMethod String getResult(String matchId);

}