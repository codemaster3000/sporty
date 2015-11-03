package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.IMatch;

import java.util.Date;

/**
 * Created by f00 on 03.11.15.
 *  TODO hibernatemapping
 */
public class Match implements IMatch {

    public Date date;
    public Team homeTeam;
    public Team guestTeam;
    public MatchResult matchResult;
    public String location;
    public String referee;

    @Override
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public Team getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(Team guestTeam) {
        this.guestTeam = guestTeam;
    }

    @Override
    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public MatchResult getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(MatchResult matchResult) {
        this.matchResult = matchResult;
    }

    @Override
    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }
}
