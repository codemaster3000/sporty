package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.IMatch;
import at.sporty.team1.misc.converters.SQLDateConverter;
import at.sporty.team1.rmi.dtos.MatchDTO;

import javax.persistence.*;

@Entity
@Table(name = "sportyMatch")
public class Match implements IMatch {
    private Integer matchId;
    private String date;
    private String location;
    private String team1;
    private String team2;
    private String referee;
    private String matchResult;
    private Tournament tournament;

    public Match() {
    }

    @Override
    @Id
    @GeneratedValue
    @Column(name = "matchId")
    public Integer getMatchId() {
        return matchId;
    }

    @Override
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    @Override
    @Basic
    @Column(name = "matchLocation")
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    @Basic
    @Convert(converter = SQLDateConverter.class)
    @Column(name = "matchDate")
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    @Basic
    @Column(name = "team1")
    public String getTeam1() {
        return team1;
    }

    @Override
    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    @Override
    @Basic
    @Column(name = "team2")
    public String getTeam2() {
        return team2;
    }

    @Override
    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    @Override
    @Basic
    @Column(name = "matchReferee")
    public String getReferee() {
        return referee;
    }

    @Override
    public void setReferee(String referee) {
        this.referee = referee;
    }

    @Override
    @Basic
    @Column(name = "matchResult")
    public String getResult() {
        return matchResult;
    }

    @Override
    public void setResult(String matchResult) {
        this.matchResult = matchResult;
    }

    @Override
    @ManyToOne(fetch = FetchType.LAZY)
    public Tournament getTournament() {
        return tournament;
    }

    @Override
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Match match = (Match) o;

        if (matchId != null ? !matchId.equals(match.matchId) : match.matchId != null) return false;
        if (location != null ? !location.equals(match.location) : match.location != null) return false;
        if (date != null ? !date.equals(match.date) : match.date != null) return false;
        if (team1 != null ? !team1.equals(match.team1) : match.team1 != null) return false;
        if (team2 != null ? !team2.equals(match.team2) : match.team2 != null) return false;
        if (referee != null ? !referee.equals(match.referee) : match.referee != null) return false;
        if (matchResult != null ? !matchResult.equals(match.matchResult) : match.matchResult != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = matchId != null ? matchId.hashCode() :0;
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (team1 != null ? team1.hashCode() : 0);
        result = 31 * result + (team2 != null ? team2.hashCode() : 0);
        result = 31 * result + (referee != null ? referee.hashCode() : 0);
        result = 31 * result + (matchResult != null ? matchResult.hashCode() : 0);
        return result;
    }
}
