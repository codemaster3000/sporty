package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.ITeam;
import at.sporty.team1.domain.interfaces.ITournament;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a Tournament-Entity
 * TODO: Annotations, Table in DB
 */
@Entity
@Table(name = "tournament")
public class Tournament implements ITournament {
    private String id;
    private List<Match> matches; //TODO: SQL column
    private String date;
    private List<String> teams; //TODO: SQL column
    private League league;
    private Department department;
    private String location;


    @Override
    @Basic
    @Column(name = "date")
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    @Basic
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    @OneToOne
    @Column(name = "departmentId" )
    public Department getDepartment() {
        return department;
    }

    @Override
    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    @OneToOne
    @Column(name = "leagueId")
    public League getLeague() {
        return league;
    }

    @Override
    public void setLeague(League league) {
        this.league = league;
    }


    @Override
    @OneToMany
    @Column(name = "matches") //TODO is this right?
    public List<Match> getMatches() {
        return matches;
    }


    @Override
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }


    @Override
    @OneToMany
    @Column(name = "teams") //TODO --//--
    public List<String> getTeams() {
        return teams;
    }


    @Override
    public void setTeams(List<String> teams) {
        this.teams = teams;
    }

    @Override
    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }


    /* helping methods */
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
