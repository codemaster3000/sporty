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
    private Integer tournamentId;
    private String date;
    private String location;
    private Department department;
    private League league; //TODO Entity
    private List<String> teamList;
    private List<Match> matchList;

    public Tournament() {
    }

    @Override
    @Id
    @GeneratedValue
    @Column(name = "tournamentId")
    public Integer getTournamentId() {
        return tournamentId;
    }

    @Override
    public void setTournamentId(Integer tournamentId) {
        this.tournamentId = tournamentId;
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

    @Override
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "tournamentTeams",
        joinColumns = @JoinColumn(name="tournamentId")
    )
    @Column(name = "teamName")
    public List<String> getTeams() {
        return teamList;
    }

    @Override
    public void setTeams(List<String> teamList) {
        this.teamList = teamList;
    }

    @Override
    public void addTeam(String teamName) {
        if (!teamList.contains(teamName)) {
            teamList.add(teamName);
        }
    }

    @Override
    public void removeTeam(String teamName) {
        if (teamList.isEmpty()) {
            teamList.remove(teamName);
        }
    }

    @Override
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "tournamentId")
    public List<Match> getMatches() {
        return matchList;
    }

    @Override
    public void setMatches(List<Match> matchList) {
        this.matchList = matchList;
    }

    @Override
    public void addMatch(IMatch match) {

        if (!(match instanceof Match)) {
            throw new IllegalArgumentException();
        }

        if (!matchList.contains(match)) {
            matchList.add((Match) match);
        }
    }

    @Override
    public void removeMatch(IMatch match) {

        if (!(match instanceof Match)) {
            throw new IllegalArgumentException();
        }

        if (matchList.isEmpty()) {
            matchList.remove(match);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tournament tournament = (Tournament) o;

        if (tournamentId != null ? !tournamentId.equals(tournament.tournamentId) : tournament.tournamentId != null) return false;
        if (date != null ? !date.equals(tournament.date) : tournament.date != null) return false;
        if (location != null ? !location.equals(tournament.location) : tournament.location != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = tournamentId != null ? tournamentId.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        return result;
    }
}
