package at.sporty.team1.persistence.daos;

import at.sporty.team1.domain.Department;
import at.sporty.team1.domain.Member;
import at.sporty.team1.domain.Team;
import at.sporty.team1.persistence.api.ITeamDAO;
import at.sporty.team1.persistence.util.PropertyPair;
import at.sporty.team1.persistence.util.Util;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;

import javax.persistence.PersistenceException;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a DAO for Team
 */
public class TeamDAO extends HibernateGenericDAO<Team> implements ITeamDAO {

    private static final String PROP_TEAM_NAME = "teamName";
    private static final String PROP_MEMBER_ID = "memberId";
    private static final String PROP_DEPARTMENT = "department";


    /**
     * Creates a new team DAO.
     */
    public TeamDAO() {
        super(Team.class);
    }

    @Override
    public List<Team> findTeamsByName(String teamName)
    throws PersistenceException {
        return findByCriteria(Restrictions.like(PROP_TEAM_NAME, teamName, MatchMode.ANYWHERE));
    }

    @Override
    public List<Team> findTeamsByDepartment(Department department)
    throws PersistenceException {
        if (department == null) return null;
        return findByCriteria(Restrictions.eq(PROP_DEPARTMENT, department));
    }

    @Override
    public List<Team> findTeamsByMemberId(Integer memberId)
    throws PersistenceException {

        if (memberId == null) return null;

        String rawQuery =
            "SELECT team.* " +
            "FROM teamMembers " +
            "INNER JOIN team " +
            "WHERE " +
            "team.teamId = teamMembers.teamId " +
            "AND " +
            "teamMembers.memberId = :" + PROP_MEMBER_ID;

        return findBySQLQuery(
            rawQuery,
            new PropertyPair<>(PROP_MEMBER_ID, memberId)
        );
    }
}
