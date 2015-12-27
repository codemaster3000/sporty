package at.sporty.team1.shared.dtos;

import at.sporty.team1.shared.api.entity.IDTO;

/**
 * Created by f00 on 16.11.15.
 */
public class TournamentDTO implements IDTO {
    private static final long serialVersionUID = 1L;
	
	private Integer _tournamentId;
    private String _date;
    private String _location;
    private DepartmentDTO _department;

    public TournamentDTO() {
    }

    public Integer getTournamentId() {
        return _tournamentId;
    }

    public TournamentDTO setTournamentId(Integer tournamentId) {
        _tournamentId = tournamentId;
        return this;
    }

    public DepartmentDTO getDepartment() {
        return _department;
    }

    public TournamentDTO setDepartment(DepartmentDTO department) {
        _department = department;
        return this;
    }

    public String getDate() {
        return _date;
    }

    public TournamentDTO setDate(String date) {
        _date = date;
        return this;
    }

    public String getLocation() {
        return _location;
    }

	public TournamentDTO setLocation(String location) {
		_location = location;
		return this;
	}
}
