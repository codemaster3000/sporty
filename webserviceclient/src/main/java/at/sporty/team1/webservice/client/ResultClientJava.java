package at.sporty.team1.webservice.client;


import at.sporty.team1.shared.api.services.ITournamentResultService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

/**
 * Created by f00 on 27.12.15.
 */
public class ResultClientJava {
    private static final String TARGET_PROVIDER = "TournamentResultServiceService?wsdl";

    public static void main(String[] args) throws Exception {

        System.out.println("Please use a start argument which looks like (remote address): \"http://localhost:8080/webapp\"");

        URL url = new URL(String.format("%s/%s", args[0], TARGET_PROVIDER));

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://soap.webservice.team1.sporty.at/", "TournamentResultServiceService");

        Service service = Service.create(url, qname);

        ITournamentResultService resultService = service.getPort(ITournamentResultService.class);

        System.out.println(resultService.getTournamentResult(args[1]));
    }
}