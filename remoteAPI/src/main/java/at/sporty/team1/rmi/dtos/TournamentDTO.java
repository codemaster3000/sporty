package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

import java.util.List;

/**
 * Created by f00 on 16.11.15.
 */
public class TournamentDTO implements IDTO {
    public String id;
    public List<MatchDTO> matches;
    public String date;
    public List<String> teams;
    public LeagueDTO league;
    public DepartmentDTO department;

    /**
     *********************************************************************
     * Getter for property 'date'.
     *
     * @return Value for property 'date'.
     *********************************************************************/
    public String getDate() {
        return date;
    }

    /**
     *********************************************************************
     * Setter for property 'date'.
     *
     * @param date Value to set for property 'date'.
     *********************************************************************/
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *********************************************************************
     * Getter for property 'department'.
     *
     * @return Value for property 'department'.
     *********************************************************************/
    public DepartmentDTO getDepartment() {
        return department;
    }

    /**
     *********************************************************************
     * Setter for property 'department'.
     *
     * @param department Value to set for property 'department'.
     *********************************************************************/
    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    /**
     *********************************************************************
     * Getter for property 'league'.
     *
     * @return Value for property 'league'.
     *********************************************************************/
    public LeagueDTO getLeague() {
        return league;
    }

    /**
     *********************************************************************
     * Setter for property 'league'.
     *
     * @param league Value to set for property 'league'.
     *********************************************************************/
    public void setLeague(LeagueDTO league) {
        this.league = league;
    }

    /**
     *********************************************************************
     * Getter for property 'matches'.
     *
     * @return Value for property 'matches'.
     *********************************************************************/
    public List<MatchDTO> getMatches() {
        return matches;
    }

    /**
     *********************************************************************
     * Setter for property 'matches'.
     *
     * @param matches Value to set for property 'matches'.
     *********************************************************************/
    public void setMatches(List<MatchDTO> matches) {
        this.matches = matches;
    }

    /**
     *********************************************************************
     * Getter for property 'teams'.
     *
     * @return Value for property 'teams'.
     *********************************************************************/
    public List<String> getTeams() {
        return teams;
    }

    /**
     *********************************************************************
     * Setter for property 'teams'.
     *
     * @param teams Value to set for property 'teams'.
     *********************************************************************/
    public void setTeams(List<String> teams) {
        this.teams = teams;
    }

    /**
     *********************************************************************
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     *********************************************************************/
    public String getId() {
        return id;
    }

    /**
     *********************************************************************
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     *********************************************************************/
    public void setId(String id) {
        this.id = id;
    }
}
