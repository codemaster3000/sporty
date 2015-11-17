package server;

import static org.junit.Assert.assertNotNull;

import java.rmi.RemoteException;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import at.sporty.team1.application.controller.TournamentController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.UnknownEntityException;
import at.sporty.team1.rmi.exceptions.ValidationException;

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
			tournamentController.createOrSaveTournament(tournamentDTO);
		} catch (RemoteException | ValidationException e) {
			// TODO Auto-generated catch block
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
			tournamentController.createOrSaveTournament(tournamentDTO);
		} catch (RemoteException | ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void getAllTournamentTeamsTest_1() {
		
		List<String> tournamentTeams = null;
		TournamentController tournamentController = null;

		try {
			tournamentController = new TournamentController();
			tournamentTeams = tournamentController.getAllTournamentTeams();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		assertNotNull(tournamentTeams);
	}
	
	@Test
	public void getAllTournamentsTest_1() {
		
		List<TournamentDTO> tournaments = null;
		TournamentController tournamentController = null;

		try {
			tournamentController = new TournamentController();
			tournaments = tournamentController.getAllTournaments();
		} catch (RemoteException | UnknownEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(tournaments);
	}
	
	@Test
	public void createNewMatchTest_1() {
		
		String team1 = "Hobbits";
		String team2 = "Rabbits";
		String time = "13.00";
		String place = "court 1";
		TournamentDTO tournamentDTO = new TournamentDTO();
		TournamentController tournamentController = null;

		try {
			tournamentController = new TournamentController();
			tournamentController.createNewMatch(team1, team2, time, place, tournamentDTO);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
