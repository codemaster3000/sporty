package at.sporty.team1.webservice.soap.client;

import at.sporty.team1.webservice.soap.IResultService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

/**
 * Created by f00 on 27.12.15.
 */
public class ResultClientJava {

    public static void main(String[] args) throws Exception {

        URL url = new URL("http://localhost:9999/ws/getMachResult?wsdl");

        //1st argument service URI, refer to wsdl document above
        //2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://5.35.247.12/", "ResultServiceImpl");

        Service service = Service.create(url, qname);

        IResultService resultService = service.getPort(IResultService.class);

        //TODO enter a valid matchId here (15 is valid)
        System.out.println(resultService.getResult("15"));

    }

}