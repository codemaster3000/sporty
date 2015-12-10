package server;

import at.sporty.team1.application.controller.real.TournamentController;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.MatchDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;
import at.sporty.team1.shared.exceptions.NotAuthorisedException;
import at.sporty.team1.shared.exceptions.UnknownEntityException;
import at.sporty.team1.shared.exceptions.ValidationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.rmi.RemoteException;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class TournamentTest {

	@Rule 
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void saveTournamentTest_1() {

		String date = "2016-12-01";
		String location = "Dornbirn";
		
		DepartmentDTO departmentDTO = new DepartmentDTO();
		TournamentDTO tournamentDTO = new TournamentDTO();
		TournamentController tournamentController = null;
		
		departmentDTO.setSport("Soccer");
		tournamentDTO.setDate(date);
		tournamentDTO.setDepartment(departmentDTO);
		tournamentDTO.setLocation(location);
		
		try {
			tournamentController = new TournamentController();
			tournamentController.createOrSaveTournament(tournamentDTO, null);
		} catch (ValidationException | NotAuthorisedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void saveTournamentTest_2() {
		
		DepartmentDTO departmentDTO = new DepartmentDTO();
		TournamentDTO tournamentDTO = null;
		TournamentController tournamentController = null;
		
		departmentDTO.setSport("Soccer");
		
		thrown.expect(RemoteException.class);

        try {
            tournamentController = new TournamentController();
            tournamentController.createOrSaveTournament(tournamentDTO, null);

        } catch (ValidationException | NotAuthorisedException e) {
            e.printStackTrace();
		}
	}
	
	@Test
	public void getAllTournamentTeamsTest_1() {
		
		List<String> tournamentTeams = null;
		TournamentController tournamentController = null;

        Integer tournamentId = 52;

		try {
			tournamentController = new TournamentController();
			tournamentTeams = tournamentController.searchAllTournamentTeams(tournamentId);
		} catch (UnknownEntityException e) {
			e.printStackTrace();
		}
		
		assertNotNull(tournamentTeams);
	}
	
	@Test
	public void getAllTournamentsTest_1() {
		
		List<TournamentDTO> tournaments = null;
		TournamentController tournamentController = null;

		tournamentController = new TournamentController();
		tournaments = tournamentController.searchAllTournaments();

		assertNotNull(tournaments);
	}
	
	@Test
	public void createNewMatchTest_1() {

		Integer tournamentId = 1000;

        MatchDTO matchDTO = new MatchDTO();
		matchDTO.setTeam1("Hobbits");
        matchDTO.setTeam2("Rabbits");
        matchDTO.setDate("13.00");
		matchDTO.setLocation("court 1");

		try {
            TournamentController tournamentController = new TournamentController();
			tournamentController.createNewMatch(tournamentId, matchDTO, null);
		} catch (UnknownEntityException | ValidationException | NotAuthorisedException e) {
			e.printStackTrace();
		}
    }
}
