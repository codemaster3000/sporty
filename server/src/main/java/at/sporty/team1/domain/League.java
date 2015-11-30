package at.sporty.team1.domain;

import at.sporty.team1.domain.interfaces.ILeague;

import javax.persistence.*;
import java.util.List;

/**
 * Created by f00 on 30.10.15.
 */
@Entity
@Table(name = "league")
public class League implements ILeague {

    private String name;
    private List<Team> order; // ordered!! Leagueleader = first
    private List<Team> teams; // one to many
    private List<Match> matches; //one to many


    @Override
    @Id
    @Column(name = "name")
    public String getName() {
        return name;
    }


    @Override
    @OneToMany
    @Column(name = "matches")
    public List<Match> getMatches() {
        return matches;
    }


    @Override
    @OneToMany
    @Column(name = "order")
    public List<Team> getOrder() {
        return order;
    }


    @Override
    @OneToMany
    @Column(name = "teams")
    public List<Team> getTeams() {
        return teams;
    }


    @Override
    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void setOrder(List<Team> order) {
        this.order = order;
    }

    @Override
    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
