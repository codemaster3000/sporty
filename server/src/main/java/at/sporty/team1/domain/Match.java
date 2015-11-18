package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.IMatch;

import javax.persistence.*;

@Entity
@Table(name = "match")
public class Match implements IMatch {

    public Integer matchId;
    public String date;
    public String team1;
    public String team2;
    public String matchResult;
    public String location;
    public String referee;

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
    @Column(name = "matchResult")
    public String getMatchResult() {
        return matchResult;
    }

    @Override
    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }


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
    @Column(name = "location")
    public String getLocation() {
        return location;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    @Basic
    @Column(name = "result")
    public String getResult() {
        return matchResult;
    }

    @Override
    public void setResult(String matchResult) {
        this.matchResult = matchResult;
    }

    @Override
    @Basic
    @Column(name = "referee")
    public String getReferee() {
        return referee;
    }

    @Override
    public void setReferee(String referee) {
        this.referee = referee;
    }
}
