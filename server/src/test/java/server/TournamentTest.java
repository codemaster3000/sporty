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

        Integer tournamentId = 1000; //TODO change to real one

		try {
			tournamentController = new TournamentController();
			tournamentTeams = tournamentController.searchAllTournamentTeams(tournamentId);
		} catch (RemoteException | UnknownEntityException e) {
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
			tournaments = tournamentController.searchAllTournaments();
		} catch (RemoteException | UnknownEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(tournaments);
	}
	
	@Test
	public void createNewMatchTest_1() {

		Integer tournamentId = 1000; //TODO change to real one
		String team1 = "Hobbits";
		String team2 = "Rabbits";
		String time = "13.00";
		String location = "court 1";
		TournamentController tournamentController = null;

		try {
			tournamentController = new TournamentController();
			tournamentController.createNewMatch(tournamentId, team1, team2, time, location);
		} catch (RemoteException | UnknownEntityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
