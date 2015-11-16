package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.ITeam;
import at.sporty.team1.domain.interfaces.ITournament;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * Represents a Tournament-Entity
 * TODO: Annotations, Table in DB
 */
@Entity
@Table(name = "tournament")
public class Tournament implements ITournament{
    public String id;
    public List<Match> matches;
    public String date;
    public List<String> teams;
    public League league;
    public Department department;

    /**
     *********************************************************************
     * Getter for property 'date'.
     *
     * @return Value for property 'date'.
     *********************************************************************/
    @Override
    public String getDate() {
        return date;
    }

    /**
     *********************************************************************
     * Setter for property 'date'.
     *
     * @param date Value to set for property 'date'.
     *********************************************************************/
    @Override
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *********************************************************************
     * Getter for property 'department'.
     *
     * @return Value for property 'department'.
     *********************************************************************/
    @Override
    public Department getDepartment() {
        return department;
    }

    /**
     *********************************************************************
     * Setter for property 'department'.
     *
     * @param department Value to set for property 'department'.
     *********************************************************************/
    @Override
    public void setDepartment(Department department) {
        this.department = department;
    }

    /**
     *********************************************************************
     * Getter for property 'league'.
     *
     * @return Value for property 'league'.
     *********************************************************************/
    @Override
    public League getLeague() {
        return league;
    }

    /**
     *********************************************************************
     * Setter for property 'league'.
     *
     * @param league Value to set for property 'league'.
     *********************************************************************/
    @Override
    public void setLeague(League league) {
        this.league = league;
    }

    /**
     *********************************************************************
     * Getter for property 'matches'.
     *
     * @return Value for property 'matches'.
     *********************************************************************/
    @Override
    public List<Match> getMatches() {
        return matches;
    }

    /**
     *********************************************************************
     * Setter for property 'matches'.
     *
     * @param matches Value to set for property 'matches'.
     *********************************************************************/
    @Override
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    /**
     *********************************************************************
     * Getter for property 'teams'.
     *
     * @return Value for property 'teams'.
     *********************************************************************/
    @Override
    public List<String> getTeams() {
        return teams;
    }

    /**
     *********************************************************************
     * Setter for property 'teams'.
     *
     * @param teams Value to set for property 'teams'.
     *********************************************************************/
    @Override
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

    @Override
    public void addTeam(ITeam team) {

        if (!(team instanceof Team)) {
            throw new IllegalArgumentException();
        }

        if (!teams.contains(team)) {
            teams.add(team.getTeamName());
        }
    }

    @Override
    public void removeTeam(ITeam team) {

        if (!(team instanceof Team)) {
            throw new IllegalArgumentException();
        }

        if (teams.isEmpty()) {
            teams.remove(team);
        }
    }
}
