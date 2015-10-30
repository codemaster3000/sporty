package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Team;

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

}
