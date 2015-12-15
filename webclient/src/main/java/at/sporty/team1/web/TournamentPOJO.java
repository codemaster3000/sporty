package at.sporty.team1.web;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import at.sporty.team1.shared.api.ejb.ITournamentControllerEJB;
import at.sporty.team1.shared.dtos.DepartmentDTO;
import at.sporty.team1.shared.dtos.TournamentDTO;

public class TournamentPOJO {
	
	private List<TournamentDTO> _tournamentList;
	@EJB
	private ITournamentControllerEJB _tournamentController;

	public TournamentPOJO (){
		_tournamentList = _tournamentController.searchAllTournaments();
	}
	
	public List<TournamentDTO> getAllTournaments (){
		return _tournamentList;
	}
	
	public List<String> getTournamentDates(){
		List<String> dates = new ArrayList<String>();
		
		for (TournamentDTO t : _tournamentList) {
			dates.add(t.getDate());
		}
		
		return dates;
	}
	
	public List<String> getTournamentLocations(){
		List<String> locations = new ArrayList<String>();
		
		for (TournamentDTO t : _tournamentList) {
			locations.add(t.getLocation());
		}
		
		return locations;
	}
	
	public List<String> getTournamentSports(){
		List<String> sports = new ArrayList<String>();
		
		for (TournamentDTO t : _tournamentList) {
			sports.add(t.getDepartment().getSport());
		}
		
		return sports;
	}
}
