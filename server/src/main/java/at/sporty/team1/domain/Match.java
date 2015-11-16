package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.IMatch;
import at.sporty.team1.misc.converters.SQLDateConverter;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Convert;

/**
 * Created by f00 on 03.11.15.
 *  TODO hibernatemapping
 */
public class Match implements IMatch {

    public String date;
    public String team1;
    public String team2;
    public String matchResult;
    public String location;
    public String referee;

    @Override
    @Basic
    @Convert(converter = SQLDateConverter.class)
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    @Override
    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    @Override
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getResult() {
        return matchResult;
    }

    public void setResult(String matchResult) {
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
