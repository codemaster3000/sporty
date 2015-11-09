package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Team;
import at.sporty.team1.persistence.api.ITeamDAO;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Represents a DAO for Team
 */
public class TeamDAO extends HibernateGenericDAO<Team> implements ITeamDAO {

    private static final String PROP_TEAM_NAME = "teamName";

    /**
     * Creates a new team DAO.
     */
    public TeamDAO() {
        super(Team.class);
    }

    @Override
    public List<Team> findTeamsByName(String teamName) throws PersistenceException {
        return findByCriteria(Restrictions.or(Restrictions.like(PROP_TEAM_NAME, teamName, MatchMode.ANYWHERE)));
    }
}
