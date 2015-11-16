package at.sporty.team1.persistence.api;

import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.Tournament;

import javax.persistence.PersistenceException;

/**
 * Created by f00 on 16.11.15.
 */
public interface ITournamentDAO extends IGenericDAO<Tournament>{
    public void addTeam(Team team, Tournament tournament) throws PersistenceException;
    public void removeTeam(Team team, Tournament tournament) throws PersistenceException;
}
