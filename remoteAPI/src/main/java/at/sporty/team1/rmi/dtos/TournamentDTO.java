package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

import java.util.List;

/**
 * Created by f00 on 16.11.15.
 */
public class TournamentDTO implements IDTO {
    private static final long serialVersionUID = 1L;
	
	private Integer _id;
    private String _date;
    private String _location;
    private LeagueDTO _league;
    private DepartmentDTO _department;

    public TournamentDTO() {
    }

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
        _date = date;
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
        _department = department;
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
        _league = league;
        return this;
    }

    /**
     *********************************************************************
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     *********************************************************************/
    public Integer getId() {
        return _id;
    }

    /**
     *********************************************************************
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     *********************************************************************/
    public TournamentDTO setId(Integer id) {
        _id = id;
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
     * @param location Value to set for property 'location'.
     *********************************************************************/
	public TournamentDTO setLocation(String location) {
		_location = location;
		return this;
	}
}
