package server;

import at.sporty.team1.domain.Tournament;
import at.sporty.team1.persistence.PersistenceFacade;
import org.junit.Test;

import java.util.List;

/**
 * Created by sereGkaluv on 23-Nov-15.
 */
public class TournamentDAOTest {

    //This is a fast and dirty implementation of test for TournamentDAO methods

    @Test
    public void findBySportTest() {
        String sport = "Soccer";

        List<Tournament> tournamentList= PersistenceFacade.getNewTournamentDAO().findBySport(sport);

        System.out.println("\nSearch by sport: " + sport);
        for(Tournament tournament : tournamentList) {
            System.out.println("Id: " + tournament.getTournamentId());
        }
    }

    @Test
    public void findByEventDateTest() {
        String eventDate = "2016-01-01";

        List<Tournament> tournamentList= PersistenceFacade.getNewTournamentDAO().findByEventDate(eventDate);

        System.out.println("\nSearch by date: " + eventDate);
        for(Tournament tournament : tournamentList) {
            System.out.println("Id: " + tournament.getTournamentId());
        }
    }

    @Test
    public void findByLocationTest() {
        String location = "Au";

        List<Tournament> tournamentList= PersistenceFacade.getNewTournamentDAO().findByLocation(location);

        System.out.println("\nSearch by location: " + location);
        for(Tournament tournament : tournamentList) {
            System.out.println("Id: " + tournament.getTournamentId());
        }
    }
}
