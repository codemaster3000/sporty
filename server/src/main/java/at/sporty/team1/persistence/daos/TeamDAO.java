package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Team;
import at.sporty.team1.persistence.Util;
import at.sporty.team1.persistence.api.ITeamDAO;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import javax.persistence.PersistenceException;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a DAO for Team
 */
public class TeamDAO extends HibernateGenericDAO<Team> implements ITeamDAO {

    private static final String PROP_TEAM_NAME = "teamName";
    private static final String PROP_TEAMS_BY_SPORT = "teamName";

    /**
     * Creates a new team DAO.
     */
    public TeamDAO() {
        super(Team.class);
    }

    @Override
    public List<Team> findTeamsByName(String teamName) throws PersistenceException {
        return findByCriteria(Restrictions.like(PROP_TEAM_NAME, teamName, MatchMode.ANYWHERE));
    }

    @Override
    public List<Team> findTeamsBySport(String sport) throws PersistenceException {
//        String sql = "SELECT ID as {c.id}, NAME as {c.name}, " +
//                "BIRTHDATE as {c.birthDate}, MOTHER_ID as {c.mother}, {mother.*} " +
//                "FROM CAT_LOG c, CAT_LOG m WHERE {c.mother} = c.ID";
//
//        List loggedCats = .createSQLQuery(sql)
//                .addEntity("cat", Cat.class)
//                .addEntity("mother", Cat.class).list()
//        return findBySQLQuery();
        return new LinkedList<>();
    }


}
