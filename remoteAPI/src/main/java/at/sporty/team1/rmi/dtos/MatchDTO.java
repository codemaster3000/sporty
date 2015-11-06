package at.sporty.team1.rmi.dtos;

import at.sporty.team1.rmi.api.IDTO;

import java.util.Date;

public class MatchDTO implements IDTO {

    public MatchDTO() {
    }

    public Date date;
    public TeamDTO homeTeam;
    public TeamDTO guestTeam;
    public MatchResultDTO matchResult;
    public String location;
    public String referee;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TeamDTO getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(TeamDTO guestTeam) {
        this.guestTeam = guestTeam;
    }

    public TeamDTO getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(TeamDTO homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MatchResultDTO getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(MatchResultDTO matchResult) {
        this.matchResult = matchResult;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }
}
