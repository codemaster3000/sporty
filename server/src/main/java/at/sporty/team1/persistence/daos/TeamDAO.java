package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Team;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Represents a DAO for Team
 */
public class TeamDAO extends HibernateGenericDAO<Team> {
    /**
     * Constructor
     */
    public TeamDAO() {
        super(Team.class);
    }

    /**
     * get Team(s) by Name-Search
     * @param name name of the team to be searched
     *
     * @return List<Team>
     */
    public List<Team> getTeamByName(String name) {
        Criterion criterion;

        criterion = Restrictions.or(Restrictions.like("teamname", name, MatchMode.ANYWHERE));

        return super.findByCriteria(criterion);
    }

}
