package server;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import at.sporty.team1.application.controller.TournamentController;
import at.sporty.team1.rmi.api.ITournamentController;
import at.sporty.team1.rmi.dtos.DepartmentDTO;
import at.sporty.team1.rmi.dtos.TournamentDTO;
import at.sporty.team1.rmi.exceptions.ValidationException;

public class SaveTournamentTest {

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

}
