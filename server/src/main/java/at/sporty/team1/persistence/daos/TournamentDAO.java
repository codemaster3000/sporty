package at.sporty.team1.persistence.daos;


import at.sporty.team1.domain.Team;
import at.sporty.team1.domain.Tournament;
import at.sporty.team1.persistence.api.ITournamentDAO;

import javax.persistence.PersistenceException;

/**
 * Created by f00 on 16.11.15.
 */
public class TournamentDAO extends HibernateGenericDAO<Tournament> implements ITournamentDAO {

    private static final String PROP_TEAM1 = "homeTeam";
    private static final String PROP_TEAM2 = "guestTeam";
    private static final String PROP_DEPARTMENT_ID = "departmentId";

    /**
     * Creates a new TournamentDAO.
     */
    public TournamentDAO() {
        super(Tournament.class);
    }

    /**
     * add team to Tournament
     *
     * @param team ...team to add to the tournament
     * @param tournament
     *
     * @throws PersistenceException
     */
    @Override
    public void addTeam(Team team, Tournament tournament)throws PersistenceException {
        tournament.addTeam(team);
        saveOrUpdate(tournament);
    }

    /**
     * remove Team from tournament
     *
     * @param team
     * @param tournament
     *
     * @throws PersistenceException
     */
    @Override
    public void removeTeam(Team team, Tournament tournament) throws PersistenceException {
        tournament.removeTeam(team);
        saveOrUpdate(tournament);
    }

    
}
