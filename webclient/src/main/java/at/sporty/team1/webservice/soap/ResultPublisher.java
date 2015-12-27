package at.sporty.team1.webservice.soap;


import javax.xml.ws.Endpoint;
/**
 * Created by f00 on 27.12.15.
 * Endpoint publisher
 */
public class ResultPublisher{

    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9999/ws/getMatchResult", new ResultServiceImpl());
    }

}