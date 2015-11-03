package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.IMatchResult;

import javax.persistence.Basic;
import javax.persistence.Column;

/**
 * Created by f00 on 03.11.15.
 * TODO hibernatemapping
 */
public class MatchResult implements IMatchResult {
    public Team winner;
    public Team teamA;
    public Team teamB;
    public String comment;
    public Integer pointsA;
    public Integer pointsB;


    @Override
    @Basic
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }


    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }


    @Override
    @Basic
    @Column(name = "pointsA")
    public Integer getPointsA() {
        return pointsA;
    }


    @Override
    public void setPointsA(Integer pointsA) {
        this.pointsA = pointsA;
    }


    @Override
    @Basic
    @Column(name = "pointsB")
    public Integer getPointsB() {
        return pointsB;
    }
    @Override
    public void setPointsB(Integer pointsB) {
        this.pointsB = pointsB;
    }

    @Override
    @Basic
    @Column(name = "teamA")
    public Team getTeamA() {
        return teamA;
    }

    @Override
    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }
    @Override
    @Basic
    @Column(name = "winner")
    public Team getWinner() {
        return winner;
    }

    @Override
    public void setWinner(Team winner) {
        this.winner = winner;
    }

    @Override
    @Basic
    @Column(name = "teamB")
    public Team getTeamB() {
        return teamB;
    }

    @Override
    public void setTeamB(Team teamB) {
        this.teamB = teamB;
    }
}
