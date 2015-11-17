package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

import java.util.List;

/**
 * Created by f00 on 16.11.15.
 */
public class TournamentDTO implements IDTO {
    
	private static final long serialVersionUID = 1L;
	
	private String _id;
    private List<MatchDTO> _matches;
    private String _date;
    private List<String> _teams;
    private LeagueDTO _league;
    private DepartmentDTO _department;
    private String _location;

    /**
     *********************************************************************
     * Getter for property 'date'.
     *
     * @return Value for property 'date'.
     *********************************************************************/
    public String getDate() {
        return _date;
    }

    /**
     *********************************************************************
     * Setter for property 'date'.
     *
     * @param date Value to set for property 'date'.
     *********************************************************************/
    public TournamentDTO setDate(String date) {
        this._date = date;
        return this;
    }

    /**
     *********************************************************************
     * Getter for property 'department'.
     *
     * @return Value for property 'department'.
     *********************************************************************/
    public DepartmentDTO getDepartment() {
        return _department;
    }

    /**
     *********************************************************************
     * Setter for property 'department'.
     *
     * @param department Value to set for property 'department'.
     *********************************************************************/
    public TournamentDTO setDepartment(DepartmentDTO department) {
        this._department = department;
        return this;
    }

    /**
     *********************************************************************
     * Getter for property 'league'.
     *
     * @return Value for property 'league'.
     *********************************************************************/
    public LeagueDTO getLeague() {
        return _league;
    }

    /**
     *********************************************************************
     * Setter for property 'league'.
     *
     * @param league Value to set for property 'league'.
     *********************************************************************/
    public TournamentDTO setLeague(LeagueDTO league) {
        this._league = league;
        return this;
    }

    /**
     *********************************************************************
     * Getter for property 'matches'.
     *
     * @return Value for property 'matches'.
     *********************************************************************/
    public List<MatchDTO> getMatches() {
        return _matches;
    }

    /**
     *********************************************************************
     * Setter for property 'matches'.
     *
     * @param matches Value to set for property 'matches'.
     *********************************************************************/
    public TournamentDTO setMatches(List<MatchDTO> matches) {
        this._matches = matches;
        return this;
    }

    /**
     *********************************************************************
     * Getter for property 'teams'.
     *
     * @return Value for property 'teams'.
     *********************************************************************/
    public List<String> getTeams() {
        return _teams;
    }

    /**
     *********************************************************************
     * Setter for property 'teams'.
     *
     * @param teams Value to set for property 'teams'.
     *********************************************************************/
    public TournamentDTO setTeams(List<String> teams) {
        this._teams = teams;
        return this;
    }

    /**
     *********************************************************************
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     *********************************************************************/
    public String getId() {
        return _id;
    }

    /**
     *********************************************************************
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     *********************************************************************/
    public TournamentDTO setId(String id) {
        this._id = id;
        return this;
    }
    
    /**
     *********************************************************************
     * Getter for property 'location'.
     *
     * @return Value for property 'location'.
     *********************************************************************/
    public String getLocation() {
        return _location;
    }

    /**
     *********************************************************************
     * Setter for property 'location'.
     *
     * @param id Value to set for property 'location'.
     *********************************************************************/
	public TournamentDTO setLocation(String location) {
		this._location = location;
		return this;
	}
	
	
}
