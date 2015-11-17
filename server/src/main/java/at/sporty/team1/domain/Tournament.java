package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.IMatch;
import at.sporty.team1.domain.interfaces.ITournament;
import at.sporty.team1.misc.converters.SQLDateConverter;

import javax.persistence.*;
import java.util.List;

/**
 * Represents a Tournament-Entity
 */
@Entity
@Table(name = "tournament")
public class Tournament implements ITournament {
    private Integer id;
    private String date;
    private String location;
    private Department department;
    private List<String> teams;
    private League league; //TODO Entity
    private List<Match> matches; //TODO Entity


    @Override
    @Id
    @GeneratedValue
    @Column(name = "tournamentId")
    public Integer getTournamentId() {
        return id;
    }

    @Override
    public void setTournamentId(Integer id) {
        this.id = id;
    }


    @Override
    @Basic
    @Convert(converter = SQLDateConverter.class)
    @Column(name = "tournamentDate")
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    @Basic
    @Column(name = "tournamentLocation")
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    @ManyToOne
    @JoinColumn(name = "departmentId")
    public Department getDepartment() {
        return department;
    }

    @Override
    public void setDepartment(Department department) {
        this.department = department;
    }

//    @Override
//    @OneToOne
//    @Column(name = "leagueId")
//    public League getLeague() {
//        return league;
//    }
//
//    @Override
//    public void setLeague(League league) {
//        this.league = league;
//    }
//
//
//    @Override
//    @OneToMany
//    @Column(name = "matches") //TODO is this right?
//    public List<Match> getMatches() {
//        return matches;
//    }
//
//
//    @Override
//    public void setMatches(List<Match> matches) {
//        this.matches = matches;
//    }
//
//
    @Override
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "tournamentTeams",
        joinColumns=@JoinColumn(name="tournamentId")
    )
    @Column(name="teamName")
    public List<String> getTeams() {
        return teams;
    }

    @Override
    public void setTeams(List<String> teams) {
        this.teams = teams;
    }

    /* helping methods */
    @Override
    public void addTeam(String teamName) {
        if (!teams.contains(teamName)) {
            teams.add(teamName);
        }
    }

    @Override
    public void removeTeam(String teamName) {
        if (teams.isEmpty()) {
            teams.remove(teamName);
        }
    }

    @Override
    public void addMatch(IMatch match) {

        if (!(match instanceof Match)) {
            throw new IllegalArgumentException();
        }

        if (!matches.contains(match)) {
            matches.add((Match) match);
        }
    }

    @Override
    public void removeMatch(IMatch match) {

        if (!(match instanceof Match)) {
            throw new IllegalArgumentException();
        }

        if (matches.isEmpty()) {
            matches.remove(match);
        }
    }
}
