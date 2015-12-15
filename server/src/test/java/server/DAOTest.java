package server;

import at.sporty.team1.application.controller.real.MemberController;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.Tournament;
import at.sporty.team1.persistence.PersistenceFacade;
import at.sporty.team1.shared.dtos.DTOPair;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.TeamDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import org.junit.Test;

import java.util.List;

/**
 * Created by sereGkaluv on 23-Nov-15.
 */
public class DAOTest {

    //This is a fast and dirty implementation of test for DAO methods
    @Test
    public void departmentTeamFetchedListTest() {
        try {
            List<DTOPair<DepartmentDTO, TeamDTO>> l = new MemberController().loadFetchedDepartmentTeamList(2328, null);
            System.out.println("Received list of size: " + l.size());
        } catch (UnknownEntityException | NotAuthorisedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isDepartmentHeadTest() {
        Member departmentHead = PersistenceFacade.getNewMemberDAO().findById(2000);
        Integer departmentId = 1001;

        System.out.println(
            "\nIs dept head: " + PersistenceFacade.getNewDepartmentDAO().isDepartmentHead(departmentHead, departmentId)
        );
    }

    @Test
    public void findBySportTest() {
        String sport = "Soccer";

        List<Tournament> tournamentList = PersistenceFacade.getNewTournamentDAO().findBySport(sport);

        System.out.println("\nSearch by sport: " + sport);
        for(Tournament tournament : tournamentList) {
            System.out.println("Id: " + tournament.getTournamentId());
        }
    }

    @Test
    public void findByEventDateTest() {
        String eventDate = "2016-01-01";

        List<Tournament> tournamentList = PersistenceFacade.getNewTournamentDAO().findByEventDate(eventDate);

        System.out.println("\nSearch by date: " + eventDate);
        for(Tournament tournament : tournamentList) {
            System.out.println("Id: " + tournament.getTournamentId());
        }
    }

    @Test
    public void findByLocationTest() {
        String location = "Au";

        List<Tournament> tournamentList = PersistenceFacade.getNewTournamentDAO().findByLocation(location);

        System.out.println("\nSearch by location: " + location);
        for(Tournament tournament : tournamentList) {
            System.out.println("Id: " + tournament.getTournamentId());
        }
    }
}
